package com.amiculous.nasaview.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "apod")
public class ApodEntity implements Image {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String copyright;
    private String date;
    private String explanation;
    private String mediaType;
    private String title;
    private String url;
    private boolean isFavorite;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getCopyright() {
        return copyright;
    }

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public String getExplanation() {
        return explanation;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public boolean getIsFavorite() {
        return isFavorite;
    }

    @Ignore
    public ApodEntity(String copyright, String date, String explanation, String mediaType, String title, String url, boolean isFavorite) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
        this.isFavorite = isFavorite;
    }

    public ApodEntity(Image image) {
        this.id = image.getId();
        this.copyright = image.getCopyright();
        this.date = image.getDate();
        this.explanation = image.getExplanation();
        this.mediaType = image.getMediaType();
        this.title = image.getTitle();
        this.url = image.getUrl();
        this.isFavorite = image.getIsFavorite();
    }


}
