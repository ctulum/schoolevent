package com.example.schoolevents.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolevents.Adapters.LastEventsRecyclerAdapter;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;

import java.util.ArrayList;

public class SchoolClubDetailActivity extends AppCompatActivity {

    private ImageView coverImageIV;
    private ImageView logoIV;
    private TextView aboutUsTV;
    private RecyclerView lastEventsRecyclerView;

    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_club_detail);
        initActionBar();
        initApp();

        getEvents();
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView toolbarBackButton = findViewById(R.id.toolbar_left_image_view);
        toolbarBackButton.setImageResource(R.drawable.ic_back);
        toolbarTitle.setText(getResources().getString(R.string.page_event_detail));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("");

        toolbarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initApp() {
        coverImageIV = findViewById(R.id.school_club_cover_photo);
        logoIV = findViewById(R.id.school_club_logo);
        aboutUsTV = findViewById(R.id.school_club_about_us_text);
        lastEventsRecyclerView = findViewById(R.id.school_club_last_events);

        events = new ArrayList<>();
    }

    private void getEvents() {
        for(int i = 0; i < 6; i++) {
            Event event = new Event();
            event.setName("Linux Temel Eğitimi");
            event.setInstructorName("Cahit Tulum");
            events.add(event);
        }

        setUpWithAdapter();
    }

    private void setUpWithAdapter() {
        LastEventsRecyclerAdapter lastEventsRecyclerAdapter = new LastEventsRecyclerAdapter(events, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lastEventsRecyclerView.setLayoutManager(linearLayoutManager);
        lastEventsRecyclerView.setHasFixedSize(true);
        lastEventsRecyclerView.setNestedScrollingEnabled(true);
        lastEventsRecyclerView.setItemViewCacheSize(30);
        lastEventsRecyclerView.setAdapter(lastEventsRecyclerAdapter);
    }
}
