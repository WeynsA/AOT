package com.example.arno.homeracer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.view.LayoutInflater;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private CameraPosition mCameraPosition;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    UserData usr = new UserData();

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private final LatLng mDefaultLocation = new LatLng(4, 51);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private final static int LOCATION_REQUEST_CODE = 101;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    Handler handler;
    Button btnStartRace;
    Button btnStopRace;
    Boolean isCounting;
    TextView tvCounter;
    Marker destMarker;
    LatLng startLtLn, endLtLn;

    private Racer racer;

    boolean isRacing;
    boolean isFinished;

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

        handler = new Handler();
        isCounting = false;
        isRacing = false;
        isFinished = false;

        usr = getIntent().getParcelableExtra("userData");
        endLtLn = usr.getEndLtLn();
        startLtLn = usr.getStartLtLn();

        racer = new Racer(startLtLn);

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
        mMap = googleMap;

        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);

        //Settings();
        racer.Draw(mMap, this);

        mMap.addMarker(new MarkerOptions()
                .position(startLtLn)
                .title("Start!")
                .draggable(true));

        mMap.addMarker(new MarkerOptions()
                .position(endLtLn)
                .title("Finish!")
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        final LocationManager locationManager= (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng center1 = new LatLng(location.getLatitude(),location.getLongitude());
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);

                // Prompt the user for permission.
                //getLocationPermission();

                // Turn on the My Location layer and the related control on the map.
                //updateLocationUI();

                if (isCounting && !isRacing) {
                    isRacing = true;

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center1, 13));

                }

                if(isCounting && destMarker != null){
                    Location markerLoc = new Location("Destination");
                    markerLoc.setLatitude(destMarker.getPosition().latitude);
                    //markerLoc.setLatitude(endLtLn.latitude);
                    markerLoc.setLongitude(destMarker.getPosition().longitude);
                    //markerLoc.setLongitude(endLtLn.longitude);
                    float distance = location.distanceTo(markerLoc);
                    Log.d(TAG, "onLocationChanged: " + markerLoc);
                    Log.d("distancevalue", String.valueOf(distance));

                    if(distance<5000 && !isFinished){
                        isFinished = true;
                        Toast.makeText(getApplicationContext(),"You have arrived at your destination",Toast.LENGTH_LONG).show();
                        HighscoreManager.postTime(UpdateTime, usr.getUsername(), MapsActivity.this);
                        Intent i = new Intent(MapsActivity.this, HighScoreActivity.class);
                        i.putExtra("userData", usr);
                        i.putExtra("YourTime",UpdateTime);
                        StopCounter();
                        startActivity(i);
                        finish();
                    }
                    if(distance>100)
                    {
                        Toast.makeText(getApplicationContext(), "not at destination", Toast.LENGTH_LONG).show();
                        Log.d("toast","locations are not the same" + distance);

                    }}
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

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

        racer.Move(endLtLn, racer.marker,30000);
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

    ArrayList<Score> HighScore = new ArrayList<Score>();


    public void HighscoreAdd(Long timeScore ){
        UserData usr = getIntent().getParcelableExtra("userData");
        Boolean sortRace = getIntent().getBooleanExtra("SortRace", false);

        Log.d(TAG, "HighscoreAdd: ADDEDED");
        Score user = new Score(usr.getUsername(), timeScore, sortRace);

        HighScore.add(user);
        addInJSONArray(user);

        int i = 0;
        for (Score _score:HighScore){
            i++;
            Log.d(TAG, "HighscoreAdd"+i+" : " + "username: " +usr.getUsername()+" time: "+ _score.getTime());
        }
    }
    public List<Score> getDataFromSharedPreferences(Context context){
        Gson gson = new Gson();
        List<Score> productFromShared = new ArrayList<>();
        SharedPreferences sharedPref = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString("Score", "");

        Type type = new TypeToken<List<Score>>() {}.getType();
        productFromShared = gson.fromJson(jsonPreferences, type);

        return productFromShared;
    }
    private void addInJSONArray(Score productToAdd){

        Gson gson = new Gson();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        String jsonSaved = sharedPref.getString("Score", "");
        String jsonNewproductToAdd = gson.toJson(productToAdd);

        JSONArray jsonArrayProduct= new JSONArray();

        try {
            if(jsonSaved.length()!=0){
                jsonArrayProduct = new JSONArray(jsonSaved);
            }
            jsonArrayProduct.put(new JSONObject(jsonNewproductToAdd));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //SAVE NEW ARRAY
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Score", String.valueOf(jsonArrayProduct));
        editor.commit();
    }

/*    public LatLng[] Settings(){
        UserData usr = getIntent().getParcelableExtra("userData");
        Boolean sortRace = getIntent().getBooleanExtra("SortRace", false);

        if (sortRace) {
            startLtLn = new LatLng(usr.getStartLat(), usr.getStartLong());
            endLtLn = new LatLng(usr.getEndLat(), usr.getEndLong());
            racer = new Racer(startLtLn);
        }else{
            endLtLn = new LatLng(usr.getStartLat(), usr.getStartLong());
            startLtLn = new LatLng(usr.getEndLat(), usr.getEndLong());
            racer = new Racer(startLtLn);
        }
    }*/
}

