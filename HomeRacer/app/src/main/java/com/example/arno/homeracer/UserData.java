package com.example.arno.homeracer;

import android.location.Location;

import com.android.volley.toolbox.StringRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Serializable{
    int UserId;
    double EndLat, EndLong, StartLat, StartLong;
    String Username;
    /*public Userdata(){

        }

    public Userdata(String username, double endLat, double endLong, double startLat
                    ,double startLong, int userId){
            this.Username = username;
            this.UserId = userId;
            this.StartLat = startLat;
            this.StartLong = startLong;
            this.EndLat = endLat;
            this.EndLong = endLong;

        }*/

    public void setUserId(int _userId){
        this.UserId = _userId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setEndLat(double _endLat){
        this.EndLat = _endLat;
    }

    public double getEndLat() {
        return EndLat;
    }

    public void setStartLat(double _startLat){
        this.StartLat = _startLat;
    }

    public double getStartLat() {
        return StartLat;
    }

    public void setEndLong(double _endLong){
        this.EndLong = _endLong;
    }

    public double getEndLong() {
        return EndLong;
    }

    public void setStartLong(double _startLong){
        this.StartLong = _startLong;
    }

    public double getStartLong() {
        return StartLong;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getUsername() {
        return Username;
    }
}
