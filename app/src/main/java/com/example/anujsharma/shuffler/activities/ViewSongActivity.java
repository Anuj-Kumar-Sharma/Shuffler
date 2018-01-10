package com.example.anujsharma.shuffler.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.adapters.SeeAllViewPagerAdapter;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.utilities.ZoomOutPageTransformer;

import java.util.List;

public class ViewSongActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ImageView ivBackButton, ivShowPlaylist, ivAddToLibrary, ivMenu, ivShuffle, ivPrevious, ivPlayButton, ivNext, ivRepeat;
    private TextView tvPlaylistName, tvSongName, tvArtistName, tvCurrentTime, tvDuration;
    private SeekBar seekBar;
    private ViewPager viewPager;

    private List<Song> songs;
    private Playlist currentPlaylist;
    private Song currentPlayingSong;
    private SeeAllViewPagerAdapter seeAllViewPagerAdapter;
    private Bitmap theBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_song);

        Intent intent = getIntent();
        currentPlaylist = intent.getParcelableExtra(Constants.PLAYLIST_MODEL_KEY);
        songs = currentPlaylist.getSongs();
        if (songs != null && songs.size() > 0)
            currentPlayingSong = songs.get(intent.getIntExtra(Constants.CURRENT_PLAYING_SONG_POSITION, 0));

        initialize();
        initializeListeners();
    }

    private void initializeListeners() {
        ivBackButton.setOnClickListener(this);
        ivShowPlaylist.setOnClickListener(this);
        ivAddToLibrary.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        ivShuffle.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivPlayButton.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivRepeat.setOnClickListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                final String songArtwork = Utilities.getLargeArtworkUrl(songs.get(position).getSongArtwork());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initialize() {
        context = this;
        ivBackButton = findViewById(R.id.ivBackButton);
        ivShowPlaylist = findViewById(R.id.ivShowPlaylist);
        ivAddToLibrary = findViewById(R.id.ivAddToLibrary);
        ivMenu = findViewById(R.id.ivShowSongMenu);
        ivShuffle = findViewById(R.id.ivShuffle);
        ivPrevious = findViewById(R.id.ivPrevious);
        ivPlayButton = findViewById(R.id.ivPlay);
        ivNext = findViewById(R.id.ivNext);
        ivRepeat = findViewById(R.id.ivRepeat);
        tvPlaylistName = findViewById(R.id.tvPlayingFromPlaylist);
        tvSongName = findViewById(R.id.tvSongName);
        tvArtistName = findViewById(R.id.tvArtistName);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvDuration = findViewById(R.id.tvDuration);
        seekBar = findViewById(R.id.seekBar);
        viewPager = findViewById(R.id.viewSongViewPager);

        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        seeAllViewPagerAdapter = new SeeAllViewPagerAdapter(context, songs);
        viewPager.setAdapter(seeAllViewPagerAdapter);
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

                break;
            case R.id.ivPrevious:

                break;
            case R.id.ivPlay:

                break;
            case R.id.ivNext:

                break;
            case R.id.ivRepeat:

                break;
        }
    }
}
