package com.quasar_productions.phoenix.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.fragments.FragmentHome;
import com.quasar_productions.phoenix.fragments.FragmentPostByCategoryList;
import com.quasar_productions.phoenix.fragments.FragmentRecentPosts;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Categories;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;
import de.greenrobot.event.EventBus;


public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {

    public List<String> mListNameItem;
    ArrayList<Categories> categories;

    @Override
    public void onUserInformation() {
        //User information here
        this.mUserName.setText(getString(R.string.app_name));
        this.mUserEmail.setText("testing@quasar-productions.com");
        Picasso.with(this).load("http://www.edrants.com/wp-content/uploads/2009/09/placeholder.jpg").resize(72,72).into(this.mUserPhoto);
        Picasso.with(this).load("http://androidspin.com/wp-content/uploads/2014/09/wallpaper_21.jpg").resize(300,300).into(this.mUserBackground);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
           }*/
  Toolbar toolbar;
    @Override
    public void onInt(Bundle savedInstanceState) {
        //Creation of the list items is here

        savedInstanceState=this.getIntent().getExtras();

        categories = Parcels.unwrap(savedInstanceState.getParcelable(getString(R.string._categories_intent)));
        setUpDrawer(categories);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,128);

        toolbar= getToolbar();
        toolbar.setLayoutParams(params);
        toolbar.setSubtitle(" ");
        toolbar.setMinimumHeight(128);

        setSupportActionBar(toolbar);

    }


    void setUpDrawer(ArrayList<Categories> categories){
        // set listener {required}
        this.setNavigationListener(this);
        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(getString(R.string.nav_header0));
        mListNameItem.add(getString(R.string.nav_option1));
        mListNameItem.add(getString(R.string.nav_option2));
        mListNameItem.add(getString(R.string.nav_header3));
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0);
        mListIconItem.add(0);
        mListIconItem.add(0);
        mListIconItem.add(0);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        //indicate all items that have a counter
        SparseIntArray mSparseCounterItem = new SparseIntArray();

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(0);
        mListHeaderItem.add(3);


        int i=4;
        for(Categories cat:categories){
            mListNameItem.add(cat.getTitle());
            mListIconItem.add(0);
            mSparseCounterItem.put(i++,cat.getPost_count());
        }

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer(R.string.preferences, android.R.drawable.ic_menu_preferences);

        this.setNavigationAdapter(mListNameItem, mListIconItem,mListHeaderItem,mSparseCounterItem);
        setDefaultStartPositionNavigation(1);
        openDrawer();
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment mFragment;
        if(position==1){
            mFragment  = new FragmentHome().newInstance(getApplicationContext());
            toolbar.setSubtitle("Home");
            if (mFragment != null)
                mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();

        }
        else if(position==2){
            mFragment  = new FragmentRecentPosts().newInstance();
            toolbar.setSubtitle("Favorites");
            if (mFragment != null)
                mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();

        }
        else if(position>3) {
           mFragment  = new FragmentPostByCategoryList().newInstance(categories.get(position-4).getId());
            toolbar.setSubtitle(categories.get(position-4).getTitle());
            if (mFragment != null)
                mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();

        }

    }


    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {


      /*  //hide the menu when the navigation is opens
        switch (position) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;
            case 1:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;
        }*/
    }


    @Override
    public void onClickUserPhotoNavigation(View v) {
        //user photo onClick
        Toast.makeText(this,"User Profile", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClickFooterItemNavigation(View v) {
        //footer onClick
        startActivity(new Intent(this, PreferenceActivity.class));
    }

}
