package com.quasar_productions.phoenix_lib.POJO.parents.post;

import org.parceler.Parcel;

/**
 * Created by Prashant on 1/22/2015.
 */
@Parcel
public class Comments {
    private int id;
    private String name;
    private String url;
    private String date;
    private String content;
    private int parent;
    public  static final String KEY_COMMENTS = "key_comments";
    public Comments() {
    }

    public Comments(int id, String name, String url, String date, String content, int parent) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.date = date;
        this.content = content;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
