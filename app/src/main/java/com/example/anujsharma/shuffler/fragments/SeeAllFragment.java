package com.example.anujsharma.shuffler.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.adapters.SeeAllRecyclerViewAdapter;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.models.User;
import com.example.anujsharma.shuffler.utilities.Constants;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeAllFragment extends Fragment {

    private TextView tvSeeAllHeader;
    private RecyclerView rvSeeAllRecyclerView;
    private ImageView ivBackButton;
    private SeeAllRecyclerViewAdapter seeAllRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private List<Song> songs;
    private List<User> users;
    private List<Playlist> playlists;
    private int TYPE;
    private Context context;
    private String searchText;

    public SeeAllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        Bundle bundle = getArguments();
        TYPE = bundle.getInt("type");
        searchText = bundle.getString("search");
        songs = bundle.getParcelableArrayList(Constants.SONGS_MODEL_KEY);
        users = bundle.getParcelableArrayList(Constants.USERS_MODEL_KEY);
        playlists = bundle.getParcelableArrayList(Constants.PLAYLIST_MODEL_KEY);

        seeAllRecyclerViewAdapter = new SeeAllRecyclerViewAdapter(context, songs, users, playlists, new SeeAllRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int check) {

            }
        }, TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_all, container, false);

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
    }

    public void initialise(View view) {
        tvSeeAllHeader = view.findViewById(R.id.tvHeaderUserName);
        rvSeeAllRecyclerView = view.findViewById(R.id.rvSeeAll);
        ivBackButton = view.findViewById(R.id.ivUserBackButton);

        switch (TYPE) {
            case Constants.TYPE_TRACK:
                tvSeeAllHeader.setText(String.format("\"%s\" in Songs", searchText));
                break;
            case Constants.TYPE_USER:
                tvSeeAllHeader.setText(String.format("\"%s\" in Artists", searchText));
                break;
            case Constants.TYPE_PLAYLIST:
                tvSeeAllHeader.setText(String.format("\"%s\" in Playlists", searchText));
                break;
        }

        rvSeeAllRecyclerView.setAdapter(seeAllRecyclerViewAdapter);
        layoutManager = new LinearLayoutManager(context);
        rvSeeAllRecyclerView.setLayoutManager(layoutManager);
    }

}
