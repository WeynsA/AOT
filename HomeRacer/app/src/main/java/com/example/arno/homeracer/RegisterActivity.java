package com.example.arno.homeracer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPassword, etStartLong, etStartLat, etEndLong,etEndLat;
    Button btnRegister;
    TextView finish;

    public View.OnClickListener Finish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        finish = findViewById(R.id.tvAlreadyRegisterd);

        finish.setOnClickListener(Finish);

    }

}
