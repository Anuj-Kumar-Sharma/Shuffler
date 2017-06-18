package com.example.anujsharma.shuffler.backgroundTasks;

import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.anujsharma.shuffler.dataStructures.Song;

import java.io.File;
import java.text.DecimalFormat;

public class RetrieveSongMetaData extends AsyncTask<File, Void, Song> {

    private File songFile;
    private TextView title, artist, duration;

    public RetrieveSongMetaData(TextView title, TextView artist, TextView duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    @Override
    protected Song doInBackground(File... params) {
        songFile = params[0];
        Song song = getSongMetaData(songFile);
        return song;
    }

    @Override
    protected void onPostExecute(Song song) {
        if (song != null) {
            title.setText(song.getTitle());
            artist.setText(song.getArtist());
            int timeDuration = song.getDuration();
            duration.setText(timeDuration / 60 + ":" + (new DecimalFormat("00").format(timeDuration % 60)));
        }
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
        song = new Song(title, artist, genre, album, duration);
        if (metadataRetriever != null) metadataRetriever.release();
        return song;
    }
}
