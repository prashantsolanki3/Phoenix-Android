package com.quasar_productions.phoenix.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.manuelpeinado.fadingactionbar.view.ObservableWebViewWithHeader;
import com.manuelpeinado.fadingactionbar.view.OnScrollChangedCallback;
import com.nispok.snackbar.Snackbar;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostSingle;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.Utils.Utils;
import com.quasar_productions.phoenix_lib.WebView.ChromeClientPhoenix;
import com.quasar_productions.phoenix_lib.WebView.WebViewClientPhoenix;
import com.quasar_productions.phoenix_lib.requests.GetPostBySlug;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import de.greenrobot.event.EventBus;

public class PostActivityParallax extends ActionBarActivity implements OnScrollChangedCallback {

    private Toolbar mToolbar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private int mLastDampedScroll;
    private int mInitialStatusBarColor;
    private int mFinalStatusBarColor;
    private SystemBarTintManager mStatusBarManager;
    private ObservableWebViewWithHeader WebViewWithHeader;
    private GetPostBySlug getPostBySlug;
    String post_slug;
    long timestamp_id;
    ProgressWheel progressWheel;
    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton fab_share;
    PostSingle postSingle;
    ImageView fademe;
    Drawable fademeDraw;
    public final static int FINGER_RELEASED = 0;
    public final static int FINGER_TOUCHED = 1;
    public final static int FINGER_DRAGGING = 2;
    public final static int FINGER_UNDEFINED = 3;

    private int fingerState = FINGER_RELEASED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            post_slug = intent.getData().getLastPathSegment();
            timestamp_id = SystemClock.elapsedRealtime();
        }else{
            timestamp_id =getIntent().getExtras().getLong("timestamp_id");
            post_slug = getIntent().getExtras().getString("post_slug");
        }
        fademe = (ImageView) findViewById(R.id.fade_me);
        fademe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(getApplicationContext()).text("Favourites will be Activated from the next update.").show(PostActivityParallax.this);
            }
        });
        fademeDraw=fademe.getBackground();
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab_multiple_actions);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);

        final Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody =   postSingle.getPost().getUrl();
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, postSingle.getPost().getTitle_plain()+shareBody);
                startActivity(Intent.createChooser(sharingIntent, Intent.ACTION_CHOOSER));
            }
        });
        progressWheel =(ProgressWheel) findViewById(R.id.progress_wheel);

        /*Imitialization*/

        setUpToolbar();
        setUpWebView();
        sendRequests();
        setUpFadingView();


    }
    void sendRequests(){

        getPostBySlug = new GetPostBySlug(getApplicationContext(),post_slug,timestamp_id);
      //  progressWheel.spin();

    }
    public void onEvent(PostSingle event){

            postSingle =event;

            Picasso.with(getApplicationContext())
                    .load(event.getPost().getThumbnail_images().getMedium().getUrl() == null ? "error" : event.getPost().getThumbnail_images().getMedium().getUrl())
                    .into((ImageView) mHeader, new Callback() {
                        @Override
                        public void onSuccess() {
                            Resources r = getResources();
                            float paddingBottom = (float) mHeader.getWidth() * ((float) ((ImageView) mHeader).getDrawable().getIntrinsicHeight() / (float) ((ImageView) mHeader).getDrawable().getIntrinsicWidth());
                            float padding_final = (float) ((ImageView) mHeader).getDrawable().getIntrinsicHeight() * paddingBottom / (float) r.getDisplayMetrics().densityDpi;

                            Log.d("Padding", " " + padding_final + "DPI" + r.getDisplayMetrics().densityDpi);
                            Log.d("", "IMGVIEW W: " + mHeader.getWidth() + " D H: " + ((ImageView) mHeader).getDrawable().getIntrinsicHeight() + " D W: " + ((ImageView) mHeader).getDrawable().getIntrinsicWidth() + " Calcu: " + paddingBottom);
                            String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + "</head><body><div id=\"content_para\">"
                                    + postSingle.getPost().getContent() + Utils.getCache(getApplicationContext(), WebJS.class.getName())+ "<style>\n" +
                                    "  #content_para { padding: 8px 8px " + ((int) paddingBottom / 2) + "px 8px;}\n" +
                                    "</style>" + "</div></body></html>";
                            WebViewWithHeader.loadData(string, "text/html", "UTF-8");
                            Bitmap bitmap = ((BitmapDrawable) ((ImageView) mHeader).getDrawable()).getBitmap();
                            Palette palette = Palette.generate(bitmap);
                            Palette.Swatch swatch = palette.getVibrantSwatch();
//                            Palette.Swatch swatch1 = palette.getLightVibrantSwatch();
                            if (swatch != null) {
                                mToolbar.setTitleTextColor(swatch.getTitleTextColor());
                                mToolbar.setSubtitleTextColor(swatch.getBodyTextColor());
                                mToolbar.setBackgroundColor(swatch.getRgb());
                                mActionBarBackgroundDrawable.setAlpha(0);
                            }
                        }

                        @Override
                        public void onError() {
                            String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + "</head><body><div id=\"content_para\">"
                                    + postSingle.getPost().getContent() + Utils.getCache(getApplicationContext(), WebJS.class.getName())+ "<style>\n" +
                                    "  #content_para { padding: 8px 8px px 8px;}\n" +
                                    "</style>" +"</div></body></html>";
                            WebViewWithHeader.loadData(string, "text/html", "UTF-8");
                        }
                    });

    }


    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().registerSticky(this);
    }
    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().unregister(this);
        AppController.getInstance().getRequestQueue().cancelAll(post_slug+timestamp_id);
        super.onStop();
    }

    void setUpFadingView(){
        mStatusBarManager = new SystemBarTintManager(this);
        mStatusBarManager.setStatusBarTintEnabled(true);
        mInitialStatusBarColor = Color.BLACK;
        mFinalStatusBarColor = getResources().getColor(R.color.nliveo_blue_colorPrimaryDark);

        mHeader = findViewById(R.id.header);

        //  ObservableScrollable scrollView = (ObservableScrollable) findViewById(R.id.scrollview);
        // scrollView.setOnScrollChangedCallback(this);

        onScroll(-1, 0);}
    void setUpWebView(){
        WebViewWithHeader =(ObservableWebViewWithHeader) findViewById(R.id.webview_title);
        YoYo.with(Techniques.SlideInDown).interpolate(new AccelerateDecelerateInterpolator()).playOn(WebViewWithHeader);
        WebViewWithHeader.setOnScrollChangedCallback(this);
        WebViewWithHeader.getSettings().setJavaScriptEnabled(true);
        WebViewWithHeader.setWebChromeClient(new ChromeClientPhoenix());
        WebViewWithHeader.setWebViewClient(new WebViewClientPhoenix(progressWheel));

        WebViewWithHeader.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (fingerState == FINGER_RELEASED) fingerState = FINGER_TOUCHED;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_UP:
                        if(fingerState != FINGER_DRAGGING) {
                            fingerState = FINGER_RELEASED;
                            ObservableWebViewWithHeader.HitTestResult hitTestResult = ((ObservableWebViewWithHeader)v).getHitTestResult();
                            Log.d("Hit Res",""+hitTestResult.getType()+" "+hitTestResult.getExtra());
                            // Your onClick codes
                            onWebClickAction(hitTestResult.getExtra(),hitTestResult.getType());

                        }
                        else if (fingerState == FINGER_DRAGGING) fingerState = FINGER_RELEASED;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) fingerState = FINGER_DRAGGING;
                        else fingerState = FINGER_UNDEFINED;
                        break;

                    default:
                        fingerState = FINGER_UNDEFINED;

                }

                return false;
            }


        });

    }
    void setUpToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        mActionBarBackgroundDrawable = mToolbar.getBackground();
        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });
    }
    void onWebClickAction(String extras,int type){

        switch (type){
            case ObservableWebViewWithHeader.HitTestResult.EDIT_TEXT_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.EMAIL_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.GEO_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.IMAGE_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.PHONE_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.SRC_ANCHOR_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                break;
            case ObservableWebViewWithHeader.HitTestResult.UNKNOWN_TYPE:
                break;
            default:
                break;
        }
        Toast.makeText(getApplicationContext(),"Type: "+ type + " Extra: "+extras ,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return true;
    }

    @Override
    public void onScroll(int l, int scrollPosition) {
        int headerHeight = mHeader.getHeight() - mToolbar.getHeight();
        float ratio = 0;
        if (scrollPosition > 0 && headerHeight > 0)
            ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;

        updateActionBarTransparency(ratio);
        updateStatusBarColor(ratio);
        updateParallaxEffect(scrollPosition);
    }

    private void updateActionBarTransparency(float scrollRatio) {
        int newAlpha = (int) (scrollRatio * 255);
        mActionBarBackgroundDrawable.setAlpha(newAlpha);
        //Log.d("New Alpha", "new alpha: " + newAlpha + " scrollratio: " + scrollRatio);
        if(scrollRatio<1f) {
            //fademe.setVisibility(View.VISIBLE);
            mToolbar.setTitle("");
        }else {
            //fademe.setVisibility(View.GONE);
            mToolbar.setTitle(postSingle.getPost().getTitle_plain());
        }
        if(scrollRatio>0f)
            floatingActionsMenu.setVisibility(View.VISIBLE);
        else if(scrollRatio==0f)
            floatingActionsMenu.setVisibility(View.GONE);

        /*if(fademe.getVisibility()==View.VISIBLE)
            fademeDraw.setAlpha(255-newAlpha);*/
        floatingActionsMenu.setAlpha(scrollRatio);
       // mToolbar.setBackground(mActionBarBackgroundDrawable);
    }

    private void updateStatusBarColor(float scrollRatio) {
        int r = interpolate(Color.red(mInitialStatusBarColor), Color.red(mFinalStatusBarColor), 1 - scrollRatio);
        int g = interpolate(Color.green(mInitialStatusBarColor), Color.green(mFinalStatusBarColor), 1 - scrollRatio);
        int b = interpolate(Color.blue(mInitialStatusBarColor), Color.blue(mFinalStatusBarColor), 1 - scrollRatio);
        mStatusBarManager.setTintColor(Color.rgb(r, g, b));
    }

    private void updateParallaxEffect(int scrollPosition) {
        float damping = 0.5f;
        int dampedScroll = (int) (scrollPosition * damping);
        int offset = mLastDampedScroll - dampedScroll;
        mHeader.offsetTopAndBottom(-offset);

        mLastDampedScroll = dampedScroll;
    }

    private int interpolate(int from, int to, float param) {
        return (int) (from * param + to * (1 - param));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}