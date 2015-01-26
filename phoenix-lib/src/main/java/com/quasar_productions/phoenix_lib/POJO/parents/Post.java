package com.quasar_productions.phoenix_lib.POJO.parents;

import com.quasar_productions.phoenix_lib.POJO.parents.post.Author;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Categories;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Comments;
import com.quasar_productions.phoenix_lib.POJO.parents.post.Tags;
import com.quasar_productions.phoenix_lib.POJO.parents.post.attachments.Attachments;
import com.quasar_productions.phoenix_lib.POJO.parents.post.attachments.Images;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Prashant on 12/18/2014.
 */
@Parcel
public class Post{

    int id;
    //String fragment_name;
    String type;
    String slug;
    String url;
    String status;
    String title;
    String title_plain;
    String content;
    String excerpt;
    String date;
    String modified;
    ArrayList<Categories> categories;
    ArrayList<Tags> tags;
    Author author;
    ArrayList<Comments> comments;
    ArrayList<Attachments> attachments;
    int comment_count;
    String comment_status;
    String thumbnail;
    String thumbnail_size;
    Images thumbnail_images;

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }

 /*
    public String getFragment_name() {
        return fragment_name;
    }

    public void setFragment_name(String fragment_name) {
        this.fragment_name = fragment_name;
    }
*/
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Images getThumbnail_images() {
        return thumbnail_images;
    }

    public void setThumbnail_images(Images thumbnail_images) {
        this.thumbnail_images = thumbnail_images;
    }

    public String getThumbnail_size() {
        return thumbnail_size;
    }

    public void setThumbnail_size(String thumbnail_size) {
        this.thumbnail_size = thumbnail_size;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public ArrayList<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachments> attachments) {
        this.attachments = attachments;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_plain() {
        return title_plain;
    }

    public void setTitle_plain(String title_plain) {
        this.title_plain = title_plain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }

    public ArrayList<Tags> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tags> tags) {
        this.tags = tags;
    }
}