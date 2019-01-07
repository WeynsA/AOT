package com.example.arno.homeracer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arno.homeracer.Objects.Location;
import com.example.arno.homeracer.Objects.Race;
import com.example.arno.homeracer.Objects.Score;
import com.example.arno.homeracer.Objects.UserData;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Login extends AppCompatActivity {

    private Button btnStart, btnDev;
    private TextView tvRegister;
    private EditText etUser, etPassword;
    public ProgressBar spinner;

    UserData usr = new UserData();
    Race race = new Race();

    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;

    private String raceName, playerName;
    private Boolean isTour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnStart = findViewById(R.id.btnLogin);
        btnDev = findViewById(R.id.btnDev);
        spinner = findViewById(R.id.spinner);
        tvRegister = findViewById(R.id.tvRegister);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);

        isTour = false;

        btnStart.setOnClickListener(UserLogin);
        btnDev.setOnClickListener(DevOption);
        tvRegister.setOnClickListener(ToRegister);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "HomeRacerChannel";
            String description = "Channel for pushing notification from HomeRacerApplication.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("12", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private View.OnClickListener UserLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            raceName = etUser.getText().toString();
            playerName = etPassword.getText().toString();

            if (isTour)
                GetTourData();
            else if (!isTour)
                GetUserData();
            else {
                Toast.makeText(Login.this, "Please select what type of race you selected.", Toast.LENGTH_SHORT).show();
                SwapLayout();
            }
            SwapLayout();
        }
    };

    private View.OnClickListener DevOption = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener ToRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GoToRegister();
        }
    };

    private void GoToRegister() {
        Intent i = new Intent(Login.this, RegisterActivity.class);
        startActivity(i);
    }

    private void SwapLayout() {
        if (btnStart.isShown()) {
            btnStart.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_Private:
                if (checked)
                    isTour = false;
                break;
            case R.id.radio_Tour:
                if (checked)
                    isTour = true;
                break;
        }
    }

    public void GetUserData() {
        String url = "https://aothomeracer.azurewebsites.net/api/user/" + raceName;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", "jsonresponse" + response.toString());

                        try {
                            usr.setUserId(response.getInt("userId"));
                            usr.setUsername(response.getString("userName"));
                            usr.setStartLat(response.getDouble("startLat"));
                            usr.setStartLong(response.getDouble("startLong"));
                            usr.setEndLat(response.getDouble("endLat"));
                            usr.setEndLong(response.getDouble("endLong"));
                            usr.setStartStreetName(response.getString("startStreetname"));
                            usr.setEndStreetName(response.getString("endStreetname"));
                            usr.setRace(false);

                            Intent intent = new Intent(Login.this, Homescreen.class);
                            intent.putExtra("userData", usr);
                            intent.putExtra("playerName", playerName);
                            SwapLayout();
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                        NetworkResponse networkResponse;
                        if (error instanceof TimeoutError) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Request time out, try again!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        SwapLayout();

                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //mRequestQueue.add(jsonObjectRequest);
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void GetTourData() {
        String urlTour = "https://aothomeracer.azurewebsites.net/api/race/" + raceName;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, urlTour, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("tag", "jsonresponse" + response.toString());
                        if (response.length() > 0) {
                            try {
                                JSONObject jsonRace = response.getJSONObject(0);
                                race.setRaceId(jsonRace.getInt("raceId"));
                                race.setRaceName(jsonRace.getString("raceName"));

                                JSONArray locArray = jsonRace.getJSONArray("locations");
                                for (int i = 0; i < locArray.length(); i++) {
                                    JSONObject location = (JSONObject) locArray.get(i);

                                    Location locFromServer = new Location();
                                    locFromServer.setLocId(location.getInt("locId"));
                                    locFromServer.setLocLat(location.getDouble("locLat"));
                                    locFromServer.setLocLong(location.getDouble("locLong"));

                                    race.setLocations(locFromServer);
                                }

                                Intent intent = new Intent(Login.this, Homescreen.class);
                                intent.putExtra("userData", race);
                                intent.putExtra("playerName", playerName);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(Login.this, "Race does not exist yet!", Toast.LENGTH_SHORT).show();
                        SwapLayout();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                        if (error instanceof TimeoutError) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Request time out, try again!", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (error.networkResponse == null) {
                            Toast.makeText(Login.this, "Name does not exist.", Toast.LENGTH_SHORT).show();
                        }
                        SwapLayout();
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
