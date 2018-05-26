package com.example.anujsharma.shuffler.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.graphics.Palette;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.backgroundTasks.GetBitmapFromImageUrlTask;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.volley.Urls;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerService extends Service {

    private SimpleExoPlayer player;
    private Context context;
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private SharedPreference pref;

    private Playlist currentPlaylist;
    private int currentPosition;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        pref = new SharedPreference(context);

        currentPlaylist = pref.getCurrentPlaylist();
        currentPosition = pref.getCurrentPlayingSongPosition();

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();

        /*player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());

        // add media sources to the concatenatingMediaSource
        for(Song song : currentPlaylist.getSongs()) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(song.getStreamUrl()));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }*/

        DefaultRenderersFactory renderedFactory = new DefaultRenderersFactory(getApplicationContext());
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        AdaptiveTrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        DefaultLoadControl loadControl = new DefaultLoadControl();

        player = ExoPlayerFactory.newSimpleInstance(renderedFactory, trackSelector, loadControl);

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, getString(R.string.app_name)));
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        Handler mainHandler = new Handler();
        if (currentPlaylist != null)
            for (Song song : currentPlaylist.getSongs()) {
                String completeUrl = song.getStreamUrl() + "?client_id=" + Urls.CLIENT_ID;
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(completeUrl),
                        dataSourceFactory,
                        extractorsFactory,
                        mainHandler,
                        null);
                concatenatingMediaSource.addMediaSource(mediaSource);
            }


        player.prepare(concatenatingMediaSource);
        // seek to current song position
        player.seekTo(currentPosition, C.TIME_UNSET);

        player.setPlayWhenReady(true);

        // manage notification here
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                Constants.NOTIFICATION_SONG_CHANNEL_ID,
                R.string.playback_channel_name,
                Constants.NOTIFICATION_SONG_ID,
                new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        if (currentPlaylist != null)
                            return currentPlaylist.getSongs().get(player.getCurrentWindowIndex()).getTitle();
                        else return "";
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(context, MainActivity.class);
                        if (currentPlaylist != null)
                            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        if (currentPlaylist != null)
                            return currentPlaylist.getSongs().get(player.getCurrentWindowIndex()).getArtist();
                        else return "";
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(final Player player, final PlayerNotificationManager.BitmapCallback callback) {
                        if (currentPlaylist != null) {
                            String imageUrl = Utilities.getLargeArtworkUrl(currentPlaylist.getSongs().get(player.getCurrentWindowIndex()).getSongArtwork());
                            new GetBitmapFromImageUrlTask(context, new GetBitmapFromImageUrlTask.BitmapFromUrlCallback() {
                                @Override
                                public void onBitmapFound(Bitmap bitmap) {
                                    callback.onBitmap(bitmap);
                                    Palette palette = Palette.from(bitmap).generate();
                                    playerNotificationManager.setColor(palette.getDarkMutedColor(R.color.bottom_gradient));
                                }
                            }).execute(imageUrl);
                        }
                        return BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.ic_headphones);
                    }
                }
        );
        playerNotificationManager.setColorized(true);
        playerNotificationManager.setFastForwardIncrementMs(0);
        playerNotificationManager.setRewindIncrementMs(0);
        playerNotificationManager.setStopAction(null);

        playerNotificationManager.setOngoing(false);

        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });
        playerNotificationManager.setPlayer(player);

        // Media session here

        mediaSession = new MediaSessionCompat(context, Constants.MEDIA_SESSION_TAG);
        mediaSession.setActive(true);
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());

        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {

            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                // here getMediaDescription from songs in playlist and return
                if (currentPlaylist != null)
                    return currentPlaylist.getSongs().get(windowIndex).getMediaDescription(context);
                else return null;
            }
        });
        mediaSessionConnector.setPlayer(player, null);
    }

    @Override
    public void onDestroy() {
        mediaSession.release();
        mediaSessionConnector.setPlayer(null, null);
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentPlaylist = intent.getParcelableExtra(Constants.PLAYLIST_MODEL_KEY);
        currentPosition = intent.getIntExtra(Constants.CURRENT_PLAYING_SONG_POSITION, 0);
        return START_STICKY;
    }
}
