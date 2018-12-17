package com.example.arno.homeracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

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
        endLat = findViewById(R.id.tvStartLong);
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
        btnRace2.setOnClickListener(StartRace2);
       }


    private View.OnClickListener StartRace1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            i.putExtra("SortRace", true);
            startActivity(i);
        }
    };

    private View.OnClickListener StartRace2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            i.putExtra("SortRace", false);
            startActivity(i);
        }
    };

}

