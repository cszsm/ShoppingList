package com.zscseh93.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
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

    private String mName;
    private int mQuantity;
    private int mPrice;
    private String mPlaceName = null;
    private String mPlaceAddress = null;
    private double mLat;
    private double mLng;
    private String mPhotoFileName;

    public Item() {
    }

    public Item(String name) {
        mName = name;
    }

    public Item(Parcel parcel) {
        mName = parcel.readString();
        mQuantity = parcel.readInt();
        mPrice = parcel.readInt();
        mPlaceName = parcel.readString();
        mPlaceAddress = parcel.readString();
        mLat = parcel.readDouble();
        mLng = parcel.readDouble();
        mPhotoFileName = parcel.readString();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public String getPlaceAddress() {
        return mPlaceAddress;
    }

    public LatLng getPlaceLatLng() {
        return new LatLng(mLat, mLng);
    }

    public void setPhotoFileName(String photoFileName) {
        mPhotoFileName = photoFileName;
    }

    public String getPhotoFileName() {
        return mPhotoFileName;
    }

    public void setPlace(String placeName, String placeAddress, LatLng placeLatLng) {
        mPlaceName = placeName;
        mPlaceAddress = placeAddress;
        mLat = placeLatLng.latitude;
        mLng = placeLatLng.longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mQuantity);
        dest.writeInt(mPrice);
        dest.writeString(mPlaceName);
        dest.writeString(mPlaceAddress);
        dest.writeDouble(mLat);
        dest.writeDouble(mLng);
        dest.writeString(mPhotoFileName);
    }
}
