package com.example.schoolevents.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.Models.AccessToken;
import com.example.schoolevents.Models.User;
import com.example.schoolevents.Models.UserSession;
import com.example.schoolevents.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private ImageView exitButton;
    private TextInputLayout emailET;
    private TextInputLayout passwordET;
    private Button loginButton;
    private Button registerButton;
    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.login_progress_bar);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_button:
                signInUser();
                break;
            case R.id.register_button:
                Intent registerIntent = new Intent(this, RegisterActivity.class);
                startActivityForResult(registerIntent, 1);
                break;
            case R.id.login_close_button:
                finish();
                break;
            default:
                break;
        }
    }

    private void signInUser() {
        progressBar.setVisibility(View.VISIBLE);
        String email = emailET.getEditText().getText().toString();
        if(email.isEmpty()){
            emailET.setError("Field can't be empty!");
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            emailET.setError(null);
        }
        AppHelper.setUser(email);
        String password = passwordET.getEditText().getText().toString();
        if(password.isEmpty()){
            passwordET.setError("Field can't be empty!");
            progressBar.setVisibility(View.GONE);
            return;
        } else {
            passwordET.setError(null);
        }

        final Map<String, String> loginParams = new HashMap<>();
        loginParams.put("email", email);
        loginParams.put("password", password);
        String loginURL = AppHelper.getServerUrl() + "/register/login";
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginURL, new JSONObject(loginParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String accessToken = response.getString("access_token");

                    SharedPreferences authTokens = getSharedPreferences("auth", MODE_PRIVATE);
                    SharedPreferences.Editor editor = authTokens.edit();
                    editor.putString("access_token", accessToken);
                    editor.apply();

                    AccessToken userAccessToken = new AccessToken(accessToken);
                    UserSession userSession = new UserSession(userAccessToken);
                    AppHelper.setCurrSession(userSession);

                    JSONObject payload = CognitoJWTParser.getPayload(accessToken);
                    JSONObject user = new JSONObject(payload.getString("user"));

                    setUserCredentials(user);

                    progressBar.setVisibility(View.GONE);
                    launchUser();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: error -> " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void launchUser() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void setUserCredentials(JSONObject user) {
        try {
            User.setId(user.getString("_id"));
            User.setEmail(user.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if(extras != null) {
                        if(extras.containsKey("email"))
                            emailET.getEditText().setText(extras.getString("email"));
                    }
                }
                break;
            default:
                break;
        }
    }
}
