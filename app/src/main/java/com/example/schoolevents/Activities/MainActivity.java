package com.example.schoolevents.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolevents.Adapters.MainViewPagerAdapter;
import com.example.schoolevents.Fragments.ClubFragment;
import com.example.schoolevents.Fragments.EventFragment;
import com.example.schoolevents.R;

public class MainActivity extends AppCompatActivity {

    private TabLayout mainTab;
    private ViewPager viewPager;

    private ClubFragment clubFragment;
    private EventFragment eventFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initFragments();
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_container);
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
                Toast.makeText(this, "Soon.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}
