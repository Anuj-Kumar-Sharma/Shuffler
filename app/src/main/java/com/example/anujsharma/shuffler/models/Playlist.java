package com.example.anujsharma.shuffler.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuj5 on 07-01-2018.
 */

public class Playlist implements Parcelable {

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
    private static final String UNKNOWN = "Unknown";
    private String type = null;
    private long playlist_id, duration, user_id;
    private String genre, permalink, title, artwork_url;
    private int track_count, likes_count;
    private List<Song> songs;
    private User user;
    private boolean selfMade;


    public Playlist(JSONObject playlist) {
        try {
            playlist_id = playlist.getLong("id");
            duration = playlist.has("duration") ? playlist.getLong("duration") : 0;
            user_id = playlist.getLong("user_id");
            genre = playlist.has("genre") ? playlist.getString("genre") : UNKNOWN;
            permalink = playlist.has("permalink") ? playlist.getString("permalink") : UNKNOWN;
            title = playlist.has("title") ? playlist.getString("title") : UNKNOWN;
            artwork_url = playlist.has("artwork_url") ? playlist.getString("artwork_url") : UNKNOWN;
            track_count = playlist.has("track_count") ? playlist.getInt("track_count") : 0;
            likes_count = playlist.has("likes_count") ? playlist.getInt("likes_count") : 0;
            user = new User(playlist.getJSONObject("user"));
            JSONArray tracks = playlist.has("tracks") ? playlist.getJSONArray("tracks") : null;
            songs = new ArrayList<>();
            if (tracks != null)
                for (int i = 0; i < tracks.length(); i++) {
                    Song song = new Song(tracks.getJSONObject(i));
                    songs.add(song);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Playlist(List<Song> songs, String type) {
        this.type = type;
        this.songs = songs;
        this.title = type;
    }

    public Playlist(long playlist_id, String title, int track_count, String artwork_url) {
        this.playlist_id = playlist_id;
        this.title = title;
        this.track_count = track_count;
        this.artwork_url = artwork_url;
        this.selfMade = true;
    }

    protected Playlist(Parcel in) {
        playlist_id = in.readLong();
        duration = in.readLong();
        user_id = in.readLong();
        genre = in.readString();
        permalink = in.readString();
        title = in.readString();
        artwork_url = in.readString();
        track_count = in.readInt();
        songs = in.createTypedArrayList(Song.CREATOR);
        user = in.readParcelable(User.class.getClassLoader());
    }

    public boolean isSelfMade() {
        return this.selfMade;
    }

    public void setArtwork_url(String artwork_url) {
        this.artwork_url = artwork_url;
    }

    public void setTrack_count(int track_count) {
        this.track_count = track_count;
    }

    public long getPlaylistId() {
        return playlist_id;
    }

    public long getDuration() {
        return duration;
    }

    public long getUserId() {
        return user_id;
    }

    public String getGenre() {
        return genre;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtworkUrl() {
        return artwork_url;
    }

    public int getTracksCount() {
        return track_count;
    }

    public int getLikesCount() {
        return likes_count;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public User getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(playlist_id);
        dest.writeLong(duration);
        dest.writeLong(user_id);
        dest.writeString(genre);
        dest.writeString(permalink);
        dest.writeString(title);
        dest.writeString(artwork_url);
        dest.writeInt(track_count);
        dest.writeTypedList(songs);
        dest.writeParcelable(user, flags);
    }
}
