package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.anujsharma.shuffler.activities.MainActivity.TAG;

/**
 * Created by anuj5 on 10-01-2018.
 */

public class MySqliteHelper extends SQLiteOpenHelper {

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String ARTWORK_URL = "artwork_url";
    public static final String ARTIST_NAME = "artist_name";
    public static final String TYPE_NAME = "type_name";
    public static final String HISTORY_TABLE = "history_table";

    public static final String PLAYLISTS_LIST_TABLE = "playlists_list_table";
    public static final String PLAYLIST_ID = "playlist_id";
    public static final String PLAYLIST_TITLE = "playlist_title";
    public static final String PLAYLIST_TRACK_COUNT = "playlist_track_count";
    public static final String PLAYLIST_ARTWORK_BLOB = "playlist_artwork_blob";

    public static final String SONGS_TABLE = "song_table";
    public static final String SONG_ID = "song_id";
    public static final String SONG_ARTIST_ID = "song_user_id";
    public static final String SONG_DURATION = "song_duration";
    public static final String SONG_TITLE = "song_title";
    public static final String SONG_ARTIST_NAME = "song_artist_name";
    public static final String SONG_GENRE = "song_genre";
    public static final String SONG_ARTWORK_URL = "song_artwork_url";
    public static final String SONG_STREAM_URL = "song_stream_url";
    public static final String SONG_PLAYBACK_COUNT = "song_playback_count";
    public static final String SONG_LIKES_COUNT = "song_likes_count";


    private static final String DATABASE_NAME = "shufflerDb";
    private static final int DATABASE_VERSION = 4;

    private static final String CREATE_HISTORY_TABLE = "CREATE TABLE " +
            HISTORY_TABLE + " (" +
            ID + " INTEGER , " +
            TYPE + " INTEGER , " +
            ARTWORK_URL + " VARCHAR , " +
            ARTIST_NAME + " VARCHAR , " +
            TYPE_NAME + " VARCHAR )";
    private static final String DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + HISTORY_TABLE;

    private static final String CREATE_PLAYLISTS_LIST_TABLE = "CREATE TABLE " +
            PLAYLISTS_LIST_TABLE + " (" +
            PLAYLIST_ID + " INTEGER , " +
            PLAYLIST_TITLE + " VARCHAR , " +
            PLAYLIST_TRACK_COUNT + " INTEGER , " +
            ARTWORK_URL + " VARCHAR ," +
            PLAYLIST_ARTWORK_BLOB + " BLOB )";
    private static final String DROP_PLAYLISTS_LIST_TABLE = "DROP TABLE IF EXISTS " + PLAYLISTS_LIST_TABLE;

    private static final String CREATE_SONGS_TABLE = "CREATE TABLE " +
            SONGS_TABLE + " (" +
            SONG_ID + " INTEGER , " +
            PLAYLIST_ID + " INTEGER , " +
            SONG_ARTIST_ID + " INTEGER , " +
            SONG_DURATION + " INTEGER , " +
            SONG_TITLE + " VARCHAR , " +
            SONG_ARTIST_NAME + " VARCHAR , " +
            SONG_GENRE + " VARCHAR , " +
            SONG_ARTWORK_URL + " VARCHAR , " +
            SONG_STREAM_URL + " VARCHAR , " +
            SONG_PLAYBACK_COUNT + " INTEGER , " +
            SONG_LIKES_COUNT + " INTEGER )";
    private static final String DROP_SONGS_TABLE = "DROP TABLE IF EXISTS " + SONGS_TABLE;


    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_HISTORY_TABLE);
            db.execSQL(CREATE_PLAYLISTS_LIST_TABLE);
            db.execSQL(CREATE_SONGS_TABLE);
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(DROP_HISTORY_TABLE);
            db.execSQL(DROP_PLAYLISTS_LIST_TABLE);
            db.execSQL(DROP_SONGS_TABLE);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
