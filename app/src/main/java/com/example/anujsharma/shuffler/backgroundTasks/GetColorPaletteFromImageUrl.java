package com.example.anujsharma.shuffler.backgroundTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

/**
 * Created by anuj5 on 13-01-2018.
 */

public class GetColorPaletteFromImageUrl extends AsyncTask<String, Void, Palette> {

    private String TAG = "TAG";
    private Bitmap theBitmap;
    private Context context;
    private PaletteCallback paletteCallback;

    public GetColorPaletteFromImageUrl(Context context, PaletteCallback paletteCallback) {
        this.context = context;
        this.paletteCallback = paletteCallback;
    }

    @Override
    protected Palette doInBackground(String... strings) {
        String url = strings[0];

        try {
            theBitmap = Glide.
                    with(context).
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

        paletteCallback.onPostExecute(palette);
    }

    public interface PaletteCallback {
        void onPostExecute(Palette palette);
    }
}
