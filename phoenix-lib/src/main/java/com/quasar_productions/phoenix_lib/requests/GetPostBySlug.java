package com.quasar_productions.phoenix_lib.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.manuelpeinado.fadingactionbar.view.ObservableWebViewWithHeader;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostSingle;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.Utils.Utils;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by prashant on 15/1/15.
 */
public class GetPostBySlug {
    JsonObjectRequest jsonObjectRequest;
    String postSlug;
    Context context;
    RequestId requestId;
    public GetPostBySlug(Context context,final String postSlug,RequestId requestId) {
        this.postSlug=postSlug;
        this.requestId=requestId;
        this.context=context;
        FetchResults(postSlug);
    }

    public void FetchResults(final String postSlug){

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://quasar-academy.com/mypreciousapi/get_post/?id&slug="+postSlug, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("VOLLEY", "Response: " + response.toString());
                if (response != null) {
                PostSingle event = parseJsonFeed(response);
                   EventBus.getDefault().post(event);
              }
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

    private PostSingle parseJsonFeed(JSONObject response) {
            Gson gson=new Gson();
            PostSingle postSingle= gson.fromJson(response.toString(), PostSingle.class);
            postSingle.setRequestId(requestId);
            return postSingle;
    }
}
