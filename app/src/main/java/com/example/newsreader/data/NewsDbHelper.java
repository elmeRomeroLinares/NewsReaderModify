package com.example.newsreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.example.newsreader.data.NewsContract.NewsEntry;

public class NewsDbHelper extends SQLiteOpenHelper {

    // database name
    private static final String NEWS_DATABASE_NAME = "articles.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    NewsDbHelper(Context context) {
        super(context, NEWS_DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("NewsDbHelper", "onCreateMethod");

        // CREATE TABLE savedNews
        String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + "(" +
                NewsEntry._ID + " INTEGER , " +
                NewsEntry.COLUMN_ARTICLE_BODY + " TEXT, " +
                NewsEntry.COLUMN_ARTICLE_HEADLINE + " TEXT PRIMARY KEY ON CONFLICT REPLACE, " +
                NewsEntry.COLUMN_ARTICLE_SECTION + " TEXT, " +
                NewsEntry.COLUMN_ARTICLE_THUMBNAIL + " TEXT, " + NewsEntry.COLUMN_ARTICLE_WEB_URL + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
