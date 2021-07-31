package com.dicoding.consumerapp2;

import android.database.Cursor;

import java.util.ArrayList;

public class MapHelper {

    public static ArrayList<User> mapCursorToArrayList(Cursor usersCursor){
        ArrayList<User> usersList = new ArrayList<>();

        while (usersCursor.moveToNext()){
            String username = usersCursor.getString(usersCursor.getColumnIndexOrThrow(com.dicoding.consumerapp2.DatabaseContract.FavColumns.USERNAME));
            String avatar = usersCursor.getString(usersCursor.getColumnIndexOrThrow(com.dicoding.consumerapp2.DatabaseContract.FavColumns.AVATAR));
            String followers = usersCursor.getString(usersCursor.getColumnIndexOrThrow(com.dicoding.consumerapp2.DatabaseContract.FavColumns.FOLLOWERS));
            String following = usersCursor.getString(usersCursor.getColumnIndexOrThrow(com.dicoding.consumerapp2.DatabaseContract.FavColumns.FOLLOWING));
            String repository = usersCursor.getString(usersCursor.getColumnIndexOrThrow(com.dicoding.consumerapp2.DatabaseContract.FavColumns.REPO));
            usersList.add(new User(username,avatar,followers,following,repository));
        }
        return usersList;
    }
}
