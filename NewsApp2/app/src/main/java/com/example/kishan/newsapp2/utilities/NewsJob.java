package com.example.kishan.newsapp2.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.example.kishan.newsapp2.model.DataHelper;
import com.example.kishan.newsapp2.model.DataUtils;
import com.example.kishan.newsapp2.model.NewsItem;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Kishan on 7/27/2017.
 */

public class NewsJob extends JobService{

    AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                Toast.makeText(NewsJob.this, "News refreshed", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                refreshArticles(NewsJob.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                super.onPostExecute(o);

            }
        };

        mBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {

        if (mBackgroundTask != null) mBackgroundTask.cancel(false);

        return true;
    }

    public static void refreshArticles(Context context) {
        ArrayList<NewsItem> result = null;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new DataHelper(context).getWritableDatabase();

        try {
            DataUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);
            DataUtils.bulkInsert(db, result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }

}