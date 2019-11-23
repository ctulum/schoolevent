package com.example.schoolevents.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolevents.Adapters.MainViewPagerAdapter;
import com.example.schoolevents.Fragments.ClubFragment;
import com.example.schoolevents.Fragments.EventFragment;
import com.example.schoolevents.R;

public class MainActivity extends AppCompatActivity {

    private TabLayout mainTab;
    private ViewPager viewPager;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TextView loginText;
    private TextView createEventText;
    private TextView createClubText;

    private ClubFragment clubFragment;
    private EventFragment eventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initFragments();
        initApp();
    }

    private void initApp() {
        drawerLayout = findViewById(R.id.main_container);
        NavigationView navView = findViewById(R.id.nav_view);
        createClubText = findViewById(R.id.create_club);
        createEventText = findViewById(R.id.create_event);
        loginText = findViewById(R.id.login);

        createEventText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
            }
        });

        createClubText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateClubActivity.class);
                startActivity(intent);
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu_my_account:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                return true;
            default:
                return false;
        }
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
}
