package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.viewHolders.PlaylistsListViewHolder;

import java.util.List;

/**
 * Created by anuj5 on 11-03-2018.
 */

public class PlaylistsListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Playlist> playlists;
    private ItemClickListener itemClickListener;

    public PlaylistsListRecyclerViewAdapter(final Context context, ItemClickListener itemClickListener, List<Playlist> playlists) {
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.playlists = playlists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_playlists_list, parent, false);
        final PlaylistsListViewHolder playlistsListViewHolder = new PlaylistsListViewHolder(view);

        playlistsListViewHolder.parentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(view, playlistsListViewHolder.getAdapterPosition(), Constants.EACH_PLAYLIST_LAYOUT_CLICKED);
            }
        });
        playlistsListViewHolder.ivPlaylistMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(playlistsListViewHolder.ivPlaylistMenu, playlistsListViewHolder.getAdapterPosition(), Constants.EACH_PLAYLIST_MENU_CLICKED);
            }
        });
        return playlistsListViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlaylistsListViewHolder playlistsListViewHolder = (PlaylistsListViewHolder) holder;
        Playlist playlist = playlists.get(position);
        playlistsListViewHolder.tvPlaylistTitle.setText(playlist.getTitle());
        String count = playlist.getTracksCount() + " TRACKS";
        playlistsListViewHolder.tvPlaylistTrackCount.setText(count);

        if (!playlist.getArtworkUrl().isEmpty()) {
            Glide.with(context)
                    .load(playlist.getArtworkUrl())
                    .into(playlistsListViewHolder.ivPlaylistArtwork);
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public void addToPlaylistsList(List<Playlist> playlists) {
        this.playlists = playlists;
        notifyDataSetChanged();
    }

    public void deleteFromPlaylistsList(int position, List<Playlist> playlists) {
        this.playlists = playlists;
        notifyItemRemoved(position);
    }

    public void updatePlaylist(int position, List<Playlist> playlists) {
        this.playlists = playlists;
        notifyItemChanged(position);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }
}
