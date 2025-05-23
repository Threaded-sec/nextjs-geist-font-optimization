package com.example.moviewatcher.models;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String duration;
    private List<Episode> episodes;
    private int creditCost;

    public Movie(String id, String title, String description, String thumbnailUrl, 
                String duration, List<Episode> episodes, int creditCost) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.episodes = episodes;
        this.creditCost = creditCost;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getDuration() { return duration; }
    public List<Episode> getEpisodes() { return episodes; }
    public int getCreditCost() { return creditCost; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setEpisodes(List<Episode> episodes) { this.episodes = episodes; }
    public void setCreditCost(int creditCost) { this.creditCost = creditCost; }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", episodes=" + episodes +
                ", creditCost=" + creditCost +
                '}';
    }
}
