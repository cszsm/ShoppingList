package com.zscseh93.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by zscse on 2016. 05. 13..
 */
public class Item extends SugarRecord implements Parcelable {

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

    private String mItemId;
    private String mName;

    private static int sLastId = 0;

    public Item() {
    }

    public Item(String name) {
        // TODO: megnézni ez így jó-e
        mItemId = "id_" + sLastId++;
        mName = name;
    }

    public Item(Parcel parcel) {
        mItemId = parcel.readString();
        mName = parcel.readString();
    }


    public String getItemId() {
        return mItemId;
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
        dest.writeString(mItemId);
        dest.writeString(mName);
    }
}
