package com.quasar_productions.phoenix_lib.Utils;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.quasar_productions.phoenix_lib.POJO.ColorScheme;
import com.quasar_productions.phoenix_lib.R;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Prashant on 11/17/2014.
 */
public class Utils {
    public static void addToCache(Context ctx,String data, String tag) {
    //    try {
            FileOutputStream outputStream;
            try {
                outputStream = ctx.openFileOutput(tag, Context.MODE_PRIVATE);
                outputStream.write(data.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("INTERNAL STORAGE WRITE","ERROR: "+e.getMessage());
            }
           /* ctx.getFilesDir()
            if(!new File(Constant.fullPath).exists())
                new File(Constant.fullPath).mkdir();
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                    new File(Constant.fullPath, fileName)));
            out.writeUTF(data);
            out.flush();
            out.close();
            Log.d("saveToExternalStorage()", "saved");
        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
        }*/

    }

    public static String getCache(Context ctx,String tag) {
        /*ObjectInputStream in;
        JSONObject json = null;
        String respString;
        try {
            in = new ObjectInputStream(new FileInputStream(new File(
                    Constant.fullPath, fileName)));
            respString = in.readUTF();
            json = new JSONObject(respString);
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;*/
        //}
        FileInputStream fin;
        int c;
        String temp = "";
        try {
            fin = ctx.openFileInput(tag);
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
//string temp contains all the data of the file.
            fin.close();
            return temp;

        } catch (Exception e) {
            Log.d("INTERNAL STORAGE READ","ERROR: "+e.getMessage());
            return null;
        }
    }
/*

    public static int getDp(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
    }
*/

    public static Hashtable<String,Hashtable<String,Integer>> paletteToInt(Palette palette){
        Hashtable<String,Hashtable<String,Integer>> finalInts= new Hashtable<>();

        List<Palette.Swatch> swatches = palette.getSwatches();
        for (Palette.Swatch swatch:swatches){
          if(swatch.equals(palette.getDarkMutedSwatch())){
              finalInts.put(ColorScheme.DARK_MUTED,setValues(swatch));
            }else if(swatch.equals(palette.getDarkVibrantSwatch())){
              finalInts.put(ColorScheme.DARK_VIBRANT,setValues(swatch));
          }else if(swatch.equals(palette.getLightMutedSwatch())){
              finalInts.put(ColorScheme.LIGHT_MUTED,setValues(swatch));
          }else if(swatch.equals(palette.getLightVibrantSwatch())){
              finalInts.put(ColorScheme.LIGHT_VIBRANT,setValues(swatch));
          }else if(swatch.equals(palette.getMutedSwatch())){
              finalInts.put(ColorScheme.MUTED,setValues(swatch));
          }else if(swatch.equals(palette.getVibrantSwatch())) {
              finalInts.put(ColorScheme.VIBRANT, setValues(swatch));
          }
        }

        return finalInts;

    }

   private static Hashtable<String,Integer> setValues(Palette.Swatch swatch){
       Hashtable<String, Integer> ints = new Hashtable<>();
       if(swatch!=null) {
           ints.put(ColorScheme.POPULATION, swatch.getPopulation());
           ints.put(ColorScheme.BODYTEXTCOLOR, swatch.getBodyTextColor());
           ints.put(ColorScheme.RGB, swatch.getRgb());
           ints.put(ColorScheme.TITLETEXTCOLOR, swatch.getTitleTextColor());
       } else {
            ints.put(ColorScheme.POPULATION,-1);
           ints.put(ColorScheme.BODYTEXTCOLOR,-1);
           ints.put(ColorScheme.RGB,-1);
           ints.put(ColorScheme.TITLETEXTCOLOR,-1);
       }
       return ints;
   }

}
