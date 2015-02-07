package com.quasar_productions.phoenix_lib.requests;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Author;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by prashant on 15/1/15.
 */
public class GetRecentPosts {
    JsonObjectRequest jsonObjectRequest;
    RequestId requestId;
    int page;

    public GetRecentPosts(RequestId requestId) {
        this.requestId=requestId;
        this.page=1;
        FetchResults(1);
    }
    public GetRecentPosts(int page,RequestId requestId) {
        this.page=page;
        this.requestId=requestId;
        FetchResults(page);
    }

    public void FetchResults(int Page){

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://quasar-academy.com/mypreciousapi/get_recent_posts/?count=5&page="+Page+"&post_type", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                //if (response != null) {
                   EventBus.getDefault().post(parseJsonFeed(response));
            //    }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
                EventBus.getDefault().post(error);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,requestId);
    }

    private PostsResult parseJsonFeed(JSONObject response) {
        Gson gson= new Gson();
        PostsResult postsResult = gson.fromJson(response.toString(),PostsResult.class);
        postsResult.setRequestId(requestId);
        return postsResult;

    }
}
