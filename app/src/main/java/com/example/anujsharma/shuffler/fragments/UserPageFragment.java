package com.example.anujsharma.shuffler.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.adapters.MyDatabaseAdapter;
import com.example.anujsharma.shuffler.adapters.SeeAllRecyclerViewAdapter;
import com.example.anujsharma.shuffler.adapters.UserPageRecyclerViewAdapter;
import com.example.anujsharma.shuffler.backgroundTasks.GetColorPaletteFromImageUrl;
import com.example.anujsharma.shuffler.dao.PlaylistsDao;
import com.example.anujsharma.shuffler.dao.TracksDao;
import com.example.anujsharma.shuffler.dao.UsersDao;
import com.example.anujsharma.shuffler.models.Playlist;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.models.User;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.SharedPreference;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.volley.RequestCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPageFragment extends Fragment implements RequestCallback {

    private TracksDao tracksDao;
    private UsersDao usersDao;
    private PlaylistsDao playlistsDao;
    private ArrayList<Song> songs;
    private Context context;
    private ImageView ivBackButton, ivUserImage;
    private TextView tvHeaderUserName, tvuserName, tvFollowersCount, shufflePlayButton;
    private RecyclerView rvUserRecyclerView;
    private UserPageRecyclerViewAdapter userPageRecyclerViewAdapter;
    private LinearLayout llUserPage;
    private SeeAllRecyclerViewAdapter seeAllRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private User currentUser;
    private long currentUserId;
    private Playlist currentPlaylist;
    private int currentSongIndex;
    private SharedPreference pref;
    private int TYPE;
    private GetColorPaletteFromImageUrl getColorPaletteFromImageUrl;
    private GradientDrawable gd;
    private MyDatabaseAdapter myDatabaseAdapter;

    public UserPageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        songs = new ArrayList<>();
        pref = new SharedPreference(context);
        tracksDao = new TracksDao(context, this);
        usersDao = new UsersDao(context, this);
        playlistsDao = new PlaylistsDao(context, this);
        myDatabaseAdapter = new MyDatabaseAdapter(context, new MyDatabaseAdapter.OnAPlaylistDataChanged() {

            @Override
            public void onASongAdded(int playlistId) {
                currentPlaylist = myDatabaseAdapter.getPlaylist(currentPlaylist);
                songs = (ArrayList<Song>) currentPlaylist.getSongs();
                seeAllRecyclerViewAdapter.notifySongAddedToPlaylist(songs);
                String tvText = String.format("PLAYLIST  •  %d TRACKS", currentPlaylist.getTracksCount());
                tvFollowersCount.setText(tvText);
            }

            @Override
            public void onASongRemoved(int playlistId, int position) {
                currentPlaylist = myDatabaseAdapter.getPlaylist(currentPlaylist);
                songs = (ArrayList<Song>) currentPlaylist.getSongs();
                seeAllRecyclerViewAdapter.notifySongRemovedFromPlaylist(songs, position);
                String tvText = String.format("PLAYLIST  •  %d TRACKS", currentPlaylist.getTracksCount());
                tvFollowersCount.setText(tvText);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move));
        }

        Bundle bundle = getArguments();
        TYPE = bundle.getInt(Constants.TYPE);
        switch (TYPE) {
            case Constants.TYPE_USER:
                currentUser = bundle.getParcelable(Constants.USER_MODEL_KEY);
                currentUserId = bundle.getLong(Constants.USER_ID_KEY);
                userPageRecyclerViewAdapter = new UserPageRecyclerViewAdapter(context, new UserPageRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position, int check) {
                        switch (check) {
                            case Constants.EACH_SONG_LAYOUT_CLICKED:
                                Playlist playlist = new Playlist(songs, currentUser.getUsername());
                                ((MainActivity) getActivity()).playSongInMainActivity(position, playlist);
                                changeSelectedPosition(position + 1);
                                break;
                            case Constants.EACH_SONG_MENU_CLICKED:

                                break;
                        }
                    }
                });
                break;
            case Constants.TYPE_PLAYLIST:
                currentPlaylist = bundle.getParcelable(Constants.PLAYLIST_MODEL_KEY);
                seeAllRecyclerViewAdapter = new SeeAllRecyclerViewAdapter(context, songs, null, null, new SeeAllRecyclerViewAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position, int check) {
                        switch (check) {
                            case Constants.EACH_SONG_LAYOUT_CLICKED:
                                Playlist playlist = currentPlaylist;
                                if (!currentPlaylist.isSelfMade())
                                    playlist = new Playlist(songs, currentPlaylist.getTitle());
                                ((MainActivity) getActivity()).playSongInMainActivity(position, playlist);
                                changeSelectedPosition(position);
                                break;
                            case Constants.EACH_SONG_MENU_CLICKED:
                                PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(context, R.style.PopupMenu), view);
                                popupMenu.getMenuInflater().inflate(R.menu.each_song_pop_up_menu, popupMenu.getMenu());
                                Menu menu = popupMenu.getMenu();
                                menu.findItem(R.id.popAddToPlaylist).setVisible(false);
                                menu.findItem(R.id.popRemoveFromPlaylist).setVisible(true);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.popGotoArtistProfile:
                                                UserPageFragment userPageFragment = new UserPageFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(Constants.TYPE, Constants.TYPE_USER);
                                                bundle.putLong(Constants.USER_ID_KEY, songs.get(position).getUser_id());
                                                userPageFragment.setArguments(bundle);
                                                getFragmentManager().beginTransaction().replace(R.id.mainFrameContainer, userPageFragment, Constants.FRAGMENT_USER_PAGE).
                                                        addToBackStack(null).commit();
                                                return true;
                                            case R.id.popLike:

                                                return true;
                                            case R.id.popShare:

                                                return true;
                                            case R.id.popRemoveFromPlaylist:
                                                myDatabaseAdapter.deleteASongFromPlaylist(currentPlaylist, (int) songs.get(position).getId(), position);
                                                return true;

                                        }
                                        return false;
                                    }
                                });
                                popupMenu.show();
                                break;
                        }
                    }
                }, Constants.TYPE_TRACK);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_page, container, false);

        initialise(view);
        initialiseListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        onCreate(null);
    }

    private void initialiseListeners() {
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        shufflePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (TYPE) {
                    case Constants.TYPE_USER:
                        Toast.makeText(context, "Cannot shuffle this playlist.", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.TYPE_PLAYLIST:
                        pref.setIsShuffleOn(true);
                        ((MainActivity) getActivity()).playSongInMainActivity(0, currentPlaylist);
                        break;

                }
            }
        });
    }

    private void initialise(View view) {
        ivBackButton = view.findViewById(R.id.ivUserBackButton);
        ivUserImage = view.findViewById(R.id.userImage);
        tvuserName = view.findViewById(R.id.userName);
        shufflePlayButton = view.findViewById(R.id.ivShuffleButton);
        tvHeaderUserName = view.findViewById(R.id.tvHeaderUserName);
        tvFollowersCount = view.findViewById(R.id.userFollowers);
        rvUserRecyclerView = view.findViewById(R.id.userRecyclerView);
        llUserPage = view.findViewById(R.id.llUserPage);

        linearLayoutManager = new LinearLayoutManager(context);
        rvUserRecyclerView.setLayoutManager(linearLayoutManager);

        gd = new GradientDrawable();
        gd.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);

        getColorPaletteFromImageUrl = new GetColorPaletteFromImageUrl(context, new GetColorPaletteFromImageUrl.PaletteCallback() {
            @Override
            public void onPostExecute(Palette palette) {
                changeBackground(Utilities.getBackgroundColorFromPalette(palette));
            }
        });

        switch (TYPE) {
            case Constants.TYPE_USER:
                if (currentUser != null) {
                    initializeUser();
                }
                rvUserRecyclerView.setAdapter(userPageRecyclerViewAdapter);
                usersDao.getUserWithId(Long.toString(currentUserId));
                break;
            case Constants.TYPE_PLAYLIST:
                if (currentPlaylist != null) {
                    initialisePlaylist();
                }
                rvUserRecyclerView.setAdapter(seeAllRecyclerViewAdapter);
                break;
        }

    }

    public void changeBackground(int color) {
        gd.setColors(new int[]{color, context.getResources().getColor(R.color.colorDark)});
        llUserPage.setBackground(gd);
    }

    private void initialisePlaylist() {

        if (!currentPlaylist.getArtworkUrl().isEmpty())
            getColorPaletteFromImageUrl.execute(currentPlaylist.getArtworkUrl());
        else if (currentPlaylist.getPlaylistArtworkBlob() != null) {
            Palette palette = Palette.from(Utilities.getBitmapFromBytes(currentPlaylist.getPlaylistArtworkBlob())).generate();
            changeBackground(Utilities.getBackgroundColorFromPalette(palette));
        }

        tvuserName.setText(currentPlaylist.getTitle());
        tvHeaderUserName.setText(currentPlaylist.getTitle());

        String tvText = String.format("PLAYLIST  •  %d TRACKS", currentPlaylist.getTracksCount());

        if (!currentPlaylist.isSelfMade()) {
            if (currentPlaylist.getLikesCount() > 0)
                tvFollowersCount.setText(String.format("%s  •  %s LIKES", tvText, Utilities.formatIntegerWithCommas(currentPlaylist.getLikesCount(), "")));
            else tvFollowersCount.setText(tvText);
            Glide.with(context)
                    .load(Utilities.getLargeArtworkUrl(currentPlaylist.getArtworkUrl()))
                    .placeholder(R.drawable.ic_playlist)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivUserImage);
            playlistsDao.getTracksFromPlaylistId(currentPlaylist.getPlaylistId());
        }
        else {
            tvuserName.setVisibility(View.GONE);
            tvFollowersCount.setText(tvText);
            songs.clear();
            songs = (ArrayList<Song>) currentPlaylist.getSongs();
            changeSelectedPosition(Utilities.getSelectedPosition(context, songs, 0));
            seeAllRecyclerViewAdapter.changeSongData(songs);
            if (currentPlaylist.getPlaylistArtworkBlob() != null) {
                Bitmap bitmap = Utilities.getBitmapFromBytes(currentPlaylist.getPlaylistArtworkBlob());
                ivUserImage.setImageBitmap(bitmap);
            }
        }
    }

    private void initializeUser() {

        getColorPaletteFromImageUrl.execute(currentUser.getUserAvatar());

        tvuserName.setText(currentUser.getUsername());
        tvHeaderUserName.setText(currentUser.getUsername());
        String followersCount = Utilities.formatIntegerWithCommas(currentUser.getFollowersCount(), " FOLLOWERS");
        tvFollowersCount.setText(followersCount);
        try {
            Glide.with(context)
                    .load(Utilities.getLargeArtworkUrl(currentUser.getUserAvatar()))
                    .asBitmap()
                    .centerCrop()
                    .placeholder(context.getResources().getDrawable(R.drawable.ic_user_placeholder))
                    .into(new BitmapImageViewTarget(ivUserImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivUserImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception ignored) {

        }
        tracksDao.getTracksFromUserId(currentUser.getId(), 100);
    }

    @Override
    public void onListRequestSuccessful(ArrayList list, int check, boolean status) {
        switch (check) {
            case Constants.SEARCH_SONGS_WITH_USER_ID:
                if (status) {
                    songs.clear();
                    if (list != null) songs.addAll(list);
                    else break;
                    Collections.sort(songs, new Comparator<Song>() {
                        @Override
                        public int compare(Song o1, Song o2) {
                            if (o2.getFavoritngsCount() > o1.getFavoritngsCount()) return 1;
                            else if (o2.getFavoritngsCount() < o1.getFavoritngsCount()) return -1;
                            else return 0;
                        }
                    });
                    if (songs.size() > 4) {
                        changeSelectedPosition(Utilities.getSelectedPosition(context, songs.subList(0, 4), 1));
                        userPageRecyclerViewAdapter.changeSongData(songs.subList(0, 4));
                    } else {
                        changeSelectedPosition(Utilities.getSelectedPosition(context, songs, 1));
                        userPageRecyclerViewAdapter.changeSongData(songs);
                    }
                }
                break;
            case Constants.SEARCH_SONG_WITH_PLAYLIST_ID:
                if (status) {
                    songs.clear();
                    if (list != null) songs.addAll(list);
                    else break;
                    /*Collections.sort(songs, new Comparator<Song>() {
                        @Override
                        public int compare(Song o1, Song o2) {
                            if (o2.getFavoritngsCount() > o1.getFavoritngsCount()) return 1;
                            else return -1;
                        }
                    });*/
                    changeSelectedPosition(Utilities.getSelectedPosition(context, songs, 0));
                    seeAllRecyclerViewAdapter.changeSongData(songs);
                }
                break;
        }
    }

    @Override
    public void onObjectRequestSuccessful(Object object, int check, boolean status) {
        switch (check) {
            case Constants.SEARCH_USER_WITH_ID:
                if (status) {
                    this.currentUser = (User) object;
                    initializeUser();
                }
        }

    }

    public void changeSelectedPosition(int index) {
        // currentIndex and selected position both are in recyclerview position
        switch (TYPE) {
            case Constants.TYPE_USER:
                userPageRecyclerViewAdapter.notifyItemChanged(userPageRecyclerViewAdapter.getSelectedPosition());
                currentSongIndex = index;
                userPageRecyclerViewAdapter.setSelectedPosition(currentSongIndex);
                userPageRecyclerViewAdapter.notifyItemChanged(currentSongIndex);
                break;
            case Constants.TYPE_PLAYLIST:
                seeAllRecyclerViewAdapter.notifyItemChanged(seeAllRecyclerViewAdapter.getSelectedPosition());
                currentSongIndex = index;
                seeAllRecyclerViewAdapter.setSelectedPosition(currentSongIndex);
                seeAllRecyclerViewAdapter.notifyItemChanged(currentSongIndex);
                break;
        }
    }
}
