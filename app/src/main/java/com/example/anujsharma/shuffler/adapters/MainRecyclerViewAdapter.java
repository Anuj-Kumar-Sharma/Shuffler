package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>{

    private Context context;

    public MainRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_song, parent, false);
        return new MyViewHolder(itemView1);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.songId.setText(""+(position+1));
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title, songId, artist, album, duration;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tvTitle);
            songId = (TextView) itemView.findViewById(R.id.songId);
            artist = (TextView) itemView.findViewById(R.id.tvArtist);
            album = (TextView) itemView.findViewById(R.id.tvAlbum);
            duration = (TextView) itemView.findViewById(R.id.tvDuration);
        }
    }
}
