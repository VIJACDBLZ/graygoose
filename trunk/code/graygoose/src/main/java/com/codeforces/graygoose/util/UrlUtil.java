package com.codeforces.graygoose.util;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
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

    private static final String CONTENT_TYPE_HTTP_HEADER = "Content-Type";
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
                futureResponses.add(urlFetchService.fetchAsync(new URL(urlString)));
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
                    final HTTPResponse httpResponse = futureResponse.get();
                    logger.info("URL [" + urlString + "] has been successfully fetched.");

                    final int responsecode = httpResponse.getResponseCode();
                    final Charset responseCharset = getHttpResponseCharset(httpResponse);
                    final byte[] responseContent = httpResponse.getContent();

                    final String responseText = responseCharset == null ?
                            new String(responseContent) :
                            new String(responseContent, responseCharset);

                    responses[i] = new ResponseCheckingService.Response(urlString, responsecode, responseText);
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
                final int charsetEqPos = headerValue.indexOf(CHARSET_EQ);

                if (charsetEqPos >= 0) {
                    final int charsetNamePos = charsetEqPos + CHARSET_EQ_LENGTH;
                    final int semicolonPos = headerValue.indexOf(';', charsetNamePos);

                    final String charsetName = (semicolonPos == -1 ?
                            headerValue.substring(charsetNamePos) :
                            headerValue.substring(charsetNamePos, semicolonPos)).trim();

                    return Charset.isSupported(charsetName) ? Charset.forName(charsetName) : null;
                }
            }
        }

        return null;
    }

    private UrlUtil() {
    }
}
