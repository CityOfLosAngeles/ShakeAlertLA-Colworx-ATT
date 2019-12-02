package com.app.shakealertla.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.shakealertla.UserInterface.Fragments.BaseFragment;

import java.util.ArrayList;

public class HomeTabstPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3"};
    private Context context;
    private ArrayList<Fragment> fragments;

    // Colworx : View Pager Adapter used for Tabs in Home Screen with Bottom Navigation bar, in Shelter for Map and list tabs and in Recent Earthquakes screen for Map and List tabs
    public HomeTabstPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
//        return tabTitles[position];
        return null;
    }
}