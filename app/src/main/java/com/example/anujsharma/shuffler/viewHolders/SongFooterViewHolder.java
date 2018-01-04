package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 04-01-2018.
 */

public class SongFooterViewHolder extends RecyclerView.ViewHolder {

    public TextView footerText;

    public SongFooterViewHolder(View itemView) {
        super(itemView);
        footerText = (TextView) itemView.findViewById(R.id.tvFooterText);
    }
}
