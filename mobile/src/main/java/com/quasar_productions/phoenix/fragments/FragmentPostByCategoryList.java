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

import com.android.volley.VolleyError;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.activities.PostActivityParallax;
import com.quasar_productions.phoenix.activities.PostActivityPlain;
import com.quasar_productions.phoenix.adapters.LinearRecyclerViewAdapter;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.ColorScheme;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.requests.GetPostsByCategory;

import org.lucasr.twowayview.ItemClickSupport;
import org.parceler.Parcels;

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
    RequestId requestId;
    public FragmentPostByCategoryList newInstance(int categoryId){
        FragmentPostByCategoryList mFragment = new FragmentPostByCategoryList();
        Bundle args = new Bundle();
        requestId = new RequestId();
        requestId.generateId(categoryId);
        args.putInt(ARG_CATEGORY, categoryId);
        args.putParcelable(RequestId.KEY_REQUEST_ID,Parcels.wrap(requestId));
        mFragment.setArguments(args);
        getPostsByCategory = new GetPostsByCategory(categoryId,requestId);
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
        requestId = Parcels.unwrap(getArguments().getParcelable(RequestId.KEY_REQUEST_ID));
        setupRecyclerView(rootView);
       // setupRefreshView(rootView);
        setRetainInstance(true);
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
        AppController.getInstance().cancelPendingRequests(requestId);
        super.onStop();
    }
    // This method will be called when a MessageEvent is posted
   public void onEventAsync(PostsResult event){

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

                Intent intent = new Intent(getActivity(), PostActivityPlain.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Post.KEY_POST, Parcels.wrap(childViewHolder.post));
                /*bundle.putInt("title_bg", childViewHolder.titleBg);
                bundle.putInt("title_color", childViewHolder.titleColor);*/
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
                    Log.d("Endless Scroll", "List Finished");
                    getPostsByCategory = new GetPostsByCategory(categoryId, ++page, requestId);
                    loading = true;

                }
            }

        });
    }

}