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
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.models.User;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.Utilities;
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
    private static final int TYPE_FOOTER = 101;
    private static final String[] headers = {"Songs", "Artists"};
    private static final String[] footers = {"See all songs", "See all artists"};
    private List<Song> songs;
    private List<User> users;
    private Context context;
    private ItemClickListener itemClickListener;
    private int selectedPosition;

    public SearchSongRecyclerViewAdapter(Context context, ItemClickListener itemClickListener) {
        this.songs = new ArrayList<>();
        this.users = new ArrayList<>();
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

        if (position == 0) return TYPE_HEADER;
        else if (position < songs.size() + 1) return TYPE_TRACK;
        else if (position == songs.size() + 1) return TYPE_FOOTER;

        else if (position == songs.size() + 2) return TYPE_HEADER;
        else if (position < songs.size() + users.size() + 3) return TYPE_USER;
        else return TYPE_FOOTER;
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
                return new SongFooterViewHolder(view);
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
                        itemClickListener.onItemClick(view, songViewHolder.getAdapterPosition() - 1, Constants.EACH_SONG_MENU_CLICKED);
                    }
                });
                return songViewHolder;
            case TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_user, parent, false);
                final EachUserViewHolder userViewHolder = new EachUserViewHolder(view);

                userViewHolder.userMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, userViewHolder.getAdapterPosition() - 7, Constants.EACH_USER_MENU_CLICKED);
                    }
                });

                userViewHolder.eachUserRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(view, userViewHolder.getAdapterPosition() - 7, Constants.EACH_USER_LAYOUT_CLICKED);
                    }
                });
                return userViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int index;
        switch (holder.getItemViewType()) {
            case TYPE_TRACK:
                Song song = songs.get(position - 1);
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
                User user = users.get(position - 7);
                final EachUserViewHolder eachUserViewHolder = (EachUserViewHolder) holder;
                eachUserViewHolder.userName.setText(user.getUsername());
                String followersCount = Utilities.formatInteger(user.getFollowersCount());
                eachUserViewHolder.followersCount.setText(followersCount);
//                Picasso.with(context).load(user.getUserAvatar()).into(eachUserViewHolder.userImage);
                try {
                    Glide.with(context).
                            load(user.getUserAvatar())
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

            case TYPE_HEADER:
                index = position / 6;
                SongHeaderViewHolder songHeaderViewHolder = (SongHeaderViewHolder) holder;
                songHeaderViewHolder.headerText.setText(headers[index]);
                switch (index) {
                    case 0:
                        if (songs.size() > 0)
                            songHeaderViewHolder.headerText.setVisibility(View.VISIBLE);
                        else songHeaderViewHolder.headerText.setVisibility(View.GONE);
                        break;
                    case 1:
                        if (users.size() > 0)
                            songHeaderViewHolder.headerText.setVisibility(View.VISIBLE);
                        else songHeaderViewHolder.headerText.setVisibility(View.GONE);
                        break;
                }
                break;

            case TYPE_FOOTER:
                index = position / 6;
                SongFooterViewHolder songFooterViewHolder = (SongFooterViewHolder) holder;
                songFooterViewHolder.footerText.setText(footers[index]);
                switch (index) {
                    case 0:
                        if (songs.size() == 4)
                            songFooterViewHolder.footerText.setVisibility(View.VISIBLE);
                        else songFooterViewHolder.footerText.setVisibility(View.GONE);
                        break;
                    case 1:
                        if (users.size() == 4)
                            songFooterViewHolder.footerText.setVisibility(View.VISIBLE);
                        else songFooterViewHolder.footerText.setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return songs.size() + users.size() + 4;
    }

    public void changeSongData(List<Song> songs) {
        this.songs = songs;
        this.notifyDataSetChanged();
    }

    public void changeUserData(List<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }

}
