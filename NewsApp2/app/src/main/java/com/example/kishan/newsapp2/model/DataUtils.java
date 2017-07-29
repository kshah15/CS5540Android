package com.example.kishan.newsapp2.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.kishan.newsapp2.model.Contract.NewsItem.*;

import java.util.ArrayList;

/**
 * Created by Kishan on 7/24/2017.
 */

public class DataUtils {

    // Get news articles from the database
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_PUBLISHED_AT + " DESC"
        );
        return cursor;
    }

    // Insert api
    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> newsItems) {

        db.beginTransaction();
        try {
            for (NewsItem a : newsItems) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_SOURCE, a.getSource());
                cv.put(COLUMN_AUTHOR, a.getAuthor());
                cv.put(COLUMN_TITLE, a.getTitle());
                cv.put(COLUMN_DESCRIPTION, a.getDescription());
                cv.put(COLUMN_URL, a.getUrl());
                cv.put(COLUMN_URL_TO_IMAGE, a.getUrlToImage());
                cv.put(COLUMN_PUBLISHED_AT, a.getPublishedAt());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }
}