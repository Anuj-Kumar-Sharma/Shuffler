package com.example.anujsharma.shuffler.backgroundTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.example.anujsharma.shuffler.adapters.SqliteDbAdapter;
import com.example.anujsharma.shuffler.models.Song;

import java.io.File;
import java.util.ArrayList;

public class FetchSongFilesTask extends AsyncTask<File, Void, Void> {

    private static final String TAG = "myErrors";
    private File rootFile;
    private ProgressDialog progressDialog;
    private Context context;
    private SqliteDbAdapter sqliteDbAdapter;

    public FetchSongFilesTask(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        sqliteDbAdapter = new SqliteDbAdapter(context);
    }

    @Override
    protected Void doInBackground(File... params) {
        rootFile = params[0];
        getSongs(rootFile);
        return null;
    }


    private ArrayList<File> getSongs(File root) {
        ArrayList<File> songsList = new ArrayList<>();
        File[] files = root.listFiles();
        try {
            Log.d(TAG, root.getPath());

            for (File singleFile : files) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    songsList.addAll(getSongs(singleFile));
                } else {
                    if ((singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".m4a"))
                            /*&& !singleFile.getName().startsWith("Call@")*/) {

                        sqliteDbAdapter.addNewSong(getSongMetaData(singleFile));
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return songsList;
    }

    public Song getSongMetaData(File file) {
        Song song;
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(file.toURI().getPath());
        String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (title == null) return null;
        String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        if (album == null) album = "Unknown Album";
        String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (artist == null) artist = "Unknown Artist";
        String genre = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        if (genre == null) genre = "Unknown Genre";
        int duration = Integer.parseInt(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        song = new Song(title, artist, genre, album, duration, file);
        metadataRetriever.release();
        return song;
    }
}
