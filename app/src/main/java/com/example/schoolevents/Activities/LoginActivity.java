package com.example.schoolevents.Activities;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.schoolevents.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView exitButton;
    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initApp();
    }

    private void initApp() {
        exitButton = findViewById(R.id.login_close_button);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_button:
                break;
            case R.id.register_button:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.login_close_button:
                finish();
                break;
            default:
                break;
        }
    }
}
