package com.quasar_productions.phoenix_lib.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.quasar_productions.phoenix_lib.R;

import java.util.HashMap;

public class PrefManager {
    // Shared Preferences
    static SharedPreferences pref;
    // Editor for Shared preferences
    static SharedPreferences.Editor editor;
    // Context
    static Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static String PREF_NAME;
     
    //Accessible from outside
    public static final String KEY_WEBSITE_URL = "website_url";
    public static final String KEY_API_ENDPOINT = "api_endpoint";
    public static final String KEY_FEATURED_SLUG = "featured_post_slug";
    public static final String KEY_FEATURED_POST_COUNT = "featured_post_count";
    public static final String KEY_DYNAMIC_COLORS = "dynamic_colors";
    public static final String KEY_DYNAMIC_COLORS_TYPE = "dynamic_colors_type";

    // Constructor
    public PrefManager(Context context){
        this._context = context;
        PREF_NAME = _context.getString(R.string.app_name);
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }

    public static PrefManager Init(Context context){
        PrefManager prefManager = new PrefManager(context);
       return prefManager;
    }
     
    /**
     * Set Website Configuration.
     * */
    public void setWebsiteConfig(String webisite_url, String endpoint) {
        // Storing website in pref
        editor.putString(KEY_WEBSITE_URL, webisite_url);

        // Storing email in pref
        editor.putString(KEY_API_ENDPOINT, endpoint);

        // commit changes
        editor.commit();
    }

     
    /**
     * Get Website Configuration.
     * */
    public HashMap<String, String> getWebsiteConfig(){
        HashMap<String, String> config = new HashMap<>();
        // user name
        config.put(KEY_WEBSITE_URL, pref.getString(KEY_WEBSITE_URL, null));
         
        // user email id
        config.put(KEY_API_ENDPOINT,pref.getString(KEY_API_ENDPOINT, null));
         
        // return user
        return config;
    }

    /**
     * Get Website Configuration.
     * */
    public HashMap<String, String> getFeaturedConfig(){
        HashMap<String, String> config = new HashMap<>();
        // user name
        config.put(KEY_FEATURED_POST_COUNT, pref.getString(KEY_FEATURED_POST_COUNT, null));

        // user email id
        config.put(KEY_FEATURED_SLUG, pref.getString(KEY_FEATURED_SLUG, null));

        // return user
        return config;
    }
    public boolean getDynamicColors(){
        return pref.getBoolean(KEY_DYNAMIC_COLORS,true);
    }

    public int getColorType(){
        return Integer.parseInt(pref.getString(KEY_DYNAMIC_COLORS_TYPE,"1"));
    }
}