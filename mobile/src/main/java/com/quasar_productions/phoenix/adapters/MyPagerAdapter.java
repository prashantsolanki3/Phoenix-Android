package com.quasar_productions.phoenix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quasar_productions.phoenix.fragments.FirstFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;
 
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager); 
        } 
 
        // Returns total number of pages 
        @Override 
        public int getCount() { 
            return NUM_ITEMS;
        } 
 
        // Returns the fragment to display for that page 
        @Override 
        public Fragment getItem(int position) {
            switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment 
                return FirstFragment.newInstance(0, "Comments");
            case 1: // Fragment # 0 - This will show FirstFragment different title 
                return FirstFragment.newInstance(1, "Related Posts");
            case 2: // Fragment # 1 - This will show SecondFragment 
                return FirstFragment.newInstance(2, "Attachments");
            case 3:
                return FirstFragment.newInstance(3, "Other Features");
            default: 
                return FirstFragment.newInstance(position,"Some Error Occurred");
            } 
        } 
 
        // Returns the page title for the top indicator 
        @Override 
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        } 
 
    } 