package com.codeforces.graygoose.util;

import com.google.appengine.api.urlfetch.*;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

public class UrlUtil {
    private static final Logger logger = Logger.getLogger(UrlUtil.class);

    private static final double URL_FETCH_DEADLINE_SECONDS = 10D;

    private static final String CONTENT_TYPE_HTTP_HEADER = "content-type";
    private static final String CHARSET_EQ = "charset=";
    private static final int CHARSET_EQ_LENGTH = CHARSET_EQ.length();

    public static ResponseCheckingService.Response fetchUrl(String urlString) {
        return fetchUrls(Arrays.asList(urlString)).get(0);
    }

    public static List<ResponseCheckingService.Response> fetchUrls(List<String> urlStrings) {
        final int urlStringCount = urlStrings.size();
        final String[] urlStringsInternal = new String[urlStringCount];
        urlStrings.toArray(urlStringsInternal);

        final URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();

        final ResponseCheckingService.Response[] responses = new ResponseCheckingService.Response[urlStringCount];
        final List<Future<HTTPResponse>> futureResponses = new ArrayList<Future<HTTPResponse>>(urlStringCount);

        for (int i = 0; i < urlStringCount; ++i) {
            final String urlString = urlStringsInternal[i];
            logger.info("Start to fetch URL [" + urlString + "] ...");

            try {
                HTTPRequest httpRequest = new HTTPRequest(new URL(urlString), HTTPMethod.GET,
                        FetchOptions.Builder.withDeadline(URL_FETCH_DEADLINE_SECONDS));
                httpRequest.setHeader(new HTTPHeader("X-User-Agent", "Graygoose"));
                futureResponses.add(urlFetchService.fetchAsync(httpRequest));
            } catch (MalformedURLException e) {
                logger.error("Fetch error: " + e.getMessage() + ".");
                futureResponses.add(null);
                responses[i] = new ResponseCheckingService.Response(urlString, -1, e.getMessage());
            }
        }

        for (int i = 0; i < urlStringCount; ++i) {
            final Future<HTTPResponse> futureResponse = futureResponses.get(i);

            if (futureResponse != null) {
                final String urlString = urlStringsInternal[i];

                try {
                    HTTPResponse httpResponse = futureResponse.get();
                    logger.info("URL [" + urlString + "] has been successfully fetched.");

                    int responseCode = httpResponse.getResponseCode();
                    Charset responseCharset = getHttpResponseCharset(httpResponse);
                    final byte[] responseContent = httpResponse.getContent();

                    String responseText = responseCharset == null ?
                            new String(responseContent) :
                            new String(responseContent, responseCharset);

                    if (responseCharset == null) {
                        responseCharset = getCharsetByHtml(responseText);
                        if (responseCharset != null) {
                            responseText = new String(responseContent, responseCharset);
                        }
                    }

                    responses[i] = new ResponseCheckingService.Response(urlString, responseCode, responseText);
                } catch (Exception e) {
                    logger.error("Fetch error: " + e.getMessage() + ".");
                    responses[i] = new ResponseCheckingService.Response(urlString, -1, e.getMessage());
                }
            }
        }

        return Arrays.asList(responses);
    }

    private static Charset getHttpResponseCharset(HTTPResponse httpResponse) {
        final List<HTTPHeader> headers = httpResponse.getHeaders();

        for (HTTPHeader header : headers) {
            final String headerName = header.getName();
            final String headerValue = header.getValue();

            if (CONTENT_TYPE_HTTP_HEADER.equalsIgnoreCase(headerName)) {
                return extractCharset(headerValue);
            }
        }

        return null;
    }

    private static Charset extractCharset(String s) {
        s = s.replaceAll("[\\r\\n\\s]+", "");
        final int sLength = s.length();
        final int charsetEqPos = s.indexOf(CHARSET_EQ);

        if (charsetEqPos >= 0) {
            final int charsetNamePos = charsetEqPos + CHARSET_EQ_LENGTH;

            int endPos = charsetNamePos;
            while (endPos < sLength && isCharsetCharacter(s.charAt(endPos))) {
                ++endPos;
            }

            final String charsetName = (endPos == -1 ?
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
            String[] tokens = head.split("[<>]");
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
