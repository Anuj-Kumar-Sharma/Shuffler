package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.dataStructures.Song;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Song> songsList;

    public MainRecyclerViewAdapter(Context context, ArrayList<Song> songsList) {
        this.context = context;
        this.songsList = songsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_song, parent, false);
        return new MyViewHolder(itemView1);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (songsList.get(position) != null) {
            holder.songId.setText("" + (position + 1));
            holder.title.setText(songsList.get(position).getTitle());
            if (songsList.get(position).getArtist() != null)
                if (songsList.get(position).getArtist().trim().isEmpty())
                    songsList.get(position).setArtist("Unknown Artist");
            holder.artist.setText(songsList.get(position).getArtist());
            int duration = songsList.get(position).getDuration();
            holder.duration.setText(duration / 60 + ":" + (new DecimalFormat("00").format(duration % 60)));
        }
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title, songId, artist, duration;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvTitle);
            songId = (TextView) itemView.findViewById(R.id.songId);
            artist = (TextView) itemView.findViewById(R.id.tvArtist);
            duration = (TextView) itemView.findViewById(R.id.tvDuration);
        }
    }
}
