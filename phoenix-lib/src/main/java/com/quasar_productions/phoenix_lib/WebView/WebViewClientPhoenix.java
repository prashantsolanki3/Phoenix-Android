package com.quasar_productions.phoenix_lib.WebView;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by Prashant on 1/24/2015.
 */
public class WebViewClientPhoenix extends WebViewClient{
    ProgressWheel progressWheel;

    public WebViewClientPhoenix(ProgressWheel progressWheel) {
        this.progressWheel = progressWheel;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(progressWheel!=null)
            progressWheel.stopSpinning();
        if(!view.isEnabled())
        view.setEnabled(true);

    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
