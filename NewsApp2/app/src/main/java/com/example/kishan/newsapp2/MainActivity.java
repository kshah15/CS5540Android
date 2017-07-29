package com.example.kishan.newsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.kishan.newsapp2.utilities.NewsJob;
import com.example.kishan.newsapp2.utilities.ScheduleUtil;
import com.example.kishan.newsapp2.utilities.NewsAdapter;
import org.json.JSONException;


import com.example.kishan.newsapp2.model.DataHelper;
import com.example.kishan.newsapp2.model.DataUtils;
import com.example.kishan.newsapp2.model.Contract;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, NewsAdapter.ItemClickListener {
    static final String TAG = "mainactivity";

    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private EditText mSearchBoxEditText;
    private ProgressBar mLoadingIndicator;

    // Done: 4. Create local field member of type SQLiteDatabase Also create Cursor object
    private SQLiteDatabase mDb;
    private Cursor cursor;

    // Done: 2. Create constant int to identify loader
    private static final int NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Done: 7. Activity laod what's currently in database into recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        // Done: 6. Check if app is installed before,  If not then load data into database.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);

        if (isFirst) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }
        ScheduleUtil.scheduleRefresh(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Done: 4. Get a writable database reference, store it in mDb
        mDb = new DataHelper(MainActivity.this).getReadableDatabase();
        cursor = DataUtils.getAll(mDb);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(mNewsAdapter);
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
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Done: 2. Implement methods onCreateLoader, onLoadFinished, and onLoaderReset
    //         to loadmanager,  LoaderManager.LoaderCallbacks<Void>
    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {

        // Done: 2. Return new AsyncTaskLoader<Void> as anonymous inner class with this.
        return new AsyncTaskLoader<Void>(this) {

            // Done: 2. Show loading_indicator on the start of loading.
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                // Done: 7. Refresh articles on method
                NewsJob.refreshArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        // Done: 2. When loading is finished.
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        // Done: 7. get info from database
        mDb = new DataHelper(MainActivity.this).getReadableDatabase();
        cursor = DataUtils.getAll(mDb);

        // Done: 7. Reset data in recyclerview.
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {}

    // Done: 4. Update onListItemClick in to the Cursor
    @Override
    public void onListItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.NewsItem.COLUMN_URL));
        Log.d(TAG, String.format("Url %s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    // Done: 2. Removed class FetchNewsTask.
}