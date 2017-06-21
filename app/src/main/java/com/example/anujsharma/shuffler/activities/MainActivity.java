package com.example.anujsharma.shuffler.activities;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.adapters.MainRecyclerViewAdapter;
import com.example.anujsharma.shuffler.backgroundTasks.FetchSongFilesTask;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyMainActivity";
    private final int REQUEST_PERMS_CODE = 1;
    private RecyclerView mainRecyclerView;
    private LinearLayoutManager layoutManager;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private ArrayList<File> songsList;
    private FetchSongFilesTask fetchSongFilesTask;
    private MediaPlayer mediaPlayer;

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
            }
        });

        if (hasPermissons()) {
            mainStuff();
        } else {
            requestPermissions();
        }
    }


    public void mainStuff() {
        File rootFile = new File(Environment.getExternalStorageDirectory().getPath() + "/SHAREit/files/audios/");
        fetchSongFilesTask = new FetchSongFilesTask(this);
        fetchSongFilesTask.execute(rootFile);

        mainRecyclerView = (RecyclerView) findViewById(R.id.mainRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this, mediaPlayer);
        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(mainRecyclerViewAdapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

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

        if (allowed == true) {
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
}
