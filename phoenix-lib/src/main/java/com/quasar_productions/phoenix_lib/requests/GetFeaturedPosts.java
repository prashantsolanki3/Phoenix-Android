package com.quasar_productions.phoenix_lib.requests;

import android.content.Context;
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
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.Utils.WebConfigBuilder;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by prashant on 15/1/15.
 */
public class GetFeaturedPosts {
    JsonObjectRequest jsonObjectRequest;
    Context context;
    RequestId requestId;

    public GetFeaturedPosts(Context context,RequestId requestId) {
        this.context=context;
        this.requestId=requestId;
        FetchResults(1);
    }
    public GetFeaturedPosts(Context context,RequestId requestId,int page) {
        this.context=context;
        this.requestId=requestId;
        FetchResults(page);
    }

    public void FetchResults(int page){
        WebConfigBuilder webConfigBuilder = new WebConfigBuilder(context);
        Log.d(getClass().getName(),webConfigBuilder.getFeturedPostURL(page));
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, webConfigBuilder.getFeturedPostURL(page)
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                EventBus.getDefault().post(parseJsonFeed(response));
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

            Log.d("Complete response: ", response.toString());
            Gson gson= new Gson();
            PostsResult postsResult = gson.fromJson(response.toString(), PostsResult.class);
            postsResult.setRequestId(requestId);
            return postsResult;
    }
}
