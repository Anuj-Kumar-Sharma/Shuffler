package com.example.anujsharma.shuffler.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.utilities.Constants;

/**
 * Created by anuj5 on 16-01-2018.
 */

public class NotificationGenerator {

    public static void showSongNotification(Context context, String title, String artist, Bitmap bitmap) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_song);

        remoteViews.setTextViewText(R.id.tvNotificationSongTitle, title);
        remoteViews.setTextViewText(R.id.tvNotificationArtistName, artist);
        /*Glide.with(context)
                .load(Utilities.getLargeArtworkUrl(artworkUrl))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        remoteViews.setImageViewBitmap(R.id.ivNotificationImage, resource);
                    }
                });*/

        remoteViews.setImageViewBitmap(R.id.ivNotificationImage, bitmap);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), Constants.NOTIFICATION_SONG_CHANNEL_ID);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.lg_shuffle)
                .setAutoCancel(true)
                .setContentTitle("Shuffler")
                .setContentText("Song Notification")
                .setContent(remoteViews);

        Log.d("TAG", "notification");

        notificationManager.notify(Constants.NOTIFICATION_SONG_ID, notificationBuilder.build());

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(Constants.NOTIFICATION_SONG_ID, mBuilder.build());
*/
    }
}
