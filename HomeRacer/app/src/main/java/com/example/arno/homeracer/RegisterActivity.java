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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText etRUsername, etPassword, etStartLong, etStartLat, etEndLong, etEndLat, etStreet1, etStreet2, etCity1, etCity2;
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

            getLatLng(Street2, City2, etEndLat, etEndLong);
        }
    };

    public View.OnClickListener Register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Username = String.valueOf(etRUsername.getText());
            Password = String.valueOf(etPassword.getText());
            Street1 = String.valueOf(etStreet1.getText());
            City1 = String.valueOf(etCity1.getText());
            Street2 = String.valueOf(etStreet2.getText());
            City2 = String.valueOf(etCity2.getText());

            getLatLng();
            String urly = getLatLng();
            Log.d("RegisterAcitivy", "onClick: " + urly);*/
            Toast.makeText(RegisterActivity.this, "Register successful.", Toast.LENGTH_SHORT).show();
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
        etEndLat = findViewById(R.id.etEndLat);
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
}
