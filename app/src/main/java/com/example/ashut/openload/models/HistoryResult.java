
package com.example.ashut.openload.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistoryResult {

    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("Year")
    @Expose
    private String year;
    @SerializedName("DownloadLink")
    @Expose
    private String downloadLink;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public HistoryResult() {

    }

    public HistoryResult(String name, String genre, String year, String imageUrl) {
        super();
        this.name = name;
        this.genre = genre;
        this.year = year;
        image = imageUrl;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getGenre() {
        return genre;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
