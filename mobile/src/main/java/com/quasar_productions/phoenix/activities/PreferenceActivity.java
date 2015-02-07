package com.quasar_productions.phoenix.activities;

import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.Utils.PrefManager;

import java.util.HashMap;
import java.util.Hashtable;

public class PreferenceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
           /* getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();*/
            getFragmentManager().beginTransaction().replace(android.R.id.content,
                    new PlaceholderFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preference, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
           case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends PreferenceFragment {

        public PlaceholderFragment() {
        }
        HashMap<String,String> websiteConfig;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           websiteConfig= PrefManager.Init(getActivity().getApplicationContext()).getWebsiteConfig();
           PrefManager.Init(getActivity().getApplicationContext()).setWebsiteConfig("","");
            addPreferencesFromResource(R.xml.preference_xml);
        }

        @Override
        public void onDestroy() {
            HashMap<String,String>hashMap=PrefManager.Init(getActivity().getApplicationContext()).getWebsiteConfig();
            if(hashMap.get(PrefManager.KEY_WEBSITE_URL).equals("")||hashMap.get(PrefManager.KEY_API_ENDPOINT).equals(""))
            PrefManager.Init(getActivity().getApplicationContext()).setWebsiteConfig(websiteConfig.get(PrefManager.KEY_WEBSITE_URL),websiteConfig.get(PrefManager.KEY_API_ENDPOINT));
            super.onDestroy();
        }
    }
}
