package com.quasar_productions.phoenix_lib.POJO;

import com.quasar_productions.phoenix_lib.POJO.parents.Post;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Prashant on 1/22/2015.
 */
@Parcel
public class PostsResult {

    private String status;
    private int count;
    private int count_total;
    private int page;
    private ArrayList<Post> posts;
    private String error;
    private String fragment_name;

    public String getFragment_name() {
        return fragment_name;
    }

    public void setFragment_name(String fragment_name) {
        this.fragment_name = fragment_name;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public PostsResult() {

    }

    /**
     * If Multiple Posts in Response.
     * @param posts
     * @param page
     * @param count_total
     * @param count
     * @param status
     */
    public PostsResult(ArrayList<Post> posts, int page, int count_total, int count, String status) {
        this.posts = posts;
        this.page = page;
        this.count_total = count_total;
        this.count = count;
        this.status = status;
    }

    /**
     * If Response in Erroe
     * @param status
     * @param error
     */
    public PostsResult(String status, String error) {
        this.status = status;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

}
