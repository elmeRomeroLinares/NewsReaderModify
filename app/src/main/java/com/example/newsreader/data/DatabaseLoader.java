package com.example.newsreader.data;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import com.example.newsreader.data.NewsContract.NewsEntry;
import com.example.newsreader.model.Article;

import java.util.ArrayList;

public class DatabaseLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private String[] projection = {NewsEntry._ID, NewsEntry.COLUMN_ARTICLE_WEB_URL,
            NewsEntry.COLUMN_ARTICLE_THUMBNAIL, NewsEntry.COLUMN_ARTICLE_SECTION,
            NewsEntry.COLUMN_ARTICLE_HEADLINE, NewsEntry.COLUMN_ARTICLE_BODY};

    private ArrayList<Article> mData;


    public DatabaseLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        mData = new ArrayList<>();

        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Article> loadInBackground() {

        Cursor cursor = getContext().getContentResolver().query(NewsEntry.CONTENT_URI, projection,
                null, null, null);

        if (cursor != null) {
            int bodyColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_ARTICLE_BODY);
            int headlineColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_ARTICLE_HEADLINE);
            int sectionColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_ARTICLE_SECTION);
            int thumbnailColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_ARTICLE_THUMBNAIL);
            int onlineColumnIndex = cursor.getColumnIndex(NewsEntry.COLUMN_ARTICLE_WEB_URL);

                while (cursor.moveToNext()) {
                    String sectionName = cursor.getString(sectionColumnIndex);
                    String webUrl = cursor.getString(onlineColumnIndex);
                    String headline = cursor.getString(headlineColumnIndex);
                    String thumbnail = cursor.getString(thumbnailColumnIndex);
                    String body = cursor.getString(bodyColumnIndex);
                    mData.add(new Article(sectionName, webUrl, headline, thumbnail, body));
                }
            cursor.close();
        }

        return mData;
    }
}