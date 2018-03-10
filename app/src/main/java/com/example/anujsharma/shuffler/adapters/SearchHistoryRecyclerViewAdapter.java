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
import com.example.anujsharma.shuffler.models.HybridModel;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.viewHolders.SearchHistoryViewHolder;
import com.example.anujsharma.shuffler.viewHolders.SongFooterViewHolder;
import com.example.anujsharma.shuffler.viewHolders.SongHeaderViewHolder;

import java.util.List;

/**
 * Created by anuj5 on 10-01-2018.
 */

public class SearchHistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HISTORY_HEADER = 0;
    private static final int TYPE_HISTORY_FOOTER = 2;
    private static final int TYPE_HISTORY_DATA = 1;
    private Context context;
    private List<HybridModel> historyList;
    private ItemClickListener itemClickListener;


    public SearchHistoryRecyclerViewAdapter(Context context, List<HybridModel> historyList, ItemClickListener itemClickListener) {
        this.context = context;
        this.historyList = historyList;
        this.itemClickListener = itemClickListener;
//        Log.d("TAG", "size of history is " +historyList.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_HISTORY_HEADER;
        if (position <= historyList.size()) return TYPE_HISTORY_DATA;
        if (position == historyList.size() + 1) return TYPE_HISTORY_FOOTER;

        return TYPE_HISTORY_HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_HISTORY_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_header, parent, false);
                return new SongHeaderViewHolder(view);
            case TYPE_HISTORY_FOOTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_song_footer, parent, false);
                final SongFooterViewHolder songFooterViewHolder = new SongFooterViewHolder(view);
                songFooterViewHolder.footerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(v, songFooterViewHolder.getAdapterPosition(), Constants.CLEAR_HISTORY_CLICKED);
                    }
                });
                return songFooterViewHolder;
            case TYPE_HISTORY_DATA:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_search_history_row, parent, false);
                final SearchHistoryViewHolder searchHistoryViewHolder = new SearchHistoryViewHolder(view);
                final View transitionView = searchHistoryViewHolder.ivTypeImage;
                searchHistoryViewHolder.historyLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(transitionView, searchHistoryViewHolder.getAdapterPosition() - 1, Constants.HISTORY_LAYOUT_CLICKED);
                    }
                });
                searchHistoryViewHolder.ivTypeCross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(view, searchHistoryViewHolder.getAdapterPosition() - 1, Constants.HISTORY_CROSS_CLICKED);
                    }
                });
                return searchHistoryViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HISTORY_HEADER:
                SongHeaderViewHolder songHeaderViewHolder = (SongHeaderViewHolder) holder;
                songHeaderViewHolder.headerText.setText(R.string.recent_searches);
                break;
            case TYPE_HISTORY_FOOTER:
                SongFooterViewHolder songFooterViewHolder = (SongFooterViewHolder) holder;
                songFooterViewHolder.footerText.setText(R.string.clear_recent_searches);
                songFooterViewHolder.footerText.setTextColor(context.getResources().getColor(R.color.color_unselected));
                break;
            case TYPE_HISTORY_DATA:
                final SearchHistoryViewHolder searchHistoryViewHolder = (SearchHistoryViewHolder) holder;
                HybridModel history = historyList.get(position - 1);
                searchHistoryViewHolder.tvTypeName.setText(history.getName());
                switch (history.getType()) {
                    case Constants.TYPE_TRACK:
                        searchHistoryViewHolder.tvType.setText(String.format("Song \u2022 %s", history.getSongArtist()));
                        Glide.with(context)
                                .load(Utilities.getLargeArtworkUrl(history.getArtworkUrl()))
                                .placeholder(R.drawable.ic_headphones)
                                .centerCrop()
                                .into(searchHistoryViewHolder.ivTypeImage);
                        break;
                    case Constants.TYPE_USER:
                        searchHistoryViewHolder.tvType.setText(R.string.artist);
                        try {
                            Glide.with(context)
                                    .load(Utilities.getLargeArtworkUrl(history.getArtworkUrl()))
                                    .asBitmap()
                                    .centerCrop()
                                    .placeholder(context.getResources().getDrawable(R.drawable.ic_user_placeholder))
                                    .into(new BitmapImageViewTarget(searchHistoryViewHolder.ivTypeImage) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            searchHistoryViewHolder.ivTypeImage.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                        } catch (Exception ignored) {

                        }
                        break;
                    case Constants.TYPE_PLAYLIST:
                        searchHistoryViewHolder.tvType.setText(R.string.playlist);
                        Glide.with(context)
                                .load(Utilities.getLargeArtworkUrl(history.getArtworkUrl()))
                                .placeholder(R.drawable.ic_playlist)
                                .centerCrop()
                                .into(searchHistoryViewHolder.ivTypeImage);
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (historyList == null || historyList.size() == 0) return 0;
        else return historyList.size() + 2;
    }

    public void updateHistory(List<HybridModel> historyList) {
        this.historyList = historyList;
        this.notifyDataSetChanged();
    }

    public void deleteFromHistory(int position, List<HybridModel> historyList) {
        this.historyList = historyList;
        if (historyList.size() == 0) this.notifyDataSetChanged();
        else this.notifyItemRemoved(position);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position, int check);
    }
}
