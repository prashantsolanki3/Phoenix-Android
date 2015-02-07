package com.quasar_productions.phoenix_lib.POJO.parents.post;

import org.parceler.Parcel;

/**
 * Created by Prashant on 12/18/2014.
 */
@Parcel
public class Categories{
    int id;
    int parent;
    int post_count;
    String slug;
    String title;
    String description;
    public static final String KEY_CATEGORIES= "key_categories";
    public Categories() {
    }

    public Categories(int id, int parent, int post_count, String slug, String title, String description) {
        this.id = id;
        this.parent = parent;
        this.post_count = post_count;
        this.slug = slug;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
