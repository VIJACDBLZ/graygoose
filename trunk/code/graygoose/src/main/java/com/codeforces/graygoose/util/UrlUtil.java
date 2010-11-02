package com.codeforces.graygoose.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlUtil {
    public static ResponseChecker.Response fetchUrl(String urlString) {
        ResponseChecker.Response response = null;

        HttpURLConnection connection = null;
        Reader reader = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String text = IOUtils.toString(reader);

            response = new ResponseChecker.Response(urlString, connection.getResponseCode(), text);
        } catch (IOException e) {
            response = new ResponseChecker.Response(urlString, -1, e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // No operation.
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }

    private UrlUtil() {
    }
}
