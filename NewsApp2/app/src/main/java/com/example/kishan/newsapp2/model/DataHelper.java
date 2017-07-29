package com.example.kishan.newsapp2.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import static com.example.kishan.newsapp2.model.Contract.*;
/**
 * Created by Kishan on 7/24/2017.
 */

public class DataHelper extends SQLiteOpenHelper  {


    // Create static final String called DATABASE_NAME and set it to "newsitems.db"
    // Create static final int called DATABASE_VERSION.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NewsItems.db";

    // To logging
    private static final String TAG = "DataHelper";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWSITEM_TABLE = "CREATE TABLE " + Contract.NewsItem.TABLE_NAME + " (" +
                Contract.NewsItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.NewsItem.COLUMN_SOURCE + " TEXT NOT NULL, " +
                Contract.NewsItem.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                Contract.NewsItem.COLUMN_TITLE + " TEXT NOT NULL, " +
                Contract.NewsItem.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                Contract.NewsItem.COLUMN_URL + " TEXT NOT NULL, " +
                Contract.NewsItem.COLUMN_URL_TO_IMAGE + " TEXT NOT NULL, " +
                Contract.NewsItem.COLUMN_PUBLISHED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        Log.d(TAG, "Create SQL: " + SQL_CREATE_NEWSITEM_TABLE);
        db.execSQL(SQL_CREATE_NEWSITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DATABASE_VERSION changed, drop the table and create new db for it.
        db.execSQL("DROP TABLE IF EXISTS " + Contract.NewsItem.TABLE_NAME);
        onCreate(db);
    }

}