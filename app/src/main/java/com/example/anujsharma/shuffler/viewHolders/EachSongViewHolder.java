package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class EachSongViewHolder extends RecyclerView.ViewHolder {

    public TextView songName, artistName, duration, playbackCount, likesCount;
    public ImageView ivMenu;
    public LinearLayout eachSongLayout;

    public EachSongViewHolder(View itemView) {
        super(itemView);
        songName = (TextView) itemView.findViewById(R.id.tvEachSongName);
        artistName = (TextView) itemView.findViewById(R.id.tvEachSongArtist);
        ivMenu = (ImageView) itemView.findViewById(R.id.ivEachSongMenu);
        eachSongLayout = (LinearLayout) itemView.findViewById(R.id.eachSongLayout);
        duration = (TextView) itemView.findViewById(R.id.tvEachSongDuration);
        playbackCount = (TextView) itemView.findViewById(R.id.tvEachSongPlayCount);
        likesCount = (TextView) itemView.findViewById(R.id.tvEachSongLikesCount);
    }
}
