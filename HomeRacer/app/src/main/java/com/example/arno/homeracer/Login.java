package com.example.arno.homeracer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private Button btnStart, btnSkip;
    private TextView tvRegister;
    private EditText etUser, etPassword;
    public ProgressBar spinner;
   // public UserData user;

    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;


    private View.OnClickListener UserLogin = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //attemptLogin();
            SwapLayout();
            //getUser();
            //GetGame1("1");
            GetGame("1");
        }
    };

    private View.OnClickListener SkipToMap = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            SwapLayout();
            Intent i = new Intent(Login.this, MapsActivity.class);
            startActivity(i);
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
            btnSkip.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        } else {
            btnStart.setVisibility(View.VISIBLE);
            btnSkip.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
    }

    private void attemptLogin(){
        getUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnStart = findViewById(R.id.btnLogin);
        btnSkip = findViewById(R.id.btnSkip);
        spinner = findViewById(R.id.spinner);
        tvRegister = findViewById(R.id.tvRegister);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);

        btnStart.setOnClickListener(UserLogin);
        btnSkip.setOnClickListener(SkipToMap);
        tvRegister.setOnClickListener(ToRegister);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void getUser() {
       final String userLogin = etUser.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();

        String url= "https://worldapi.azurewebsites.net/api/homeracer/user/"+userLogin;

        mRequestQueue = Volley.newRequestQueue(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("LoginActivity", response);

                editor.putString("UID", response);
                editor.apply();


                TextView debug = findViewById(R.id.tvDebug);
                debug.setText(response);

                Toast.makeText(getApplicationContext(),"logged in", Toast.LENGTH_LONG).show();

                Intent i = new Intent(Login.this, Homescreen.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error.response", error.toString());
                SwapLayout();
            }
        });
        mRequestQueue.add(stringRequest);

    }

    public void GetGame1(String UID){
        final SharedPreferences userDataPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = userDataPref.edit();

        String url = "https://worldapi.azurewebsites.net/api/homeracer/user/"+2;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", "jsonresponse" + response.toString());
                        try {
                            Intent i = new Intent(Login.this, Homescreen.class);
                            i.putExtra("userId", response.getInt("userId"));
                            i.putExtra("userName", response.getString("userName"));
                            i.putExtra("startLat", response.getDouble("startLat"));
                            i.putExtra("startLong", response.getDouble("startLong"));
                            i.putExtra("endLat", response.getDouble("endLat"));
                            i.putExtra("endLong", response.getDouble("endLong"));

                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
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

    UserData usr = new UserData();

    public void GetGame(String UID){
        final SharedPreferences userDataPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = userDataPref.edit();

        String url = "https://worldapi.azurewebsites.net/api/homeracer/user/"+2;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", "jsonresponse" + response.toString());
                        try {

                            usr.setUserId((response.getInt("userId")));
                            usr.setUsername(response.getString("userName"));
                            usr.setStartLat((response.getDouble("startLat")));
                            usr.setStartLong(response.getDouble("startLong"));
                            usr.setEndLat((response.getDouble("endLat")));
                            usr.setEndLong((response.getDouble("endLong")));

                            Intent intent = new Intent(Login.this, Homescreen.class);
                            intent.putExtra("userToHome", usr);
                            //intent.putExtra("currentUser", response.getString("userName"));
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


}
