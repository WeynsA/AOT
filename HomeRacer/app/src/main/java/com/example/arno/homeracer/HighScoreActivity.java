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
import android.widget.Toast;

import com.example.arno.homeracer.Helpers.HighscoreManager;
import com.example.arno.homeracer.Helpers.ServerCallback;
import com.example.arno.homeracer.Objects.Race;
import com.example.arno.homeracer.Objects.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {
    TextView tvHighscore, tvTijden, tvRoutes, tvYourScore;
    Button btnClear;
    HighscoreManager highscoreManager = new HighscoreManager();
    List<String> myHighsScore;
    UserData usr = new UserData();
    Race race = new Race();

    float hours, minutes, seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        try{
            usr = getIntent().getParcelableExtra("userData");
        }catch (ClassCastException ex){
            race = getIntent().getParcelableExtra("userData");
        }
        String playerName = getIntent().getStringExtra("playerName");

        tvHighscore = findViewById(R.id.tvHighScore);
        tvRoutes = findViewById(R.id.tvRoutes);
        tvTijden = findViewById(R.id.tvTijden);
        btnClear = findViewById(R.id.btnClear);
        tvYourScore = findViewById(R.id.tvYourTime);

        ScrollingMovementMethod letsgo = new ScrollingMovementMethod();

        tvRoutes.setMovementMethod(letsgo);
        tvTijden.setMovementMethod(letsgo);

        final Float yourTime = (getIntent().getFloatExtra("YourTime", 0)/1000);
        //Integer yourTimeInt = Integer.parseInt(yourTime.toString());
        Log.d("TimeInSec", "onCreate: " + yourTime);
        minutes = yourTime/60;
        seconds = yourTime;

        //String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        if (minutes > 1)
            tvYourScore.append(String.format("%2.0f", minutes) + "min " + String.format("%2.0f", seconds) + "sec." + "\n");
        else
            tvYourScore.append(String.format("%2.3f", seconds) + "sec.");
        //tvYourScore.setText(timeString);

        btnClear.setOnClickListener(ClearHighScore);

        String highscore;
        highscoreManager.GetHighscore(race.getRaceName(), HighScoreActivity.this, new ServerCallback() {
            @Override
            public void onSuccess() {
               myHighsScore = new ArrayList<String>(Arrays.asList(HighscoreManager.getString().split(",")));
                int i = 0;
                try {

                    myHighsScore.remove(0);
                    myHighsScore.remove(myHighsScore.size()-1);

                    for (String item : myHighsScore) {
                        i++;
                        float time = Float.parseFloat(item)/1000;

                        String index = String.valueOf(i);
                        String tijdAdd = String.format("%.2f",time);
                        tvTijden.append(index + ". " + tijdAdd + "\n");

                        if (yourTime == time && i==1)
                            Toast.makeText(HighScoreActivity.this, "You have set a new highscore!", Toast.LENGTH_SHORT).show();
                        else if (yourTime == time)
                            tvYourScore.append("Your place: " + String.valueOf(i));

                    }
                }catch (ArrayIndexOutOfBoundsException ex)
                {
                    tvTijden.setText("You're the first one to complete this tour!");
                }
            }
        });
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
