package com.example.arno.homeracer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    TextView tvHighscore, tvTijden, tvRoutes, tvYourScore, tvHeader, tvPlayerName;
    Button btnClear;
    HighscoreManager highscoreManager = new HighscoreManager();
    List<String> myHighsScore;
    UserData usr = new UserData();
    Race race = new Race();

    String highScoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        tvHighscore = findViewById(R.id.tvHighScore);
        tvRoutes = findViewById(R.id.tvRoutes);
        tvTijden = findViewById(R.id.tvTijden);
        btnClear = findViewById(R.id.btnClear);
        tvYourScore = findViewById(R.id.tvYourTime);
        tvHeader = findViewById(R.id.tvTijd);
        tvPlayerName = findViewById(R.id.tvPlayerName);

        try{
            usr = getIntent().getParcelableExtra("userData");
            highScoreName = usr.getUsername();
        }catch (ClassCastException ex){
            race = getIntent().getParcelableExtra("userData");
            highScoreName = race.getRaceName();
        }
        tvHeader.append(highScoreName);
        String playerName = getIntent().getStringExtra("playerName");
        tvPlayerName.setText(playerName + ", your race stats:");


        //Making table scrollable
        ScrollingMovementMethod letsgo = new ScrollingMovementMethod();
        tvTijden.setMovementMethod(letsgo);

        final Float yourTime = (getIntent().getFloatExtra("YourTime", 0));
        //Displaying the achieved time
        Log.d("TimeInSec", "onCreate: " + yourTime);
        tvYourScore.append(highscoreManager.ConverTime(yourTime));

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(HighScoreActivity.this, "12")
                .setSmallIcon(R.drawable.biker_up)
                .setContentTitle("Highscore!")
                .setContentText("You have just set a new highscore on HomeRacer!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        btnClear.setOnClickListener(ClearHighScore);
        //Displaying highscores
        highscoreManager.GetHighscore(highScoreName, HighScoreActivity.this, new ServerCallback() {
            @Override
            public void onSuccess() {
               myHighsScore = new ArrayList<String>(Arrays.asList(HighscoreManager.getString().split(",")));
                int i = 0;
                try {
                    myHighsScore.remove(0);
                    myHighsScore.remove(myHighsScore.size()-1);

                    for (String item : myHighsScore) {
                        i++;
                        float time = Float.parseFloat(item);

                        String index = String.valueOf(i);
                        String tijdAdd = HighscoreManager.ConverTime(time);
                        tvTijden.append(index + ".     " + tijdAdd + "\n");

                        if (yourTime == time && i==1){
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(HighScoreActivity.this);
                            notificationManager.notify(i, mBuilder.build());
                        }
                            //Toast.makeText(HighScoreActivity.this, "You have set a new highscore!", Toast.LENGTH_SHORT).show();
                        else if (yourTime == time)
                            tvYourScore.append("\n"+"Your place: " + String.valueOf(i));
                    }
                }catch (ArrayIndexOutOfBoundsException ex)
                {
                    tvTijden.setText("You're the first one to complete this tour!");
                }
            }
        });
    }
    //Clearing local highscore file --NOT IN USE--
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
