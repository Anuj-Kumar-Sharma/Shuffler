package com.example.anujsharma.shuffler.activities;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.adapters.MainRecyclerViewAdapter;
import com.example.anujsharma.shuffler.dataStructures.Song;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyMainActivity";
    private RecyclerView mainRecyclerView;
    private LinearLayoutManager layoutManager;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private ArrayList<Song> songsList;
    private MediaMetadataRetriever metadataRetriever;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddSongActivity.class);
                startActivity(intent);
            }
        });

        songsList = getSongs(new File(Environment.getExternalStorageDirectory().getPath() + "/SHAREit/files/audios/"));
        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this, songsList);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(mainRecyclerViewAdapter);


    }

    private ArrayList<Song> getSongs(File root) {
        ArrayList<Song> songsList = new ArrayList();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                songsList.addAll(getSongs(singleFile));
            } else {
                if ((singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".m4a"))
                        && !singleFile.getName().startsWith("Call@")) {
                    songsList.add(getSongMetaData(singleFile));
                }
            }
        }
        return songsList;
    }

    public Song getSongMetaData(File file) {
        Song song;
        Uri uri = Uri.fromFile(file);
        metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(file.toURI().getPath());
        String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (title == null) return null;
        String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String genre = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        int duration = Integer.parseInt(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        song = new Song(title, artist, genre, album, duration);
        if (metadataRetriever != null) metadataRetriever.release();
        return song;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
