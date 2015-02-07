package com.quasar_productions.phoenix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quasar_productions.phoenix.fragments.CommentFragment;
import com.quasar_productions.phoenix.fragments.FragmentPostByAuthorList;
import com.quasar_productions.phoenix.fragments.FragmentPostByCategoryList;
import com.quasar_productions.phoenix.fragments.PagerFragment;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;
        Post post;
        public MyPagerAdapter(FragmentManager fragmentManager,Post post) {
            super(fragmentManager); 
            this.post=post;
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
                return CommentFragment.newInstance(post);
            case 1: // Fragment # 0 - This will show FirstFragment different title 
                return PagerFragment.newInstance(1, "Related Posts");
            case 2: // Fragment # 1 - This will show SecondFragment 
                return FragmentPostByAuthorList.newInstance(post.getAuthor().getId());
            case 3:
                return PagerFragment.newInstance(3, "Gallery");
            default: 
                return PagerFragment.newInstance(position, "Some Error Occurred");
            } 
        } 
 
        // Returns the page title for the top indicator 
        @Override 
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        } 
 
    } 