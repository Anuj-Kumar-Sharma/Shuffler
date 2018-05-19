package com.example.anujsharma.shuffler.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.receivers.NotificationGenerator;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.FisherYatesShuffle;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.volley.Urls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuj5 on 13-01-2018.
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mp;
    private List<Song> songs;
    private int songPosition;
    private Playlist playlist;
    private SharedPreference pref;
    private Context context;
    private MusicServiceInterface musicServiceInterface;
    private ViewMusicInterface viewMusicInterface;
    private int state;
    private AudioManager audioManager;
    private NotificationGenerator notificationGenerator;
    private ArrayList<Integer> shuffleSongList;

    @Override
    public void onCreate() {
        super.onCreate();

        initPlayer();
        initialise();
    }

    private void initialise() {
        context = this;
        pref = new SharedPreference(context);
        playlist = pref.getCurrentPlaylist();
        state = -1;
        if (playlist != null) songs = playlist.getSongs();
        songPosition = pref.getCurrentPlayingSongPosition();
        shuffleSongList = pref.getCurrentPlaylistShuffleArray();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void initPlayer() {
        mp = new MediaPlayer();

        mp.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mp.setOnErrorListener(this);
        mp.setOnCompletionListener(this);
        mp.setOnBufferingUpdateListener(this);
        mp.setOnPreparedListener(this);
        mp.setOnSeekCompleteListener(this);
    }

    //activity will bind to service
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent) {
        mp.stop();
        mp.release();
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //check if playback has reached the end of a track
        if (mp.getCurrentPosition() > 0) {
            if (songPosition != pref.getCurrentPlaylist().getSongs().size() - 1) mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        mp.start();
        setState(Constants.MUSIC_STARTED);
        notificationGenerator.updateView(true, songPosition);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int current = getPosition();
                if (viewMusicInterface != null) viewMusicInterface.onMusicProgress(current / 100);
                if (songPosition < pref.getCurrentPlaylist().getSongs().size())
                    musicServiceInterface.onMusicProgress((int) ((current * 10000) / pref.getCurrentPlaylist().getSongs().get(songPosition).getDuration()));
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    public void setCallbacks(MusicServiceInterface musicServiceInterface) {
        this.musicServiceInterface = musicServiceInterface;
    }

    public void setViewMusicCallbacks(ViewMusicInterface viewMusicInterface) {
        this.viewMusicInterface = viewMusicInterface;
    }

    public void startSong() {
        final Song song = pref.getCurrentPlaylist().getSongs().get(songPosition);

        showNotification(song);

        setState(Constants.MUSIC_LOADED);
        musicServiceInterface.onSongChanged(songPosition);

        if (viewMusicInterface != null) {
            viewMusicInterface.onSongChanged(songPosition);
        }
        mp.reset();
        String url = song.getStreamUrl() + "?client_id=" + Urls.CLIENT_ID;
        pref.setCurrentPlayingSong(song.getId());

        try {
            mp.setDataSource(url);
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getSongPosition() {
        return this.songPosition;
    }

    public void setSongPosition(int songPosition) {
        this.songPosition = songPosition;
    }

    public int getPosition() {
        return mp.getCurrentPosition();
    }

    public int getDur() {
        return mp.getDuration();
    }

    public boolean isPlaying() {
        return mp.isPlaying();
    }

    public void pausePlayer() {
        mp.pause();
        setState(Constants.MUSIC_PAUSED);
        notificationGenerator.updateView(false, songPosition);
    }

    public void seek(int posn) {
        mp.seekTo(posn);
    }

    public void go() {

        if (state != -1) {
            audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            mp.start();
            setState(Constants.MUSIC_PLAYED);
            notificationGenerator.updateView(true, songPosition);
        } else {
            startSong();
        }
    }

    //skip to previous track
    public void playPrev() {

        if (pref.getIsShuffleOn()) {
            songPosition = FisherYatesShuffle.getPreviousShufflePosition(context);
        } else {
            if (songPosition == 0) {
                if (pref.getIsRepeatOn())
                    songPosition = pref.getCurrentPlaylist().getSongs().size() - 1;
                else {
                    setState(Constants.MUSIC_ENDED);
                    notificationGenerator.updateView(false, songPosition);
                }
            } else {
                songPosition--;
            }
        }
        startSong();
    }

    //skip to next
    public void playNext() {
        if (pref.getIsShuffleOn()) {
            songPosition = FisherYatesShuffle.getNextShufflePosition(context);

        } else {
            if (songPosition == pref.getCurrentPlaylist().getSongs().size() - 1) {
                if (pref.getIsRepeatOn())
                    songPosition = 0;
                else {
                    setState(Constants.MUSIC_ENDED);
                    notificationGenerator.updateView(false, songPosition);
                }
            } else {
                songPosition++;
            }
        }
        startSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    private void showNotification(final Song song) {
        notificationGenerator = new NotificationGenerator();
        Glide.with(context)
                .load(Utilities.getLargeArtworkUrl(song.getSongArtwork()))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        notificationGenerator.showSongNotification(context, songPosition, resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        notificationGenerator.showSongNotification(context, songPosition, null);
                    }
                });
    }

    @Override
    public void onAudioFocusChange(int i) {
        switch (i) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (getState() == Constants.MUSIC_PLAYED) go();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pausePlayer();
                break;
        }
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
        musicServiceInterface.onMusicDisturbed(state, pref.getCurrentPlaylist().getSongs().get(songPosition));
        if (viewMusicInterface != null) {
            viewMusicInterface.onMusicDisturbed(state, pref.getCurrentPlaylist().getSongs().get(songPosition));
        }
    }

    public interface MusicServiceInterface {
        void onMusicDisturbed(int state, Song song);

        void onSongChanged(int newPosition);

        void onMusicProgress(int position);
    }

    public interface ViewMusicInterface {
        void onMusicDisturbed(int state, Song song);

        void onSongChanged(int newPosition);

        void onMusicProgress(int position);
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
