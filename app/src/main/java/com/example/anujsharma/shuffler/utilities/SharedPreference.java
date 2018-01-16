package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 04-01-2018.
 */

public class SharedPreference {

    public static final String CURRENT_PLAYING_SONG_ID = "currentPlayingSongId";
    public static final String CURRENT_SONG_LIST = "currentSongList";
    public static final String SHUFFLE_ON = "shuffleOn";
    public static final String REPEAT_ON = "repeatOn";


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
}
