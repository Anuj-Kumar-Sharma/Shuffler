package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 04-01-2018.
 */

public class SharedPreference {

    public static final String CURRENT_PLAYING_SONG_ID = "currentPlayingSongId";


    private SharedPreferences pref;
    private int PRIVATE_MODE = 0;

    public SharedPreference(Context context) {
        pref = context.getSharedPreferences(context.getString(R.string.shared_pref_name), PRIVATE_MODE);
    }

    public String getCurrentPlayingSong() {
        return pref.getString(CURRENT_PLAYING_SONG_ID, null);
    }

    public void setCurrentPlayingSong(String songId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(CURRENT_PLAYING_SONG_ID, songId);
        editor.apply();
    }
}
