package com.example.arno.homeracer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

    private View.OnClickListener UserLogin = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            GetUserData();
            SwapLayout();
        }
    };

    private View.OnClickListener DevOption = new View.OnClickListener(){
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

    private void GoToRegister(){
        Intent i = new Intent(Login.this, RegisterActivity.class);
        startActivity(i);
    }

    private void SwapLayout(){
        if (btnStart.isShown())
        {
            btnStart.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
    }

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

        btnStart.setOnClickListener(UserLogin);
        btnDev.setOnClickListener(DevOption);
        tvRegister.setOnClickListener(ToRegister);
    }

    public void GetUserData(){
        final EditText etUserName = findViewById(R.id.etUser);

        String url = "https://aothomeracer.azurewebsites.net/api/user/"+etUserName.getText();
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
                        if(error instanceof TimeoutError) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Request time out, try again!", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (error.networkResponse == null){
                            GetTourData(etUserName.getText().toString());
                        }
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

    public void GetTourData(String url){
        String urlTour =  "https://aothomeracer.azurewebsites.net/api/race/"+ url;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, urlTour, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("tag", "jsonresponse" + response.toString());
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
                            intent.putExtra("playerName", etPassword.getText().toString());
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
                        if(error instanceof TimeoutError)
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Request time out, try again!", Toast.LENGTH_LONG);
                            toast.show();
                        }else if (error.networkResponse == null){
                            Toast.makeText(Login.this, "Name does not exist.", Toast.LENGTH_SHORT).show();
                        }
                        SwapLayout();
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
