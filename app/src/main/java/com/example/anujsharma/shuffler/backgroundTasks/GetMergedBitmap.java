package com.example.anujsharma.shuffler.backgroundTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by anuj5 on 12-03-2018.
 */

public class GetMergedBitmap extends AsyncTask<List<Song>, Void, Bitmap> {

    private Context context;
    private MergedBitmapCallback mergedBitmapCallback;

    public GetMergedBitmap(Context context, MergedBitmapCallback mergedBitmapCallback) {
        this.context = context;
        this.mergedBitmapCallback = mergedBitmapCallback;
    }

    @Override
    protected Bitmap doInBackground(List<Song>[] lists) {
        List<Song> songs = lists[0];
        List<Bitmap> bitmaps = new ArrayList<>();

        int loopCount = 4;
        if (songs.size() == 1) {
            loopCount = 1;
        }

        for (int i = 0; i < loopCount; i++) {
            Bitmap bitmap = null;
            try {
                bitmap = Glide.
                        with(context).
                        load(Utilities.getMediumArtworkUrl(songs.get(i).getSongArtwork())).
                        asBitmap().
                        into(-1, -1).
                        get();
            } catch (final ExecutionException | InterruptedException e) {
                Log.e("TAG", e.getMessage());
            }
            bitmaps.add(bitmap);
        }

        Log.d("TAG", "bitmaps size " + bitmaps.size());

        if (loopCount == 4) return Utilities.mergeThemAll(bitmaps);
        else return bitmaps.get(0);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        mergedBitmapCallback.onBitmapReady(bitmap);
    }

    public interface MergedBitmapCallback {
        void onBitmapReady(Bitmap bitmap);
    }
}
