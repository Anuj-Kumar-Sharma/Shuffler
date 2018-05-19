package com.example.anujsharma.shuffler.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;

/**
 * Created by anuj5 on 16-01-2018.
 */

public class NotificationGenerator {

    private static RemoteViews remoteViews, bigRemoteView;
    private static NotificationCompat.Builder notificationBuilder;
    private static NotificationManager notificationManager;

    private int currentSongPosition;
    private boolean isPlaying = true;

    public void showSongNotification(Context context, int currentSongPosition, Bitmap bitmap) {

        SharedPreference pref = new SharedPreference(context);
        Playlist currentPlaylist = pref.getCurrentPlaylist();
        this.currentSongPosition = currentSongPosition;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_song);
        bigRemoteView = new RemoteViews(context.getPackageName(), R.layout.notification_expand_song);

        remoteViews.setTextViewText(R.id.tvNotificationSongTitle, currentPlaylist.getSongs().get(currentSongPosition).getTitle());
        remoteViews.setTextViewText(R.id.tvNotificationArtistName, currentPlaylist.getSongs().get(currentSongPosition).getArtist());
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.ivNotificationImage, bitmap);
        } else {
            remoteViews.setImageViewResource(R.id.ivNotificationImage, R.drawable.ic_headphones);
        }

        bigRemoteView.setTextViewText(R.id.tvNotificationSongTitle, currentPlaylist.getSongs().get(currentSongPosition).getTitle());
        bigRemoteView.setTextViewText(R.id.tvNotificationArtistName, currentPlaylist.getSongs().get(currentSongPosition).getArtist());
        if (bitmap != null) {
            bigRemoteView.setImageViewBitmap(R.id.ivNotificationImage, bitmap);
        } else {
            bigRemoteView.setImageViewResource(R.id.ivNotificationImage, R.drawable.ic_headphones);
        }

        notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), Constants.NOTIFICATION_SONG_CHANNEL_ID);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.putExtra(Constants.FROM_NOTIFICATION, true);
        notifyIntent.putExtra(Constants.PLAYLIST_MODEL_KEY, currentPlaylist);
        notifyIntent.putExtra(Constants.CURRENT_PLAYING_SONG_POSITION, this.currentSongPosition);
        notifyIntent.putExtra(Constants.IS_PLAYING, isPlaying);


//        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.lg_shuffle)
                .setContentTitle("Shuffler")
                .setContentText("Song Notification")
                .setContent(remoteViews)
                .setCustomBigContentView(bigRemoteView)
                .setPriority(Notification.PRIORITY_MAX);

        Intent next = new Intent(Constants.CLICK_NEXT);
        Intent previous = new Intent(Constants.CLICK_PREVIOUS);
        Intent playPause = new Intent(Constants.CLICK_PLAY_PAUSE);

        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationNext, nextPendingIntent);
        bigRemoteView.setOnClickPendingIntent(R.id.ivNotificationNext, nextPendingIntent);

        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationPrevious, previousPendingIntent);
        bigRemoteView.setOnClickPendingIntent(R.id.ivNotificationPrevious, previousPendingIntent);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playPause, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ivNotificationPlay, playPendingIntent);
        bigRemoteView.setOnClickPendingIntent(R.id.ivNotificationPlay, playPendingIntent);
        notificationBuilder.setOngoing(true);
        notificationManager.notify(Constants.NOTIFICATION_SONG_ID, notificationBuilder.build());
    }

    public void updateView(boolean isPlaying, int currentSongPosition) {
        this.isPlaying = isPlaying;
        this.currentSongPosition = currentSongPosition;

        if (isPlaying) {
            remoteViews.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_pause);
            bigRemoteView.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_pause);
            notificationBuilder.setOngoing(true);
        } else {
            notificationBuilder.setOngoing(false);
            remoteViews.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_play);
            bigRemoteView.setImageViewResource(R.id.ivNotificationPlay, R.drawable.ic_play);
        }

        notificationBuilder.setContent(remoteViews);
        notificationBuilder.setCustomBigContentView(bigRemoteView);
        notificationManager.notify(Constants.NOTIFICATION_SONG_ID, notificationBuilder.build());
    }
}
