package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 07-01-2018.
 */

public class EachPlaylistViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivPlaylistImage, ivPlaylistMenu;
    public TextView tvPlaylistTitle, tvPlaylistUser, tvPlaylistLikesCount, tvPlaylistsTracksCount;
    public RelativeLayout rlPlaylistLayout;

    public EachPlaylistViewHolder(View itemView) {
        super(itemView);
        ivPlaylistImage = itemView.findViewById(R.id.ivEachPlaylistImage);
        ivPlaylistMenu = itemView.findViewById(R.id.ivPlaylistMenu);
        tvPlaylistLikesCount = itemView.findViewById(R.id.tvPlaylistLikesCount);
        tvPlaylistsTracksCount = itemView.findViewById(R.id.tvPlaylistTracksCount);
        tvPlaylistTitle = itemView.findViewById(R.id.tvPlaylistTitle);
        tvPlaylistUser = itemView.findViewById(R.id.tvPlaylistUserName);
        rlPlaylistLayout = itemView.findViewById(R.id.rlEachPlaylistLayout);
    }
}
