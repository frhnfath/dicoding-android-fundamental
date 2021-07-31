package com.dicoding.githubuser007.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.dicoding.githubuser007.database.DatabaseContract.FavoriteColumns.USERNAME;
import static com.dicoding.githubuser007.database.DatabaseContract.TABLE_NAME;

public class UserHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static UserHelper INSTANCE;
    private static DatabaseHelper dataBaseHelper;
    private static SQLiteDatabase sqLiteDatabase;

    private UserHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static UserHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public long insert(ContentValues values) {
        return sqLiteDatabase.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return sqLiteDatabase.update(DATABASE_TABLE, values, USERNAME + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return sqLiteDatabase.delete(DATABASE_TABLE, USERNAME + " = ?", new String[]{id});
    }

    public void open() throws SQLException {
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (sqLiteDatabase.isOpen()) sqLiteDatabase.close();
    }

    public Cursor queryAll() {
        return sqLiteDatabase.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                USERNAME + " ASC");
    }

    public Cursor queryById(String id) {
        return sqLiteDatabase.query(
                DATABASE_TABLE,
                null,
                USERNAME + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }
}
