package com.quasar_productions.phoenix.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.adapters.CommentsRecyclerViewAdapter;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;

import org.lucasr.twowayview.ItemClickSupport;
import org.parceler.Parcel;
import org.parceler.Parcels;

public class CommentFragment extends Fragment {
    Post post;
    RecyclerView recyclerView;
    ItemClickSupport itemClickSupport;
    LinearLayoutManager linearLayoutManager;
    CommentsRecyclerViewAdapter commentsRecyclerViewAdapter;
    // newInstance constructor for creating fragment with arguments 
    public static CommentFragment newInstance(Post post) {
        CommentFragment fragmentFirst = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", Parcels.wrap(post));
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    } 
 
    // Store instance variables based on arguments passed 
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    post = Parcels.unwrap(getArguments().getParcelable("post"));

    }
 
    // Inflate the view for the fragment based on layout XML 
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.comments_recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        commentsRecyclerViewAdapter = new CommentsRecyclerViewAdapter(getActivity().getApplicationContext(),post.getComments());
        recyclerView.setAdapter(commentsRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRetainInstance(true);
        return view;
    } 
} 