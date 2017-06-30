package com.example.kishan.newsapp2.utilities;

import android.net.Uri;
import android.util.Log;

import com.example.kishan.newsapp2.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Kishan on 6/29/2017.
 */

public class NetworkUtils {

    public static final String TAG = "NetworkUtils";

    final static String BASE_URL = "https://newsapi.org/v1/articles";

    final static String SOURCE_PARAM = "source";
    final static String SORT_PARAM = "sortBy";
    final static String APIKEY_PARAM = "apiKey";

    private final static String src = "the-next-web";
    private final static String sort = "latest";
    private final static String key = "23bff5156f8a47be836246b06643b4b7";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SOURCE_PARAM, src)
                .appendQueryParameter(SORT_PARAM, sort)
                .appendQueryParameter(APIKEY_PARAM, key)
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

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
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

    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {
        ArrayList<NewsItem> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("articles");

        String Source = main.getString("source");
        String Authorname;
        String Title;
        String NewsDescription;
        String UrlBar;
        String UrlBarImage;
        String PublishedAt;

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            Authorname = item.getString("author");
            Title = item.getString("title");
            NewsDescription = item.getString("description");
            UrlBar = item.getString("url");
            UrlBarImage = item.getString("urlToImage");
            PublishedAt = item.getString("publishedAt");

            NewsItem newsItem = new NewsItem(Source, Authorname, Title, NewsDescription, UrlBar, UrlBarImage, PublishedAt);
            result.add(newsItem);
        }
        return result;
    }
}