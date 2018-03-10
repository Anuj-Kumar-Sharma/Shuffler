package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 17-02-2018.
 */

public class YourLibraryTopContent extends RecyclerView.ViewHolder {

    public TextView tvText;
    public ImageView ivIcon;
    public RelativeLayout relativeLayout;

    public YourLibraryTopContent(View itemView) {
        super(itemView);

        tvText = itemView.findViewById(R.id.tvYourLibraryRecyclerViewText);
        ivIcon = itemView.findViewById(R.id.ivYourLibraryRecyclerViewIcon);
        relativeLayout = itemView.findViewById(R.id.rlYourLibrary);
    }
}
