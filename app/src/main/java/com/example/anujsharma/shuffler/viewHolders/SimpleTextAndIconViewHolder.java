package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 11-03-2018.
 */

public class SimpleTextAndIconViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textView;
    public RelativeLayout relativeLayout;

    public SimpleTextAndIconViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivYourLibraryRecyclerViewIcon);
        relativeLayout = itemView.findViewById(R.id.rlYourLibrary);
        textView = itemView.findViewById(R.id.tvYourLibraryRecyclerViewText);
    }
}
