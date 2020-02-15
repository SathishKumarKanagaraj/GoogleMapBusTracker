package com.bustrackingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    private String busRouteValue;

    public ViewPagerAdapter(FragmentManager fm, int tabCountValue, String selectedBus) {

        super(fm);
        this.tabCount = tabCountValue;
        this.busRouteValue = selectedBus;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MapsActivity();
                Bundle args = new Bundle();
                args.putString("route_value", busRouteValue);
                fragment.setArguments(args);
                break;

            case 1:
                fragment = new BusListFragment();
                Bundle arg = new Bundle();
                arg.putString("route_value", busRouteValue);
                fragment.setArguments(arg);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
