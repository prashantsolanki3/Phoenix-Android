package com.quasar_productions.phoenix.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nispok.snackbar.Snackbar;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.adapters.MyPagerAdapter;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostSingle;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.Utils.Utils;
import com.quasar_productions.phoenix_lib.WebView.ChromeClientPhoenix;
import com.quasar_productions.phoenix_lib.WebView.WebViewClientPhoenix;
import com.quasar_productions.phoenix_lib.requests.GetPostBySlug;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import de.greenrobot.event.EventBus;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class PostActivityPlain extends ActionBarActivity implements MaterialTabListener {

    private Toolbar mToolbar;
   // private ImageView mHeader;
    private GetPostBySlug getPostBySlug;
    String post_slug;
    long timestamp_id;
    ProgressWheel progressWheel;
    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton fab_share;
    PostSingle postSingle;
    ImageView fademe;
    WebView webView;

    public final static int FINGER_RELEASED = 0;
    public final static int FINGER_TOUCHED = 1;
    public final static int FINGER_DRAGGING = 2;
    public final static int FINGER_UNDEFINED = 3;

    private int fingerState = FINGER_RELEASED;


    private SlidingUpPanelLayout Slide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_activity_plain);
    //    mHeader =(ImageView) findViewById(R.id.header);
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            post_slug = intent.getData().getLastPathSegment();
            timestamp_id = SystemClock.elapsedRealtime();
        }else{
            timestamp_id =getIntent().getExtras().getLong("timestamp_id");
            post_slug = getIntent().getExtras().getString("post_slug");
        }
        fademe = (ImageView)findViewById(R.id.fade_me);
        fademe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.with(getApplicationContext()).text("Favourites will be Activated from the next update.").show(PostActivityPlain.this);
            }
        });
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
        setUpSlider();

    }
    @Override
    public void onTabSelected(MaterialTab tab) {
        // when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
        if(!Slide.isPanelExpanded())
            Slide.expandPanel();
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
        if(!Slide.isPanelExpanded())
            Slide.expandPanel();
    }

    ViewPager pager;
    MaterialTabHost tabHost;
    void setUpSlider(){

        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);

        pager = (ViewPager) findViewById(R.id.vpPager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
                if(!Slide.isPanelExpanded())
                    Slide.expandPanel();
            }
        });


        tabHost.addTab(tabHost.newTab().setText("Comments").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("Related").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("Attachments").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("Etc.").setTabListener(this));

        Slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        Slide.setPanelHeight(96);

        Slide.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("TAG", "onPanelSlide, offset " + slideOffset);
            }


            @Override
            public void onPanelExpanded(View panel) {
                Log.i("TAG", "onPanelExpanded");


            }


            @Override
            public void onPanelCollapsed(View panel) {
                Log.i("TAG", "onPanelCollapsed");


            }


            @Override
            public void onPanelAnchored(View panel) {
                Log.i("TAG", "onPanelAnchored");
            }


            @Override
            public void onPanelHidden(View panel) {
                Log.i("TAG", "onPanelHidden");
            }
        });
        Slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    void sendRequests(){

        getPostBySlug = new GetPostBySlug(getApplicationContext(),post_slug,timestamp_id);
        //  progressWheel.spin();

    }
    public void onEvent(PostSingle event) {

        postSingle = event;
        mToolbar.setTitle(postSingle.getPost().getTitle_plain());

/*
        Picasso.with(getApplicationContext())
                .load(event.getPost().getThumbnail_images().getMedium().getUrl() == null ? "error" : event.getPost().getThumbnail_images().getMedium().getUrl())
                .into(mHeader, new Callback() {
                    @Override
                    public void onSuccess() {
                        Resources r = getResources();
                        float paddingBottom = (float) mHeader.getWidth() * ((float) (mHeader).getDrawable().getIntrinsicHeight() / (float) (mHeader).getDrawable().getIntrinsicWidth());
                        float padding_final = (float) (mHeader).getDrawable().getIntrinsicHeight() * paddingBottom / (float) r.getDisplayMetrics().densityDpi;



                        Log.d("Padding", " " + padding_final + "DPI" + r.getDisplayMetrics().densityDpi);
                        Log.d("", "IMGVIEW W: " + mHeader.getWidth() + " D H: " + (mHeader).getDrawable().getIntrinsicHeight() + " D W: " + (mHeader).getDrawable().getIntrinsicWidth() + " Calcu: " + paddingBottom);
                        String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + "</head><body>"+s+"<div id=\"content_para\">"
                                + postSingle.getPost().getContent() + Utils.getCache(getApplicationContext(), WebJS.class.getName())+ "<style>\n" +
                                "  #content_para { padding: 8px 8px " + ((int) paddingBottom / 2) + "px 8px;}\n" +
                                "</style>" + "</div></body></html>";
                        webView.loadData(string, "text/html", "UTF-8");
                        Bitmap bitmap = ((BitmapDrawable) (mHeader).getDrawable()).getBitmap();
                        Palette palette = Palette.generate(bitmap);
                        Palette.Swatch swatch = palette.getVibrantSwatch();
//                            Palette.Swatch swatch1 = palette.getLightVibrantSwatch();
                        if (swatch != null) {
                            mToolbar.setTitleTextColor(swatch.getTitleTextColor());
                            mToolbar.setSubtitleTextColor(swatch.getBodyTextColor());
                            mToolbar.setBackgroundColor(swatch.getRgb());
                        }
                    }

                    @Override
                    public void onError() {
                        String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + "</head><body><div id=\"content_para\">"
                                + postSingle.getPost().getContent() + Utils.getCache(getApplicationContext(), WebJS.class.getName())+ "<style>\n" +
                                "  #content_para { padding: 8px 8px px 8px;}\n" +
                                "</style>" +"</div></body></html>";
                        webView.loadData(string, "text/html", "UTF-8");
                    }
                });*/

        Picasso.with(getApplicationContext()).load(postSingle.getPost().getThumbnail()).resize(20,20).centerInside().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                Palette palette = Palette.generate(bitmap);
                Palette.Swatch swatch = palette.getVibrantSwatch();
//                            Palette.Swatch swatch1 = palette.getLightVibrantSwatch();
                if (swatch != null) {
                    mToolbar.setTitleTextColor(swatch.getTitleTextColor());
                    mToolbar.setSubtitleTextColor(swatch.getBodyTextColor());
                    mToolbar.setBackgroundColor(swatch.getRgb());
                    tabHost.setPrimaryColor(swatch.getRgb());
                    tabHost.setAccentColor(swatch.getTitleTextColor());
                }


                String s = "<img src=\"" + postSingle.getPost().getThumbnail() + "\" alt=\"Smiley face\" height=\"" + height / 3 + "\" width=\"" + width + "\">";
                String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + "</head><body>" + s + "<div id=\"content_para\">"
                        + postSingle.getPost().getContent() + Utils.getCache(getApplicationContext(), WebJS.class.getName()) + "<style>\n" +
                        "  #content_para { padding: " + mToolbar.getHeight() / 2 + "px 8px px 8px;}\n" +
                        "</style>" + "</div></body></html>";
                webView.loadData(string, "text/html", "UTF-8");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

                String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + "</head><body><div id=\"content_para\">"
                        + postSingle.getPost().getContent() + Utils.getCache(getApplicationContext(), WebJS.class.getName()) + "<style>\n" +
                        "  #content_para { padding: " + mToolbar.getHeight() / 2 + "px 8px px 8px;}\n" +
                        "</style>" + "</div></body></html>";
                webView.loadData(string, "text/html", "UTF-8");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

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

    void setUpWebView(){
        webView =(WebView) findViewById(R.id.webview_title);
        YoYo.with(Techniques.SlideInDown).interpolate(new AccelerateDecelerateInterpolator()).playOn(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new ChromeClientPhoenix());
        webView.setWebViewClient(new WebViewClientPhoenix(progressWheel));


        webView.setOnTouchListener(new View.OnTouchListener() {

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
                            WebView.HitTestResult hitTestResult = ((WebView)v).getHitTestResult();
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
            case WebView.HitTestResult.EDIT_TEXT_TYPE:
                break;
            case WebView.HitTestResult.EMAIL_TYPE:
                break;
            case WebView.HitTestResult.GEO_TYPE:
                break;
            case WebView.HitTestResult.IMAGE_TYPE:
                break;
            case WebView.HitTestResult.PHONE_TYPE:
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                break;
            case WebView.HitTestResult.UNKNOWN_TYPE:
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
    public void onBackPressed() {
       if(Slide.isPanelExpanded())
           Slide.collapsePanel();
        else
            super.onBackPressed();
    }
}