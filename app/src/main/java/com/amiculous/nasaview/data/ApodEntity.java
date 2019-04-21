package com.amiculous.nasaview.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "apod_favorites")
public class ApodEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String copyright;
    private String date;
    private String explanation;
    private String media_type;
    private String title;
    private String url;
    private boolean isFavorite;

    public long getId() {
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

    public String getMedia_type() {
        return media_type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean getIsFavorite() {return isFavorite;}

    //setter required for fields not in constructor with Room
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setId(long id) {this.id = id;}

    @Ignore
    public ApodEntity() {}

    public ApodEntity(String copyright, @NonNull String date, @NonNull String explanation, @NonNull String media_type, @NonNull String title, @NonNull String url) {
        if (copyright == null)
            this.copyright = "";
        else
            this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.media_type = media_type;
        this.title = title;
        this.url = url;
        this.isFavorite = false;
    }

    @Ignore
    public ApodEntity(long id, String copyright, @NonNull String date, @NonNull String explanation, @NonNull String media_type, @NonNull String title, @NonNull String url) {
        this(copyright, date, explanation, media_type, title, url);
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //https://stackoverflow.com/a/7089687
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(copyright);
        parcel.writeString(date);
        parcel.writeString(explanation);
        parcel.writeString(media_type);
        parcel.writeString(url);
        parcel.writeString(title);
        parcel.writeInt(isFavorite ? 1 : 0);
    }

    private ApodEntity(Parcel in) {
        id = in.readInt();
        copyright = in.readString();
        date = in.readString();
        explanation = in.readString();
        media_type = in.readString();
        url = in.readString();
        title = in.readString();
        isFavorite = in.readInt() != 0;
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
