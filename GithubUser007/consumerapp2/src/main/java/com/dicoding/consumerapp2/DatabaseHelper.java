package com.dicoding.consumerapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbuser";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USER = String.format("CREATE TABLE %s"
            + " (%s TEXT PRIMARY KEY NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            com.dicoding.consumerapp2.DatabaseContract.TABLE_NAME,
            com.dicoding.consumerapp2.DatabaseContract.FavColumns.USERNAME,
            com.dicoding.consumerapp2.DatabaseContract.FavColumns.AVATAR,
            com.dicoding.consumerapp2.DatabaseContract.FavColumns.FOLLOWERS,
            com.dicoding.consumerapp2.DatabaseContract.FavColumns.FOLLOWING,
            com.dicoding.consumerapp2.DatabaseContract.FavColumns.REPO
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + com.dicoding.consumerapp2.DatabaseContract.TABLE_NAME);
        onCreate(db);
    }
}
