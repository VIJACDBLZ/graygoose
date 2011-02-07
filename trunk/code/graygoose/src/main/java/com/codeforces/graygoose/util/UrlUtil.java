package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Response;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.urlfetch.*;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class UrlUtil {
    private static final Logger logger = Logger.getLogger(UrlUtil.class);

    private static final HTTPHeader GRAYGOOSE_AGENT_HTTP_HEADER = new HTTPHeader("X-User-Agent", "Graygoose");

    private static final String CONTENT_TYPE_HTTP_HEADER = "content-type";
    private static final String CHARSET_EQ = "charset=";
    private static final int CHARSET_EQ_LENGTH = CHARSET_EQ.length();

    private static final Pattern HTML_SPLIT_PATTERN = Pattern.compile("[<>]");
    private static final Pattern TRANSFORM_CHARSET_STRING_PATTERN = Pattern.compile("[\\r\\n\\s]+");

    public static Response fetchUrl(String urlString, int attemptCount) {
        return fetchUrls(Arrays.asList(urlString), attemptCount).get(0);
    }

    public static List<Response> fetchUrls(List<String> urlStrings, int attemptCount) {
        if (attemptCount < 1) {
            throw new IllegalArgumentException("Argument \'attemptCount\' can't be less than \'1\'.");
        }

        URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();

        int urlStringCount = urlStrings.size();
        String[] urlStringsInternal = new String[urlStrings.size()];
        urlStrings.toArray(urlStringsInternal);

        Response[] responses = new Response[urlStringCount];
        Future[] futureResponses = new Future[urlStringCount];

        for (int attemptIndex = 0; attemptIndex < attemptCount; ++attemptIndex) {
            fetchUrlsInternal(urlFetchService, urlStringCount, urlStringsInternal, responses, futureResponses);
        }

        return Arrays.asList(responses);
    }

    @SuppressWarnings({"OverlyLongMethod"})
    private static void fetchUrlsInternal(URLFetchService urlFetchService, int urlStringCount,
                                          String[] urlStringsInternal, Response[] responses, Future[] futureResponses) {
        for (int i = 0; i < urlStringCount; ++i) {
            if (responses[i] != null && responses[i].getCode() != -1) {
                continue;
            }

            String urlString = urlStringsInternal[i];
            logger.info("Start to fetch URL [" + urlString + "] ...");

            try {
                HTTPRequest httpRequest = new HTTPRequest(new URL(urlString));
                httpRequest.setHeader(GRAYGOOSE_AGENT_HTTP_HEADER);
                futureResponses[i] = urlFetchService.fetchAsync(httpRequest);
            } catch (MalformedURLException e) {
                logger.error("Fetch error: " + e.getMessage() + '.');
                futureResponses[i] = null;
                responses[i] = new Response(urlString, -1, new Text(urlString + "; " + e.getMessage()));
            }
        }

        for (int i = 0; i < urlStringCount; ++i) {
            if (responses[i] != null && responses[i].getCode() != -1) {
                continue;
            }

            Future futureResponse = futureResponses[i];

            if (futureResponse != null) {
                String urlString = urlStringsInternal[i];

                try {
                    HTTPResponse httpResponse = (HTTPResponse) futureResponse.get();
                    logger.info("URL [" + urlString + "] has been successfully fetched.");

                    int responseCode = httpResponse.getResponseCode();
                    Charset responseCharset = getHttpResponseCharset(httpResponse);
                    byte[] responseContent = httpResponse.getContent();

                    String responseText = responseCharset == null ?
                            new String(responseContent) :
                            new String(responseContent, responseCharset);

                    if (responseCharset == null) {
                        responseCharset = getCharsetByHtml(responseText);
                        if (responseCharset != null) {
                            responseText = new String(responseContent, responseCharset);
                        }
                    }

                    responses[i] = new Response(urlString, responseCode, new Text(responseText));
                } catch (Exception e) {
                    logger.error("Fetch error: " + e.getMessage() + '.');
                    responses[i] = new Response(urlString, -1, new Text(urlString + "; " + e.getMessage()));
                }
            }
        }
    }

    private static Charset getHttpResponseCharset(HTTPResponse httpResponse) {
        List<HTTPHeader> headers = httpResponse.getHeaders();

        for (HTTPHeader header : headers) {
            String headerName = header.getName();
            String headerValue = header.getValue();

            if (CONTENT_TYPE_HTTP_HEADER.equalsIgnoreCase(headerName)) {
                return extractCharset(headerValue);
            }
        }

        return null;
    }

    private static Charset extractCharset(String s) {
        s = TRANSFORM_CHARSET_STRING_PATTERN.matcher(s).replaceAll("");
        int sLength = s.length();
        int charsetEqPos = s.indexOf(CHARSET_EQ);

        if (charsetEqPos >= 0) {
            int charsetNamePos = charsetEqPos + CHARSET_EQ_LENGTH;

            int endPos = charsetNamePos;
            while (endPos < sLength && isCharsetCharacter(s.charAt(endPos))) {
                ++endPos;
            }

            String charsetName = (endPos == -1 ?
                    s.substring(charsetNamePos) :
                    s.substring(charsetNamePos, endPos)).trim();

            return Charset.isSupported(charsetName) ? Charset.forName(charsetName) : null;
        } else {
            return null;
        }
    }

    /**
     * @param c Character to analyze.
     * @return {@code true} iff character is a part of charset or whitespace.
     */
    private static boolean isCharsetCharacter(char c) {
        return Character.isWhitespace(c)
                || Character.isLetterOrDigit(c)
                || c == '-';
    }

    private static Charset getCharsetByHtml(String html) {
        html = html.toLowerCase();
        int headEnd = html.indexOf("</head>");

        if (headEnd > 0) {
            String head = html.substring(0, headEnd);
            String[] tokens = HTML_SPLIT_PATTERN.split(head);
            for (String token : tokens) {
                if (token.contains(CONTENT_TYPE_HTTP_HEADER)) {
                    return extractCharset(token);
                }
            }
        }

        return null;
    }

    private UrlUtil() {
    }
}
