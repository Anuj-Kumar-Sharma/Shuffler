package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 11-03-2018.
 */

public class PlaylistsListViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout parentLinearLayout;
    public ImageView ivPlaylistArtwork, ivPlaylistMenu;
    public TextView tvPlaylistTitle, tvPlaylistTrackCount;

    public PlaylistsListViewHolder(View itemView) {
        super(itemView);

        parentLinearLayout = itemView.findViewById(R.id.llPlaylistList);
        ivPlaylistArtwork = itemView.findViewById(R.id.ivPlaylistArtwork);
        ivPlaylistMenu = itemView.findViewById(R.id.ivPlaylistMenu);
        tvPlaylistTitle = itemView.findViewById(R.id.tvPlaylistTitle);
        tvPlaylistTrackCount = itemView.findViewById(R.id.tvPlaylistTracksCount);
    }
}
