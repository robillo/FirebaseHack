package com.appbusters.robinkamboj.firebasehack.Models;

/**
 * Created by rishabhshukla on 21/06/17.
 */

public class Assignment {
    Assignment(){

    }
    private String title;
    private String uri;
    private String uid;
    private Long timestamp;
    private String postUid;

    public String getPostUid() {
        return postUid;
    }

    public void setPostUid(String postUid) {
        this.postUid = postUid;
    }

    public Assignment(String title, String uri, String uid, Long timestamp, String postId) {
        this.title = title;
        this.uri = uri;
        this.uid = uid;
        this.timestamp = timestamp;
        this.postUid = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
