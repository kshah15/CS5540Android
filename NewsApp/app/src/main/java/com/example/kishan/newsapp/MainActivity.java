package com.example.kishan.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kishan.newsapp.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView NewsView;
    private TextView NewsUrlView;
    private EditText NewsSearchBox;
    private ProgressBar LoadIndi;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.news_menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_newssearch) {
            NewsUrlView.setText("");
            loadNewsData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsView = (TextView) findViewById(R.id.news_data);
        NewsSearchBox = (EditText) findViewById(R.id.news_search);
        NewsUrlView = (TextView) findViewById(R.id.news_url);

        LoadIndi = (ProgressBar) findViewById(R.id.load_indi);

        }

    private void loadNewsData() {

        URL NewsSearchUrl = NetworkUtils.buildUrl();
        NewsUrlView.setText(NewsSearchUrl.toString());


        new getNewsData().execute();
    }

    public class getNewsData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadIndi.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            URL newsRequestURL = NetworkUtils.buildUrl();

            try {
                return NetworkUtils.getResponseFromHttpUrl(newsRequestURL);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String newsData) {
            LoadIndi.setVisibility(View.INVISIBLE);

            if (newsData != null) {
                NewsUrlView.setText(newsData);
            }
        }
    }
}