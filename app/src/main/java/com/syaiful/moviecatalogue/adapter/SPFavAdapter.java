package com.syaiful.moviecatalogue.adapter;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.syaiful.moviecatalogue.R;
import com.syaiful.moviecatalogue.fragment.FavContainerFragment;
import com.syaiful.moviecatalogue.fragment.FavFragment;


public class SPFavAdapter extends FragmentPagerAdapter {
    private final FavContainerFragment mContext;

    public SPFavAdapter(FavContainerFragment context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_movie_title,
            R.string.tab_tv_title
    };
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = FavFragment.newInstance(position + 1);
        return fragment;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        return 2;
    }
}
