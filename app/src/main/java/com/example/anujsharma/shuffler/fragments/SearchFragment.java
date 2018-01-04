package com.example.anujsharma.shuffler.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.adapters.SearchSongRecyclerViewAdapter;
import com.example.anujsharma.shuffler.dao.TracksDao;
import com.example.anujsharma.shuffler.dao.UsersDao;
import com.example.anujsharma.shuffler.fonts.TypefaceEditText;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.models.User;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.volley.RequestCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements RequestCallback {

    private TracksDao tracksDao;
    private UsersDao usersDao;
    private TypefaceEditText etSearch;
    private ImageView crossOut;
    private RecyclerView rvSearchResult;
    private SearchSongRecyclerViewAdapter searchSongRecyclerAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Song> songs;
    private ArrayList<User> users;
    private Context context;
    private RelativeLayout relativeLayout;
    private int currentSongIndex;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        songs = new ArrayList<>();
        users = new ArrayList<>();
        tracksDao = new TracksDao(context, this);
        usersDao = new UsersDao(context, this);

        searchSongRecyclerAdapter = new SearchSongRecyclerViewAdapter(context, new SearchSongRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int check) {
                switch (check) {
                    case Constants.EACH_SONG_LAYOUT_CLICKED:
                        ((MainActivity) getActivity()).playSongInMainActivity(songs.get(position));
                        changeSelectedPosition(position + 1);
                        break;
                    case Constants.EACH_SONG_MENU_CLICKED:
                        Toast.makeText(context, "View clicked at " + position, Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.EACH_USER_LAYOUT_CLICKED:

                        break;
                    case Constants.EACH_USER_MENU_CLICKED:

                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) context).modifyBottomLayout(1);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initialise(view);
        initialiseListeners();
        return view;
    }

    private void initialiseListeners() {
        crossOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                InputMethodManager imgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imgr != null) {
                    imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                etSearch.requestFocus();
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imgr != null) {
                        imgr.hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
                    }
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        currentSongIndex = 0;
        clearSelectedPosition();
        layoutManager.scrollToPosition(0);
        String query = etSearch.getText().toString();
        tracksDao.getTracksWithQuery(query, 100);
    }


    private void initialise(View view) {
        etSearch = (TypefaceEditText) view.findViewById(R.id.xetSearch);
        crossOut = (ImageView) view.findViewById(R.id.ivSearchCross);
        rvSearchResult = (RecyclerView) view.findViewById(R.id.rvSearchResult);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.search_background);

        rvSearchResult.setAdapter(searchSongRecyclerAdapter);
        layoutManager = new LinearLayoutManager(context);
        rvSearchResult.setLayoutManager(layoutManager);

        searchSongRecyclerAdapter.notifyDataSetChanged();
    }

    public void setBackground(String url) {
        /*Glide.with(context)
                .load(url)
                .into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                relativeLayout.setBackground(resource);
            }
        });*/

    }

    @Override
    public void onListRequestSuccessful(ArrayList list, int check, boolean status) {
        switch (check) {
            case Constants.SEARCH_SONGS_WITH_QUERY:
                if (status) {
                    songs.clear();
                    songs.addAll(list);
//                    setBackground(songs.get(0).getSongArtwork());
                    Collections.sort(songs, new Comparator<Song>() {
                        @Override
                        public int compare(Song o1, Song o2) {
                            if (o2.getLikesCount() > o1.getLikesCount()) return 1;
                            else return -1;
                        }
                    });
                    if (songs.size() > 4) {
                        searchSongRecyclerAdapter.changeSongData(songs.subList(0, 4));
                    } else {
                        searchSongRecyclerAdapter.changeSongData(songs);
                    }
                    usersDao.getUsersWithQuery(etSearch.getText().toString(), 20);
                }
                break;
            case Constants.SEARCH_USERS_WITH_QUERY:
                if (status) {
                    users.clear();
                    users.addAll(list);
                    Collections.sort(users, new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            if (o2.getFollowersCount() > o1.getFollowersCount()) return 1;
                            else return -1;
                        }
                    });
                    if (users.size() > 4) {
                        searchSongRecyclerAdapter.changeUserData(users.subList(0, 4));
                    } else {
                        searchSongRecyclerAdapter.changeUserData(users);
                    }
                }
                break;
        }
    }

    @Override
    public void onObjectRequestSuccessful(Object object, int check, boolean status) {

    }

    public void clearSelectedPosition() {
        searchSongRecyclerAdapter.notifyItemChanged(searchSongRecyclerAdapter.getSelectedPosition());
        searchSongRecyclerAdapter.setSelectedPosition(-1);
    }

    public void changeSelectedPosition(int index) {
        // currentIndex and selected position both are in recyclerview position
        searchSongRecyclerAdapter.notifyItemChanged(searchSongRecyclerAdapter.getSelectedPosition());
        currentSongIndex = index;
        searchSongRecyclerAdapter.setSelectedPosition(currentSongIndex);
        searchSongRecyclerAdapter.notifyItemChanged(currentSongIndex);
    }

}
