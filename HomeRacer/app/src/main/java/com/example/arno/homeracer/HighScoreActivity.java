package com.example.arno.homeracer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {
    TextView tvHighscore, tvTijden, tvRoutes, tvYourScore;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        tvHighscore = findViewById(R.id.tvHighScore);
        tvRoutes = findViewById(R.id.tvRoutes);
        tvTijden = findViewById(R.id.tvTijden);
        btnClear = findViewById(R.id.btnClear);
        tvYourScore = findViewById(R.id.tvYourTime);

        ScrollingMovementMethod letsgo = new ScrollingMovementMethod();

        tvRoutes.setMovementMethod(letsgo);
        tvTijden.setMovementMethod(letsgo);

        Long yourTime = getIntent().getLongExtra("YourTime", 0);
        Log.d("HAHA", "onCreate: " + yourTime);
        tvYourScore.setText("Your time: " + String.valueOf(yourTime/1000) + " sec");

        btnClear.setOnClickListener(ClearHighScore);


        MapsActivity mapsActivity = new MapsActivity();
        List<Score> list = mapsActivity.getDataFromSharedPreferences( HighScoreActivity.this);

        Collections.sort(list, new Comparator<Score>() {
            public int compare (Score s1, Score s2){
                return Long.compare(s1.getTime(), s2.getTime());
            }
        });

            int i = 0;
            for (Score _scores : list) {
                i++;
                String userAdd = i + ". " + _scores.getUsernameHS();
                String tijdAdd = String.valueOf(_scores.getTime()/1000);
                //Log.d("PLZWORK", "HighscoreAdd" + i + " : " + "username: " + _scores.getUsernameHS() + " time: " + _scores.getTime());
                tvRoutes.setText(tvRoutes.getText() + userAdd + "\n");
                tvTijden.setText(tvTijden.getText() + tijdAdd + "\n");
            }
    }
    private View.OnClickListener ClearHighScore = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Score", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();
            SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPref1.edit();
            editor1.clear();
            editor1.apply();
        }
    };
}
