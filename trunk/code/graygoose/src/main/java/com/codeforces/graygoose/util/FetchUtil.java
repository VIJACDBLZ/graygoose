package com.codeforces.graygoose.util;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchUtil {

    public static ResponseChecker.Response fetchUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String text = IOUtils.toString(reader);

        ResponseChecker.Response response =
                new ResponseChecker.Response(urlString, connection.getResponseCode(), text);

        reader.close();
        connection.disconnect();

        return response;
    }

    protected FetchUtil() {
    }
}
