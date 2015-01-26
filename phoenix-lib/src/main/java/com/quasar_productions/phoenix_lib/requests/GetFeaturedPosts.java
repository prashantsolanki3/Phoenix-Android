package com.quasar_productions.phoenix_lib.requests;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by prashant on 15/1/15.
 */
public class GetFeaturedPosts {
    JsonObjectRequest jsonObjectRequest;
    String featuredSlug;
    long timestamp_id;

    public GetFeaturedPosts(final String featuredSlug, long timestamp_id) {
        this.featuredSlug=featuredSlug;
        this.timestamp_id=timestamp_id;
        FetchResults(featuredSlug,1);
    }
    public GetFeaturedPosts(final String featuredSlug, int page, long timestamp_id) {
        this.featuredSlug=featuredSlug;
        this.timestamp_id=timestamp_id;
        FetchResults(featuredSlug,page);
    }

    public void FetchResults(final String featuredSlug, int Page){

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://quasar-academy.com/mypreciousapi/get_tag_posts/?id&slug="+featuredSlug+"&count=5&page="+Page+"&post_type", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                EventBus.getDefault().post(parseJsonFeed(response));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,""+featuredSlug+timestamp_id);
    }

    private PostsResult parseJsonFeed(JSONObject response) {

            Log.d("Complete response: ", response.toString());
            Gson gson= new Gson();
            PostsResult postsResult = gson.fromJson(response.toString(), PostsResult.class);
            postsResult.setFragment_name(featuredSlug+timestamp_id);
            return postsResult;
    }
}
