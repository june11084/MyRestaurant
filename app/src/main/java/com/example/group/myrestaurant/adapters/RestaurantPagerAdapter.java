package com.example.group.myrestaurant.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.group.myrestaurant.models.RestaurantModel;
import com.example.group.myrestaurant.ui.RestaurantDetailFragment;

import java.util.ArrayList;
public class RestaurantPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<RestaurantModel> mRestaurants;

    public RestaurantPagerAdapter(FragmentManager fm, ArrayList<RestaurantModel> restaurants) {
        super(fm);
        mRestaurants = restaurants;
    }

    @Override
    public Fragment getItem(int position) {
        return RestaurantDetailFragment.newInstance(mRestaurants.get(position));
    }

    @Override
    public int getCount() {
        return mRestaurants.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mRestaurants.get(position).getName();
    }
}
