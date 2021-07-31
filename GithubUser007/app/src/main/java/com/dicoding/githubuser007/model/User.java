package com.dicoding.githubuser007.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String avatar;
    private String followers;
    private String following;
    private String repository;
    protected User(Parcel in) {
        name = in.readString();
        avatar = in.readString();
        followers = in.readString();
        following = in.readString();
        repository = in.readString();
    }

    public User(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getRepo() {
        return repository;
    }

    public void setRepo(String repo) {
        this.repository = repo;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(String username, String avatar, String followers, String following, String repository) {
        this.name = username;
        this.avatar = avatar;
        this.followers = followers;
        this.following = following;
        this.repository = repository;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(followers);
        dest.writeString(following);
        dest.writeString(repository);
    }
}
