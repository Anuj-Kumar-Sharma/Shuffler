package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.models.Song;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    public int NOW_PLAYING = -1;
    private Context context;
    private MediaPlayer mediaPlayer;
    private SqliteDbAdapter sqliteDbAdapter;
    private ArrayList<Song> songs;

    public MainRecyclerViewAdapter(Context context, MediaPlayer mediaPlayer) {
        this.context = context;
        this.mediaPlayer = mediaPlayer;
        sqliteDbAdapter = new SqliteDbAdapter(context);
        songs = sqliteDbAdapter.getAllSongs();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_song, parent, false);
        return new MyViewHolder(itemView1);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.songId.setText((position + 1) + "");

        Song song = songs.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        long timeDuration = song.getDuration();
        holder.duration.setText(timeDuration / 60 + ":" + (new DecimalFormat("00").format(timeDuration % 60)));


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (NOW_PLAYING == position) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            holder.ivPlayIndicator.setImageResource(R.drawable.ic_pause);
                        } else {
                            mediaPlayer.start();
                            holder.ivPlayIndicator.setImageResource(R.drawable.ic_play);
                        }
                    } else {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = MediaPlayer.create(context, Uri.parse(String.valueOf(songs.get(position).getSongFile())));
                        mediaPlayer.start();
                        NOW_PLAYING = position;
                        holder.ivPlayIndicator.setVisibility(View.VISIBLE);
                    }
                } else {
                    mediaPlayer = MediaPlayer.create(context, Uri.parse(String.valueOf(songs.get(position).getSongFile())));
                    mediaPlayer.start();
                    NOW_PLAYING = position;
                    holder.ivPlayIndicator.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title, songId, artist, duration;
        private View view;
        private ImageView ivPlayIndicator;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            songId = (TextView) itemView.findViewById(R.id.songId);
            artist = (TextView) itemView.findViewById(R.id.tvArtist);
            duration = (TextView) itemView.findViewById(R.id.tvDuration);
            ivPlayIndicator = (ImageView) itemView.findViewById(R.id.ivPlayIndicator);
        }
    }
}
