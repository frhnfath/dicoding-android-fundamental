package com.dicoding.githubuser007.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.dicoding.githubuser007";
    private static final String SCHEME = "content";
    public static String TABLE_NAME = "favorite";
    public static final class FavoriteColumns implements BaseColumns {
        public static String USERNAME = "username";
        public static String FOLLOWERS = "followers";
        public static String FOLLOWING = "following";
        public static String AVATAR = "avatar";
        public static String REPOSITORY = "repository";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
