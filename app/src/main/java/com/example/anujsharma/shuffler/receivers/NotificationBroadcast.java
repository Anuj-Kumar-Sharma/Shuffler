package com.example.anujsharma.shuffler.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.services.MusicService;
import com.example.anujsharma.shuffler.utilities.Constants;

import java.util.Objects;

/**
 * Created by anuj5 on 20-01-2018.
 */

public class NotificationBroadcast extends BroadcastReceiver {

    private static final String TAG = "NotificationBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Constants.CLICK_NEXT)) {
            MusicService musicService = MainActivity.musicSrv;
            if (musicService != null) musicService.playNext();
        } else if (Objects.equals(intent.getAction(), Constants.CLICK_PLAY_PAUSE)) {
            MusicService musicService = MainActivity.musicSrv;
            if (musicService != null && musicService.getState() != Constants.MUSIC_LOADED) {
                if (musicService.isPlaying()) musicService.pausePlayer();
                else musicService.go();
            }
        } else if (Objects.equals(intent.getAction(), Constants.CLICK_PREVIOUS)) {
            MusicService musicService = MainActivity.musicSrv;
            if (musicService != null) musicService.playPrev();
        }
    }
}
