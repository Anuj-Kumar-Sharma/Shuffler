package com.example.anujsharma.shuffler.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class User implements Parcelable {

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
    private long id;
    private String username, permalink, fullName;
    private String userAvatar;
    private int followersCount, tracksCount, followingsCount, playlistCount;

    public User(long id, String username, String permalink, String userAvatar) {
        this.id = id;
        this.username = username;
        this.permalink = permalink;
        this.userAvatar = userAvatar;
    }

    public User(JSONObject user) {
        try {
            this.id = user.getLong("id");
            this.username = user.getString("username");
            this.userAvatar = user.getString("avatar_url");
            this.permalink = user.getString("permalink");
            this.fullName = user.has("full_name") ? user.getString("full_name") : "Unknown";
            this.followersCount = user.has("followers_count") ? user.getInt("followers_count") : 0;
            this.followingsCount = user.has("followings_count") ? user.getInt("followings_count") : 0;
            this.tracksCount = user.has("track_count") ? user.getInt("track_count") : 0;
            this.playlistCount = user.has("playlist_count") ? user.getInt("playlist_count") : 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected User(Parcel in) {
        id = in.readLong();
        username = in.readString();
        permalink = in.readString();
        userAvatar = in.readString();
        fullName = in.readString();
        followersCount = in.readInt();
        followingsCount = in.readInt();
        tracksCount = in.readInt();
        playlistCount = in.readInt();
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public String getFullName() {
        return fullName;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getTracksCount() {
        return tracksCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    public int getPlaylistCount() {
        return playlistCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(username);
        dest.writeString(permalink);
        dest.writeString(userAvatar);
        dest.writeString(fullName);
        dest.writeInt(followersCount);
        dest.writeInt(followingsCount);
        dest.writeInt(tracksCount);
        dest.writeInt(playlistCount);
    }
}
