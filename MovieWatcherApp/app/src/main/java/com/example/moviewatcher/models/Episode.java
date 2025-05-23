package com.example.moviewatcher.models;

import java.io.Serializable;

public class Episode implements Serializable {
    private String id;
    private String title;
    private int episodeNumber;
    private String videoUrl;
    private String thumbnailUrl;
    private String duration;
    private int creditCost;
    private boolean isWatched;

    public Episode(String id, String title, int episodeNumber, String videoUrl, 
                  String thumbnailUrl, String duration, int creditCost) {
        this.id = id;
        this.title = title;
        this.episodeNumber = episodeNumber;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.creditCost = creditCost;
        this.isWatched = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getEpisodeNumber() { return episodeNumber; }
    public String getVideoUrl() { return videoUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getDuration() { return duration; }
    public int getCreditCost() { return creditCost; }
    public boolean isWatched() { return isWatched; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setCreditCost(int creditCost) { this.creditCost = creditCost; }
    public void setWatched(boolean watched) { isWatched = watched; }

    @Override
    public String toString() {
        return "Episode{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", videoUrl='" + videoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", creditCost=" + creditCost +
                ", isWatched=" + isWatched +
                '}';
    }
}
