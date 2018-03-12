package com.example.anujsharma.shuffler.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.adapters.MyDatabaseAdapter;
import com.example.anujsharma.shuffler.adapters.PlaylistsListRecyclerViewAdapter;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.utilities.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistsListFragment extends Fragment {

    private Context context;
    private ImageView ivBackButton, ivCreatePlaylistButton;
    private RecyclerView rvPlaylistsList;
    private MyDatabaseAdapter myDatabaseAdapter;
    private PlaylistsListRecyclerViewAdapter playlistsListRecyclerViewAdapter;
    private List<Playlist> playlists;

    public PlaylistsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlists_list, container, false);

        context = getContext();
        initialise(view);
        initialiseListeners();

        return view;
    }

    private void initialiseListeners() {
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ivCreatePlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.create_playlist_dialog_layout, null);
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
        });
    }

    private void initialise(View view) {
        ivBackButton = view.findViewById(R.id.ivPlaylistsBackButton);
        ivCreatePlaylistButton = view.findViewById(R.id.ivCreatePlaylist);
        rvPlaylistsList = view.findViewById(R.id.rvPlaylistsList);

        myDatabaseAdapter = new MyDatabaseAdapter(context, new MyDatabaseAdapter.OnDatabaseChanged() {
            @Override
            public void onPlaylistAdded() {
                playlists = myDatabaseAdapter.getPlaylistsList();
                playlistsListRecyclerViewAdapter.addToPlaylistsList(playlists);
            }

            @Override
            public void onPlaylistDeleted(int position) {
                playlists = myDatabaseAdapter.getPlaylistsList();
                playlistsListRecyclerViewAdapter.deleteFromPlaylistsList(position, playlists);
            }

            @Override
            public void onPlaylistUpdated(int position) {
                playlists = myDatabaseAdapter.getPlaylistsList();
                playlistsListRecyclerViewAdapter.updatePlaylist(position, playlists);
                Playlist updatedPlaylist = myDatabaseAdapter.getPlaylist(playlists.get(position));
                ((MainActivity) getActivity()).updatePlaylistInMainActivity(updatedPlaylist);
            }
        });

        playlists = myDatabaseAdapter.getPlaylistsList();

        playlistsListRecyclerViewAdapter = new PlaylistsListRecyclerViewAdapter(context, new PlaylistsListRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, int check) {
                switch (check) {
                    case Constants.EACH_PLAYLIST_LAYOUT_CLICKED:
                        Playlist playlist = myDatabaseAdapter.getPlaylist(playlists.get(position));
                        UserPageFragment userPageFragment = new UserPageFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.TYPE, Constants.TYPE_PLAYLIST);
                        bundle.putParcelable(Constants.PLAYLIST_MODEL_KEY, playlist);
                        userPageFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.mainFrameContainer, userPageFragment, Constants.FRAGMENT_USER_PAGE)
                                .addToBackStack(userPageFragment.getClass().getName()).commit();
                        break;
                    case Constants.EACH_PLAYLIST_MENU_CLICKED:
                        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(context, R.style.PopupMenu), view);
                        popupMenu.getMenuInflater().inflate(R.menu.playlists_list_pop_up_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.popPlayAll:

                                        break;
                                    case R.id.popShare:

                                        break;
                                    case R.id.popDeletePlaylist:
                                        showDeleteConfirmationDialog(playlists.get(position), position);
                                        break;
                                    case R.id.popRenamePlaylist:
                                        showRenamePlaylistDialog(playlists.get(position), position);
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                        break;
                }
            }
        }, playlists);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvPlaylistsList.setAdapter(playlistsListRecyclerViewAdapter);
        rvPlaylistsList.setLayoutManager(linearLayoutManager);

    }

    private void showRenamePlaylistDialog(final Playlist playlist, final int position) {
        View mView = getLayoutInflater().inflate(R.layout.create_playlist_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText etPlaylistTitle = mView.findViewById(R.id.etCreatePlaylist);
        final TextView tvHeaderMessage = mView.findViewById(R.id.tvCreatePlaylistMessage);
        final TextView tvCancel = mView.findViewById(R.id.tvCancelButton);
        final TextView tvCreate = mView.findViewById(R.id.tvCreateButton);
        final RelativeLayout relativeLayout = mView.findViewById(R.id.rlCreatePlaylistDialogue);

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF616261, context.getResources().getColor(R.color.colorPrimary)});
        relativeLayout.setBackground(gd);

        etPlaylistTitle.setText(playlist.getTitle());
        etPlaylistTitle.selectAll();
        tvHeaderMessage.setText("Rename Playlist");
        tvCreate.setText("RENAME");

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
                            //renaming the playlist
                            playlist.setTitle(title);
                            playlist.setType(title);
                            myDatabaseAdapter.updatePlaylist(playlist, position);
                            dialog.dismiss();
                            Toast.makeText(context, "Playlist renamed.", Toast.LENGTH_SHORT).show();
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

    private void showDeleteConfirmationDialog(final Playlist playlist, final int position) {
        View mView = getLayoutInflater().inflate(R.layout.dialogue_delete_playlist_confirmation, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(mView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView tvMessage = mView.findViewById(R.id.tvConfirmationMessage);
        final TextView tvCancel = mView.findViewById(R.id.tvCancelButton);
        final TextView tvDelete = mView.findViewById(R.id.tvDeleteButton);
        final RelativeLayout relativeLayout = mView.findViewById(R.id.rlDeletePlaylistConfirmation);

        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF616261, context.getResources().getColor(R.color.colorPrimary)});
        relativeLayout.setBackground(gd);

        tvMessage.setText(String.format("Are you sure you want to delete the playlist '%s' ?", playlist.getTitle()));
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDatabaseAdapter.deletePlaylist((int) playlist.getPlaylistId(), position);
                dialog.dismiss();
            }
        });
    }

}
