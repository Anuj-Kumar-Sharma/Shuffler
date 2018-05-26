package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.models.User;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.viewHolders.EachPlaylistViewHolder;
import com.example.anujsharma.shuffler.viewHolders.EachSongViewHolder;
import com.example.anujsharma.shuffler.viewHolders.EachUserViewHolder;

import java.util.List;

/**
 * Created by anuj5 on 06-01-2018.
 */

public class SeeAllRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Song> songs;
    private List<User> users;
    private List<Playlist> playlists;
    private int TYPE;
    private Context context;
    private ItemClickListener itemClickListener;
    private int selectedPosition;

    public SeeAllRecyclerViewAdapter(Context context, List<Song> songs, List<User> users, List<Playlist> playlists, ItemClickListener itemClickListener, int TYPE) {
        this.context = context;
        this.songs = songs;
        this.users = users;
        this.playlists = playlists;
        this.itemClickListener = itemClickListener;
        this.TYPE = TYPE;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        switch (TYPE) {
            case Constants.TYPE_TRACK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_track, parent, false);
                final EachSongViewHolder songViewHolder = new EachSongViewHolder(view);
                songViewHolder.eachSongLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(v, songViewHolder.getAdapterPosition(), Constants.EACH_SONG_LAYOUT_CLICKED);
                    }
                });
                songViewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(v, songViewHolder.getAdapterPosition(), Constants.EACH_SONG_MENU_CLICKED);
                    }
                });
                return songViewHolder;
            case Constants.TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_user, parent, false);
                final EachUserViewHolder userViewHolder = new EachUserViewHolder(view);
                userViewHolder.userMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, userViewHolder.getAdapterPosition(), Constants.EACH_USER_MENU_CLICKED);
                    }
                });

                userViewHolder.eachUserRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, userViewHolder.getAdapterPosition(), Constants.EACH_USER_LAYOUT_CLICKED);
                    }
                });
                return userViewHolder;
            case Constants.TYPE_PLAYLIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_playlist, parent, false);
                final EachPlaylistViewHolder eachPlaylistViewHolder = new EachPlaylistViewHolder(view);
                eachPlaylistViewHolder.ivPlaylistMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, eachPlaylistViewHolder.getAdapterPosition(), Constants.EACH_PLAYLIST_MENU_CLICKED);
                    }
                });

                eachPlaylistViewHolder.rlPlaylistLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, eachPlaylistViewHolder.getAdapterPosition(), Constants.EACH_PLAYLIST_LAYOUT_CLICKED);
                    }
                });
                return eachPlaylistViewHolder;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (TYPE) {
            case Constants.TYPE_TRACK:
                Song song = songs.get(position);
                EachSongViewHolder eachSongViewHolder = (EachSongViewHolder) holder;
                eachSongViewHolder.songName.setText(song.getTitle());
                if (song.getUser() != null)
                    eachSongViewHolder.artistName.setText(song.getUser().getUsername());
                else if (song.getArtist() == null || song.getArtist().isEmpty())
                    eachSongViewHolder.artistName.setText(context.getResources().getString(R.string.unknown_artist));
                else
                    eachSongViewHolder.artistName.setText(song.getArtist());
                String likes = "";
                if (song.getLikesCount() == 0)
                    likes = Utilities.formatInteger(song.getFavoritngsCount());
                else likes = Utilities.formatInteger(song.getLikesCount());
                eachSongViewHolder.likesCount.setText(likes);
                String playbacks = Utilities.formatInteger(song.getPlaybackCount());
                eachSongViewHolder.playbackCount.setText(playbacks);
                String duration = Utilities.formatTime(song.getDuration());
                eachSongViewHolder.duration.setText(duration);

                if (position == selectedPosition) {
                    eachSongViewHolder.songName.setTextColor(context.getResources().getColor(R.color.colorAccent));
                } else {
                    eachSongViewHolder.songName.setTextColor(context.getResources().getColor(R.color.white));
                }
                break;

            case Constants.TYPE_USER:
                User user = users.get(position);
                final EachUserViewHolder eachUserViewHolder = (EachUserViewHolder) holder;
                eachUserViewHolder.userName.setText(user.getUsername());
                String followersCount = Utilities.formatInteger(user.getFollowersCount());
                eachUserViewHolder.followersCount.setText(followersCount);
                try {
                    Glide.with(context)
                            .load(user.getUserAvatar())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(context.getResources().getDrawable(R.drawable.ic_user_placeholder))
                            .into(new BitmapImageViewTarget(eachUserViewHolder.userImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    eachUserViewHolder.userImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (Exception ignored) {

                }
                break;
            case Constants.TYPE_PLAYLIST:
                Playlist playlist = playlists.get(position);
                final EachPlaylistViewHolder playlistViewHolder = (EachPlaylistViewHolder) holder;
                playlistViewHolder.tvPlaylistUser.setText(playlist.getUser().getUsername());
                playlistViewHolder.tvPlaylistTitle.setText(playlist.getTitle());
                String tracksCount = playlist.getTracksCount() > 1 ? playlist.getTracksCount() + " TRACKS" : playlist.getTracksCount() + " TRACK";
                playlistViewHolder.tvPlaylistsTracksCount.setText(tracksCount);
                String likesCount = Utilities.formatInteger(playlist.getLikesCount());
                playlistViewHolder.tvPlaylistLikesCount.setText(likesCount);
                Glide.with(context)
                        .load(playlist.getArtworkUrl())
                        .placeholder(R.drawable.ic_playlist)
                        .centerCrop()
                        .into(playlistViewHolder.ivPlaylistImage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (TYPE) {
            case Constants.TYPE_TRACK:
                return songs.size();
            case Constants.TYPE_USER:
                return users.size();
            case Constants.TYPE_PLAYLIST:
                return playlists.size();
        }
        return 0;
    }

    public void changeSongData(List<Song> songs) {
        this.songs = songs;
        this.notifyDataSetChanged();
    }

    public void changeUserData(List<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    public void notifySongRemovedFromPlaylist(List<Song> songs, int position) {
        this.songs = songs;
        this.notifyItemRemoved(position);
    }

    public void notifySongAddedToPlaylist(List<Song> songs) {
        this.songs = songs;
        this.notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }
}
