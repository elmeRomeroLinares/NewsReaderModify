package com.example.newsreader.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.newsreader.data.NewsContract.NewsEntry;

public class NewsContentProvider extends ContentProvider {

    // NewsDbHelper object
    NewsDbHelper mNewsDbHelper;

    // UriMatcher object
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // UriMatcher code
    private static final int NEWS = 100;
    private static final int NEWS_ID = 101;

    static {
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_NEWS_TABLE, NEWS);

        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY,
                NewsContract.PATH_NEWS_TABLE + "/#", NEWS_ID);
    }

    @Override
    public boolean onCreate() {
        mNewsDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Readable Data base
        SQLiteDatabase rDatabase = mNewsDbHelper.getReadableDatabase();

        Cursor cursor;

        // look for a Uri match returning coincidence code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                // cursor containing all rows in table
                cursor = rDatabase.query(NewsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case NEWS_ID:
                // cursor containing row with ID
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = rDatabase.query(NewsEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can't query unknown URI " + uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return NewsContract.CONTENT_LIST_TYPE;
            case NEWS_ID:
                return NewsContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        if (match == NEWS) {
            return insertArticle(uri, contentValues);
        } else {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertArticle(Uri uri, ContentValues values) {
        // creates writable database
        SQLiteDatabase wDatabase = mNewsDbHelper.getWritableDatabase();

        // values check
        String sectionName = values.getAsString(NewsEntry.COLUMN_ARTICLE_SECTION);
        //if (sectionName == null) { values.put(NewsEntry.COLUMN_ARTICLE_SECTION, "Error Saving Article"); }

        String webUrl = values.getAsString(NewsEntry.COLUMN_ARTICLE_WEB_URL);
        //if (webUrl == null) { values.put(NewsEntry.COLUMN_ARTICLE_WEB_URL, "0"); }

        String headLine = values.getAsString(NewsEntry.COLUMN_ARTICLE_HEADLINE);
        //if (headLine == null) { values.put(NewsEntry.COLUMN_ARTICLE_HEADLINE, "0"); }

        String thumbnail = values.getAsString(NewsEntry.COLUMN_ARTICLE_THUMBNAIL);
        //if (thumbnail == null) { values.put(NewsEntry.COLUMN_ARTICLE_THUMBNAIL, "0"); }

        String body = values.getAsString(NewsEntry.COLUMN_ARTICLE_BODY);
        //if (body == null) {values.put(NewsEntry.COLUMN_ARTICLE_BODY, "0"); }

        Log.d("News Content Provider", sectionName + " " + webUrl + " " + headLine + " " + thumbnail + " " + body);

        // Insert new article with given values
        long id = wDatabase.insert(NewsEntry.TABLE_NAME, null, values);

        // Insertion result status
        if (id == -1) {
            Log.e("News Content Provider ", "Failed to insert row for " + uri );
            return null;
        }

        // Return URI with the ID of the new inserted row appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // get writable database
        SQLiteDatabase wDatabase = mNewsDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NEWS:
                return wDatabase.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
            case NEWS_ID:
                // Delete single row given an ID
                selection = NewsEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return wDatabase.delete(NewsEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
