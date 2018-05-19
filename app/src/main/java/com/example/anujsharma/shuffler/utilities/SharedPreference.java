package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.models.Playlist;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by anuj5 on 04-01-2018.
 */

public class SharedPreference {

    public static final String CURRENT_PLAYING_SONG_ID = "currentPlayingSongId";
    public static final String CURRENT_PLAYLIST = "currentSongList";
    public static final String CURRENT_PLAYING_SONG_POSITION = "currentPlayingSongPosition";

    public static final String SHUFFLE_ON = "shuffleOn";
    public static final String REPEAT_ON = "repeatOn";
    private static final String CURRENT_PLAYLIST_SHUFFLE_STRING = "playlistShuffleArray";
    private static final String CURRENT_SHUFFLE_SONG_POSITION = "currentShuffleSongPosition";


    private SharedPreferences pref;
    private int PRIVATE_MODE = 0;

    public SharedPreference(Context context) {
        pref = context.getSharedPreferences(context.getString(R.string.shared_pref_name), PRIVATE_MODE);
    }

    public long getCurrentPlayingSong() {
        return pref.getLong(CURRENT_PLAYING_SONG_ID, 0);
    }

    public void setCurrentPlayingSong(long songId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(CURRENT_PLAYING_SONG_ID, songId);
        editor.apply();
    }

    public boolean getIsShuffleOn() {
        return pref.getBoolean(SHUFFLE_ON, false);
    }

    public void setIsShuffleOn(boolean shuffleOn) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(SHUFFLE_ON, shuffleOn);
        editor.apply();
    }

    public boolean getIsRepeatOn() {
        return pref.getBoolean(REPEAT_ON, false);
    }

    public void setIsRepeatOn(boolean repeatOn) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(REPEAT_ON, repeatOn);
        editor.apply();
    }

    public Playlist getCurrentPlaylist() {
        String playlistJson = pref.getString(CURRENT_PLAYLIST, null);
        Gson gson = new Gson();
        return gson.fromJson(playlistJson, Playlist.class);
    }

    public void setCurrentPlaylist(Playlist playlist) {
        Gson gson = new Gson();
        String playlistJson = gson.toJson(playlist);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CURRENT_PLAYLIST, playlistJson);
        editor.apply();
    }

    public int getCurrentPlayingSongPosition() {
        return pref.getInt(CURRENT_PLAYING_SONG_POSITION, 0);
    }

    public void setCurrentPlayingSongPosition(int position) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CURRENT_PLAYING_SONG_POSITION, position);
        editor.apply();
    }

    public ArrayList<Integer> getCurrentPlaylistShuffleArray() {
        String playlistShuffleArrayJson = pref.getString(CURRENT_PLAYLIST_SHUFFLE_STRING, null);
        Gson gson = new Gson();
        ArrayList list = gson.fromJson(playlistShuffleArrayJson, ArrayList.class);
        ArrayList<Integer> uList = new ArrayList<>();
        if (list != null)
            for (Object e : list) {
                Double d = (Double) e;
                Integer i = d.intValue();
                uList.add(i);
            }
        return uList;
    }

    public void setCurrentPlaylistShuffleArray(ArrayList<Integer> playlistShuffleArray) {
        Gson gson = new Gson();
        String playlistShuffleArrayString = gson.toJson(playlistShuffleArray);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CURRENT_PLAYLIST_SHUFFLE_STRING, playlistShuffleArrayString);
        editor.apply();
    }

    public int getCurrentShuffleSongPosition() {
        return pref.getInt(CURRENT_SHUFFLE_SONG_POSITION, 0);
    }

    public void setCurrentShuffleSongPosition(int position) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CURRENT_SHUFFLE_SONG_POSITION, position);
        editor.apply();
    }
}
