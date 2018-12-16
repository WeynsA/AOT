package com.example.arno.homeracer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.Image;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.SystemClock;


import com.facebook.places.Places;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private CameraPosition mCameraPosition;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    Handler handler;
    Button btnStartRace;
    Button btnStopRace;
    Boolean isCounting;
    TextView tvCounter;

    int Seconds, Minutes, MilliSeconds;
    long MilliSecondTime, StartTime, TimeBuff, UpdateTime = 0L;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        isCounting = false;
        handler = new Handler();

        tvCounter = findViewById(R.id.tvCounter);
        btnStartRace = findViewById(R.id.btnStartMap);
        btnStopRace = findViewById(R.id.btnStopMap);
        btnStartRace.setOnClickListener(StartClick);
        btnStopRace.setOnClickListener(StopClick);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        UserData usr = getIntent().getParcelableExtra("DataToMaps");
        Boolean sortRace = getIntent().getBooleanExtra("SortRace", false);

        Double latitude = 0.0;
        Double longitude = 0.0;
        if (sortRace) {
            //Toast.makeText(getApplicationContext() ,"Button clicked!", Toast.LENGTH_LONG).show();
            latitude = usr.getStartLat();
            longitude = usr.getStartLong();
        }else{
            latitude = usr.getEndLat();
            longitude = usr.getEndLong();
        }

        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
        LatLng finish = new LatLng(latitude, longitude);
        Log.d(TAG, "onMapReady: " + finish);
        mMap.addMarker(new MarkerOptions()
                    .position(finish)
                    .title("Finish!"));
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();

            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private View.OnClickListener StartClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isCounting){
                isCounting = true;
                StartCounter();
                btnStartRace.setText("Pauze");
                btnStopRace.setVisibility(View.VISIBLE);
            }else{
                isCounting = false;
                PauzeCounter();
                btnStartRace.setText("Start");
            }
        }
    };

    private View.OnClickListener StopClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PauzeCounter();
            btnStopRace.setVisibility(View.INVISIBLE);
            btnStartRace.setText("Start");
            isCounting = false;
            StopCounter();
            }
    };

    public void StartCounter(){
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }

    public void PauzeCounter(){
        TimeBuff += MilliSecondTime;

        handler.removeCallbacks(runnable);
    }

    public void StopCounter(){
        HighscoreAdd(UpdateTime);
        MilliSecondTime = 0L ;
        StartTime = 0L ;
        TimeBuff = 0L ;
        UpdateTime = 0L ;
        Seconds = 0 ;
        Minutes = 0 ;
        MilliSeconds = 0 ;

        tvCounter.setText("00:00:00");
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MilliSecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MilliSecondTime;

            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            tvCounter.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }
    };

    ArrayList<Score> HighScore = new ArrayList<Score>(3);

    public void HighscoreAdd(Long timeScore ){
        UserData usr = getIntent().getParcelableExtra("DataToMaps");
        Boolean sortRace = getIntent().getBooleanExtra("SortRace", false);

        Log.d(TAG, "HighscoreAdd: ADDEDED");
        Score user = new Score(usr.getUsername(), timeScore, sortRace);

        HighScore.add(user);

        Collections.sort(HighScore, new Comparator<Score>() {
            public int compare (Score s1, Score s2){
                return Long.compare(s1.getTime(), s2.getTime());
            }
        });

        int i = 0;
        for (Score _score:HighScore){
            i++;
            Log.d(TAG, "HighscoreAdd"+i+" : " + "username: " +usr.getUsername()+" time: "+ _score.getTime());
        }
    }

   /* public void saveArrayList(ArrayList<Score> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }*/
}

