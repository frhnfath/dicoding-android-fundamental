package com.dicoding.githubuser007.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dicoding.githubuser007.helper.UserHelper;

import static com.dicoding.githubuser007.database.DatabaseContract.AUTHORITY;
import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.dicoding.githubuser007.database.DatabaseContract.TABLE_NAME;

public class MyProvider extends ContentProvider {
    private UserHelper userHelper;
    private static final int NOTIFICATION = 1;
    private static final int NOTIFICATION_ID = 2;

    @Override
    public boolean onCreate() {
        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            default:
                cursor = null;
                break;
            case NOTIFICATION:
                cursor = userHelper.queryAll();
                break;
            case NOTIFICATION_ID:
                cursor = userHelper.queryById(uri.getLastPathSegment());
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        switch (uriMatcher.match(uri)) {
            default:
                added = 0;
                break;
            case NOTIFICATION:
                added = userHelper.insert(values);
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
    return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (uriMatcher.match(uri)) {
            default:
                deleted = 0;
                Log.d("TAG", "Unsuccessfully Deleted");
                break;
            case NOTIFICATION_ID:
                deleted = userHelper.deleteById(uri.getLastPathSegment());
                Log.d("TAG", "Successfully Deleted");
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (uriMatcher.match(uri)) {
            default:
                updated = 0;
                break;
            case NOTIFICATION_ID:
                updated = userHelper.update(uri.getLastPathSegment(), values);
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTIFICATION);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", NOTIFICATION_ID);
    }
}
