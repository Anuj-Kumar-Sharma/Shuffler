package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by anuj5 on 12-01-2018.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private Context context;
    private MyErrorListener myErrorListener;

    public CustomLinearLayoutManager(Context context, MyErrorListener myErrorListener) {
        super(context);
        this.context = context;
        this.myErrorListener = myErrorListener;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            myErrorListener.onIoobeFound();
            Log.e("TAG", "meet a IOOBE in RecyclerView");
        }
    }

    public interface MyErrorListener {
        void onIoobeFound();
    }
}
