package com.example.anujsharma.shuffler.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
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
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.volley.RequestCallback;
import com.example.anujsharma.shuffler.volley.Urls;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RequestCallback {

    public static final String TAG = "TAG";
    public static final String HOME_FRAGMENT = "homeFragment";
    public static final String SEARCH_FRAGMENT = "searchFragment";
    public static final String YOUR_LIBRARY_FRAGMENT = "yourLibraryFragment";
    private final int REQUEST_PERMS_CODE = 1;
    SharedPreference pref;
    private boolean initHomeFragment, initSearchFragment, initYourLibraryFragment;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private YourLibraryFragment yourLibraryFragment;
    /*private RecyclerView mainRecyclerView;
    private LinearLayoutManager layoutManager;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private ArrayList<File> songsList;
    private FetchSongFilesTask fetchSongFilesTask;*/
    private MediaPlayer mediaPlayer;
    private Context context;
    private ProgressBar mainSongLoader;
    private TextView tvHome, tvSearch, tvMyProfile, tvSongName;
    private ImageView ivPlay, ivNext, ivFullView;
    private Song currentPlayingSong;
    private TracksDao tracksDao;
    private Playlist currentPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialise();
        initialiseListeners();
        if (hasPermissons()) {
            mainStuff();
        } else {
            requestPermissions();
        }
    }

    private void initialiseListeners() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlay(mp);
            }
        });

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlay(mediaPlayer);
            }
        });
        ivFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlaylist != null) {
                    Intent intent = new Intent(context, ViewSongActivity.class);
                    intent.putExtra(Constants.PLAYLIST_MODEL_KEY, currentPlaylist);
                    intent.putExtra(Constants.CURRENT_PLAYING_SONG_POSITION, 0);
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
                    intent.putExtra(Constants.CURRENT_PLAYING_SONG_POSITION, 0);
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
//        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        tvSongName = findViewById(R.id.tvSongName);
        mainSongLoader = findViewById(R.id.pbLoadSong);
        tvSongName.setSelected(true);
        ivFullView = findViewById(R.id.ivUpArrow);
        ivNext = findViewById(R.id.ivPlayNext);
        ivPlay = findViewById(R.id.ivPlaySong);

        mediaPlayer = new MediaPlayer();
        tracksDao.getTrackWithId(String.valueOf(pref.getCurrentPlayingSong()));
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

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }*/

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

    public void playSongInMainActivity(Song song) {
        currentPlayingSong = song;
        pref.setCurrentPlayingSong(song.getId());
        tvSongName.setText(song.getTitle());
        mainSongLoader.setVisibility(View.VISIBLE);
        ivFullView.setVisibility(View.GONE);
        String url = song.getStreamUrl() + "?client_id=" + Urls.CLIENT_ID;
        mediaPlayer.reset();
        pref.setCurrentPlayingSong(song.getId());
        Log.d("TAG", "currently playing " + url);

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void togglePlay(MediaPlayer mp) {
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
        } else {
            mainSongLoader.setVisibility(View.GONE);
            ivFullView.setVisibility(View.VISIBLE);
            mp.start();
            ivPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }

    public void modifyCurrentSongList(Playlist playlist) {
        this.currentPlaylist = playlist;
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
        pref.setCurrentPlayingSong(currentPlayingSong.getId());
    }

    @Override
    public void onObjectRequestSuccessful(Object object, int check, boolean status) {
        switch (check) {
            case Constants.SEARCH_SONG_WITH_ID:
                if (status) {
                    currentPlayingSong = (Song) object;
                    tvSongName.setText(currentPlayingSong.getTitle());
                }
                break;
        }
    }
}
