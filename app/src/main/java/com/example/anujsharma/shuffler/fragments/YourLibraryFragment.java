package com.example.anujsharma.shuffler.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class YourLibraryFragment extends Fragment {

    private static final String MYPROFILEFRAGMENT = "myProfileFragment";
    ImageView headerMyProfile;

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
        headerMyProfile = (ImageView) view.findViewById(R.id.ivHeaderProfile);
    }

}
