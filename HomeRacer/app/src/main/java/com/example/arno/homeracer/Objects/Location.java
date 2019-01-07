package com.example.arno.homeracer.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Location implements Parcelable{
    private int LocId;
    private String LocName;
    private Double LocLong, LocLat;
    private LatLng LocationLtLng;

    public Location(){
    }

    public void setLocId(int locId) {
        LocId = locId;
    }

    public void setLocName(String locName) {
        LocName = locName;
    }

    public void setLocLong(Double locLong) {
        LocLong = locLong;
    }

    public void setLocLat(Double locLat) {
        LocLat = locLat;
    }

    public void setLocationLtLng(LatLng locationLtLng) {
        LocationLtLng = locationLtLng;
    }

    public int getLocId() {
        return LocId;
    }

    public String getLocName() {
        return LocName;
    }

    public Double getLocLong() {
        return LocLong;
    }

    public Double getLocLat() {
        return LocLat;
    }

    public LatLng getLocationLtLng() {
        return LocationLtLng;
    }

    public LatLng getLocationLtLn(){
        LocationLtLng = new LatLng(LocLat,LocLong);

        return  LocationLtLng;
    }

    public Location(Parcel in) {
        LocId = in.readInt();
        LocName = in.readString();
        LocLat = in.readDouble();
        LocLong = in.readDouble();
        LocationLtLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(LocId);
        parcel.writeString(LocName);
        parcel.writeDouble(LocLat);
        parcel.writeDouble(LocLong);
        parcel.writeParcelable(this.LocationLtLng, i);
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>(){
        @Override
        public Location createFromParcel(Parcel parcel) {
            return new Location(parcel);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
