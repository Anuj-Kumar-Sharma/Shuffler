package com.example.anujsharma.shuffler.backgroundTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.anujsharma.shuffler.activities.ViewSongActivity;

import java.util.concurrent.ExecutionException;

/**
 * Created by anuj5 on 13-01-2018.
 */

public class GetColorPaletteFromImageUrl extends AsyncTask<String, Void, Palette> {

    private String TAG = "TAG";
    private Bitmap theBitmap;
    private ViewSongActivity viewSongActivity;

    public GetColorPaletteFromImageUrl(Context context) {
        viewSongActivity = (ViewSongActivity) context;
    }

    @Override
    protected Palette doInBackground(String... strings) {
        String url = strings[0];

        try {
            theBitmap = Glide.
                    with(viewSongActivity).
                    load(url).
                    asBitmap().
                    into(-1, -1).
                    get();
        } catch (final ExecutionException | InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }

        Palette palette = null;
        if (theBitmap != null)
            palette = Palette.from(theBitmap).generate();
        return palette;
    }

    @Override
    protected void onPostExecute(Palette palette) {
        super.onPostExecute(palette);

        if (palette != null)
            viewSongActivity.changeBackground(palette.getDarkMutedColor(0xFF616261));
        else
            viewSongActivity.changeBackground(0xFF616261);
    }
}
