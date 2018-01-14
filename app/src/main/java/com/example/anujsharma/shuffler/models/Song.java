package com.example.anujsharma.shuffler.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class Song implements Parcelable {
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    private long id, duration;
    private String title, artist, genre, album;
    private File songFile;
    private String permalink;
    private String songArtwork, streamUrl;
    private int playbackCount, likesCount, favoritngsCount;
    private User user;

    public Song(long id, long duration, String title, String artist, String genre, String permalink,
                String songArtwork, String streamUrl, int playbackCount, int likesCount, User user) {
        this.id = id;
        this.duration = duration;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.permalink = permalink;
        this.songArtwork = songArtwork;
        this.streamUrl = streamUrl;
        this.playbackCount = playbackCount;
        this.likesCount = likesCount;
        this.user = user;
    }

    public Song(String title, String artist, String genre, String album, int duration, File songFile) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.album = album;
        this.duration = duration;
        this.songFile = songFile;
    }

    public Song(JSONObject song) {
        try {
            this.id = song.getLong("id");
            this.duration = song.getLong("duration");
            this.title = song.getString("title").trim();
            this.genre = song.getString("genre").trim();
            this.permalink = song.getString("permalink").trim();
            this.songArtwork = song.getString("artwork_url");
            this.streamUrl = song.getString("stream_url");
            this.playbackCount = song.has("playback_count") ? song.getInt("playback_count") : 0;
            this.likesCount = song.has("likes_count") ? song.getInt("likes_count") : 0;
            this.favoritngsCount = song.has("favoritings_count") ? song.getInt("favoritings_count") : 0;
            this.user = new User(song.getJSONObject("user"));
            this.artist = this.user.getUsername();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Song(Parcel in) {
        id = in.readLong();
        duration = in.readLong();
        title = in.readString();
        artist = in.readString();
        genre = in.readString();
        album = in.readString();
        permalink = in.readString();
        songArtwork = in.readString();
        streamUrl = in.readString();
        playbackCount = in.readInt();
        likesCount = in.readInt();
        favoritngsCount = in.readInt();
    }

    public User getUser() {
        return user;
    }

    public long getId() {
        return id;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getSongArtwork() {
        return songArtwork;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public int getPlaybackCount() {
        return playbackCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public long getDuration() {
        return duration;
    }

    public File getSongFile() {
        return songFile;
    }

    public int getFavoritngsCount() {
        return favoritngsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(duration);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(genre);
        dest.writeString(album);
        dest.writeString(permalink);
        dest.writeString(songArtwork);
        dest.writeString(streamUrl);
        dest.writeInt(playbackCount);
        dest.writeInt(likesCount);
        dest.writeInt(favoritngsCount);
    }
}
