package com.example.anujsharma.shuffler.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.adapters.MyDatabaseAdapter;
import com.example.anujsharma.shuffler.adapters.SimpleRecyclerViewAdapter;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;

import java.util.List;

/**
 * Created by anuj5 on 11-03-2018.
 */

public class DialogBoxes {

    public static void showAddSongToPlaylistDialog(final Context context, View mView, final View newPlaylistView, final Song song) {

        final MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(context);
        final List<Playlist> playlists = myDatabaseAdapter.getPlaylistsList();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView tvNewPlaylist = mView.findViewById(R.id.tvDialogNewPlaylist);
        final TextView tvYourPlaylistsMessage = mView.findViewById(R.id.tvYourPlaylistsMessage);
        final RecyclerView rvPlaylists = mView.findViewById(R.id.rvPlaylistsList);
        final RelativeLayout relativeLayout = mView.findViewById(R.id.rlDialogAddToPlaylist);

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF616261, context.getResources().getColor(R.color.colorPrimary)});
        relativeLayout.setBackground(gd);

        tvNewPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreatePlayListDialog(context, newPlaylistView);
                dialog.dismiss();
            }
        });

        if (playlists.size() > 0) {
            SimpleRecyclerViewAdapter simpleRecyclerViewAdapter = new SimpleRecyclerViewAdapter(context, playlists, new SimpleRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int check) {
                    boolean songAdded = myDatabaseAdapter.addSongToPlaylist(song, playlists.get(position), position);
                    dialog.dismiss();
                    if (songAdded)
                        Toast.makeText(context, "Playlist saved.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "This Song is already present in the Playlist.", Toast.LENGTH_SHORT).show();
                }
            });

            rvPlaylists.setLayoutManager(new LinearLayoutManager(context));
            rvPlaylists.setAdapter(simpleRecyclerViewAdapter);
        } else {
            tvYourPlaylistsMessage.setVisibility(View.GONE);
        }
    }

    public static void showCreatePlayListDialog(final Context context, View mView) {

        final MyDatabaseAdapter myDatabaseAdapter = new MyDatabaseAdapter(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText etPlaylistTitle = mView.findViewById(R.id.etCreatePlaylist);
        final TextView tvCancel = mView.findViewById(R.id.tvCancelButton);
        final TextView tvCreate = mView.findViewById(R.id.tvCreateButton);
        final RelativeLayout relativeLayout = mView.findViewById(R.id.rlCreatePlaylistDialogue);

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF616261, context.getResources().getColor(R.color.colorPrimary)});
        relativeLayout.setBackground(gd);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = etPlaylistTitle.getText().toString();
                if (!title.isEmpty()) {
                    if (title.length() >= 2 && title.length() <= 60) {
                        if (!myDatabaseAdapter.containsPlaylist(title.trim())) {
                            //creating new playlist

                            int id = title.hashCode();
                            Playlist playlist = new Playlist(id, title, 0, "");
                            myDatabaseAdapter.createNewPlaylist(playlist);
                            dialog.dismiss();
                            Toast.makeText(context, "Playlist created successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Playlist '" + title + "' already exists.", Toast.LENGTH_SHORT).show();
                            etPlaylistTitle.setText("");
                        }
                    } else {
                        Toast.makeText(context, "Playlist Name cannot be smaller than 2 characters (or larger than 60 characters)!", Toast.LENGTH_LONG).show();
                        etPlaylistTitle.setText("");
                    }
                } else {
                    Toast.makeText(context, "Playlist name cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
