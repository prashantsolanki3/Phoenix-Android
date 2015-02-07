package com.quasar_productions.phoenix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.activities.PostActivityPlain;
import com.quasar_productions.phoenix.adapters.LinearRecyclerViewAdapter;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.ColorScheme;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Author;
import com.quasar_productions.phoenix_lib.requests.GetPostsByAuthor;

import org.lucasr.twowayview.ItemClickSupport;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class FragmentPostByAuthorList extends Fragment {


    ItemClickSupport itemClick;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private RecyclerView mRecyclerView;
    LinearRecyclerViewAdapter adapter;
    private static int authorId;
    private static GetPostsByAuthor getPostsByAuthor;
    private int page =1;
    ProgressWheel progressWheel;
    PostsResult postsResult;
    RequestId requestId;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("postResult",Parcels.wrap(postsResult));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            postsResult = Parcels.unwrap(savedInstanceState.getParcelable("postResult"));
            Log.d("status", postsResult.getStatus());
        }
        setRetainInstance(true);
    }

    public static FragmentPostByAuthorList newInstance(int authorId){
        FragmentPostByAuthorList mFragment = new FragmentPostByAuthorList();
        Bundle args = new Bundle();
        RequestId requestId = new RequestId();
        args.putInt(Author.KEY_AUTHOR, authorId);
        requestId.generateId(authorId);
        args.putParcelable(RequestId.KEY_REQUEST_ID,Parcels.wrap(requestId));
        mFragment.setArguments(args);
        return mFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.layout_recyclerview, container, false);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        if(!progressWheel.isSpinning())
        progressWheel.spin();
        authorId = getArguments().getInt(Author.KEY_AUTHOR);
        requestId=Parcels.unwrap(getArguments().getParcelable(RequestId.KEY_REQUEST_ID));
        getPostsByAuthor = new GetPostsByAuthor(getActivity().getApplicationContext(),authorId,new RequestId());
        setupRecyclerView(rootView);
       // setupRefreshView(rootView);
           if(savedInstanceState!=null){
                Log.d("Author savedInst","Not Null");
                postsResult = Parcels.unwrap(savedInstanceState.getParcelable("postResult"));
            if(postsResult.getCount()>0&&postsResult.getRequestId().equals(requestId))
                adapter.addPosts(postsResult.getPosts());
            if(!progressWheel.isSpinning())
                progressWheel.spin();
            Log.d("POST RESULT","NOT NULL");
            }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);

    }
    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().unregister(this);
        AppController.getInstance().cancelPendingRequests(requestId);
        super.onStop();
    }
    // This method will be called when a MessageEvent is posted
   public void onEventAsync(PostsResult event){
      postsResult=event;
       if(event.getCount()>0&&event.getRequestId().equals(requestId))
           adapter.addPosts(event.getPosts());
       /*if(mSwipeRefreshLayout!=null)
           if(mSwipeRefreshLayout.isRefreshing())
               mSwipeRefreshLayout.setRefreshing(false);*/
       /*else
           EventBus.getDefault().unregister(this);*/
   }

    public void onEvent(VolleyError volleyError){
        Snackbar.with(getActivity()).text("Unable to Load. Try Again Later.").show(getActivity());
    }


    private void setupRecyclerView(final View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mRecyclerView.setLongClickable(true);

        itemClick = ItemClickSupport.addTo(mRecyclerView);
        itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View child, int position, long id) {
                LinearRecyclerViewAdapter.SimpleViewHolder childViewHolder = (LinearRecyclerViewAdapter.SimpleViewHolder) parent.getChildViewHolder(child);


/* Intent i = new Intent(getActivity(),Detailed.class);
                i.putExtra("product",childViewHolder.product);
*/

                childViewHolder.featured_src.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.with(getActivity()).text("Nailed It").show(getActivity());
                    }
                });
                childViewHolder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.with(getActivity()).text("Nailed Title").show(getActivity());
                    }
                });
/*
                FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                Fragment mFragment = new FragmentPostViewWithoutScroll().newInstance(childViewHolder.post.getSlug());
                if (mFragment != null) {
                    mFragmentManager.beginTransaction().replace(br.liveo.navigationliveo.R.id.container, mFragment).addToBackStack(null).commit();

                }*/
                Intent intent = new Intent(getActivity(), PostActivityPlain.class);
                Bundle bundle = new Bundle();
                bundle.putString("post_slug", childViewHolder.post.getSlug());
                bundle.putParcelable(Post.KEY_POST, Parcels.wrap(childViewHolder.post));
                bundle.putParcelable(ColorScheme.KEY_COLOR_SCHEME,Parcels.wrap(childViewHolder.getColorScheme()));
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

        });


        itemClick.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
                Snackbar.with(getActivity()) // context
                        .text("Different colors this time") // text to be displayed
                        .actionLabel("Action") // action button label
                        .swipeToDismiss(true)
                        .animation(true)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                Snackbar.with(getActivity()).swipeToDismiss(true).animation(true).text("Undo Clicked").show(getActivity());
                            }
                        })
                        .show(getActivity());
                return true;
            }
        });

        adapter = new LinearRecyclerViewAdapter(getActivity(), new ArrayList<Post>(), progressWheel);
        mRecyclerView.setAdapter(adapter);
        //Setting Up Layout Manger
        final LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
                // updateState(scrollState);

            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                /*
                 * SwipeRefresh Layout
                 */
              /*  int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);*/


                /**/
                /* Endless Scrolling*/
                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();


                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    //Your Code Here...

                    Log.d("Endless Scroll", "List Finished");
                    getPostsByAuthor = new GetPostsByAuthor(getActivity().getApplicationContext(),authorId, ++page, requestId);
                    loading = true;

                }

                /**/
            }

        });


    }
}