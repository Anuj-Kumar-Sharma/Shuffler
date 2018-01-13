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
    private static final String DATABASE_NAME = "shufflerDb";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_HISTORY_TABLE = "CREATE TABLE " +
            HISTORY_TABLE + " (" +
            ID + " INTEGER , " +
            TYPE + " INTEGER , " +
            ARTWORK_URL + " VARCHAR , " +
            ARTIST_NAME + " VARCHAR , " +
            TYPE_NAME + " VARCHAR )";
    private static final String DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + HISTORY_TABLE;


    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_HISTORY_TABLE);
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(DROP_HISTORY_TABLE);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
