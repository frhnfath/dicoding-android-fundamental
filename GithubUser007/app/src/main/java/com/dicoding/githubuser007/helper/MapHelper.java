package com.dicoding.githubuser007.helper;

import android.database.Cursor;

import com.dicoding.githubuser007.database.DatabaseContract;
import com.dicoding.githubuser007.model.User;

import java.util.ArrayList;

public class MapHelper {
    public static ArrayList<User> mapArrayList(Cursor cursor) {
        ArrayList<User> listUser = new ArrayList<>();

        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR));
            String followers = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWERS));
            String following = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.FOLLOWING));
            String repository = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.REPOSITORY));
            listUser.add(new User(username,avatar,followers,following,repository));
        }
        return listUser;
    }
}
