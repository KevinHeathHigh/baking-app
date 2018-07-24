package net.hobbitsoft.bakingapp.recipes;

import java.io.Serializable;

public class Step implements Serializable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURIL;

    public Step() {
    }

    public Step(int id, String shortDescription, String videoURL, String thumbnailURIL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.videoURL = videoURL;
        this.thumbnailURIL = thumbnailURIL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURIL() {
        return thumbnailURIL;
    }

    public void setThumbnailURIL(String thumbnailURIL) {
        this.thumbnailURIL = thumbnailURIL;
    }
}
