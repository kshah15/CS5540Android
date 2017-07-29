package com.example.kishan.newsapp2.model;

import android.provider.BaseColumns;

/**
 * Created by Kishan on 7/24/2017.
 */

public class Contract {

    public static final class NewsItem implements BaseColumns {


        //  Done: 3. Create static final members
        public static final String TABLE_NAME = "news_items";

        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_PUBLISHED_AT = "published_at";
    }
}