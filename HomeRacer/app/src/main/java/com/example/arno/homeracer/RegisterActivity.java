package com.example.arno.homeracer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;


import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText etRUsername, etPassword, etStartLong, etStartLat, etEndLong, etREndLat, etStreet1, etStreet2, etCity1, etCity2;
    String Username, Password, Street1, Street2, City1, City2;
    Double RStartLong, RStartLat, REndLong, REndLat;
    Button btnRegister, btnStartLtLn, btnEndLtLn;
    TextView finish;

    public View.OnClickListener Finish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener GetStartLatLng = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Street1 = String.valueOf(etStreet1.getText());
            City1 = String.valueOf(etCity1.getText());

            getLatLng(Street1, City1, etStartLat, etStartLong);
        }
    };

    public View.OnClickListener GetEndLatLng = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Street2 = String.valueOf(etStreet2.getText());
            City2 = String.valueOf(etCity2.getText());

            getLatLng(Street2, City2, etREndLat, etEndLong);
        }
    };

    public View.OnClickListener Register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PostRequest();
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etStartLat = findViewById(R.id.etStartLat);
        etStartLong = findViewById(R.id.etStartLong);
        etREndLat = findViewById(R.id.etEndLat);
        etEndLong = findViewById(R.id.etEndLong);
        etStreet1 = findViewById(R.id.etSteetname1);
        etCity1 = findViewById(R.id.etCity1);
        etStreet2 = findViewById(R.id.etSteetname2);
        etCity2 = findViewById(R.id.etCity2);
        btnRegister = findViewById(R.id.btnRegister);
        finish = findViewById(R.id.tvAlreadyRegisterd);
        btnEndLtLn = findViewById(R.id.btnEndLtLn);
        btnStartLtLn = findViewById(R.id.btnStartLtLn);

        finish.setOnClickListener(Finish);
        btnRegister.setOnClickListener(Register);
        btnStartLtLn.setOnClickListener(GetStartLatLng);
        btnEndLtLn.setOnClickListener(GetEndLatLng);
    }

    public void getLatLng(String street, String city, final EditText et1, final EditText et2){
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+street+"+"+city+"&key=AIzaSyBCqW3-1sRfO1_aCvsYJSqY7KclRAOJJbI";
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("tag", "jsonresponse" + response.toString());
                        try {
                            String status = response.getString("status").toString();

                            if(status.equalsIgnoreCase("OK")){
                                JSONArray results = response.getJSONArray("results");
                                JSONObject r = results.getJSONObject(0);
                                JSONObject addressComponentsArray = r.getJSONObject("geometry");
                                JSONObject end = addressComponentsArray.getJSONObject("location");

                                Double lat = end.getDouble("lat");
                                Double lng = end.getDouble("lng");

                                et1.setText(String.format("%.4f", lat));
                                et2.setText(String.format("%.4f", lng));

                            }else
                                Toast.makeText(RegisterActivity.this, "Wrong address, try removing spaces.", Toast.LENGTH_SHORT).show();


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
                        }
                    }
                }
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void PostRequest() {
        String url = "https://worldapi2.azurewebsites.net/api/homeracer/user";

        Username =  String.valueOf(etRUsername.getText());
        Street1 =  String.valueOf(etStreet1.getText());
        Street2 =  String.valueOf(etStreet2.getText());
        REndLat =  Double.parseDouble(etREndLat.getText().toString());
        REndLong =  Double.parseDouble(etEndLong.getText().toString());
        RStartLat =  Double.parseDouble(etStartLat.getText().toString());
        RStartLong =  Double.parseDouble(etStartLong.getText().toString());

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", Username);
        params.put("password", "123546");
        params.put("startLat", RStartLat.toString());
        params.put("startLong", RStartLong.toString());
        params.put("endLat", REndLat.toString());
        params.put("endLong", REndLong.toString());
        params.put("startStreetname", Street1);
        params.put("endStreetname", Street2);
        JSONObject jsonObj = new JSONObject(params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());;
                String tekst = response.toString();
                Toast.makeText(getApplicationContext(), "Register successful!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjReq);
    }
}
