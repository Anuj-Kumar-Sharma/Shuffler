package com.example.anujsharma.shuffler.services;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.volley.Urls;

import java.io.IOException;

public class MediaSessionCallback extends MediaSessionCompat.Callback implements MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener {

    private static final String TAG = "MediaSessionCallback";
    private Context mContext;
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private PlaybackStateCompat.Builder playBackStateBuilder;
    private MediaSessionCompat mSession;
    private SharedPreference pref;

    public MediaSessionCallback(Context context) {
        super();
        mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        this.playBackStateBuilder = new PlaybackStateCompat.Builder();
        this.mSession = new MediaSessionCompat(context, TAG);
        mSession.setCallback(this);
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        pref = new SharedPreference(context);
    }

    @Override
    public void onPlay() {
        super.onPlay();
        mediaPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseResources();
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
    }

    @Override
    public void onSkipToNext() {
        super.onSkipToNext();
        pref.setCurrentPlayingSongPosition(pref.getCurrentPlayingSongPosition() + 1);
        mediaPlay();
    }

    @Override
    public void onSkipToPrevious() {
        super.onSkipToPrevious();
        pref.setCurrentPlayingSongPosition(pref.getCurrentPlayingSongPosition() - 1);
        mediaPlay();
    }

    private void releaseResources() {
        mSession.setActive(false);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void mediaPlay() {
        Song song = pref.getCurrentPlaylist().getSongs().get(pref.getCurrentPlayingSongPosition());
        mMediaPlayer.reset();
        String url = song.getStreamUrl() + "?client_id=" + Urls.CLIENT_ID;
        int requestAudioFocusResult = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (requestAudioFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mSession.setActive(true);
            playBackStateBuilder.setActions(PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_STOP);
            playBackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mMediaPlayer.getCurrentPosition(), 1.0f, SystemClock.elapsedRealtime());
            mSession.setPlaybackState(playBackStateBuilder.build());
            try {
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mediaPause() {
        mMediaPlayer.pause();
        playBackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP);
        playBackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                mMediaPlayer.getCurrentPosition(), 1.0f, SystemClock.elapsedRealtime());
        mSession.setPlaybackState(playBackStateBuilder.build());
        mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playBackStateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_STOP);
        playBackStateBuilder.setState(PlaybackStateCompat.STATE_STOPPED,
                mMediaPlayer.getCurrentPosition(), 1.0f, SystemClock.elapsedRealtime());
        mSession.setPlaybackState(playBackStateBuilder.build());
    }

    @Override
    public void onAudioFocusChange(int audioFocusChanged) {
        switch (audioFocusChanged) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mediaPause();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                mediaPlay();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mediaPause();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }
}