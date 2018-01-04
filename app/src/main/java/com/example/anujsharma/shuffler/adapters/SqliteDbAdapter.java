package com.example.anujsharma.shuffler.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.anujsharma.shuffler.models.Song;

import java.io.File;
import java.util.ArrayList;

public class SqliteDbAdapter {

    public static final String TAG = "myDB";

    private static Context context;
    private MySQLiteHelper sqliteHelper;

    public SqliteDbAdapter(Context context) {
        SqliteDbAdapter.context = context;
        sqliteHelper = new MySQLiteHelper();
    }

    public long addNewSong(Song song) {
        long id = -1;

        if (!containsSong(song.getSongFile())) {

            ContentValues contentValues = new ContentValues();
            SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
            contentValues.put(MySQLiteHelper.SONG_FILE, song.getSongFile().getPath());
            contentValues.put(MySQLiteHelper.SONG_TITLE, song.getTitle());
            contentValues.put(MySQLiteHelper.SONG_ARTIST, song.getArtist());
            contentValues.put(MySQLiteHelper.SONG_ALBUM, song.getAlbum());
            contentValues.put(MySQLiteHelper.SONG_DURATION, song.getDuration());
            contentValues.put(MySQLiteHelper.SONG_GENRE, song.getGenre());

            Log.d(TAG, song.getSongFile().getPath());
            id = sqLiteDatabase.insert(MySQLiteHelper.TABLE_NAME, null, contentValues);
            Log.d(TAG, "row id " + id);
            sqLiteDatabase.close();
        }
        return id;
    }

    public boolean containsSong(File songFile) {
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        String[] columns = {MySQLiteHelper.SONG_FILE};
        String[] selectionArgs = {songFile.getPath()};
        String selection = MySQLiteHelper.SONG_FILE + " = ?";
        Cursor cursor = sqLiteDatabase.query(MySQLiteHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList();
        SQLiteDatabase sqLiteDatabase = sqliteHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(MySQLiteHelper.TABLE_NAME, null, null, null, null, null, MySQLiteHelper.SONG_TITLE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SONG_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SONG_ARTIST)),
                        cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SONG_GENRE)),
                        cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SONG_ALBUM)),
                        cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.SONG_DURATION)),
                        new File(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.SONG_FILE))));

                songs.add(song);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        cursor.close();
        return songs;
    }

    static class MySQLiteHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "myLogInDatabase";
        private static final int DATABASE_VERSION = 1;
        private static final String SONG_ID = "songId";
        private static final String SONG_FILE = "songFile";
        private static final String SONG_TITLE = "songTitle";
        private static final String SONG_ARTIST = "songArtist";
        private static final String SONG_ALBUM = "songAlbum";
        private static final String SONG_GENRE = "songGenre";
        private static final String SONG_DURATION = "songDuration";

        private static final String TABLE_NAME = "songsDBTable";
        private static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                SONG_ID + " INTEGER PRIMARY KEY , " +
                SONG_FILE + " VARCHAR , " +
                SONG_TITLE + " VARCHAR , " +
                SONG_ARTIST + " VARCHAR , " +
                SONG_ALBUM + " VARCHAR , " +
                SONG_GENRE + " VARCHAR , " +
                SONG_DURATION + " INTEGER )";

        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public MySQLiteHelper() {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate");
            try {
                db.execSQL(CREATE_TABLE);
            } catch (SQLiteException e) {
                Log.d(TAG, e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "onUpgrade");
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLiteException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }
}
