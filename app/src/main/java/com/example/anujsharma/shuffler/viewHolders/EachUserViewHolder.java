package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 04-01-2018.
 */

public class EachUserViewHolder extends RecyclerView.ViewHolder {

    public ImageView userImage, userMenu;
    public TextView userName, followersCount;
    public RelativeLayout eachUserRelativeLayout;

    public EachUserViewHolder(View itemView) {
        super(itemView);
        userImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
        userMenu = (ImageView) itemView.findViewById(R.id.ivUserMenu);
        userName = (TextView) itemView.findViewById(R.id.tvUserName);
        followersCount = (TextView) itemView.findViewById(R.id.tvFollowersCount);
        eachUserRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.rlEachUserLayout);
    }
}
