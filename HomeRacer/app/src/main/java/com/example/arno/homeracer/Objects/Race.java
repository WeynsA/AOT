package com.example.arno.homeracer.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Race implements  Parcelable{
    private int RaceId;
    private String RaceName;
    private ArrayList<Location> Locations = new ArrayList<Location>();
    private LatLng LocationLtLng;


    public Race(){
    }

    public int getRaceId() {
        return RaceId;
    }

    public void setRaceId(int raceId) {
        RaceId = raceId;
    }

    public String getRaceName() {
        return RaceName;
    }

    public void setRaceName(String raceName) {
        RaceName = raceName;
    }

    public ArrayList<Location> getLocations() {
        return Locations;
    }

    public void setLocations(Location location) {
        this.Locations.add(location);
    }

    public LatLng getLocationLtLng() {
        return LocationLtLng;
    }

    public LatLng getLocationLtLn(double lat, double lng){
        LocationLtLng = new LatLng(lat,lng);

        return  LocationLtLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.RaceId);
        dest.writeString(this.RaceName);
        dest.writeList(this.Locations);
    }

    protected Race(Parcel in) {
        this.RaceId = in.readInt();
        this.RaceName = in.readString();
        this.Locations = in.readArrayList(Location.class.getClassLoader());
    }

    public static final Creator<Race> CREATOR = new Creator<Race>() {
        @Override
        public Race createFromParcel(Parcel source) {
            return new Race(source);
        }

        @Override
        public Race[] newArray(int size) {
            return new Race[size];
        }
    };
}
