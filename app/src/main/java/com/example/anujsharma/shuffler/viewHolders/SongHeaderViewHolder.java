package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class SongHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView headerText;

    public SongHeaderViewHolder(View itemView) {
        super(itemView);
        headerText = (TextView) itemView.findViewById(R.id.tvHeaderText);
    }
}
