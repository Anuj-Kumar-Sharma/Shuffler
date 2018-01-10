package com.example.anujsharma.shuffler.utilities;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by anuj5 on 09-01-2018.
 */

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        try {
            builder.setDiskCache(
                    new InternalCacheDiskCacheFactory(context, 41943040)//40MB
            );
            builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        RequestQueue queue = new RequestQueue( // params hardcoded from Volley.newRequestQueue()
                new DiskBasedCache(new File(context.getCacheDir(), "volley")),
                new BasicNetwork(new HurlStack())) {
            @Override
            public <T> Request<T> add(Request<T> request) {
                request.setRetryPolicy(new DefaultRetryPolicy(60000, 2, 1.0f));
                return super.add(request);
            }
        };
        queue.start();
    }

}
