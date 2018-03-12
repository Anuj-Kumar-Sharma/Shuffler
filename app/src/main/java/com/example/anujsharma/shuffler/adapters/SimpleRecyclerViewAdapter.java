package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.viewHolders.SimpleTextAndIconViewHolder;

import java.util.List;

/**
 * Created by anuj5 on 11-03-2018.
 */

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Playlist> playlists;
    private ItemClickListener itemClickListener;

    public SimpleRecyclerViewAdapter(Context context, List<Playlist> playlists, ItemClickListener itemClickListener) {
        this.context = context;
        this.playlists = playlists;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_simple_text_and_icon, parent, false);
        final SimpleTextAndIconViewHolder simpleTextAndIconViewHolder = new SimpleTextAndIconViewHolder(view);

        simpleTextAndIconViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, simpleTextAndIconViewHolder.getAdapterPosition(), Constants.EACH_PLAYLIST_LAYOUT_CLICKED);
            }
        });
        return simpleTextAndIconViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        SimpleTextAndIconViewHolder simpleTextAndIconViewHolder = (SimpleTextAndIconViewHolder) holder;
        simpleTextAndIconViewHolder.textView.setText(playlist.getTitle());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }
}
