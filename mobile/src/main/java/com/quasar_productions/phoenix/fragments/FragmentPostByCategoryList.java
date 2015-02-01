package com.quasar_productions.phoenix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.activities.PostActivityParallax;
import com.quasar_productions.phoenix.activities.PostActivityPlain;
import com.quasar_productions.phoenix.adapters.LinearRecyclerViewAdapter;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.requests.GetPostsByCategory;

import org.lucasr.twowayview.ItemClickSupport;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class FragmentPostByCategoryList extends Fragment {


    ItemClickSupport itemClick;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private RecyclerView mRecyclerView;
    LinearRecyclerViewAdapter adapter;
    private boolean mSearchCheck;
    private int categoryId;
    GetPostsByCategory getPostsByCategory;
    private static String ARG_CATEGORY="category_id";
    private int page =1;
   // SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressWheel progressWheel;
    long timestamp_id;

    public FragmentPostByCategoryList newInstance(int categoryId){
        FragmentPostByCategoryList mFragment = new FragmentPostByCategoryList();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY, categoryId);
        timestamp_id = SystemClock.elapsedRealtime();
        args.putLong("timestamp_id",timestamp_id);
        mFragment.setArguments(args);
        getPostsByCategory = new GetPostsByCategory(categoryId,timestamp_id);
        return mFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.layout_recyclerview, container, false);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);

        categoryId = getArguments().getInt(ARG_CATEGORY);
        timestamp_id=getArguments().getLong("timestamp_id");
        setupRecyclerView(rootView);
       // setupRefreshView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        searchView.setQueryHint(this.getString(R.string.search));

        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
        searchView.setOnQueryTextListener(onQuerySearchView);


        menu.findItem(R.id.menu_add).setVisible(true);
        menu.findItem(R.id.menu_search).setVisible(true);

        mSearchCheck = false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub

        switch (item.getItemId()) {

            case R.id.menu_add:
                Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
                break;


            case R.id.menu_search:
                mSearchCheck = true;
                Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }



    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }


        @Override
        public boolean onQueryTextChange(String s) {
            if (mSearchCheck){
                // implement your search here
                FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                Fragment mFragment = new FragmentPostBySearch().newInstance(s);
                if (mFragment != null) {
                    mFragmentManager.beginTransaction().replace(br.liveo.navigationliveo.R.id.container, mFragment).addToBackStack(null).commit();

                }
            }
            return false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().registerSticky(this);

    }
    @Override
    public void onStop() {
        if(EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().unregister(this);
        AppController.getInstance().cancelPendingRequests(String.valueOf(categoryId).concat(String.valueOf(timestamp_id)));
        super.onStop();
    }
    // This method will be called when a MessageEvent is posted
   public void onEventAsync(PostsResult event){

       if(event.getCount()>0&&event.getFragment_name().equals(String.valueOf(categoryId).concat(String.valueOf(timestamp_id))))
           adapter.addPosts(event.getPosts());

       /*if(mSwipeRefreshLayout!=null)
           if(mSwipeRefreshLayout.isRefreshing())
               mSwipeRefreshLayout.setRefreshing(false);*/
       /*else
           EventBus.getDefault().unregister(this);*/
   }


    private void setupRecyclerView(final View view){

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
                Intent intent=new Intent(getActivity(), PostActivityPlain.class);
                Bundle bundle = new Bundle();
                timestamp_id= SystemClock.elapsedRealtime();
                bundle.putLong("timestamp_id",timestamp_id);
                bundle.putString("post_slug",childViewHolder.post.getSlug());
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
                            public void onActionClicked() {
                                Snackbar.with(getActivity()).swipeToDismiss(true).animation(true).text("Undo Clicked").show(getActivity());
                            }
                        })
                        .show(getActivity());
                return true;
            }
        });

        adapter = new LinearRecyclerViewAdapter(getActivity(),new ArrayList<Post>(),progressWheel);
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
                    getPostsByCategory = new GetPostsByCategory(categoryId,++page,timestamp_id);
                    loading = true;

                }

                /**/
            }

        });


      /*  final Drawable divider = getResources().getDrawable(R.drawable.divider);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(divider));*/
       // mSwipeRefreshLayout.setRefreshing(true);
    }
   /* private void setupRefreshView(View view){

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent_material_light),getResources().getColor(R.color.accent_material_dark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(true);
                Snackbar.with(getActivity()).animation(true).swipeToDismiss(true).text("Refreshing").show(getActivity());
                timestamp_id = SystemClock.elapsedRealtime();
                page=0;
                getPostsByCategory = new GetPostsByCategory(categoryId,++page,timestamp_id);
                adapter.clearPosts();
            }
        });
    }*/

}