package com.appbusters.robinkamboj.firebasehack.Models;

/**
 * Created by rishabhshukla on 24/04/17.
 */

public class Video {

    private String thumbnail;
    private String videoUrl;
    private String title;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Video(String thumbnail, String videoUrl, String title) {
        this.thumbnail = thumbnail;
        this.videoUrl = videoUrl;
        this.title = title;
    }
}
