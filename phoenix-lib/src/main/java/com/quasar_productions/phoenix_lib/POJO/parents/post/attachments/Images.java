package com.quasar_productions.phoenix_lib.POJO.parents.post.attachments;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Prashant on 1/22/2015.
 */
@Parcel
public class Images {
    private ImgDimen full;
    private ImgDimen thumbnail;
    private ImgDimen medium;
    private ImgDimen large;
    @SerializedName("small-wide")
    private ImgDimen small_wide;
    private ImgDimen quad;
    private ImgDimen single;
    private ImgDimen cover;
    public static final String KEY_IMAGES = "key_images";
    public Images() {
    }

    public ImgDimen getFull() {
        return full;
    }

    public void setFull(ImgDimen full) {
        this.full = full;
    }

    public ImgDimen getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImgDimen thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImgDimen getMedium() {
        return medium;
    }

    public void setMedium(ImgDimen medium) {
        this.medium = medium;
    }

    public ImgDimen getLarge() {
        return large;
    }

    public void setLarge(ImgDimen large) {
        this.large = large;
    }

    public ImgDimen getSmall_wide() {
        return small_wide;
    }

    public void setSmall_wide(ImgDimen small_wide) {
        this.small_wide = small_wide;
    }

    public ImgDimen getQuad() {
        return quad;
    }

    public void setQuad(ImgDimen quad) {
        this.quad = quad;
    }

    public ImgDimen getSingle() {
        return single;
    }

    public void setSingle(ImgDimen single) {
        this.single = single;
    }

    public ImgDimen getCover() {
        return cover;
    }

    public void setCover(ImgDimen cover) {
        this.cover = cover;
    }
}


