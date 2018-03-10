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
import android.widget.Toast;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;
import com.example.anujsharma.shuffler.adapters.YourLibraryRecyclerViewAdapter;
import com.example.anujsharma.shuffler.utilities.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourLibraryFragment extends Fragment {

    private static final String MYPROFILEFRAGMENT = "myProfileFragment";
    private Context context;
    private ImageView headerMyProfile;
    private RecyclerView recyclerView;
    private YourLibraryRecyclerViewAdapter recyclerViewAdapter;

    public YourLibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_library, container, false);

        ((MainActivity) getActivity()).modifyBottomLayout(2);
        initialise(view);
        initialiseListeners();

        return view;
    }

    private void initialiseListeners() {
        headerMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainFrameContainer, myProfileFragment, MYPROFILEFRAGMENT)
                        .addToBackStack(myProfileFragment.getClass().getName()).commit();
            }
        });
    }

    public void initialise(View view) {
        context = getContext();
        headerMyProfile = view.findViewById(R.id.ivHeaderProfile);
        recyclerView = view.findViewById(R.id.rvLibrary);
        recyclerViewAdapter = new YourLibraryRecyclerViewAdapter(context, new YourLibraryRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int check) {
                switch (check) {
                    case Constants.YOUR_LIBRARY_TOP_CONTENT_CLICKED:
                        switch (position) {
                            case 0: // playlists
                                Toast.makeText(context, "sup", Toast.LENGTH_SHORT).show();
                                break;
                            case 1: // songs

                                break;
                            case 2: // artists

                                break;
                        }
                        break;
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
