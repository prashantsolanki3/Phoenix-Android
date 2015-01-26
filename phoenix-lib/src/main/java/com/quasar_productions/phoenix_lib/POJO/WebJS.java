package com.quasar_productions.phoenix_lib.POJO;

import org.parceler.Parcel;

/**
 * Created by Prashant on 1/23/2015.
 */
@Parcel
public class WebJS {

    String javaScript;

    public WebJS() {
    }

    public WebJS(String javaScript) {
        this.javaScript = javaScript;
    }

    public String getJavaScript() {
        return javaScript;
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }
}
