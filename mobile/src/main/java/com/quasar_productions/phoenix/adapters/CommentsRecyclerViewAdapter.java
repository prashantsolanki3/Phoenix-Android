package com.quasar_productions.phoenix.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Comments;

import org.jsoup.examples.HtmlToPlainText;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.SimpleViewHolder> {
    private final Context mContext;
    private ArrayList<Comments> commentlist = null;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public Comments comments;
        public TextView content;
        public TextView date;
        public LinearLayout linearLayout;
        public SimpleViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            content = (TextView) view.findViewById(R.id.content);
            date = (TextView) view.findViewById(R.id.date);
            linearLayout =(LinearLayout) view.findViewById(R.id.container);
         }

        public void setData(Context mContext, Comments comments) {
            this.comments=comments;
            if (name != null)
                name.setText(comments.getName());

            content.setText(Html.fromHtml(comments.getContent()));

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
            try{
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                        f.parse(comments.getDate()).getTime(),
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                date.setText(timeAgo);
                Log.d("Date",f.toString());
            }catch(Exception e){
                e.printStackTrace();
                date.setText(comments.getDate());
            }

            /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            timestamp.setText(timeAgo);*/

            if(comments.getParent()!=0){
                linearLayout.setPadding(100,10,10,10);
            }
        }
    }


    public CommentsRecyclerViewAdapter(Context context, ArrayList<Comments> commentlist) {
        mContext = context;
        this.commentlist = commentlist;
    }

    public void clearPosts() {
        commentlist.clear();
        commentlist = new ArrayList<>();
        notifyDataSetChanged();

    }

    public void addPosts(ArrayList<Comments> commentlist) {
        this.commentlist.addAll(commentlist);
        this.notifyItemRangeInserted(0, commentlist.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_comments, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {

        holder.setData(mContext, commentlist.get(position));
        // Here you apply the animation when the view is bound
    }


    @Override
    public int getItemCount() {
        return commentlist.size();
    }
} 