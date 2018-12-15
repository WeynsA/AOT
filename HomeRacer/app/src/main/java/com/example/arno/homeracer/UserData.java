package com.example.arno.homeracer;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;

import com.android.volley.toolbox.StringRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Parcelable {
    private int UserId;
    private String Username;
    private Double StartLat, StartLong, EndLat, EndLong;

    public UserData(){
        //empty constructor
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