package com.quasar_productions.phoenix_lib.POJO;

import org.parceler.Parcel;

/**
 * Created by Prashant on 1/26/2015.
 */
@Parcel
public class PostProperties {

    int id;
    boolean favorite;

    public PostProperties() {
        }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
