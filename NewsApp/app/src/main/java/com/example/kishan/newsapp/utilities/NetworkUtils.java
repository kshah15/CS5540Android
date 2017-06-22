package com.example.kishan.newsapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by Kishan on 6/21/2017.
 */

public class NetworkUtils {
    private static final String BASE_URL = "https://newsapi.org/v1/articles";

    final static String SOURCE_NAPP = "source";
    final static String SORT_NAPP = "sortBy";
    final static String APIKEY_NAPP = "apiKey";

    private final static String src = "the-next-web";
    private final static String sort = "latest";
    private final static String key = "23bff5156f8a47be836246b06643b4b7";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_NAPP, src)
                .appendQueryParameter(SORT_NAPP, sort)
                .appendQueryParameter(APIKEY_NAPP, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}