package com.quasar_productions.phoenix_lib.requests;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Categories;
import com.quasar_productions.phoenix_lib.Wave.WaveView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Prashant on 1/11/2015.
 */
public class GetCategories {
    JsonObjectRequest jsonObjectRequest;
    WaveView progressWheel;
    int prog=15;
    public GetCategories() {
        AddReq();
    }

    public GetCategories(WaveView progressWheel) {
        this.progressWheel = progressWheel;
        AddReq();
    }

    private void AddReq(){
        if(progressWheel!=null)
        progressWheel.setProgress(prog);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://quasar-academy.com/mypreciousapi/get_category_index/", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                if (response != null) {
                    EventBus.getDefault().post(parseJsonFeed(response));
                    if(progressWheel!=null)progressWheel.setProgress(50);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private ArrayList<Categories> parseJsonFeed(JSONObject response) {
        try {

            JSONArray feedArray = response.getJSONArray("categories");
            ArrayList<Categories> array = new ArrayList<>();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                Categories item = new Categories();
                item.setId(feedObj.getInt("id"));
                item.setTitle(feedObj.getString("title"));
                item.setDescription(feedObj.getString("description"));
                item.setPost_count(feedObj.getInt("post_count"));
                item.setSlug(feedObj.getString("slug"));
                array.add(item);
                Log.d("LOAD DATA", item.getId() + " " + item.getTitle() + " " + item.getDescription());
                prog=prog+(i*2);
                if(progressWheel!=null)progressWheel.setProgress(prog);
            }
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
        return null;
        }
    }

}
