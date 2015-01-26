package com.quasar_productions.phoenix_lib.WebView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class WebViewPhoenix extends WebView
{
    private Callbacks mCallbacks;;
 
    public WebViewPhoenix(final Context context)
    { 
        super(context);
    } 
 
    public WebViewPhoenix(final Context context, final AttributeSet attrs)
    { 
        super(context, attrs);
    } 
 
    public WebViewPhoenix(final Context context, final AttributeSet attrs, final int defStyle)
    { 
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mCallbacks.onDownMotionEvent();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mCallbacks.onUpOrCancelMotionEvent();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }
    @Override 
    protected void onScrollChanged(final int x, final int y, final int oldx, final int oldy)
    { 
        super.onScrollChanged(x,y,oldx,oldy);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(y);
        }
    } 

 
    /** 
     * Impliment in the activity/fragment/view that you want to listen to the webview 
     */
    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(Callbacks listener) {
        mCallbacks = listener;
    }

    public static interface Callbacks {
        public void onScrollChanged(int scrollY);
        public void onDownMotionEvent();
        public void onUpOrCancelMotionEvent();
    }
} 