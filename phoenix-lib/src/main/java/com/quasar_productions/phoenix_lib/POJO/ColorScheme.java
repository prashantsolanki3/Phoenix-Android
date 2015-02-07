package com.quasar_productions.phoenix_lib.POJO;

import android.support.v7.graphics.Palette;
import android.util.ArrayMap;

import com.quasar_productions.phoenix_lib.Utils.Utils;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Prashant on 2/7/2015.
 */
@Parcel
public class ColorScheme {
    public final static String DARK_MUTED="dm";
    public final static String DARK_VIBRANT="dv";
    public final static String MUTED="m";
    public final static String VIBRANT="v";
    public final static String LIGHT_MUTED="lm";
    public final static String LIGHT_VIBRANT="lv";
    public final static String RGB= "rgb";
    public final static String TITLETEXTCOLOR = "ttc";
    public final static String BODYTEXTCOLOR= "btc";
    public final static String POPULATION= "pop";
    public final static String KEY_COLOR_SCHEME="key_color_scheme";

    public static Hashtable<String,Hashtable<String,Integer>> hash;

    public ColorScheme() {}

    public ColorScheme(Palette palette) {
       hash=Utils.paletteToInt(palette);
    }

    public Hashtable<String ,Integer> getDarkMuted(){
       return hash.get(DARK_MUTED);
    }

    public Hashtable<String ,Integer> getDarkVibrant(){
        return hash.get(DARK_VIBRANT);
    }
    public Hashtable<String ,Integer> getMuted(){
        return hash.get(MUTED);
    }
    public Hashtable<String ,Integer> getVibrant(){
        return hash.get(VIBRANT);
    }
    public Hashtable<String ,Integer> getLightVibrant(){
        return hash.get(LIGHT_VIBRANT);
    }
    public Hashtable<String ,Integer> getLightMuted(){
        return hash.get(LIGHT_MUTED);
    }
}
