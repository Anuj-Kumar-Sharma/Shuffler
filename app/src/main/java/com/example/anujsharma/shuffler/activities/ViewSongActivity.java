package com.example.anujsharma.shuffler.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.adapters.SeeAllViewPagerAdapter;
import com.example.anujsharma.shuffler.backgroundTasks.GetColorPaletteFromImageUrl;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.services.MusicService;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.utilities.ZoomOutPageTransformer;

import java.util.List;

public class ViewSongActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private Context context;
    private ImageView ivBackButton, ivShowPlaylist, ivAddToLibrary, ivMenu, ivShuffle, ivPrevious, ivPlay, ivNext, ivRepeat;
    private TextView tvPlaylistName, tvSongName, tvArtistName, tvCurrentTime, tvDuration;
    private SeekBar seekBar;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;
    private List<Song> songs;
    private Playlist currentPlaylist;
    private Song currentPlayingSong;
    private SeeAllViewPagerAdapter seeAllViewPagerAdapter;
    private int currentPlayingPosition;
    private GradientDrawable gd;
    private MusicService musicService;
    private boolean musicBound;
    private Intent playIntent;
    private boolean isPlaying;
    private SharedPreference pref;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicService = binder.getService();
            musicBound = true;
            musicService.setViewMusicCallbacks(new MusicService.ViewMusicInterface() {
                @Override
                public void onMusicDisturbed(int state, Song song) {
                    switch (state) {
                        case Constants.MUSIC_STARTED:
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                            seekBar.setEnabled(true);
                            ivPlay.setClickable(true);
                            break;
                        case Constants.MUSIC_PLAYED:
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                            break;
                        case Constants.MUSIC_PAUSED:
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                            break;
                        case Constants.MUSIC_ENDED:
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                            break;
                        case Constants.MUSIC_LOADED:
                            tvSongName.setText(song.getTitle());
                            tvArtistName.setText(song.getArtist());
                            tvDuration.setText(Utilities.formatTime(song.getDuration()));
                            tvCurrentTime.setText(Utilities.formatTime(0));
                            seekBar.setProgress(0);
                            seekBar.setEnabled(false);
                            ivPlay.setClickable(false);
                            seekBar.setMax((int) (song.getDuration() / 100));
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                            break;
                    }
                }

                @Override
                public void onSongChanged(int newPosition) {
                    currentPlayingPosition = newPosition;
                    viewPager.setCurrentItem(newPosition, true);
                }

                @Override
                public void onMusicProgress(int position) {
                    seekBar.setProgress(position);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song);


        Intent intent = getIntent();
        currentPlaylist = intent.getParcelableExtra(Constants.PLAYLIST_MODEL_KEY);
        currentPlayingPosition = intent.getIntExtra(Constants.CURRENT_PLAYING_SONG_POSITION, 0);
        isPlaying = intent.getBooleanExtra(Constants.IS_PLAYING, true);
        songs = currentPlaylist.getSongs();
        if (songs != null && songs.size() > 0)
            currentPlayingSong = songs.get(currentPlayingPosition);

        initialize();
        initializeListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(getBaseContext(), MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playIntent);
        musicService = null;
        unbindService(musicConnection);
    }

    public void changeBackground(String url) {
        GetColorPaletteFromImageUrl getColorPaletteFromImageUrl = new GetColorPaletteFromImageUrl(context, new GetColorPaletteFromImageUrl.PaletteCallback() {
            @Override
            public void onPostExecute(Palette palette) {
                if (palette != null) changeBackground(palette.getDarkVibrantColor(0xFF616261));
                else changeBackground(0xFF616261);
            }
        });
        getColorPaletteFromImageUrl.execute(url);
    }

    public void changeBackground(int color) {
        gd.setColors(new int[]{color, context.getResources().getColor(R.color.bottom_gradient)});
        relativeLayout.setBackground(gd);
    }

    private void initializeListeners() {
        ivBackButton.setOnClickListener(this);
        ivShowPlaylist.setOnClickListener(this);
        ivAddToLibrary.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        ivShuffle.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivRepeat.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > currentPlayingPosition) {
                    musicService.playNext();
                } else if (position < currentPlayingPosition) {
                    musicService.playPrev();
                }
                currentPlayingPosition = position;
                changeBackground(songs.get(currentPlayingPosition).getSongArtwork());
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (musicService != null && b) {
                    musicService.seek(i * 100);
                }
                tvCurrentTime.setText(Utilities.formatTime((long) i * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                musicService.pausePlayer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.go();
            }
        });
    }

    private void initialize() {
        context = this;
        pref = new SharedPreference(context);
        ivBackButton = findViewById(R.id.ivBackButton);
        ivShowPlaylist = findViewById(R.id.ivShowPlaylist);
        ivAddToLibrary = findViewById(R.id.ivAddToLibrary);
        ivMenu = findViewById(R.id.ivShowSongMenu);
        ivShuffle = findViewById(R.id.ivShuffle);
        ivPrevious = findViewById(R.id.ivPrevious);
        ivPlay = findViewById(R.id.ivPlay);
        ivNext = findViewById(R.id.ivNext);
        ivRepeat = findViewById(R.id.ivRepeat);
        tvPlaylistName = findViewById(R.id.tvPlayingFromPlaylist);
        tvSongName = findViewById(R.id.tvSongName);
        tvArtistName = findViewById(R.id.tvArtistName);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvDuration = findViewById(R.id.tvDuration);
        seekBar = findViewById(R.id.seekBar);
        viewPager = findViewById(R.id.viewSongViewPager);

        if (songs.get(currentPlayingPosition).getSongArtwork() != null)
            changeBackground(songs.get(currentPlayingPosition).getSongArtwork());
        else
            changeBackground(0xFF616261);

        tvSongName.setText(songs.get(currentPlayingPosition).getTitle());
        tvSongName.setSelected(true);
        tvArtistName.setText(songs.get(currentPlayingPosition).getArtist());
        tvDuration.setText(Utilities.formatTime(songs.get(currentPlayingPosition).getDuration()));
        tvPlaylistName.setText(currentPlaylist.getTitle());

        if (isPlaying) ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        else ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        seekBar.setMax((int) (songs.get(currentPlayingPosition).getDuration() / 100));

        if (pref.getIsRepeatOn())
            ivRepeat.setColorFilter(context.getResources().getColor(R.color.colorAccent));
        if (pref.getIsShuffleOn())
            ivShuffle.setColorFilter(context.getResources().getColor(R.color.colorAccent));

        relativeLayout = findViewById(R.id.rlViewSongLayout);
        gd = new GradientDrawable();
        gd.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);

        relativeLayout.setBackground(gd);

        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        seeAllViewPagerAdapter = new SeeAllViewPagerAdapter(context, songs);
        viewPager.setAdapter(seeAllViewPagerAdapter);
        viewPager.setCurrentItem(currentPlayingPosition);

        if (MainActivity.musicSrv.getState() == Constants.MUSIC_LOADED) {
            seekBar.setEnabled(false);
            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackButton:
                finish();
                break;
            case R.id.ivShowPlaylist:

                break;
            case R.id.ivAddToLibrary:

                break;
            case R.id.ivShowSongMenu:

                break;
            case R.id.ivShuffle:
                if (musicService.isShuffle()) {
                    ivShuffle.setColorFilter(context.getResources().getColor(R.color.white));
                    musicService.setShuffle(false);
                } else {
                    ivShuffle.setColorFilter(context.getResources().getColor(R.color.colorAccent));
                    musicService.setShuffle(true);
                }
                break;
            case R.id.ivPrevious:
                if (songs != null && currentPlayingPosition - 1 >= 0) {
                    currentPlayingPosition--;
                    viewPager.setCurrentItem(currentPlayingPosition, true);
                }
                musicService.playPrev();
                break;
            case R.id.ivPlay:
                if (musicService.getState() != Constants.MUSIC_LOADED) {
                    if (musicService.isPlaying()) {
                        musicService.pausePlayer();
                    } else {
                        musicService.go();
                    }
                }
                break;
            case R.id.ivNext:
                if (songs != null && currentPlayingPosition + 1 < songs.size()) {
                    currentPlayingPosition++;
                    viewPager.setCurrentItem(currentPlayingPosition, true);
                }
                musicService.playNext();
                break;
            case R.id.ivRepeat:
                if (musicService.isRepeat()) {
                    ivRepeat.setColorFilter(context.getResources().getColor(R.color.white));
                    musicService.setRepeat(false);
                } else {
                    ivRepeat.setColorFilter(context.getResources().getColor(R.color.colorAccent));
                    musicService.setRepeat(true);
                }
                break;
        }
    }
}
