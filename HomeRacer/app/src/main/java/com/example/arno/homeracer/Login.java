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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class Login extends AppCompatActivity {

    private Button btnStart, btnSkip;
    private TextView tvRegister;
    private EditText etUser, etPassword;
    public ProgressBar spinner;

    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;


    private View.OnClickListener UserLogin = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            //attemptLogin();
            //SwapLayout();
            getUser();
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
        String userLogin = etUser.getText().toString().trim();
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
}
