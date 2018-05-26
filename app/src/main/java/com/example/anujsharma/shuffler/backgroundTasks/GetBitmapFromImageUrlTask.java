package com.example.anujsharma.shuffler.backgroundTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class GetBitmapFromImageUrlTask extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "GetBitmapImageUrlTag";
    private Context context;
    private BitmapFromUrlCallback bitmapFromUrlCallback;

    public GetBitmapFromImageUrlTask(Context context, BitmapFromUrlCallback bitmapFromUrlCallback) {
        this.context = context;
        this.bitmapFromUrlCallback = bitmapFromUrlCallback;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            return Glide.
                    with(context).
                    load(strings[0]).
                    asBitmap().
                    into(-1, -1).
                    get();
        } catch (final ExecutionException | InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        bitmapFromUrlCallback.onBitmapFound(bitmap);
    }

    public interface BitmapFromUrlCallback {
        void onBitmapFound(Bitmap bitmap);
    }
}
