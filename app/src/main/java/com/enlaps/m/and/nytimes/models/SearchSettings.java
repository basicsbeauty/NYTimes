package com.enlaps.m.and.nytimes.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Atom on 2/14/16.
 */
public class SearchSettings implements Parcelable {

    public enum ArticleOrder {
        OLDEST,
        NEWEST,
    }

    public enum NewsDesk {
        ARTS,
        FASHION_STYLE,
        SPORTS,
    }

    public Date                 endDate;
    public ArticleOrder         order;
    public ArrayList<NewsDesk>  newsDesk;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(endDate != null ? endDate.getTime() : -1);
        dest.writeInt(this.order == null ? -1 : this.order.ordinal());
        dest.writeList(this.newsDesk);
    }

    public SearchSettings() {
        endDate = new Date();
        newsDesk = new ArrayList<>();
        order = ArticleOrder.NEWEST;
    }

    protected SearchSettings(Parcel in) {
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        int tmpOrder = in.readInt();
        this.order = tmpOrder == -1 ? null : ArticleOrder.values()[tmpOrder];
        this.newsDesk = new ArrayList<NewsDesk>();
        in.readList(this.newsDesk, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchSettings> CREATOR = new Parcelable.Creator<SearchSettings>() {
        public SearchSettings createFromParcel(Parcel source) {
            return new SearchSettings(source);
        }

        public SearchSettings[] newArray(int size) {
            return new SearchSettings[size];
        }
    };
}
