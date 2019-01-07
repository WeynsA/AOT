package com.example.arno.homeracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.arno.homeracer.Objects.Race;
import com.example.arno.homeracer.Objects.UserData;

public class Homescreen extends AppCompatActivity {
    TextView startLong, startLat, userId, userNameTv, endLong, endLat, tvStartStreet, tvEndStreet, tvYourScore;
    Button btnRace1, btnRace2, btnHighScore;
    JsonObjectRequest jsonObjectRequest;
    Boolean toHome;
    public static Intent i;

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

        Intent intent = getIntent();
        try {
            final UserData race = intent.getParcelableExtra("userData");
            int _id = race.getUserId();
            String _username = race.getUsername();
            Double _startLat = race.getStartLat();
            Double _startLong = race.getStartLong();
            Double _endLat = race.getEndLat();
            Double _endLong = race.getEndLong();
            String _startStreet = race.getStartStreetName();
            String _endStreet = race.getEndStreetName();

            userNameTv.setText("Welcome " + _username + "!");
            startLat.setText("StartLat: " + _startLat);
            startLong.setText("StartLong: " + _startLong);
            endLat.setText("EndLat: " + _endLat);
            endLong.setText("EndLong: " + _endLong);
            tvStartStreet.setText(_startStreet);
            tvEndStreet.setText(_endStreet);

            toHome = false;
            race.setRace(toHome);

            i = new Intent(Homescreen.this, MapsActivity.class);
            i.putExtra("userData", race);
        }catch (ClassCastException ex){
            final Race race = intent.getParcelableExtra("userData");
            userNameTv.setText("Your about to start race: " + race.getRaceName() + "!");

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
            final UserData usr = getIntent().getParcelableExtra("userData");
            usr.setRace(true);
            i.putExtra("Finished", false);
            startActivity(i);
        }
    };

    private View.OnClickListener StartHighScore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Homescreen.this, HighScoreActivity.class);
            startActivity(i);
        }
    };
}

