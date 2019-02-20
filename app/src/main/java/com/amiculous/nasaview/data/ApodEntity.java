package com.amiculous.nasaview.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "apod_favorites")
public class ApodEntity implements Image {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String copyright;
    private String date;
    private String explanation;
    private String mediaType;
    private String title;
    private String url;
    public int getId() {
        return id;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Ignore
    public ApodEntity(String copyright, String date, String explanation, String mediaType, String title, String url) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
    }

    public ApodEntity(int id, String copyright, String date, String explanation, String mediaType, String title, String url) {
        this.id = id;
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
    }

    public ApodEntity(Image image) {
        this.id = image.getId();
        this.copyright = image.getCopyright();
        this.date = image.getDate();
        this.explanation = image.getExplanation();
        this.mediaType = image.getMediaType();
        this.title = image.getTitle();
        this.url = image.getUrl();
    }


}
