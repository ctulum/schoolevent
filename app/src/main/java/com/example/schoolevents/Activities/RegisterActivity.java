package com.example.schoolevents.Activities;

import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.schoolevents.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private TextInputLayout confirmPasswordET;
    private ImageView exitButton;
    private Button registerButton;

    private String email;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initApp();
    }

    private void initApp() {
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        confirmPasswordET = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        exitButton = findViewById(R.id.register_close_button);

        registerButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.register_button:
                int errorCount = validate();
                if(errorCount > 0) showError();
                else register();
                break;
            case R.id.register_close_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void register() {
        // TODO: REGISTER USER
    }

    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Hata")
                .setMessage("Lütfen hataları giderip tekrar deneyiniz.")
                .setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private int validate() {
        int errorCount = 0;
        email = emailET.getEditText().getText().toString();
        password = passwordET.getEditText().getText().toString();
        confirmPassword = confirmPasswordET.getEditText().getText().toString();

        if(email.isEmpty()) {
            emailET.getEditText().setError("Email alanı boş bırakılamaz.");
            errorCount++;
        }
        if(password.isEmpty()) {
            passwordET.getEditText().setError("Parola alanı boş bırakılamaz.");
            errorCount++;
        }
        if(confirmPassword.isEmpty()) {
            confirmPasswordET.getEditText().setError("Parola alanı boş bırakılamaz.");
            errorCount++;
        }
        if(!confirmPassword.equals(password)) {
            errorCount++;
            passwordET.getEditText().setError("Parolalar eşleşmiyor.");
            confirmPasswordET.getEditText().setError("Parolalar eşleşmiyor.");
        }

        return errorCount;
    }
}
