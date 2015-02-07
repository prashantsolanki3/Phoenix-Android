package com.quasar_productions.phoenix_lib.Utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Prashant on 2/7/2015.
 */
public class WebConfigBuilder {
    private Context context;
    private PrefManager prefManager;
    private String endpoint;
    private String categoryURL = "get_category_index";

    public WebConfigBuilder(Context context) {
        this.context = context;
        prefManager = new PrefManager(this.context);
        HashMap<String,String> configURL = prefManager.getWebsiteConfig();
        endpoint = configURL.get(PrefManager.KEY_WEBSITE_URL).concat("/").concat(configURL.get(PrefManager.KEY_API_ENDPOINT)).concat("/");
    }

    public static WebConfigBuilder init(Context context){
        WebConfigBuilder webConfigBuilder= new WebConfigBuilder(context);
        return webConfigBuilder;
    }

    public String getCategoriesURL(int parent) {
        HashMap<String,String> configURL = prefManager.getWebsiteConfig();
        if(configURL!=null)
        if (configURL.get(PrefManager.KEY_WEBSITE_URL)==null || configURL.get(PrefManager.KEY_API_ENDPOINT)==null) {
            Log.d(getClass().getName(),"Null Strings");
            return "error";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(endpoint);
            stringBuilder.append(buildCategoryIndexURL(parent));
            stringBuilder.append("/");

            return stringBuilder.toString();
        }
        else
            return "error";
    }
    private String buildCategoryIndexURL(int parent){
        if(parent!=0)
            return  "get_category_index/?parent="+parent;
        else
            return "get_category_index/?parent";
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getFeturedPostURL(int page) {
        HashMap<String,String> configURL = prefManager.getFeaturedConfig();
        if(configURL!=null)
            if (configURL.get(PrefManager.KEY_FEATURED_POST_COUNT)==null || configURL.get(PrefManager.KEY_FEATURED_SLUG)==null) {
                Log.d(getClass().getName(),"Null Strings");
                return "error";
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getEndpoint());
                stringBuilder.append(buildPostByTagsURL(0,configURL.get(PrefManager.KEY_FEATURED_SLUG),configURL.get(PrefManager.KEY_FEATURED_POST_COUNT),page,null));
                return stringBuilder.toString();
            }
        else
            return "error";
    }

  private String buildPostByTagsURL(int id,String featuredTagSlug,String postCount,int page,String post_type){

    if(id!=0)
        if(post_type!=null)
            return  "get_tag_posts/?id="+id+"&slug&count="+postCount+"&page="+page+"&post_type="+post_type;
        else
            return  "get_tag_posts/?id="+id+"&slug&count="+postCount+"&page="+page+"&post_type";
    else
        if(post_type!=null)
            return  "get_tag_posts/?id&slug="+featuredTagSlug+"&count="+postCount+"&page="+page+"&post_type="+post_type;
        else
            return  "get_tag_posts/?id&slug="+featuredTagSlug+"&count="+postCount+"&page="+page+"&post_type";

  }
    public String getPostByAuthorURL(int authorId,String slug,String postCount,int page,String post_type) {
        HashMap<String,String> configURL = prefManager.getFeaturedConfig();
        if(configURL!=null)
            if (configURL.get(PrefManager.KEY_FEATURED_POST_COUNT)==null || configURL.get(PrefManager.KEY_FEATURED_SLUG)==null) {
                Log.d(getClass().getName(),"Null Strings");
                return "error";
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getEndpoint());
                stringBuilder.append(buildPostBYAuthorURL(authorId,slug,configURL.get(PrefManager.KEY_FEATURED_POST_COUNT), page,post_type));
                return stringBuilder.toString();
            }
        else
            return "error";
    }

    private String buildPostBYAuthorURL(int authorId,String slug,String postCount,int page,String post_type){
        if(authorId!=0)
        return "get_tag_posts/?id="+authorId+"&slug&count="+postCount+"&page="+page+"&post_type="+post_type;
        else
            return "get_tag_posts/?id&slug="+slug+"&count="+postCount+"&page="+page+"&post_type="+post_type;
    }

}
