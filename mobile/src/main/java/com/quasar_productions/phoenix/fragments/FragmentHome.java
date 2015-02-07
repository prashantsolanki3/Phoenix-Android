package com.quasar_productions.phoenix.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
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
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.activities.PostActivityPlain;
import com.quasar_productions.phoenix.adapters.HorizontalLinearRecyclerViewAdapter;
import com.quasar_productions.phoenix_lib.AppController;
import com.quasar_productions.phoenix_lib.POJO.ColorScheme;
import com.quasar_productions.phoenix_lib.POJO.PostsResult;
import com.quasar_productions.phoenix_lib.POJO.RequestId;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.quasar_productions.phoenix_lib.requests.GetFeaturedPosts;


import org.lucasr.twowayview.ItemClickSupport;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;

import de.greenrobot.event.EventBus;


public class FragmentHome extends Fragment {


    ItemClickSupport itemClick;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private RecyclerView mRecyclerView;
    HorizontalLinearRecyclerViewAdapter adapter;
    private boolean mSearchCheck;

    private int page =1;
    private RequestId requestId;
    public static FragmentHome newInstance(Context context){
        FragmentHome mFragment = new FragmentHome();
        Bundle bundle= new Bundle();
        RequestId requestId1 = new RequestId();
        GetFeaturedPosts getFeaturedPosts = new GetFeaturedPosts(context,requestId1);
        bundle.putParcelable(RequestId.KEY_REQUEST_ID, Parcels.wrap(requestId1));
        mFragment.setArguments(bundle);
        return mFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View rootView = inflater.inflate(R.layout.layout_home, container, false);
        requestId = Parcels.unwrap(getArguments().getParcelable(RequestId.KEY_REQUEST_ID));
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));
        setupRecyclerView(rootView);
        setRetainInstance(true);

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
        AppController.getInstance().getRequestQueue().cancelAll(requestId);
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(EventBus.getDefault().isRegistered(this));
        EventBus.getDefault().unregister(this);
    }

    // This method will be called when a MessageEvent is posted
   public void onEventAsync(PostsResult event){

       if(event.getCount()>0&&event.getRequestId()==requestId)
           adapter.addPosts(event.getPosts());

   }

   public void onEvent(VolleyError volleyError){
       Snackbar.with(getActivity()).text("Unable to Load. Try Again Later.").show(getActivity());
   }


    private void setupRecyclerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setHapticFeedbackEnabled(true);
        mRecyclerView.setLongClickable(true);

        itemClick = ItemClickSupport.addTo(mRecyclerView);
        itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View child, int position, long id) {
                HorizontalLinearRecyclerViewAdapter.SimpleViewHolder childViewHolder = (HorizontalLinearRecyclerViewAdapter.SimpleViewHolder) parent.getChildViewHolder(child);


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
                Intent intent = new Intent(getActivity(), PostActivityPlain.class);
                Bundle bundle = new Bundle();
                RequestId requestId1 = new RequestId();
                requestId1.generateId(childViewHolder.post.getSlug());
                bundle.putParcelable(Post.KEY_POST, Parcels.wrap(childViewHolder.post));
                bundle.putParcelable(RequestId.KEY_REQUEST_ID, Parcels.wrap(requestId1));
                bundle.putParcelable(ColorScheme.KEY_COLOR_SCHEME,Parcels.wrap(childViewHolder.getColorScheme()));
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

/* startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_left);
*/

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

        adapter = new HorizontalLinearRecyclerViewAdapter(getActivity(), new ArrayList<Post>());
        mRecyclerView.setAdapter(adapter);
        //Setting Up Layout Manger
        final LinearLayoutManager mLayoutManager;

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);

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
               /* int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
*/

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
                    GetFeaturedPosts getFeaturedPosts = new GetFeaturedPosts(getActivity().getApplicationContext(), requestId, ++page);
                    loading = true;

                }
            }

        });
    }
}