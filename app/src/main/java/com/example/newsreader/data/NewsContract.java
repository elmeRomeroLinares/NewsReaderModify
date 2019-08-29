package com.example.newsreader.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {

    // Content authority
    public static final String CONTENT_AUTHORITY = "com.example.newsreader";

    // Base content URI
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // PATH_TableName
    public static final String PATH_NEWS_TABLE = "savedArticles";

    // MIME type for a list of news
    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_NEWS_TABLE;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
            + CONTENT_AUTHORITY + "/" + PATH_NEWS_TABLE;

    // private constructor to avoid instantiation
    private NewsContract(){}

    public static final class NewsEntry implements BaseColumns {

        // full URI for Entry class
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NEWS_TABLE);

        // table savedNews name constant
        public static final String TABLE_NAME = "savedArticles";

        // table savedNews columns
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ARTICLE_HEADLINE = "headline";
        public static final String COLUMN_ARTICLE_SECTION = "section";
        public static final String COLUMN_ARTICLE_THUMBNAIL = "thumbnail";
        public static final String COLUMN_ARTICLE_BODY = "body";
        public static final String COLUMN_ARTICLE_WEB_URL = "online";
    }


}
