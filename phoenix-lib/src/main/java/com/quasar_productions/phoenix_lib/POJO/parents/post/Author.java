package com.quasar_productions.phoenix_lib.POJO.parents.post;

import org.parceler.Parcel;

/**
 * Created by Prashant on 12/18/2014.
 */
@Parcel
public class Author{
     int id;
     String slug;
     String name;
     String first_name;
     String last_name;
     String nickname;
     String url;
     String description;
     public static final String KEY_AUTHOR="key_author";

    public Author() {
    }

    public Author(int id, String slug, String name, String first_name, String last_name, String nickname, String url, String description) {
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nickname = nickname;
        this.url = url;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
