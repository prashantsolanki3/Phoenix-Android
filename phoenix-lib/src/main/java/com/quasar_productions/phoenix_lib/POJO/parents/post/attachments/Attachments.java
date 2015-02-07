package com.quasar_productions.phoenix_lib.POJO.parents.post.attachments;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Prashant on 1/22/2015.
 */
@Parcel
public class Attachments {
     int id;
     String url;
     String slug;
     String title;
     String description;
     String caption;
     int parent;
     String mime_type;
     public static final String KEY_ATTACHMENTS="key_attachments";
     //Images images;
   //  ArrayList<Images> images;

    public Attachments() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public Attachments(int id, String url, String slug, String title, String description, String caption, int parent, String mime_type, ArrayList<Images> images) {
        this.id = id;
        this.url = url;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.caption = caption;
        this.parent = parent;
        this.mime_type = mime_type;
  //      this.images = images;
    }
/*
    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }*/
}
