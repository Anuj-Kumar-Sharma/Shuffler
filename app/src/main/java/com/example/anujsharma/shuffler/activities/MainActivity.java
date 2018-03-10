package com.example.anujsharma.shuffler.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.dao.TracksDao;
import com.example.anujsharma.shuffler.fragments.HomeFragment;
import com.example.anujsharma.shuffler.fragments.SearchFragment;
import com.example.anujsharma.shuffler.fragments.YourLibraryFragment;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.services.MusicService;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.volley.RequestCallback;
import com.example.anujsharma.shuffler.volley.Urls;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RequestCallback {

    public static final String TAG = "TAG";
    public static final String HOME_FRAGMENT = "homeFragment";
    public static final String SEARCH_FRAGMENT = "searchFragment";
    public static final String YOUR_LIBRARY_FRAGMENT = "yourLibraryFragment";
    //service
    public static MusicService musicSrv;
    private final int REQUEST_PERMS_CODE = 1;
    SharedPreference pref;
    private boolean initHomeFragment, initSearchFragment, initYourLibraryFragment;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private YourLibraryFragment yourLibraryFragment;
    //    private MediaPlayer mediaPlayer;
    private Context context;
    private ProgressBar mainSongLoader;
    private View progressView;
    private TextView tvHome, tvSearch, tvMyProfile, tvSongName;
    private ImageView ivPlay, ivNext, ivFullView;
    private TracksDao tracksDao;
    private int currentSongPosition;
    private Playlist currentPlaylist;
    private Intent playIntent;
    //binding
    private boolean musicBound = false;
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;
            musicSrv.setCallbacks(new MusicService.MusicServiceInterface() {
                @Override
                public void onMusicDisturbed(int state, Song song) {
                    switch (state) {
                        case Constants.MUSIC_STARTED:
                            ivPlay.setClickable(true);
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
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
                            ivPlay.setClickable(false);
                            tvSongName.setText(song.getTitle());
                            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                            break;
                    }
                }

                @Override
                public void onSongChanged(int newPosition) {
                    currentSongPosition = newPosition;
                }

                @Override
                public void onMusicProgress(int position) {
                    progressView.getBackground().setLevel(position);
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
        setContentView(R.layout.activity_main);


        if (playIntent == null) {
            playIntent = new Intent(getBaseContext(), MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        initialise();
        initialiseListeners();
        if (hasPermissons()) {
            mainStuff();
        } else {
            requestPermissions();
        }
    }

    public void playSongInMainActivity(int songPosition, Playlist playlist) {
        Song song = playlist.getSongs().get(songPosition);
        currentSongPosition = songPosition;
        pref.setCurrentPlayingSong(song.getId());
        pref.setCurrentPlaylist(playlist);
        pref.setCurrentPlayingSongPosition(songPosition);
        tvSongName.setText(song.getTitle());
        this.currentPlaylist = playlist;
        String url = song.getStreamUrl() + "?client_id=" + Urls.CLIENT_ID;
        Log.d("TAG", "currently playing " + url);

        musicSrv.setSongPosition(songPosition);
        musicSrv.setSongs(playlist.getSongs());
        musicSrv.startSong();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playIntent);
        musicSrv = null;
        unbindService(musicConnection);
    }

    private void initialiseListeners() {

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicSrv.isPlaying()) {
                    musicSrv.pausePlayer();
                } else {
                    musicSrv.go();
                }
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicSrv.playNext();
            }
        });

        ivFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlaylist != null) {
                    Intent intent = new Intent(context, ViewSongActivity.class);
                    intent.putExtra(Constants.PLAYLIST_MODEL_KEY, currentPlaylist);
                    intent.putExtra(Constants.CURRENT_PLAYING_SONG_POSITION, currentSongPosition);
                    intent.putExtra(Constants.IS_PLAYING, musicSrv.isPlaying());
                    context.startActivity(intent);
                }
            }
        });

        tvSongName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlaylist != null) {
                    Intent intent = new Intent(context, ViewSongActivity.class);
                    intent.putExtra(Constants.PLAYLIST_MODEL_KEY, currentPlaylist);
                    intent.putExtra(Constants.CURRENT_PLAYING_SONG_POSITION, currentSongPosition);
                    intent.putExtra(Constants.IS_PLAYING, musicSrv.isPlaying());
                    context.startActivity(intent);
                }
            }
        });
    }

    public void initialise() {
        context = getApplicationContext();
        pref = new SharedPreference(context);
        tracksDao = new TracksDao(context, this);

        tvHome = findViewById(R.id.xtvHome);
        tvSearch = findViewById(R.id.xtvSearch);
        tvMyProfile = findViewById(R.id.xtvMyProfile);
        tvSongName = findViewById(R.id.tvSongName);
        mainSongLoader = findViewById(R.id.pbLoadSong);
        tvSongName.setSelected(true);
        ivFullView = findViewById(R.id.ivUpArrow);
        ivNext = findViewById(R.id.ivPlayNext);
        ivPlay = findViewById(R.id.ivPlaySong);
        progressView = findViewById(R.id.progressView);

        currentPlaylist = pref.getCurrentPlaylist();
        currentSongPosition = pref.getCurrentPlayingSongPosition();
        if (currentPlaylist != null) {
            Song currentSong = currentPlaylist.getSongs().get(currentSongPosition);
            tvSongName.setText(currentSong.getTitle());
        }
    }


    public void modifyBottomLayout(int position) {
        switch (position) {
            case 0:
                tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_selected, 0, 0);
                tvHome.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search, 0, 0);
                tvSearch.setTextColor(ContextCompat.getColor(context, R.color.color_unselected));
                tvMyProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_library, 0, 0);
                tvMyProfile.setTextColor(ContextCompat.getColor(context, R.color.color_unselected));
                break;
            case 1:
                tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
                tvHome.setTextColor(ContextCompat.getColor(context, R.color.color_unselected));
                tvSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search_selected, 0, 0);
                tvSearch.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvMyProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_library, 0, 0);
                tvMyProfile.setTextColor(ContextCompat.getColor(context, R.color.color_unselected));
                break;
            case 2:
                tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
                tvHome.setTextColor(ContextCompat.getColor(context, R.color.color_unselected));
                tvSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search, 0, 0);
                tvSearch.setTextColor(ContextCompat.getColor(context, R.color.color_unselected));
                tvMyProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_library_selected, 0, 0);
                tvMyProfile.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
        }
    }

    public void homeTabClicked(View view) {
        if (!initHomeFragment) {
            initHomeFragment = true;
            homeFragment = new HomeFragment();
        }
        addFragmentToMainFrameContainer(homeFragment, HOME_FRAGMENT);
    }

    public void searchTabClicked(View view) {
        if (!initSearchFragment) {
            initSearchFragment = true;
            searchFragment = new SearchFragment();
        }
        addFragmentToMainFrameContainer(searchFragment, SEARCH_FRAGMENT);
    }

    public void yourLibraryClicked(View view) {
        if (!initYourLibraryFragment) {
            initYourLibraryFragment = true;
            yourLibraryFragment = new YourLibraryFragment();
        }
        addFragmentToMainFrameContainer(yourLibraryFragment, YOUR_LIBRARY_FRAGMENT);
    }


    public void addFragmentToMainFrameContainer(Fragment fragment, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainFrameContainer);
        if (currentFragment != null && currentFragment.getClass().equals(fragment.getClass())) {

        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainFrameContainer, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void mainStuff() {
        /*File rootFile = new File(Environment.getExternalStorageDirectory().getPath()*//* + "/SHAREit/files/audios/"*//*);
        fetchSongFilesTask = new FetchSongFilesTask(this);
        fetchSongFilesTask.execute(rootFile);
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this, mediaPlayer);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(mainRecyclerViewAdapter);*/

        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameContainer, fragment, HOME_FRAGMENT)
                .addToBackStack(null).commit();
    }

    @SuppressLint("WrongConstant")
    private boolean hasPermissons() {
        int res = 0;

        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
        for (String permission : permissions) {
            res = checkCallingOrSelfPermission(permission);
            if (res != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_PERMS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case REQUEST_PERMS_CODE:
                for (int res : grantResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
        }

        if (allowed) {
            mainStuff();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Storage read permission denied. Music won't be shown if permission is denied", Toast.LENGTH_SHORT).show();
                    requestPermissions();
                }
            }
        }
    }


    @Override
    public void onListRequestSuccessful(ArrayList list, int check, boolean status) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        pref.setCurrentPlayingSong(currentPlaylist.getSongs().get(currentSongPosition).getId());
        pref.setCurrentPlayingSongPosition(musicSrv.getSongPosition());
    }

    @Override
    public void onObjectRequestSuccessful(Object object, int check, boolean status) {
    }
}
