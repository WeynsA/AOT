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
    TextView startLong, startLat, userId, userName;
    Button btnSkip;
    JsonObjectRequest jsonObjectRequest;

    public UserData currentUser = new UserData();
    RequestQueue mRequestQueue;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        btnSkip = findViewById(R.id.btnStartRace);
        startLat = findViewById(R.id.tvStartLat);
        startLong = findViewById(R.id.tvStartLong);
        userId = findViewById(R.id.tvId);
        userName = findViewById(R.id.tvUserName);
        final SharedPreferences userDataPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String UID = preferences.getString("UID","");
        //userId.setText(UID);

        GetGame("1");
        //final String UID = userDataPref.getString("startLatPref", "");
         startLat.setText("start latitude is: " + userDataPref.getString("startLatPref",""));
         userId.setText("start longitude is: " + userDataPref.getString("startLongPref", ""));
         startLong.setText("End latitude is: " + userDataPref.getString("endLatPref", ""));
         userName.setText("End longitude is: " + userDataPref.getString("endLongPref", ""));

        btnSkip.setOnClickListener(SkipToMap);

    }

    private View.OnClickListener SkipToMap = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Homescreen.this, MapsActivity.class);
            startActivity(i);
        }
    };

    public void GetGame(String UID){
        final SharedPreferences userDataPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = userDataPref.edit();

        String url = "https://worldapi.azurewebsites.net/api/homeracer/user/"+UID;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", "jsonresponse" + response.toString());
                        try {
                            //String name = response.getString("userName");
                            currentUser.setUserId(response.getInt("userId"));
                            String userIdPref = String.valueOf(response.getInt("userId"));
                            currentUser.setStartLat(response.getDouble("startLat"));
                            String startLatitude = String.valueOf(response.getDouble("startLat"));
                            currentUser.setStartLong(response.getDouble("startLong"));
                            String startLongitude = String.valueOf(response.getDouble("startLong"));
                            currentUser.setEndLat(response.getDouble("endLat"));
                            String endLatitude = String.valueOf(response.getDouble("endLat"));
                            currentUser.setEndLong(response.getDouble("endLong"));
                            String endLongitude = String.valueOf(response.getDouble("endLong"));
                            currentUser.setUsername(response.getString("userName"));
                            String userNamePref = (response.getString("userName"));

                            editor.putString("startLatPref", startLatitude);
                            editor.putString("startLongPref", startLongitude);
                            editor.putString("endLatPref", endLatitude);
                            editor.putString( "endLongPref",endLongitude);
                            editor.putString("userIdPref", userIdPref);
                            editor.putString("userNamePref",userNamePref);
                            editor.commit();

                            //startLong.setText(String.valueOf(currentUser.getStartLong()));
                            //userName.setText(currentUser.getUsername());

                            //Intent sendObj = new Intent(Homescreen.this, Homescreen.class);
                            bundle = new Bundle();
                            bundle.putSerializable("userInfo", currentUser);
                            //sendObj.putExtras(bundle);
                            //startActivity(sendObj);

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
