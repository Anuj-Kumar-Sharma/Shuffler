package com.example.anujsharma.shuffler.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anujsharma.shuffler.R;
import com.example.anujsharma.shuffler.fragments.HomeFragment;
import com.example.anujsharma.shuffler.fragments.SearchFragment;
import com.example.anujsharma.shuffler.fragments.YourLibraryFragment;

/**
 * Created by anuj5 on 30-12-2017.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private HomeFragment homeFragment;
    //    private MyProfileFragment myProfileFragment;
    private SearchFragment searchFragment;
    private YourLibraryFragment yourLibraryFragment;
    private Context context;

    private Integer tabTitles[] = new Integer[]{R.string.home, R.string.search, R.string.your_library};
    private Integer tabIcons[] = new Integer[]{R.drawable.ic_home, R.drawable.ic_search, R.drawable.ic_library};


    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (homeFragment == null) homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                if (searchFragment == null) searchFragment = new SearchFragment();
                return searchFragment;
            case 2:
                if (yourLibraryFragment == null) yourLibraryFragment = new YourLibraryFragment();
                return yourLibraryFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        if (position == 0) {
            imageView.setImageResource(R.drawable.ic_home);
            tv.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else
            imageView.setImageResource(tabIcons[position]);
        tv.setText(tabTitles[position]);
        return v;
    }
}
