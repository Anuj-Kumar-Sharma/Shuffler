package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.viewHolders.YourLibraryTopContent;

/**
 * Created by anuj5 on 17-02-2018.
 */

public class YourLibraryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PLAYLISTS = 0;
    private static final int TYPE_SONGS = 1;
    private static final int TYPE_ARTISTS = 2;
    private Context context;
    private ItemClickListener itemClickListener;

    public YourLibraryRecyclerViewAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_PLAYLISTS;
        if (position == 1) return TYPE_SONGS;
        if (position == 2) return TYPE_ARTISTS;

        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_PLAYLISTS:
            case TYPE_SONGS:
            case TYPE_ARTISTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_your_library_top, parent, false);
                final YourLibraryTopContent yourLibraryTopContent = new YourLibraryTopContent(view);
                yourLibraryTopContent.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view, yourLibraryTopContent.getAdapterPosition(), Constants.YOUR_LIBRARY_TOP_CONTENT_CLICKED);
                    }
                });
                return yourLibraryTopContent;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        YourLibraryTopContent yourLibraryTopContent;
        switch (holder.getItemViewType()) {
            case TYPE_PLAYLISTS:
                yourLibraryTopContent = (YourLibraryTopContent) holder;
                yourLibraryTopContent.tvText.setText(R.string.playlists);
                yourLibraryTopContent.ivIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_musical_note));
                break;
            case TYPE_SONGS:
                yourLibraryTopContent = (YourLibraryTopContent) holder;
                yourLibraryTopContent.tvText.setText(R.string.songs);
                yourLibraryTopContent.ivIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_song_note));

                break;
            case TYPE_ARTISTS:
                yourLibraryTopContent = (YourLibraryTopContent) holder;
                yourLibraryTopContent.tvText.setText(R.string.artists);
                yourLibraryTopContent.ivIcon.setBackground(context.getResources().getDrawable(R.drawable.ic_microphone));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }
}
