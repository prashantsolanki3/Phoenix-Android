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
public class GetPostsByCategory {
    JsonObjectRequest jsonObjectRequest;
    int categoryId;
    long timestamp_id;
    RequestId requestId;
    public GetPostsByCategory(final int categoryId,RequestId requestId) {
        this.categoryId=categoryId;
        this.requestId =requestId;
        FetchResults(categoryId,1);
    }
    public GetPostsByCategory(final int categoryId,int page,RequestId requestId) {
        this.categoryId=categoryId;
        this.requestId=requestId;
        FetchResults(categoryId,page);
    }

    public void FetchResults(final int categoryId, int Page){

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://quasar-academy.com/mypreciousapi/get_category_posts/?id="+categoryId+"&slug&count=5&page="+Page+"&post_type", null, new Response.Listener<JSONObject>() {

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
        Gson gson=new Gson();
        PostsResult postsResult=gson.fromJson(response.toString(),PostsResult.class);
        postsResult.setRequestId(requestId);
        return postsResult;
    }

}
