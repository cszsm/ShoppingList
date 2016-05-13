package com.zscseh93.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zscse on 2016. 05. 13..
 */
public class Item implements Parcelable {

    public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private String mName;

    public Item(String name) {
        mName = name;
    }

    public Item(Parcel parcel) {
        mName = parcel.readString();
    }

    public String getName() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
    }
}
