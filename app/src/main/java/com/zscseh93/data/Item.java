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
    private String mPlaceId = null;
    private String mPlaceName = null;
    private String mPlaceAddress = null;

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
        mPlaceId = parcel.readString();
        mPlaceName = parcel.readString();
        mPlaceAddress = parcel.readString();
    }


    public String getItemId() {
        return mItemId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public String getPlaceAddress() {
        return mPlaceAddress;
    }

    public void setPlace(String placeId, String placeName, String placeAddress) {
        mPlaceId = placeId;
        mPlaceName = placeName;
        mPlaceAddress = placeAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mItemId);
        dest.writeString(mName);
        dest.writeString(mPlaceId);
        dest.writeString(mPlaceName);
        dest.writeString(mPlaceAddress);
    }
}
