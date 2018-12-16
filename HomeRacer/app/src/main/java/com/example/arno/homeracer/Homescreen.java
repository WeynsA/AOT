package com.example.arno.homeracer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Homescreen extends AppCompatActivity {
    TextView startLong, startLat, userId, userNameTv, endLong, endLat;
    Button btnRace1, btnRace2;
    JsonObjectRequest jsonObjectRequest;
    public static Intent i;

    // public UserData currentUser = new UserData();
    RequestQueue mRequestQueue;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        btnRace1 = findViewById(R.id.btnStartRace);
        btnRace2 = findViewById(R.id.btnStartRace2);
        startLat = findViewById(R.id.tvStartLat);
        startLong = findViewById(R.id.tvStartLong);
        endLat = findViewById(R.id.tvEndLat);
        endLong = findViewById(R.id.tvEndLong);
        userId = findViewById(R.id.tvId);
        userNameTv = findViewById(R.id.tvUserName);

        Intent intent = getIntent();
        final UserData usr = intent.getParcelableExtra("userToHome");
        int _id = usr.getUserId();
        String _username = usr.getUsername();
        Double _startLat = usr.getStartLat();
        Double _startLong = usr.getStartLong();
        Double _endLat = usr.getEndLat();
        Double _endLong = usr.getEndLong();

        userNameTv.setText("Welcome: " + _username);
        startLat.setText("StartLat: " + _startLat);
        startLong.setText("StartLong: " + _startLong);
        endLat.setText("EndLat: " + _endLat);
        endLong.setText("EndLong: " + _endLong);

        i = new Intent(Homescreen.this, MapsActivity.class);
        i.putExtra("DataToMaps", usr);

        btnRace1.setOnClickListener(StartRace1);
    }
    String haha;

    private View.OnClickListener StartRace1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent i = new Intent(Homescreen.this, MapsActivity.class);
            i.putExtra("SortRace", true);
            startActivity(i);
        }
    };
}

