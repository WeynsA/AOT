package com.example.arno.homeracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.arno.homeracer.Objects.Race;
import com.example.arno.homeracer.Objects.UserData;

public class Homescreen extends AppCompatActivity {
    TextView startLong, startLat, userId, userNameTv, endLong, endLat, tvStartStreet, tvEndStreet, tvTourName;
    Button btnRace1, btnRace2, btnHighScore;
    JsonObjectRequest jsonObjectRequest;
    Boolean toHome;
    public static Intent i;
    ConstraintLayout myLayout;
    UserData usr = new UserData();
    Race race = new Race();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        btnRace1 = findViewById(R.id.btnStartRace);
        btnRace2 = findViewById(R.id.btnStartRace2);
        btnHighScore = findViewById(R.id.btnHighScore);
        startLat = findViewById(R.id.tvStartLat);
        startLong = findViewById(R.id.tvStartLong);
        endLat = findViewById(R.id.tvEndLat);
        endLong = findViewById(R.id.tvEndLong);
        userId = findViewById(R.id.tvId);
        userNameTv = findViewById(R.id.tvUserName);
        tvEndStreet = findViewById(R.id.tvEndStreet);
        tvStartStreet = findViewById(R.id.tvStartStreet);
        myLayout = findViewById(R.id.homescreenLayout);
        tvTourName = findViewById(R.id.tvTourName);

        Intent intent = getIntent();
        try {
            usr = intent.getParcelableExtra("userData");
            int _id = usr.getUserId();
            String _username = usr.getUsername();
            Double _startLat = usr.getStartLat();
            Double _startLong = usr.getStartLong();
            Double _endLat = usr.getEndLat();
            Double _endLong = usr.getEndLong();
            String _startStreet = usr.getStartStreetName();
            String _endStreet = usr.getEndStreetName();

            userNameTv.setText("Welcome " + _username + "!");
            startLat.setText("StartLat: " + _startLat);
            startLong.setText("StartLong: " + _startLong);
            endLat.setText("EndLat: " + _endLat);
            endLong.setText("EndLong: " + _endLong);
            tvStartStreet.setText(_startStreet);
            tvEndStreet.setText(_endStreet);

            toHome = false;
            usr.setRace(toHome);

            i = new Intent(Homescreen.this, MapsActivity.class);
            i.putExtra("userData", usr);
        }catch (ClassCastException ex){
            myLayout.removeAllViews();
            myLayout.addView(tvTourName);
            myLayout.addView(btnRace1);
            btnRace1.setText("Start");
            race = intent.getParcelableExtra("userData");
            tvTourName.append(race.getRaceName());
            i = new Intent(Homescreen.this, MapsActivity.class);
            i.putExtra("userData", race);
            i.putExtra("typeRace", "Tour");
        }
        i.putExtra("playerName", getIntent().getStringExtra("playerName"));

        btnRace1.setOnClickListener(StartRace1);
        btnRace2.setOnClickListener(StartRace2);
        btnHighScore.setOnClickListener(StartHighScore);
       }

    private View.OnClickListener StartRace1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            i.putExtra("Finished", false);
            startActivity(i);
        }
    };

    private View.OnClickListener StartRace2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usr.setRace(true);
            i.putExtra("Finished", false);
            startActivity(i);
        }
    };

    private View.OnClickListener StartHighScore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Homescreen.this, HighScoreActivity.class);
            i.putExtra("userData", usr);
            i.putExtra("playerName", getIntent().getStringExtra("playerName"));
            startActivity(i);
        }
    };
}

