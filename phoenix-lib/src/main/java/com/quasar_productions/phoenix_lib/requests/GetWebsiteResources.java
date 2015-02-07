package com.quasar_productions.phoenix_lib.requests;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.greenrobot.event.EventBus;

/**
 * Created by Prashant on 1/23/2015.
 */
public class GetWebsiteResources {
        StringRequest resourceReq;
        String websiteURL;
        RequestId requestId;
    public GetWebsiteResources(String websiteURL,RequestId requestId) {
        this.websiteURL=websiteURL;
        this.requestId=requestId;
        AddReq();
        }
    private void AddReq(){
        resourceReq = new StringRequest(
                websiteURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //       VolleyLog.d("VOLLEY", "Response: " + response.toString());
                Document doc = Jsoup.parse(response);
                EventBus.getDefault().post(extractJS(doc));
                EventBus.getDefault().post(extractCSS(doc));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
                EventBus.getDefault().post(error);
            }
        });
        AppController.getInstance().addToRequestQueue(resourceReq,requestId);
    }

   private WebJS extractJS(Document doc) {
        Elements js = doc.select("script");
        StringBuilder stringBuilder=new StringBuilder();
        for(Element hd:js)
            stringBuilder.append(" ").append(hd.toString());

        return new WebJS(stringBuilder.toString());
   }
    private WebCSS extractCSS(Document doc) {
        Elements links = doc.select("link");
        StringBuilder stringBuilder=new StringBuilder();
        for(Element hd:links)
            stringBuilder.append(" ").append(hd.toString());

        return new WebCSS(stringBuilder.toString());
   }
}
