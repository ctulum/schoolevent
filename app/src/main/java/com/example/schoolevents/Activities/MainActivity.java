package com.example.schoolevents.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser;
import com.example.schoolevents.Adapters.MainViewPagerAdapter;
import com.example.schoolevents.Fragments.ClubFragment;
import com.example.schoolevents.Fragments.EventFragment;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.UniversalImageLoader;
import com.example.schoolevents.Models.AccessToken;
import com.example.schoolevents.Models.User;
import com.example.schoolevents.Models.UserSession;
import com.example.schoolevents.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TabLayout mainTab;
    private ViewPager viewPager;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView loginText;
    private TextView createEventText;
    private TextView createClubText;
    private TextView logOutText;

    private ClubFragment clubFragment;
    private EventFragment eventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkAvailable()){
            AppHelper.init(getApplicationContext());
            setUniversalImageLoader();
            User.init();
            findCurrentSession();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Oopss..").setMessage("There is no internet connection please check it!").setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recreate();
                }
            }).create().show();
        }
        initActionBar();
        initFragments();
        initApp();
    }

    private void setUniversalImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void initApp() {
        drawerLayout = findViewById(R.id.main_container);
        NavigationView navView = findViewById(R.id.nav_view);
        createClubText = findViewById(R.id.create_club);
        createEventText = findViewById(R.id.create_event);
        loginText = findViewById(R.id.login);
        logOutText = findViewById(R.id.log_out);

        if(AppHelper.getCurrSession() != null) {
            loginText.setVisibility(View.GONE);
            createClubText.setVisibility(View.VISIBLE);
            createEventText.setVisibility(View.VISIBLE);
            logOutText.setVisibility(View.VISIBLE);
        } else {
            loginText.setVisibility(View.VISIBLE);
            createClubText.setVisibility(View.GONE);
            createEventText.setVisibility(View.GONE);
            logOutText.setVisibility(View.GONE);
        }

        createEventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        createClubText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateClubActivity.class);
                startActivity(intent);
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        logOutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences authTokens = getSharedPreferences("auth", MODE_PRIVATE);
                SharedPreferences.Editor editor = authTokens.edit();
                editor.remove("access_token");
                editor.apply();
                logOutText.setVisibility(View.GONE);
                createClubText.setVisibility(View.GONE);
                createEventText.setVisibility(View.GONE);
                loginText.setVisibility(View.VISIBLE);
                AppHelper.setCurrSession(null);
                Toast.makeText(MainActivity.this, "Başarıyla çıkış yapıldı.", Toast.LENGTH_SHORT).show();
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        int width = getResources().getDisplayMetrics().widthPixels/2;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) navView.getLayoutParams();
        params.width = width + (width / 2) + (width / 8);
        navView.setLayoutParams(params);


        drawerToggle = setDrawerToggle();
        drawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void initActionBar() {
        toolbar = findViewById(R.id.toolbar_container);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getResources().getString(R.string.page_home));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("");
    }

    private void initFragments() {
        mainTab = findViewById(R.id.main_tab_layout);
        viewPager = findViewById(R.id.main_view_pager);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        // Adding Fragments
        clubFragment = new ClubFragment();
        eventFragment = new EventFragment();

        mainViewPagerAdapter.addFragment(clubFragment, "Club Fragment");
        mainViewPagerAdapter.addFragment(eventFragment, "Event Fragment");

        // Adapter Setup
        viewPager.setAdapter(mainViewPagerAdapter);
        mainTab.setupWithViewPager(viewPager);
    }

    private ActionBarDrawerToggle setDrawerToggle(){
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void findCurrentSession() {
        getLastAuth();
        String username = User.getId();
        if (username != null) {
            AppHelper.setUser(username);
        }
    }

    private void getLastAuth() {
        SharedPreferences authTokens = getSharedPreferences("auth", MODE_PRIVATE);
        AccessToken userAccessToken = new AccessToken(authTokens.getString("access_token", null));
        UserSession userSession = new UserSession(userAccessToken);
        if(userSession.isValid()) {
            try {
                JSONObject payload = new JSONObject(CognitoJWTParser.getPayload(userAccessToken.getJWTToken()).getString("user"));
                AppHelper.setCurrSession(userSession);
                setUserCredentials(payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            clearCachedTokens();
        }
    }

    private void clearCachedTokens() {
        SharedPreferences authTokens = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = authTokens.edit();
        editor.remove("access_token");
        editor.remove("refresh_token");
        editor.apply();
    }

    private void setUserCredentials(JSONObject user) {
        try {
            User.setId(user.getString("_id"));
            User.setEmail(user.getString("email"));
            User.setAccountType(user.getInt("account_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                createEventText.setVisibility(View.VISIBLE);
                createClubText.setVisibility(View.VISIBLE);
                logOutText.setVisibility(View.VISIBLE);
                loginText.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
