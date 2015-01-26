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
    long timestamp_id;
    int page;
    String get_recent_post_id="recent";

    public GetRecentPosts(long timestamp_id) {
        this.timestamp_id=timestamp_id;
        this.page=1;
        FetchResults(1);
    }
    public GetRecentPosts(int page,long timestamp_id) {
        this.page=page;
        this.timestamp_id=timestamp_id;
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
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,get_recent_post_id.concat(String.valueOf(timestamp_id)));
    }

    private PostsResult parseJsonFeed(JSONObject response) {
       /* try {
            Log.d("Complete response: ",response.toString());
            if(response.getInt("count")!=0) {
                JSONArray feedArray = response.getJSONArray("posts");
                ArrayList<Post> array = new ArrayList<>();
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    //Setting Post Fields
                    Post item = new Post("recent");
                    item.setId(feedObj.getInt("id"));
                    item.setTitle(feedObj.optString("title"));
                    item.setType(feedObj.optString("type"));
                    item.setSlug(feedObj.optString("slug"));
                    item.setThumbnail(feedObj.optString("thumbnail"));

                    //Setting Author
                    Author author = new Author();
                    JSONObject authorObj = (JSONObject) feedObj.get("author");
                    author.setName(authorObj.optString("name"));
                    author.setId(authorObj.getInt("id"));
                    item.setAuthor(author);

                    //Adding Post to Post Array
                    array.add(item);
                    Log.d("LOAD DATA", "POST ID: " + item.getId() + "POST TITLE: " + item.getTitle());

                }
                return array;
            }else{
                ArrayList<Post> arrayList = new ArrayList<>();
                Post item =new Post();
                item.setId(0);
                arrayList.add(item);
                return arrayList;
            }
            } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }*/
        Gson gson= new Gson();
        PostsResult postsResult = gson.fromJson(response.toString(),PostsResult.class);
        postsResult.setFragment_name(get_recent_post_id.concat(String.valueOf(timestamp_id)));
        return postsResult;

    }
}
