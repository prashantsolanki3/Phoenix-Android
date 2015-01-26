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

import com.pnikosis.materialishprogress.ProgressWheel;
import com.quasar_productions.phoenix.R;
import com.quasar_productions.phoenix.implimentations.PaletteTransformation;
import com.quasar_productions.phoenix_lib.POJO.parents.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.SimpleViewHolder> {

    private final Context mContext;
    private ArrayList<Post> postlist = null;
    ProgressWheel progressWheel;
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

        public void setData(Context mContext, Post post,int height,int width) {
            this.post = post;
            if (title != null)
                title.setText(post.getTitle());

            if (featured_src != null)
                Picasso.with(mContext)
                        .load(post.getThumbnail()==null?"error":post.getThumbnail())
                        .placeholder(R.drawable.abc_ab_share_pack_holo_dark)
                        .transform(PaletteTransformation.instance())
                        .resize(width,height)
                        .error(R.drawable.abc_btn_check_material)
                        .into(featured_src, new Callback() {
                            @Override
                            public void onSuccess() {

                                Bitmap bitmap = ((BitmapDrawable) featured_src.getDrawable()).getBitmap(); // Ew!
                                Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch swatch = palette.getVibrantSwatch();
                                        if (swatch != null) {
                                            title.setBackgroundColor(swatch.getRgb());
                                            title.setTextColor(swatch.getTitleTextColor());
                                        }
                                    }
                                });


                            }

                            @Override
                            public void onError() {

                            }
                        });
        }
    }


    public GridRecyclerViewAdapter(Context context, ArrayList<Post> postlist) {
        mContext = context;
        this.postlist = postlist;
    }
    public GridRecyclerViewAdapter(Context context, ArrayList<Post> postlist,ProgressWheel progressWheel) {
        mContext = context;
        this.postlist = postlist;
        this.progressWheel=progressWheel;
    }
    public void clearPosts() {
        postlist.clear();
        postlist = new ArrayList<>();
        notifyDataSetChanged();

    }

    public void addPosts(ArrayList<Post> postlist) {
        if(progressWheel!=null)
            if (progressWheel.isSpinning())
                progressWheel.stopSpinning();
        this.postlist.addAll(postlist);
        this.notifyItemRangeInserted(0, postlist.size() - 1);
        notifyDataSetChanged();

    }
/*

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        postlist.clear();

        if (charText.length() == 0) {
            postlist.addAll(arraylist);
        } else {
            for (Post wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText) || wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    postlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

    public void sortByNameAsc() {
        Collections.sort(postlist, Post.nameAscending);
        notifyDataSetChanged();
    }

    public void sortByNameDesc() {
        Collections.sort(postlist, Post.nameDescending);
        notifyDataSetChanged();
    }

    public void sortByPriceAsc() {
        Collections.sort(postlist, Post.priceAscending);
        notifyDataSetChanged();
    }

    public void sortByPriceDesc() {
        Collections.sort(postlist, Post.priceDescending);
        notifyDataSetChanged();
    }

    public void sortByPopDesc() {
        Collections.sort(postlist, Post.popularityDescending);
        notifyDataSetChanged();
    }

    public void sortByPopAsc() {
        Collections.sort(postlist, Post.popularityAscending);
        notifyDataSetChanged();
    }
*/

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_featured_title, parent, false);
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
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        setAnimation(holder.container, position);
        if(position%6<2){
            holder.setData(mContext, postlist.get(position),200,200);
        }else
            holder.setData(mContext, postlist.get(position),100,100);
        // Here you apply the animation when the view is bound

        boolean isVertical = true;
        final View itemView = holder.itemView;
/*        if (mLayoutId == R.layout.layout_recyclerview) {
            final GridLayoutManager.LayoutParams lp =
                    (GridLayoutManager.LayoutParams) itemView.getLayoutParams();


            final int span1 = (position == 0 || position == 3 ? 2 : 1);
            final int span2 = (position == 0 ? 2 : (position == 3 ? 3 : 1));


            final int colSpan = (isVertical ? span2 : span1);
            final int rowSpan = (isVertical ? span1 : span2);


            if (lp.rowSpan != rowSpan || lp.colSpan != colSpan) {
                lp.rowSpan = rowSpan;
                lp.colSpan = colSpan;
                itemView.setLayoutParams(lp);
            }
        }*/
    }


    @Override
    public int getItemCount() {
        return postlist.size();
    }
} 