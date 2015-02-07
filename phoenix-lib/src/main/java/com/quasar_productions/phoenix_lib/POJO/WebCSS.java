package com.quasar_productions.phoenix_lib.POJO;

import org.parceler.Parcel;

/**
 * Created by Prashant on 1/23/2015.
 */
@Parcel
public class WebCSS {
    String webCSS;
    public static String KEY_WEBCSS = "key_webCSS";
    public WebCSS(String webCSS) {
        this.webCSS = webCSS;
    }

    public WebCSS() {
    }

    public String getWebCSS() {
        return webCSS;
    }

    public void setWebCSS(String webCSS) {
        this.webCSS = webCSS;
    }
}
