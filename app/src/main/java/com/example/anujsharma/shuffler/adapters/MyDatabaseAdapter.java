package com.example.anujsharma.shuffler.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.anujsharma.shuffler.models.HybridModel;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.MySqliteHelper;
import com.example.anujsharma.shuffler.utilities.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by anuj5 on 10-01-2018.
 */

public class MyDatabaseAdapter {

    private static MySqliteHelper mySqliteHelper;
    private Context context;
    private List<HybridModel> historyList;
    private OnDatabaseChanged onDatabaseChanged;

    public MyDatabaseAdapter(Context context) {
        this.context = context;
        mySqliteHelper = new MySqliteHelper(context);
    }

    public MyDatabaseAdapter(Context context, OnDatabaseChanged onDatabaseChanged) {
        this.context = context;
        this.onDatabaseChanged = onDatabaseChanged;
        mySqliteHelper = new MySqliteHelper(context);
    }

    public List<HybridModel> getHistroyList() {
        historyList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(MySqliteHelper.HISTORY_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                HybridModel history = new HybridModel(
                        cursor.getInt(cursor.getColumnIndex(MySqliteHelper.TYPE)),
                        cursor.getLong(cursor.getColumnIndex(MySqliteHelper.ID)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.ARTWORK_URL)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.TYPE_NAME)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.ARTIST_NAME)));

                historyList.add(history);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        Collections.reverse(historyList);
        return historyList;
    }

    public long addToHistory(HybridModel history) {
        long id;

        deleteFromHistory(history.getId());
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();

        contentValues.put(MySqliteHelper.TYPE, history.getType());
        contentValues.put(MySqliteHelper.ARTIST_NAME, history.getSongArtist());
        contentValues.put(MySqliteHelper.TYPE_NAME, history.getName());
        contentValues.put(MySqliteHelper.ARTWORK_URL, history.getArtworkUrl());
        contentValues.put(MySqliteHelper.ID, history.getId());

        id = sqLiteDatabase.insert(MySqliteHelper.HISTORY_TABLE, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long deleteFromHistory(Long id) {
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();
        String selection = MySqliteHelper.ID + " = ?";

        String[] selectionArgs = null;
        if (id == null) selection = null;
        else selectionArgs = new String[]{String.valueOf(id)};
        return sqLiteDatabase.delete(MySqliteHelper.HISTORY_TABLE, selection, selectionArgs);
    }

    public long containsInHistory(long id) {
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getReadableDatabase();
        String[] columns = {MySqliteHelper.ID};
        String[] selectionArgs = {String.valueOf(id)};
        String selection = MySqliteHelper.ID + " = ?";
        Cursor cursor = sqLiteDatabase.query(MySqliteHelper.HISTORY_TABLE, columns, selection, selectionArgs, null, null, null);

        cursor.close();
        sqLiteDatabase.close();
        return cursor.getCount();
    }

    public List<Playlist> getPlaylistsList() {
        List<Playlist> playlistsList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(MySqliteHelper.PLAYLISTS_LIST_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Playlist playlist = new Playlist(cursor.getInt(cursor.getColumnIndex(MySqliteHelper.PLAYLIST_ID)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.PLAYLIST_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(MySqliteHelper.PLAYLIST_TRACK_COUNT)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.ARTWORK_URL)));
                playlistsList.add(playlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        Collections.reverse(playlistsList);
        return playlistsList;
    }

    public long createNewPlaylist(Playlist playlist) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();

        contentValues.put(MySqliteHelper.PLAYLIST_ID, playlist.getPlaylistId());
        contentValues.put(MySqliteHelper.PLAYLIST_TITLE, playlist.getTitle());
        contentValues.put(MySqliteHelper.PLAYLIST_TRACK_COUNT, playlist.getTracksCount());
        contentValues.put(MySqliteHelper.ARTWORK_URL, playlist.getArtworkUrl());

        long insert_id = sqLiteDatabase.insert(MySqliteHelper.PLAYLISTS_LIST_TABLE, null, contentValues);
        if (onDatabaseChanged != null) onDatabaseChanged.onPlaylistAdded();
        sqLiteDatabase.close();
        return insert_id;
    }

    public boolean containsPlaylist(String title) {

        boolean containsPlaylist;
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(MySqliteHelper.PLAYLISTS_LIST_TABLE, new String[]{MySqliteHelper.PLAYLIST_ID},
                MySqliteHelper.PLAYLIST_TITLE + " = ?", new String[]{title}, null, null, null);
        containsPlaylist = cursor.getCount() > 0;

        cursor.close();
        sqLiteDatabase.close();
        return containsPlaylist;
    }

    public void deletePlaylist(int playlistId, int position) {
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();
        String selection = MySqliteHelper.PLAYLIST_ID + " = ?";

        String[] selectionArgs = new String[]{String.valueOf(playlistId)};
        sqLiteDatabase.delete(MySqliteHelper.PLAYLISTS_LIST_TABLE, selection, selectionArgs);
        sqLiteDatabase.close();

        deleteAllSongsFromAPlaylist(playlistId);

        onDatabaseChanged.onPlaylistDeleted(position);
    }

    public void updatePlaylist(Playlist playlist, int position) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();

        contentValues.put(MySqliteHelper.PLAYLIST_TITLE, playlist.getTitle());
        contentValues.put(MySqliteHelper.PLAYLIST_TRACK_COUNT, playlist.getTracksCount());
        contentValues.put(MySqliteHelper.ARTWORK_URL, playlist.getArtworkUrl());

        sqLiteDatabase.update(MySqliteHelper.PLAYLISTS_LIST_TABLE, contentValues,
                MySqliteHelper.PLAYLIST_ID + " = ?", new String[]{String.valueOf(playlist.getPlaylistId())});

        sqLiteDatabase.close();
        if (onDatabaseChanged != null) onDatabaseChanged.onPlaylistUpdated(position);
    }

    public void addSongToPlaylist(Song song, Playlist playlist, int position) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();

        contentValues.put(MySqliteHelper.SONG_ID, song.getId());
        contentValues.put(MySqliteHelper.PLAYLIST_ID, playlist.getPlaylistId());
        contentValues.put(MySqliteHelper.SONG_ARTIST_ID, song.getUser_id());
        contentValues.put(MySqliteHelper.SONG_DURATION, song.getDuration());
        contentValues.put(MySqliteHelper.SONG_TITLE, song.getTitle());
        contentValues.put(MySqliteHelper.SONG_ARTIST_NAME, song.getArtist());
        contentValues.put(MySqliteHelper.SONG_GENRE, song.getGenre());
        contentValues.put(MySqliteHelper.SONG_ARTWORK_URL, song.getSongArtwork());
        contentValues.put(MySqliteHelper.SONG_STREAM_URL, song.getStreamUrl());
        contentValues.put(MySqliteHelper.SONG_PLAYBACK_COUNT, song.getPlaybackCount());
        contentValues.put(MySqliteHelper.SONG_LIKES_COUNT, song.getLikesCount());

        sqLiteDatabase.insert(MySqliteHelper.SONGS_TABLE, null, contentValues);
        sqLiteDatabase.close();

        playlist.setTrack_count(playlist.getTracksCount() + 1);

        if (playlist.getArtworkUrl().isEmpty() && playlist.getTracksCount() >= 4) {
            Playlist newPlaylist = getPlaylist(playlist);
            List<Song> songs = newPlaylist.getSongs();
            final List<Bitmap> imageList = new ArrayList<>();

            for (int i = 0; i < 4; i++) {

                Glide.with(context).
                        load(songs.get(i).getSongArtwork()).
                        asBitmap().
                        into(new BitmapImageViewTarget(new ImageView(context)) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                //Play with bitmap
                                imageList.add(resource);
                                super.setResource(resource);
                            }
                        });

            }

            Bitmap bitmap = Utilities.mergeThemAll(imageList);


//            playlist.setArtwork_url(location);
        }
        updatePlaylist(playlist, position);
    }

    public Playlist getPlaylist(Playlist playlist) {
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(MySqliteHelper.SONGS_TABLE, null,
                MySqliteHelper.PLAYLIST_ID + " = ?", new String[]{String.valueOf(playlist.getPlaylistId())}, null, null, null);

        List<Song> songs = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(cursor.getLong(cursor.getColumnIndex(MySqliteHelper.SONG_ID)),
                        cursor.getLong(cursor.getColumnIndex(MySqliteHelper.SONG_ARTIST_ID)),
                        cursor.getLong(cursor.getColumnIndex(MySqliteHelper.SONG_DURATION)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.SONG_ARTIST_NAME)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.SONG_GENRE)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.SONG_ARTWORK_URL)),
                        cursor.getString(cursor.getColumnIndex(MySqliteHelper.SONG_STREAM_URL)),
                        cursor.getInt(cursor.getColumnIndex(MySqliteHelper.SONG_PLAYBACK_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(MySqliteHelper.SONG_LIKES_COUNT)));
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        playlist.setSongs(songs);

        return playlist;
    }

    public void deleteAllSongsFromAPlaylist(int playlistId) {
        SQLiteDatabase sqLiteDatabase = mySqliteHelper.getWritableDatabase();
        String selection = MySqliteHelper.PLAYLIST_ID + " = ?";

        String[] selectionArgs = new String[]{String.valueOf(playlistId)};
        sqLiteDatabase.delete(MySqliteHelper.SONGS_TABLE, selection, selectionArgs);
        sqLiteDatabase.close();
    }

    public interface OnDatabaseChanged {
        void onPlaylistAdded();

        void onPlaylistDeleted(int position);

        void onPlaylistUpdated(int position);

//        void onSongAdded();
    }
}
