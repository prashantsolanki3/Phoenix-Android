package com.quasar_productions.phoenix.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.implimentations.PaletteTransformation;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HorizontalLinearRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalLinearRecyclerViewAdapter.SimpleViewHolder> {
    private final Context mContext;
    private List<Post> postlist = null;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;
        public final ImageView featured_src;
        public Post post;
        LinearLayout container;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
           featured_src = (ImageView) view.findViewById(R.id.featured_src);
            container = (LinearLayout) view.findViewById(R.id.frame);
        }

        public void setData(Context mContext, Post post) {
            this.post = post;
            if (title != null)
                title.setText(post.getTitle());
            if (featured_src != null)
                Picasso.with(mContext)
                        .load(post.getThumbnail()==null?"error":post.getThumbnail())
                        .placeholder(R.drawable.abc_ab_share_pack_holo_dark)
                        .transform(PaletteTransformation.instance())
                        .resize(100,100)
                        .error(R.drawable.abc_btn_check_material)
                        .into(featured_src, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) featured_src.getDrawable()).getBitmap(); // Ew!
                                Palette palette = PaletteTransformation.getPalette(bitmap);
                                Palette.Swatch swatch = palette.getVibrantSwatch();
                                if (swatch != null) {
                                    title.setBackgroundColor(swatch.getRgb());
                                    title.setTextColor(swatch.getTitleTextColor());
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
        }
    }


    public HorizontalLinearRecyclerViewAdapter(Context context, List<Post> postlist) {
        mContext = context;
        this.postlist = postlist;
    }

    public void clearPosts() {
        postlist.clear();
        postlist = new ArrayList<>();
        notifyDataSetChanged();

    }

    public void addPosts(List<Post> postlist) {
        this.postlist.addAll(postlist);
        this.notifyItemRangeInserted(0, postlist.size() - 1);
        notifyDataSetChanged();
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_horizontal_featured_title, parent, false);
        return new SimpleViewHolder(view);
    }

    private int lastPosition = -1;

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext,android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        setAnimation(holder.container, position);
        holder.setData(mContext, postlist.get(position));
        // Here you apply the animation when the view is bound

    }


    @Override
    public int getItemCount() {
        return postlist.size();
    }
} 