package com.example.arno.homeracer;

import android.content.Context;
import android.graphics.Bitmap;
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
        private int id;
        LatLng initLoc;
        public Marker marker;
        List<Step> steps = new ArrayList<>();
        private int iter;
        Handler handler;
        //ApiHelper apiHelper;
        int speed = 10;

        Boolean newDirections = false;

        public Racer(LatLng location) {
            initLoc = location;
        }

        public int setColor() {
            return 1;
        }
        // we moeten hier waarschijnlijk ook een soort van AI programmeren anders gaan de spookjes
        // op 1 rechte lijn terecht komen achter de speler.

       /* public void getSteps(LatLng destination) {
            steps = new ArrayList<>();
            String baseUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + marker.getPosition().latitude + "," + marker.getPosition().longitude + "&destination=" + destination.latitude + "," + destination.longitude + "&mode=walking&key=" + BuildConfig.GoogleSecAPIKEY;
            Log.d("Steps", baseUrl);
            final JSONDeserializer jsonDeserializer = new JSONDeserializer();
            apiHelper.get(baseUrl, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    steps = jsonDeserializer.getSteps(apiHelper.getJsonObject());
                    newDirections = true;
                    marker.setPosition(steps.get(0).start);
                    Log.d("Movement", "newdirections: " + newDirections);

                    FollowPath();
                }
            });

        }*/

    public void Draw(GoogleMap mMap, Context context) {
        int height = 80;
        int width = 80;
        marker = mMap.addMarker(new MarkerOptions()
                .position(initLoc));
    }


       /* public void FollowPath() {
            handler = new Handler();
            final long start = SystemClock.uptimeMillis();


            handler.post(new Runnable() {
                long elapsed;
                long time = 30000;

                @Override
                public void run() {
                    elapsed = SystemClock.uptimeMillis() - start;
                    Log.d("Movement", "Moving to point: " + iter);
                    time = (steps.get(0).distance * 1000)/(speed);
                    Log.d("Movement" ,"Step: " + steps.get(0));
                    Log.d("Movement", "Time for this step: " + time);
                    Move(steps.get(1).start, marker, time);
                    iter++;
                    steps.remove(0);
                    Log.d("Movement", "Steps to go = " + steps.size());
                    if (1 < steps.size()) {
                        handler.postDelayed(this, time + 500);
                    }
                }
            });
        }*/

        public void Move(LatLng dest, Marker mrkr, long time) {
            Log.d("Movement", "MarkerLocation: " + mrkr.getPosition());
            MarkerAnimation.animateMarkerToGB(marker, dest, new LatLngInterpolator.Spherical(), time);



        }

        public LatLng getLocation() {
            return marker.getPosition();
        }
    }
