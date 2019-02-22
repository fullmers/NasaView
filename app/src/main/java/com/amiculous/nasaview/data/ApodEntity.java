package com.amiculous.nasaview.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "apod_favorites")
public class ApodEntity implements Image, Parcelable {

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
    public ApodEntity(String copyright, @NonNull String date, @NonNull String explanation, @NonNull String mediaType, @NonNull String title, @NonNull String url) {
        if (copyright == null)
            this.copyright = "";
        else
            this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
    }

    public ApodEntity(int id, String copyright, @NonNull String date, @NonNull String explanation, @NonNull String mediaType, @NonNull String title, @NonNull String url) {
        this.id = id;
        if (copyright == null)
            this.copyright = "";
        else
            this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
    }

    public ApodEntity(@NonNull Image image) {
        this.id = image.getId();
        if (image.getCopyright() == null)
            this.copyright = "";
        else
            this.copyright = image.getCopyright();
        this.date = image.getDate();
        this.explanation = image.getExplanation();
        this.mediaType = image.getMediaType();
        this.title = image.getTitle();
        this.url = image.getUrl();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(copyright);
        parcel.writeString(date);
        parcel.writeString(explanation);
        parcel.writeString(mediaType);
        parcel.writeString(url);
        parcel.writeString(title);
    }

    private ApodEntity(Parcel in) {
        id = in.readInt();
        copyright = in.readString();
        date = in.readString();
        explanation = in.readString();
        mediaType = in.readString();
        url = in.readString();
        title = in.readString();
    }

    public static final Parcelable.Creator<ApodEntity> CREATOR = new Parcelable.Creator<ApodEntity>() {
        @Override
        public ApodEntity createFromParcel(Parcel parcel) {
            return new ApodEntity(parcel);
        }

        @Override
        public ApodEntity[] newArray(int i) {
            return new ApodEntity[i];
        }
    };

}
