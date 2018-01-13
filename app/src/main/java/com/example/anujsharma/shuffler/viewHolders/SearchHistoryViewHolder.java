package com.example.anujsharma.shuffler.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

/**
 * Created by anuj5 on 10-01-2018.
 */

public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivTypeImage, ivTypeCross;
    public TextView tvType, tvTypeName;
    public LinearLayout historyLayout;

    public SearchHistoryViewHolder(View itemView) {
        super(itemView);

        ivTypeImage = itemView.findViewById(R.id.ivTypeImage);
        ivTypeCross = itemView.findViewById(R.id.ivCross);
        tvType = itemView.findViewById(R.id.type);
        tvTypeName = itemView.findViewById(R.id.typeName);
        historyLayout = itemView.findViewById(R.id.historyLayout);
    }
}
