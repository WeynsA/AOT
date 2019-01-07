package com.example.arno.homeracer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arno.homeracer.Helpers.HighscoreManager;
import com.example.arno.homeracer.Helpers.ServerCallback;
import com.example.arno.homeracer.Objects.Race;
import com.example.arno.homeracer.Objects.Racer;
import com.example.arno.homeracer.Objects.Score;
import com.example.arno.homeracer.Objects.UserData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private CameraPosition mCameraPosition;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private final LatLng mDefaultLocation = new LatLng(4, 51);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private final static int LOCATION_REQUEST_CODE = 101;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    UserData usr = new UserData();
    Race race = new Race();
    List<com.example.arno.homeracer.Objects.Location> raceLocations;

    Handler handler;
    Button btnStartRace, btnStopRace;
    Boolean isCounting;
    TextView tvCounter;
    Marker destMarker, checkPoint;
    LatLng startLtLn, checkPntLtLn, finishLtLn;
    Racer racer;
    Intent i;

    boolean isRacing, isFinished, isTour;
    int Seconds, Minutes, MilliSeconds, index, finishIndex;
    long MilliSecondTime, StartTime, TimeBuff, UpdateTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        index = 0;

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        handler = new Handler();
        isCounting = false;
        isRacing = false;
        isFinished = false;

        try {
            usr = getIntent().getParcelableExtra("userData");
            finishLtLn = usr.getEndLtLn();
            startLtLn = usr.getStartLtLn();
            isTour = false;
        } catch (ClassCastException ex) {
            isTour = true;
            race = getIntent().getParcelableExtra("userData");
            raceLocations = race.getLocations();
            finishIndex = raceLocations.size() - 1;
            finishLtLn = raceLocations.get(finishIndex).getLocationLtLn();
            startLtLn = NextLocation();
            checkPntLtLn = NextLocation();
        }

        tvCounter = findViewById(R.id.tvCounter);
        btnStartRace = findViewById(R.id.btnStartMap);
        btnStopRace = findViewById(R.id.btnStopMap);
        btnStartRace.setOnClickListener(StartClick);
        btnStopRace.setOnClickListener(StopClick);

        i = new Intent(MapsActivity.this, HighScoreActivity.class);
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
        mMap = googleMap;

        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);

        if (isTour) {
            checkPoint =
                    mMap.addMarker(new MarkerOptions()
                            .position(checkPntLtLn)
                            .title("CheckPoint")
                            .draggable(true));
        }

        mMap.addMarker(new MarkerOptions()
                .position(startLtLn)
                .title("Start!")
                .draggable(true));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLtLn, 13));

        mMap.addMarker(new MarkerOptions()
                .position(finishLtLn)
                .title("Finish!")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng center1 = new LatLng(location.getLatitude(), location.getLongitude());
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);

                if (isCounting && !isRacing) {
                    isRacing = true;

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center1, 14));
                }

                if (isCounting && destMarker != null) {
                    Location markerLoc = new Location("Destination");
                    markerLoc.setLatitude(destMarker.getPosition().latitude);
                    markerLoc.setLongitude(destMarker.getPosition().longitude);

                    float distance = location.distanceTo(markerLoc);

                    String destMarkerTitle = destMarker.getTitle();
                    if (distance < 5000 && index != finishIndex && destMarkerTitle.contains("CheckPoint")) {
                        checkPoint.remove();
                        checkPoint =
                                mMap.addMarker(new MarkerOptions()
                                        .draggable(true)
                                        .position(NextLocation())
                                        .title("CheckPoint"));
                    } else if (distance < 5000 && index == finishIndex && !isFinished && destMarkerTitle.contains("Finish")) {
                        isFinished = true;
                        Toast.makeText(getApplicationContext(), "You have arrived at your destination", Toast.LENGTH_LONG).show();

                        i.putExtra("YourTime", Float.valueOf(UpdateTime));
                        i.putExtra("playerName", getIntent().getStringExtra("playerName"));

                        PostTime();
                        StopCounter();
                        startActivity(i);
                        finish();
                    } else {
                        //Toast.makeText(getApplicationContext(), "not at destination", Toast.LENGTH_LONG).show();
                        Log.d("toast", "locations are not the same" + distance);
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                destMarker = marker;
                marker.showInfoWindow();

            }
        });
    }

    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this,
                permissionType);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permissionType}, requestCode
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private View.OnClickListener StartClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isCounting) {
                isCounting = true;
                StartCounter();
                btnStartRace.setText("Pauze");
                btnStopRace.setVisibility(View.VISIBLE);
            } else {
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

    public void StartCounter() {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        //racer.Move(checkPntLtLn, racer.marker, 30000);
    }

    public void PauzeCounter() {
        TimeBuff += MilliSecondTime;

        handler.removeCallbacks(runnable);
    }

    public void StopCounter() {
        MilliSecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;

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

    public LatLng NextLocation() {
        LatLng _loc = raceLocations.get(index).getLocationLtLn();
        index++;
        return _loc;
    }

    public void PostTime() {
        if (isTour) {
            HighscoreManager.postTime(UpdateTime, race.getRaceName(), MapsActivity.this);
            i.putExtra("userData", race);

        } else {
            HighscoreManager.postTime(UpdateTime, usr.getUsername(), MapsActivity.this);
            i.putExtra("userData", usr);
        }
    }
}

