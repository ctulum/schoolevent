package com.example.schoolevents.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private TextInputLayout confirmPasswordET;
    private ImageView exitButton;
    private Button registerButton;
    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.register_progress_bar);

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
                else {
                    try {
                        register();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.register_close_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void register() throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", emailET.getEditText().getText().toString());
        jsonObject.put("password", passwordET.getEditText().getText().toString());
        final String requestBody = jsonObject.toString();
        String url = AppHelper.getServerUrl() + "/register/sign-up";
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, "Basarıyla kayit oldunuz!", Toast.LENGTH_SHORT).show();
                Intent loginReturnIntent = new Intent();
                loginReturnIntent.putExtra("email", emailET.getEditText().getText());
                setResult(Activity.RESULT_OK, loginReturnIntent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onErrorResponse: error -> " + error);
                Toast.makeText(RegisterActivity.this, "Bir hata oluştu lütfen tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        requestQueue.add(jsonObjectRequest);
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
