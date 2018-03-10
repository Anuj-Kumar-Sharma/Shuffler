package com.example.anujsharma.shuffler.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.utilities.Constants;

/**
 * Created by anuj5 on 16-01-2018.
 */

public class NotificationGenerator {

    static RemoteViews remoteViews;
    static NotificationCompat.Builder notificationBuilder;
    static NotificationManager notificationManager;

    public static void showSongNotification(Context context, String title, String artist, Bitmap bitmap) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_song);

        remoteViews.setTextViewText(R.id.tvNotificationSongTitle, title);
        remoteViews.setTextViewText(R.id.tvNotificationArtistName, artist);
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.ivNotificationImage, bitmap);
        } else {
            remoteViews.setImageViewResource(R.id.ivNotificationImage, R.drawable.ic_headphones);
        }

        notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), Constants.NOTIFICATION_SONG_CHANNEL_ID);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.lg_shuffle)
                .setContentTitle("Shuffler")
                .setContentText("Song Notification")
                .setContent(remoteViews);

        Intent next = new Intent(Constants.CLICK_NEXT);
        Intent previous = new Intent(Constants.CLICK_PREVIOUS);
        Intent playPause = new Intent(Constants.CLICK_PLAY_PAUSE);

        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationNext, nextPendingIntent);

        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationPrevious, previousPendingIntent);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playPause, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationPlay, playPendingIntent);
        notificationBuilder.setOngoing(true);
        notificationManager.notify(Constants.NOTIFICATION_SONG_ID, notificationBuilder.build());
    }

    public static void updateView(boolean isPlaying) {
        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_pause);
            notificationBuilder.setOngoing(true);
        } else {
            notificationBuilder.setOngoing(false);
            remoteViews.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_play);
        }

        notificationBuilder.setContent(remoteViews);
        notificationManager.notify(Constants.NOTIFICATION_SONG_ID, notificationBuilder.build());
    }
}
