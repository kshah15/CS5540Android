package com.example.kishan.newsapp2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.kishan.newsapp2.model.NewsItem;
import com.example.kishan.newsapp2.utilities.NetworkUtils;
import com.example.kishan.newsapp2.utilities.NewsAdapter;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "mainactivity";

    private RecyclerView RecyclerView;
    private NewsAdapter NewsAdapter;
    private EditText SearchBoxEditText;
    private ProgressBar LoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.setLayoutManager(layoutManager);
        RecyclerView.setHasFixedSize(true);

        SearchBoxEditText = (EditText) findViewById(R.id.search);
        LoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        loadNewsData();

    }

    private void loadNewsData() {
        new NewsDataTask("").execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        switch (itemNumber) {
            case R.id.search:
                String s = SearchBoxEditText.getText().toString();
                NewsDataTask task = new NewsDataTask(s);
                task.execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class NewsDataTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> implements NewsAdapter.ItemClickListener {
        String query;
        ArrayList<NewsItem> data;

        NewsDataTask(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            ArrayList<NewsItem> result = null;
            URL newsRequestURL = NetworkUtils.buildUrl();

            try {
                String json = NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
                result = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> newsData) {
            this.data = newsData;
            super.onPostExecute(data);
            LoadingIndicator.setVisibility(View.INVISIBLE);

            if (data != null) {

                NewsAdapter adapter = new NewsAdapter(data, this);
                RecyclerView.setAdapter(adapter);
            }
        }


        @Override
        public void onListItemClick(int clickedItemIndex) {

            openWebPage(data.get(clickedItemIndex).getUrl());
        }

        public void openWebPage(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}