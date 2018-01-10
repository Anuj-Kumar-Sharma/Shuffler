package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.Utilities;

import java.util.List;

/**
 * Created by anuj5 on 09-01-2018.
 */

public class SeeAllViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Song> songs;

    public SeeAllViewPagerAdapter(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View itemView = null;
        if (layoutInflater != null) {
            itemView = layoutInflater.inflate(R.layout.single_see_all_view_page, container, false);
        }
        ImageView imageView = null;
        if (itemView != null) {
            imageView = itemView.findViewById(R.id.ivViewPageImage);
            Glide.with(context)
                    .load(Utilities.getLargeArtworkUrl(songs.get(position).getSongArtwork()))
                    .placeholder(R.drawable.ic_headphones)
                    .centerCrop()
                    .into(imageView);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
