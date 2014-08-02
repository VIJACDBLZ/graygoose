package com.codeforces.graygoose.util;

import com.codeforces.graygoose.model.Response;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class UrlUtil {
    private static final Logger logger = Logger.getLogger(UrlUtil.class);

    private static final Header GRAYGOOSE_AGENT_HTTP_HEADER = new BasicHeader("X-User-Agent", "Graygoose");

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

        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();


        try {
            int urlStringCount = urlStrings.size();
            String[] urlStringsInternal = new String[urlStrings.size()];
            urlStrings.toArray(urlStringsInternal);

            Response[] responses = new Response[urlStringCount];
            @SuppressWarnings("unchecked") Future<HttpResponse>[] futureResponses = new Future[urlStringCount];

            for (int attemptIndex = 0; attemptIndex < attemptCount; ++attemptIndex) {
                fetchUrlsInternal(client, urlStringCount, urlStringsInternal, responses, futureResponses);
            }

            return Arrays.asList(responses);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                // No operations.
            }
        }
    }

    @SuppressWarnings({"OverlyLongMethod"})
    private static void fetchUrlsInternal(CloseableHttpAsyncClient client, int urlStringCount,
                                          String[] urlStringsInternal, Response[] responses, Future<HttpResponse>[] futureResponses) {
        for (int i = 0; i < urlStringCount; ++i) {
            if (responses[i] != null && responses[i].getCode() != -1) {
                continue;
            }

            String urlString = urlStringsInternal[i];
            logger.info("Start to fetch URL [" + urlString + "] ...");

            try {
                HttpGet request = new HttpGet(urlString);
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(10 * 1000)
                        .setConnectionRequestTimeout(20 * 1000)
                        .setSocketTimeout(30 * 1000)
                        .build();
                request.setConfig(requestConfig);

                request.setHeader(GRAYGOOSE_AGENT_HTTP_HEADER);

                futureResponses[i] = client.execute(request, null);
            } catch (RuntimeException e) {
                logger.error("Fetch error: " + e.getMessage() + '.');
                futureResponses[i] = null;
                responses[i] = Response.newResponse(urlString, -1, e.getMessage());
            }
        }

        for (int i = 0; i < urlStringCount; ++i) {
            if (responses[i] != null && responses[i].getCode() != -1) {
                continue;
            }

            Future<HttpResponse> futureResponse = futureResponses[i];

            if (futureResponse != null) {
                String urlString = urlStringsInternal[i];

                try {
                    HttpResponse httpResponse = futureResponse.get();
                    logger.info("URL [" + urlString + "] has been successfully fetched.");

                    int responseCode = httpResponse.getStatusLine().getStatusCode();
                    Charset responseCharset = getHttpResponseCharset(httpResponse);
                    System.out.println("Headers charset: " + responseCharset);

                    byte[] responseContent = IOUtils.toByteArray(httpResponse.getEntity().getContent());

                    String responseText = responseCharset == null ?
                            new String(responseContent) :
                            new String(responseContent, responseCharset);

                    if (responseCharset == null) {
                        responseCharset = getCharsetByHtml(responseText);
                        System.out.println("Html charset: " + responseCharset);
                        if (responseCharset != null) {
                            responseText = new String(responseContent, responseCharset);
                        }
                    }

                    responses[i] = Response.newResponse(urlString, responseCode, responseText);
                } catch (Exception e) {
                    logger.error("Fetch error: " + e.getMessage() + '.');
                    responses[i] = Response.newResponse(urlString, -1, e.getMessage());
                }
            }
        }
    }

    private static Charset getHttpResponseCharset(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getAllHeaders();

        for (Header header : headers) {
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
