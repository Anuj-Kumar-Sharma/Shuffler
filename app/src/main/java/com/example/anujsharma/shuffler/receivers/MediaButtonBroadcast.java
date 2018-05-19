package com.example.anujsharma.shuffler.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class MediaButtonBroadcast extends BroadcastReceiver {

    private static final String TAG = "MediaButtonBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "something received");

        String intentAction = intent.getAction();
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null) {
            return;
        }
        int action = event.getAction();
        if (action == KeyEvent.ACTION_DOWN) {
            Log.d(TAG, "button clicked");
        }
        abortBroadcast();

    }
}
