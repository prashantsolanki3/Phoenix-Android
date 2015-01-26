package com.quasar_productions.phoenix.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Categories;
import com.quasar_productions.phoenix_lib.Utils.Utils;
import com.quasar_productions.phoenix_lib.Wave.WaveView;
import com.quasar_productions.phoenix_lib.requests.GetCategories;
import com.quasar_productions.phoenix_lib.requests.GetWebsiteResources;

import org.parceler.Parcels;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class Start extends ActionBarActivity {

    boolean category_recieved=false;
    boolean webJS_recieved=false;
    boolean webCSS_recieved=false;
    ArrayList<Categories> categories;
    WaveView waveView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        waveView = (WaveView) findViewById(R.id.wave_view);
        TextView textView =(TextView) findViewById(R.id.tv_start);
        final Intent i= new Intent(this,PostActivity.class);;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });
        textView.setText(getString(R.string.app_name));
        GetCategories getCategories = new GetCategories();
        waveView.setProgress(85);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    // This method will be called when a MessageEvent is posted
    public void onEvent(ArrayList<Categories> event){
       // Toast.makeText(this, event.get(0).getTitle(), Toast.LENGTH_SHORT).show();
        category_recieved=true;
        this.categories=event;
        Log.d("Categories","true");
        startNextActivity();
        GetWebsiteResources getWebsiteResources = new GetWebsiteResources(getString(R.string.website_url));
    }

    void startNextActivity(){
        if(category_recieved&&webCSS_recieved&&webJS_recieved) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string._categories_intent), Parcels.wrap(categories));
            //Intent i = new Intent(this, PostActivity.class);
            Intent i = new Intent(this, MainActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        }
    }
    public void onEventAsync(WebJS webJS){
        webJS_recieved=true;
        Utils.addToCache(getApplicationContext(),webJS.getJavaScript(),WebJS.class.getName());
        Log.d("WebJS","true");
        startNextActivity();
    }

    public void onEventAsync(WebCSS webCSS){
        webCSS_recieved=true;
        Utils.addToCache(getApplicationContext(),webCSS.getWebCSS(),WebCSS.class.getName());
        Log.d("WebCSS","true");
        startNextActivity();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
