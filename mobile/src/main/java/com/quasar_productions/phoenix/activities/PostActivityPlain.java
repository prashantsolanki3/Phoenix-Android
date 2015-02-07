package com.quasar_productions.phoenix.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.adapters.MyPagerAdapter;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.ColorScheme;
import com.quasar_productions.phoenix_lib.POJO.PostSingle;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.WebCSS;
import com.quasar_productions.phoenix_lib.POJO.WebJS;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.Utils.PrefManager;
import com.quasar_productions.phoenix_lib.Utils.Utils;
import com.quasar_productions.phoenix_lib.WebView.ChromeClientPhoenix;
import com.quasar_productions.phoenix_lib.WebView.WebViewClientPhoenix;
import com.quasar_productions.phoenix_lib.requests.GetPostBySlug;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.parceler.Parcels;

import de.greenrobot.event.EventBus;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class PostActivityPlain extends ActionBarActivity implements MaterialTabListener {

    private Toolbar mToolbar;
    private GetPostBySlug getPostBySlug;
    String post_slug;
    RequestId requestId;
    ProgressWheel progressWheel;
    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton fab_share;
    PostSingle postSingle;
    Post post_from_intent;
    ImageView fademe;
    WebView webView;
    ViewPager pager;
    MaterialTabHost tabHost;
    public final static int FINGER_RELEASED = 0;
    public final static int FINGER_TOUCHED = 1;
    public final static int FINGER_DRAGGING = 2;
    public final static int FINGER_UNDEFINED = 3;
    private int fingerState = FINGER_RELEASED;
    private SlidingUpPanelLayout Slide;
    ColorScheme colorScheme= new ColorScheme();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().registerSticky(this);
        setContentView(R.layout.activity_post_activity_plain);
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            post_slug = intent.getData().getLastPathSegment();

        }else{
            colorScheme = Parcels.unwrap(getIntent().getExtras().getParcelable(ColorScheme.KEY_COLOR_SCHEME));
            post_from_intent = Parcels.unwrap(getIntent().getExtras().getParcelable(Post.KEY_POST));
            post_slug = post_from_intent.getSlug();
        }
        requestId = new RequestId();
        requestId.generateId(post_slug);
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

        setUpTitles();
    }


    void setUpTitles(){
        if(colorScheme!=null)
        if(PrefManager.Init(getApplicationContext()).getDynamicColors())
        switch (PrefManager.Init(getApplicationContext()).getColorType()) {
            case 1: if (colorScheme.getVibrant()!=null) {
                if (colorScheme.getVibrant().get(ColorScheme.RGB) != -1) {
                    mToolbar.setBackgroundColor(colorScheme.getVibrant().get(ColorScheme.RGB));
                    tabHost.setPrimaryColor(colorScheme.getVibrant().get(ColorScheme.RGB));
                }
                if (colorScheme.getVibrant().get(ColorScheme.TITLETEXTCOLOR) != -1) {
                    mToolbar.setTitleTextColor(colorScheme.getVibrant().get(ColorScheme.TITLETEXTCOLOR));
                    mToolbar.setSubtitleTextColor(colorScheme.getVibrant().get(ColorScheme.TITLETEXTCOLOR));
                    tabHost.setAccentColor(colorScheme.getVibrant().get(ColorScheme.TITLETEXTCOLOR));
                }
            }
                break;
            case 2: if (colorScheme.getLightVibrant()!=null) {
                if (colorScheme.getLightVibrant().get(ColorScheme.RGB) != -1) {
                    mToolbar.setBackgroundColor(colorScheme.getLightVibrant().get(ColorScheme.RGB));
                    tabHost.setPrimaryColor(colorScheme.getLightVibrant().get(ColorScheme.RGB));
                }
                if (colorScheme.getLightVibrant().get(ColorScheme.TITLETEXTCOLOR) != -1) {
                    mToolbar.setTitleTextColor(colorScheme.getLightVibrant().get(ColorScheme.TITLETEXTCOLOR));
                    mToolbar.setSubtitleTextColor(colorScheme.getLightVibrant().get(ColorScheme.TITLETEXTCOLOR));
                    tabHost.setAccentColor(colorScheme.getLightVibrant().get(ColorScheme.TITLETEXTCOLOR));
                }
            }
                break;
            case 3: if (colorScheme.getLightMuted()!=null) {
                if (colorScheme.getLightMuted().get(ColorScheme.RGB) != -1) {
                    mToolbar.setBackgroundColor(colorScheme.getLightMuted().get(ColorScheme.RGB));
                    tabHost.setPrimaryColor(colorScheme.getLightMuted().get(ColorScheme.RGB));
                }
                if (colorScheme.getLightMuted().get(ColorScheme.TITLETEXTCOLOR) != -1) {
                    mToolbar.setTitleTextColor(colorScheme.getLightMuted().get(ColorScheme.TITLETEXTCOLOR));
                    mToolbar.setSubtitleTextColor(colorScheme.getLightMuted().get(ColorScheme.TITLETEXTCOLOR));
                    tabHost.setAccentColor(colorScheme.getLightMuted().get(ColorScheme.TITLETEXTCOLOR));
                }
            }
                break;
            case 4: if (colorScheme.getDarkVibrant()!=null) {
                if (colorScheme.getDarkVibrant().get(ColorScheme.RGB) != -1) {
                    mToolbar.setBackgroundColor(colorScheme.getDarkVibrant().get(ColorScheme.RGB));
                    tabHost.setPrimaryColor(colorScheme.getDarkVibrant().get(ColorScheme.RGB));
                }
                if (colorScheme.getDarkVibrant().get(ColorScheme.TITLETEXTCOLOR) != -1) {
                    mToolbar.setTitleTextColor(colorScheme.getDarkVibrant().get(ColorScheme.TITLETEXTCOLOR));
                    mToolbar.setSubtitleTextColor(colorScheme.getDarkVibrant().get(ColorScheme.TITLETEXTCOLOR));
                    tabHost.setAccentColor(colorScheme.getDarkVibrant().get(ColorScheme.TITLETEXTCOLOR));
                }
            }
                break;
            case 5: if (colorScheme.getDarkMuted()!=null) {
                if (colorScheme.getDarkMuted().get(ColorScheme.RGB) != -1) {
                    mToolbar.setBackgroundColor(colorScheme.getDarkMuted().get(ColorScheme.RGB));
                    tabHost.setPrimaryColor(colorScheme.getDarkMuted().get(ColorScheme.RGB));
                }
                if (colorScheme.getDarkMuted().get(ColorScheme.TITLETEXTCOLOR) != -1) {
                    mToolbar.setTitleTextColor(colorScheme.getDarkMuted().get(ColorScheme.TITLETEXTCOLOR));
                    mToolbar.setSubtitleTextColor(colorScheme.getDarkMuted().get(ColorScheme.TITLETEXTCOLOR));
                    tabHost.setAccentColor(colorScheme.getDarkMuted().get(ColorScheme.TITLETEXTCOLOR));
                }
            }
                break;
            case 6: if (colorScheme.getMuted()!=null) {
                if (colorScheme.getMuted().get(ColorScheme.RGB) != -1) {
                    mToolbar.setBackgroundColor(colorScheme.getMuted().get(ColorScheme.RGB));
                    tabHost.setPrimaryColor(colorScheme.getMuted().get(ColorScheme.RGB));
                }
                if (colorScheme.getMuted().get(ColorScheme.TITLETEXTCOLOR) != -1) {
                    mToolbar.setTitleTextColor(colorScheme.getMuted().get(ColorScheme.TITLETEXTCOLOR));
                    mToolbar.setSubtitleTextColor(colorScheme.getMuted().get(ColorScheme.TITLETEXTCOLOR));
                    tabHost.setAccentColor(colorScheme.getMuted().get(ColorScheme.TITLETEXTCOLOR));
                }
            }
                break;

        }
        mToolbar.setTitle(post_from_intent.getTitle_plain());

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
        else Slide.collapsePanel();

    }

    void setUpSlider(){

        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        pager = (ViewPager) findViewById(R.id.vpPager);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),post_from_intent);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(3);
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
        tabHost.addTab(tabHost.newTab().setText("Related Posts").setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("By " + post_from_intent.getAuthor().getName()).setTabListener(this));
        tabHost.addTab(tabHost.newTab().setText("Gallery").setTabListener(this));

        Slide = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        Slide.setPanelHeight(96);

        Slide.setDragView(tabHost);
        Slide.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
//                Log.i("TAG", "onPanelSlide, offset " + slideOffset);

            }


            @Override
            public void onPanelExpanded(View panel) {
//                Log.i("TAG", "onPanelExpanded");

            }


            @Override
            public void onPanelCollapsed(View panel) {
//                Log.i("TAG", "onPanelCollapsed");

            }


            @Override
            public void onPanelAnchored(View panel) {

                //Log.i("TAG", "onPanelAnchored");
            }


            @Override
            public void onPanelHidden(View panel) {

                //Log.i("TAG", "onPanelHidden");
            }
        });


    }

    void sendRequests(){

        getPostBySlug = new GetPostBySlug(getApplicationContext(),post_slug,requestId);

    }

    public void onEvent(PostSingle event) {
        if (event.getRequestId().equals(requestId)) {
            postSingle = event;
            mToolbar.setTitle(postSingle.getPost().getTitle_plain());
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            String tags = "";
            for (int i = 0; i < postSingle.getPost().getTags().size(); ++i) {
                if (i != postSingle.getPost().getTags().size() - 1)
                    tags += postSingle.getPost().getTags().get(i).getTitle() + ", ";
                else
                    tags += postSingle.getPost().getTags().get(i).getTitle() + ".";
            }
            String materializeCSS = "<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.95.1/css/materialize.min.css\" />";
            String materializeJS = "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.95.1/js/materialize.min.js\"></script>";
            String s = "<img src=\"" + postSingle.getPost().getThumbnail() + "\" alt=\"Smiley face\" height=\"" + height / 3 + "\" width=\"" + width + "\">";
            String header = "<div class=\"row\">\n" +
                    "        <div >\n" +
                    "          <div class=\"card\">\n" +
                    "            <div class=\"card-image\">\n" + s +
                    "              <span class=\"card-title\">" + postSingle.getPost().getTitle_plain() + "</span>\n" +
                    "            </div>\n" +
                    "            <div class=\"card-content\">\n" +
                    "              <p><strong>by " + postSingle.getPost().getAuthor().getName() + ".</strong><br/>" +
                    "               <strong>Tags:</strong> " + tags + " </p>\n" +
                    "            </div>\n" +
                    "          </div>\n" +
                    "        </div>\n" +
                    "      </div>";
            String string = "<html><head>" + Utils.getCache(getApplicationContext(), WebCSS.class.getName()) + materializeCSS + "</head><body>" + header + "<div id=\"content_para\">"
                    + postSingle.getPost().getContent() + "</div>" + Utils.getCache(getApplicationContext(), WebJS.class.getName()) + materializeJS + "<style>\n" +
                    "  #content_para { padding: 8px 8px 8px 8px;}\n #topbar { padding-top :" + (mToolbar.getHeight() / 2) + "px }" +
                    "</style>" + "</body></html>";
            webView.loadData(string, "text/html; charset=UTF-8", null);
        }else{
            Toast.makeText(getApplicationContext(),"Check ReqIds",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().unregister(this);
        AppController.getInstance().getRequestQueue().cancelAll(requestId);
        super.onStop();
    }

    void setUpWebView(){
        webView =(WebView) findViewById(R.id.webview_title);
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
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true).setContentTitle("SRC_IMG").setContentText("Type" + type);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
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
        else if(webView.canGoBack())
           webView.goBack();
        else
            super.onBackPressed();
    }
    public void onEvent(VolleyError volleyError){
        Snackbar.with(this).text("Unable to Load.").swipeToDismiss(true).dismissOnActionClicked(true).duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE).actionLabel("Retry").actionListener(new ActionClickListener() {
            @Override
            public void onActionClicked(Snackbar snackbar) {
                sendRequests();
            }
        }).show(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}