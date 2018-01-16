package com.example.anujsharma.shuffler.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.receivers.NotificationGenerator;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.volley.Urls;

import java.io.IOException;
import java.util.List;

/**
 * Created by anuj5 on 13-01-2018.
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer mp;
    private List<Song> songs;
    private int songPosition;
    private SharedPreference pref;
    private Context context;
    private boolean shuffle;
    private boolean repeat;
    private MusicServiceInterface musicServiceInterface;
    private ViewMusicInterface viewMusicInterface;

    @Override
    public void onCreate() {
        super.onCreate();

        initPlayer();
        initialise();
    }

    private void initialise() {
        context = this;
        pref = new SharedPreference(context);
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
            mp.reset();
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
        mp.start();
        musicServiceInterface.onMusicDisturbed(Constants.MUSIC_STARTED, songs.get(songPosition));
        if (viewMusicInterface != null) {
            viewMusicInterface.onMusicDisturbed(Constants.MUSIC_STARTED, songs.get(songPosition));
        }

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
        final Song song = songs.get(songPosition);

        showNotification(song);

        musicServiceInterface.onMusicDisturbed(Constants.MUSIC_LOADED, song);
        musicServiceInterface.onSongChanged(songPosition);

        if (viewMusicInterface != null) {
            viewMusicInterface.onMusicDisturbed(Constants.MUSIC_LOADED, song);
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

    public int getPosn() {
        return mp.getCurrentPosition();
    }

    public int getDur() {
        return mp.getDuration();
    }

    public boolean isPlaying() {
        return mp.isPlaying();
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
        pref.setIsRepeatOn(repeat);
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        pref.setIsShuffleOn(shuffle);
    }

    public void pausePlayer() {
        mp.pause();
        musicServiceInterface.onMusicDisturbed(Constants.MUSIC_PAUSED, songs.get(songPosition));
        if (viewMusicInterface != null) {
            viewMusicInterface.onMusicDisturbed(Constants.MUSIC_PAUSED, songs.get(songPosition));
        }
    }

    public void seek(int posn) {
        mp.seekTo(posn);
    }

    public void go() {
        mp.start();
        musicServiceInterface.onMusicDisturbed(Constants.MUSIC_PLAYED, songs.get(songPosition));
        if (viewMusicInterface != null) {
            viewMusicInterface.onMusicDisturbed(Constants.MUSIC_PLAYED, songs.get(songPosition));
        }
    }

    //skip to previous track
    public void playPrev() {
        songPosition--;
        if (songPosition < 0) {
            if (repeat)
                songPosition = songs.size() - 1;
            else return;
        }
        startSong();
    }

    //skip to next
    public void playNext() {
        if (shuffle) {
            /*int newSong = songPosition;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.size());
            }
            songPosn=newSong;*/
        } else {
            songPosition++;
            if (songPosition >= songs.size()) {
                if (repeat)
                    songPosition = 0;
                else return;
            }
        }
        startSong();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    //toggle shuffle
    public void setShuffle() {
        shuffle = !shuffle;
    }

    private void showNotification(final Song song) {
        Glide.with(context)
                .load(Utilities.getLargeArtworkUrl(song.getSongArtwork()))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        NotificationGenerator.showSongNotification(context, song.getTitle(), song.getArtist(), resource);
                    }
                });
    }

    public interface MusicServiceInterface {
        void onMusicDisturbed(int state, Song song);

        void onSongChanged(int newPosition);
    }

    public interface ViewMusicInterface {
        void onMusicDisturbed(int state, Song song);

        void onSongChanged(int newPosition);
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
