package com.example.arno.homeracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.example.arno.homeracer.LatLngInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Racer {
        LatLng initLoc;
        public Marker marker;
        Handler handler;

        int height = 30;
        int width = 30;

        int speed = 10;



        public Racer(LatLng location) {
            initLoc = location;
        }


        public void Draw(GoogleMap mMap, Context context) {

            marker = mMap.addMarker(new MarkerOptions()
                    .position(initLoc)
                    .title("Racer!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.biker_up)));
        }

        public void Move(LatLng dest, Marker mrkr, long time) {
            Log.d("Movement", "MarkerLocation: " + mrkr.getPosition());
            MarkerAnimation.animateMarkerToGB(marker, dest, new LatLngInterpolator.Spherical(), time);
        }

        public LatLng getLocation() {
            return marker.getPosition();
        }
    }
