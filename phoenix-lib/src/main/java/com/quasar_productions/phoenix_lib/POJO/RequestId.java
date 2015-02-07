package com.quasar_productions.phoenix_lib.POJO;

import android.os.SystemClock;

import org.parceler.Parcel;

/**
 * Created by Prashant on 2/7/2015.
 */
@Parcel
public class RequestId {
    private long timeStamp;
    private String stringId="";
    private int intId=0;
    private boolean isInt=false;
    public static final String KEY_REQUEST_ID="key_request_id";

    public RequestId() {
        this.timeStamp=SystemClock.elapsedRealtime();
        stringId=getClass().toString();
    }
    public void generateId(String id){
        this.stringId = id;
        this.timeStamp=SystemClock.elapsedRealtime();
    }

    public void generateId(int id){
        isInt = true;
        this.intId = id;
        this.timeStamp=SystemClock.elapsedRealtime();
    }

    public Object getId() {
        if(isInt)
            return intId;
        else
            return stringId;
    }

    public String getReqId(){
       StringBuilder stringBuilder=new StringBuilder();
        if(isInt)
        stringBuilder.append(intId);
        else
        stringBuilder.append(stringId);
        stringBuilder.append(timeStamp);
        return stringBuilder.toString();
    }
}
