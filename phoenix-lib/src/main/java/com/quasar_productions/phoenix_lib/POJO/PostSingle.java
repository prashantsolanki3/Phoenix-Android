package com.quasar_productions.phoenix_lib.POJO;

import com.quasar_productions.phoenix_lib.POJO.parents.Post;

import org.parceler.Parcel;

/**
 * Created by Prashant on 1/22/2015.
 */
@Parcel
public class PostSingle {

    private String status;
    private Post post;
    private String previous_url;
    private String next_url;
    private String error;
    private String fragment_name;

    /**
     * Error Response
     * @param status
     * @param error
     */
    public PostSingle(String status, String error) {
        this.status = status;
        this.error = error;
    }

    /**
     * Successful Response
     * @param status
     * @param post
     * @param previous_url
     * @param next_url
     */
    public PostSingle(String status, Post post, String previous_url, String next_url) {
        this.status = status;
        this.post = post;
        this.previous_url = previous_url;
        this.next_url = next_url;
    }

    public PostSingle() {
    }

    public String getFragment_name() {
        return fragment_name;
    }

    public void setFragment_name(String fragment_name) {
        this.fragment_name = fragment_name;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPrevious_url() {
        return previous_url;
    }

    public void setPrevious_url(String previous_url) {
        this.previous_url = previous_url;
    }

    public String getNext_url() {
        return next_url;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}



