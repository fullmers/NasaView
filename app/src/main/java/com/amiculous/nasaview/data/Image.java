package com.amiculous.nasaview.data;

public interface Image {

    int getId();
    String getCopyright();
    String getDate();
    String getExplanation();
    String getMediaType();
    String getTitle();
    String getUrl();
    boolean getIsFavorite();
}
