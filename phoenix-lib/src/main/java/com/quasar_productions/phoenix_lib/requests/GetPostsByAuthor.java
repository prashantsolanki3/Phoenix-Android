package com.quasar_productions.phoenix_lib.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.Utils.WebConfigBuilder;

import org.json.JSONObject;
import org.parceler.transfuse.annotations.SystemService;

import de.greenrobot.event.EventBus;

/**
 * Created by prashant on 15/1/15.
 */
public class GetPostsByAuthor {
    JsonObjectRequest jsonObjectRequest;
    int authorId;
    RequestId requestId;
    Context context;
    public GetPostsByAuthor(Context context,final int authorId, RequestId requestId) {
        this.authorId=authorId;
        this.requestId=requestId;
        this.context=context;
        FetchResults(authorId,1);
    }
    public GetPostsByAuthor(Context context,final int authorId, int page, RequestId requestId) {
        this.authorId=authorId;
        this.requestId=requestId;
        this.context=context;
        FetchResults(authorId,page);
    }

    public void FetchResults(final int authorId, int Page){

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                WebConfigBuilder.init(context).getPostByAuthorURL(authorId,null,"5",1,null), null, new Response.Listener<JSONObject>() {

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
