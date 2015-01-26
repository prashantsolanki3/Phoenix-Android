package com.quasar_productions.phoenix_lib.requests;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;
import com.quasar_productions.phoenix_lib.Wave.WaveView;

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
        WaveView progressWheel;
        int prog=55;
    public GetWebsiteResources(WaveView progressWheel, String websiteURL) {
        this.progressWheel = progressWheel;
        this.websiteURL = websiteURL;
        AddReq();
    }

    public GetWebsiteResources(String websiteURL) {
        this.websiteURL=websiteURL;
        AddReq();
        }
    private void AddReq(){
        if ((progressWheel!=null))
        progressWheel.setProgress(prog);
        resourceReq = new StringRequest(
                websiteURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //       VolleyLog.d("VOLLEY", "Response: " + response.toString());
                Document doc = Jsoup.parse(response);
                EventBus.getDefault().post(extractJS(doc));
                prog=80;
                if ((progressWheel!=null))
                    progressWheel.setProgress(prog);
                EventBus.getDefault().post(extractCSS(doc));
                prog=100;
                if ((progressWheel!=null))
                    progressWheel.setProgress(prog);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(resourceReq);
        prog=65;
        if ((progressWheel!=null))
            progressWheel.setProgress(prog);
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
