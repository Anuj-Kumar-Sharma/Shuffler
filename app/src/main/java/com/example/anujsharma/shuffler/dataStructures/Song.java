package com.example.anujsharma.shuffler.dataStructures;

import java.io.File;

public class Song {
    private String title, artist, genre, album;
    private int duration;
    private File songFile;

    public Song(String title, String artist, String genre, String album, int duration, File songFile) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.album = album;
        this.duration = duration;
        this.songFile = songFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public File getSongFile() {
        return songFile;
    }

    public void setSongFile(File songFile) {
        this.songFile = songFile;
    }
}
