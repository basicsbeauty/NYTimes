package com.enlaps.m.and.nytimes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vsatish on 2/12/2016.
 */
public class NewsArticle implements Parcelable {
    public String url;
    public String title;
    public String thumbnail_url;

    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.thumbnail_url);
    }

    public NewsArticle() {
    }

    protected NewsArticle(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.thumbnail_url = in.readString();
    }

    public static final Parcelable.Creator<NewsArticle> CREATOR = new Parcelable.Creator<NewsArticle>() {
        public NewsArticle createFromParcel(Parcel source) {
            return new NewsArticle(source);
        }

        public NewsArticle[] newArray(int size) {
            return new NewsArticle[size];
        }
    };
}
