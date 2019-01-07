package com.example.arno.homeracer.Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class UserData implements Parcelable {
    private int UserId;
    private String Username, StartStreetName, EndStreetName;
    private Double StartLat, StartLong, EndLat, EndLong;
    private LatLng StartLtLn, EndLtLn;
    private Boolean toHome;

    public UserData(){
    }
    public void setUserId(int _userId) {
        this.UserId = _userId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setEndLat(Double _endLat) {
        this.EndLat = _endLat;
    }

    public Double getEndLat() {
        return EndLat;
    }

    public void setEndStreetName(String endStreetName) {
        this.EndStreetName = endStreetName;
    }

    public String getEndStreetName() {

        return EndStreetName;
    }

    public void setRace(Boolean home) {
        this.toHome = home;
    }

    public Boolean getRace() {
        return toHome;
    }

    public void setStartStreetName(String startStreetName) {

        this.StartStreetName = startStreetName;
    }

    public String getStartStreetName() {

        return StartStreetName;
    }

    public void setStartLat(Double _startLat) {
        this.StartLat = _startLat;
    }

    public Double getStartLat() {
        return StartLat;
    }

    public void setEndLong(Double _endLong) {
        this.EndLong = _endLong;
    }

    public Double getEndLong() {
        return EndLong;
    }

    public LatLng getStartLtLn(){
        if (toHome)
            StartLtLn = new LatLng(StartLat, StartLong);
        else StartLtLn = new LatLng(EndLat, EndLong);
        return StartLtLn;
    }

    public LatLng getEndLtLn(){
        if (toHome)
            EndLtLn = new LatLng(EndLat,EndLong);
        else EndLtLn = new LatLng(StartLat, StartLong);
        return  EndLtLn;
    }

    public void setStartLong(Double _startLong) {
        this.StartLong = _startLong;
    }

    public Double getStartLong() {
        return StartLong;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public UserData(Parcel in) {
        UserId = in.readInt();
        Username = in.readString();
        StartLat = in.readDouble();
        StartLong = in.readDouble();
        EndLat = in.readDouble();
        EndLong = in.readDouble();
        StartStreetName = in.readString();
        EndStreetName = in.readString();
        StartLtLn = in.readParcelable(LatLng.class.getClassLoader());
        EndLtLn = in.readParcelable(LatLng.class.getClassLoader());
        toHome = (in.readInt() == 0)?false:true;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(UserId);
        parcel.writeString(Username);
        parcel.writeDouble(StartLat);
        parcel.writeDouble(StartLong);
        parcel.writeDouble(EndLat);
        parcel.writeDouble(EndLong);
        parcel.writeString(StartStreetName);
        parcel.writeString(EndStreetName);
        parcel.writeParcelable(this.StartLtLn, i);
        parcel.writeParcelable(this.EndLtLn, i);
        parcel.writeInt(toHome ? 1:0);
    }

    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>(){
        @Override
        public UserData createFromParcel(Parcel parcel) {
            return new UserData(parcel);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };


}