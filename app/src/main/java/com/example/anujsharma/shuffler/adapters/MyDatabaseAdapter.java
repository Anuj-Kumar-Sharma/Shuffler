package com.example.anujsharma.shuffler.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.anujsharma.shuffler.models.HybridModel;
import com.example.anujsharma.shuffler.utilities.MySqliteHelper;

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

    public MyDatabaseAdapter(Context context) {
        this.context = context;
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
}
