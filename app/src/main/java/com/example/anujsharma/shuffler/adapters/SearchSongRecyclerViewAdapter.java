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
import com.example.anujsharma.shuffler.viewHolders.SongFooterViewHolder;
import com.example.anujsharma.shuffler.viewHolders.SongHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class SearchSongRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 100;
    private static final int TYPE_TRACK = 0;
    private static final int TYPE_USER = 1;
    private static final int TYPE_PLAYLIST = 2;
    private static final int TYPE_FOOTER = 101;
    private static final String[] headers = {"Songs", "Artists", "Playlists"};
    private static final String[] footers = {"See all songs", "See all artists", "See all playlists"};
    private static boolean hasTrackFooter, hasUsersFooter, hasPlaylistFooter, hasTrackHeader, hasUserHeader, hasPlaylistHeader;
    private int FOOTER_TRACK = 1;
    private int FOOTER_USER = 3;
    private int FOOTER_PLAYLIST = 5;
    private int HEADER_TRACK = 0;
    private int HEADER_USER = 9;
    private int HEADER_PLAYLIST = 11;
    private List<Song> songs;
    private List<User> users;
    private List<Playlist> playlists;
    private Context context;
    private ItemClickListener itemClickListener;
    private int selectedPosition;

    public SearchSongRecyclerViewAdapter(Context context, ItemClickListener itemClickListener) {
        this.songs = new ArrayList<>();
        this.users = new ArrayList<>();
        this.playlists = new ArrayList<>();
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER_TRACK && hasTrackHeader) return TYPE_HEADER;
        if (position < FOOTER_TRACK && hasTrackHeader) return TYPE_TRACK;
        if (position == FOOTER_TRACK && hasTrackFooter) return TYPE_FOOTER;

        if (position == HEADER_USER && hasUserHeader) return TYPE_HEADER;
        if (position < FOOTER_USER && hasUserHeader) return TYPE_USER;
        if (position == FOOTER_USER && hasUsersFooter) return TYPE_FOOTER;

        if (position == HEADER_PLAYLIST && hasPlaylistHeader) return TYPE_HEADER;
        if (position < FOOTER_PLAYLIST && hasPlaylistHeader) return TYPE_PLAYLIST;
        if (position == FOOTER_PLAYLIST && hasPlaylistFooter) return TYPE_FOOTER;

        return TYPE_HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_header, parent, false);
                return new SongHeaderViewHolder(view);
            case TYPE_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_footer, parent, false);
                final SongFooterViewHolder songFooterViewHolder = new SongFooterViewHolder(view);
                songFooterViewHolder.footerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*switch (songFooterViewHolder.getAdapterPosition()) {
                            case FOOTER_TRACK:
                                itemClickListener.onItemClick(v, songFooterViewHolder.getAdapterPosition(), Constants.SEE_ALL_SONGS_CLICKED);
                                break;
                            case 11:
                                itemClickListener.onItemClick(v, songFooterViewHolder.getAdapterPosition(), Constants.SEE_ALL_USERS_CLICKED);
                                break;
                        }*/
                        if (songFooterViewHolder.getAdapterPosition() == FOOTER_TRACK && hasTrackFooter) {
                            itemClickListener.onItemClick(v, songFooterViewHolder.getAdapterPosition(), Constants.SEE_ALL_SONGS_CLICKED);
                        } else if (songFooterViewHolder.getAdapterPosition() == FOOTER_USER && hasUsersFooter) {
                            itemClickListener.onItemClick(v, songFooterViewHolder.getAdapterPosition(), Constants.SEE_ALL_USERS_CLICKED);
                        } else if (songFooterViewHolder.getAdapterPosition() == FOOTER_PLAYLIST && hasPlaylistFooter) {
                            itemClickListener.onItemClick(v, songFooterViewHolder.getAdapterPosition(), Constants.SEE_ALL_PLAYLISTS_CLICKED);
                        }
                    }
                });
                return songFooterViewHolder;
            case TYPE_TRACK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_track, parent, false);
                final EachSongViewHolder songViewHolder = new EachSongViewHolder(view);
                songViewHolder.eachSongLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, songViewHolder.getAdapterPosition() - 1, Constants.EACH_SONG_LAYOUT_CLICKED);
                    }
                });
                songViewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(v, songViewHolder.getAdapterPosition() - 1, Constants.EACH_SONG_MENU_CLICKED);
                    }
                });
                songViewHolder.eachSongLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        itemClickListener.onItemClick(v, songViewHolder.getAdapterPosition() - 1, Constants.EACH_SONG_VIEW_LONG_CLICKED);
                        return true;
                    }
                });
                return songViewHolder;
            case TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_user, parent, false);
                final EachUserViewHolder userViewHolder = new EachUserViewHolder(view);

                userViewHolder.userMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, userViewHolder.getAdapterPosition() - (HEADER_USER + 1), Constants.EACH_USER_MENU_CLICKED);
                    }
                });

                userViewHolder.eachUserRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, userViewHolder.getAdapterPosition() - (HEADER_USER + 1), Constants.EACH_USER_LAYOUT_CLICKED);
                    }
                });
                return userViewHolder;
            case TYPE_PLAYLIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_playlist, parent, false);
                final EachPlaylistViewHolder playlistViewHolder = new EachPlaylistViewHolder(view);
                playlistViewHolder.rlPlaylistLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(v, playlistViewHolder.getAdapterPosition() - (HEADER_PLAYLIST + 1), Constants.EACH_PLAYLIST_LAYOUT_CLICKED);
                    }
                });
                playlistViewHolder.ivPlaylistMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(v, playlistViewHolder.getAdapterPosition() - (HEADER_PLAYLIST + 1), Constants.EACH_PLAYLIST_MENU_CLICKED);
                    }
                });
                return playlistViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int index;
        switch (holder.getItemViewType()) {
            case TYPE_TRACK:
                Song song = null;
                if (hasTrackHeader) song = songs.get(position - HEADER_TRACK - 1);
                else break;
                EachSongViewHolder eachSongViewHolder = (EachSongViewHolder) holder;
                eachSongViewHolder.songName.setText(song.getTitle());
                eachSongViewHolder.artistName.setText(song.getUser().getUsername());
                String likes = Utilities.formatInteger(song.getLikesCount());
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

            case TYPE_USER:
                User user;
                if (hasUserHeader && position - HEADER_USER - 1 >= 0)
                    user = users.get(position - HEADER_USER - 1);
                else break;
                final EachUserViewHolder eachUserViewHolder = (EachUserViewHolder) holder;
                eachUserViewHolder.userName.setText(user.getUsername());
                String followersCount = Utilities.formatInteger(user.getFollowersCount());
                eachUserViewHolder.followersCount.setText(followersCount);
                try {
                    Glide.with(context)
                            .load(Utilities.getLargeArtworkUrl(user.getUserAvatar()))
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

            case TYPE_PLAYLIST:
                Playlist playlist;
                if (hasPlaylistHeader && position - HEADER_PLAYLIST - 1 >= 0)
                    playlist = playlists.get(position - HEADER_PLAYLIST - 1);
                else break;
                final EachPlaylistViewHolder playlistViewHolder = (EachPlaylistViewHolder) holder;
                playlistViewHolder.tvPlaylistUser.setText(playlist.getUser().getUsername());
                playlistViewHolder.tvPlaylistTitle.setText(playlist.getTitle());
                String tracksCount = playlist.getTracksCount() > 1 ? playlist.getTracksCount() + " TRACKS" : playlist.getTracksCount() + " TRACK";
                playlistViewHolder.tvPlaylistsTracksCount.setText(tracksCount);
                String likesCount = Utilities.formatInteger(playlist.getLikesCount());
                playlistViewHolder.tvPlaylistLikesCount.setText(likesCount);
                Glide.with(context)
                        .load(Utilities.getLargeArtworkUrl(playlist.getArtworkUrl()))
                        .placeholder(R.drawable.ic_playlist)
                        .centerCrop()
                        .into(playlistViewHolder.ivPlaylistImage);


                break;

            case TYPE_HEADER:
                if (position == HEADER_TRACK && hasTrackHeader) {
                    SongHeaderViewHolder songHeaderViewHolder = (SongHeaderViewHolder) holder;
                    songHeaderViewHolder.headerText.setText(headers[0]);
                } else if (position == HEADER_USER && hasUserHeader) {
                    SongHeaderViewHolder songHeaderViewHolder = (SongHeaderViewHolder) holder;
                    songHeaderViewHolder.headerText.setText(headers[1]);
                } else if (position == HEADER_PLAYLIST && hasPlaylistHeader) {
                    SongHeaderViewHolder songHeaderViewHolder = (SongHeaderViewHolder) holder;
                    songHeaderViewHolder.headerText.setText(headers[2]);
                }
                break;

            case TYPE_FOOTER:
                if (position == FOOTER_TRACK && hasTrackFooter) {
                    SongFooterViewHolder songFooterViewHolder = (SongFooterViewHolder) holder;
                    songFooterViewHolder.footerText.setText(footers[0]);
                } else if (position == FOOTER_USER && hasUsersFooter) {
                    SongFooterViewHolder songFooterViewHolder = (SongFooterViewHolder) holder;
                    songFooterViewHolder.footerText.setText(footers[1]);
                } else if (position == FOOTER_PLAYLIST && hasPlaylistFooter) {
                    SongFooterViewHolder songFooterViewHolder = (SongFooterViewHolder) holder;
                    songFooterViewHolder.footerText.setText(footers[2]);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return FOOTER_PLAYLIST + 1;
    }

    public void changeSongData(List<Song> songs) {
        this.songs = songs;
        hasTrackHeader = songs.size() > 0;
        if (hasTrackHeader) FOOTER_TRACK = 1 + songs.size();
        else FOOTER_TRACK = songs.size();
        hasTrackFooter = songs.size() == 4;
        this.notifyItemRangeChanged(HEADER_TRACK, FOOTER_TRACK);
    }

    public void changeUserData(List<User> users) {
        this.users = users;
        hasUserHeader = users.size() > 0;
        if (hasTrackFooter) {
            HEADER_USER = FOOTER_TRACK + 1;
        } else {
            HEADER_USER = FOOTER_TRACK;
        }
        if (hasUserHeader) FOOTER_USER = HEADER_USER + users.size() + 1;
        else FOOTER_USER = HEADER_USER;
        hasUsersFooter = users.size() == 4;
        this.notifyItemRangeChanged(HEADER_USER, FOOTER_USER - HEADER_USER + 1);
    }

    public void changePlaylistData(List<Playlist> playlists) {
        this.playlists = playlists;
        hasPlaylistHeader = playlists.size() > 0;
        if (hasUsersFooter) {
            HEADER_PLAYLIST = FOOTER_USER + 1;
        } else {
            HEADER_PLAYLIST = FOOTER_USER;
        }
        if (hasPlaylistHeader) FOOTER_PLAYLIST = HEADER_PLAYLIST + playlists.size() + 1;
        else FOOTER_PLAYLIST = HEADER_PLAYLIST;
        hasPlaylistFooter = playlists.size() == 4;
        this.notifyItemRangeChanged(HEADER_PLAYLIST, FOOTER_PLAYLIST - HEADER_PLAYLIST + 1);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }

}
