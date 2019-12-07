package com.example.schoolevents.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.schoolevents.Adapters.LastEventsRecyclerAdapter;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.Models.SchoolClub;
import com.example.schoolevents.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SchoolClubDetailActivity extends AppCompatActivity {

    private static final String TAG = "SchoolClubDetailAct";

    private ImageView coverImageIV;
    private ImageView logoIV;
    private TextView aboutUsTV;
    private TextView schoolClubName;
    private RecyclerView lastEventsRecyclerView;
    private ProgressBar progressBar;
    private ProgressBar lastEventsProgressBar;
    private TextView noLastEventsTextView;

    private LastEventsRecyclerAdapter lastEventsRecyclerAdapter;
    private ArrayList<Event> events;
    private String clubId;
    private boolean status;
    private SchoolClub schoolClub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_club_detail);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("club_id"))
                clubId = extras.getString("club_id");
        }
        initActionBar();
        initApp();
        setUpWithAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!status) {
            getClub();
            getLastEvents();
        }
    }

    private void getLastEvents() {
        lastEventsProgressBar.setVisibility(View.VISIBLE);
        events.clear();
        String url = AppHelper.getServerUrl() + "/events/last-events?club_id=" + clubId;
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                lastEventsProgressBar.setVisibility(View.GONE);
                status = true;
                if(response.length() > 0) {
                    noLastEventsTextView.setVisibility(View.GONE);
                    for(int i = 0 ; i < response.length(); i++) {
                        try {
                            Event event = new Gson().fromJson(response.getJSONObject(i).toString(), Event.class);
                            events.add(event);
                            lastEventsRecyclerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else noLastEventsTextView.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                lastEventsProgressBar.setVisibility(View.GONE);
                Toast.makeText(SchoolClubDetailActivity.this, "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onErrorResponse: error -> " + error);
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void getClub() {
        progressBar.setVisibility(View.VISIBLE);
        String url = AppHelper.getServerUrl() + "/clubs?id=" + clubId;
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                schoolClub = new Gson().fromJson(response.toString(), SchoolClub.class);
                setActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onErrorResponse: error -> " + error.toString());
                Toast.makeText(SchoolClubDetailActivity.this, "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return AppHelper.getHeader();
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void setActivity() {
        ImageLoader.getInstance().displayImage(schoolClub.getLogo(), logoIV);
        ImageLoader.getInstance().displayImage(schoolClub.getCoverPhoto(), coverImageIV);
        schoolClubName.setText(schoolClub.getName());
        aboutUsTV.setText(schoolClub.getAbout());
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView toolbarBackButton = findViewById(R.id.toolbar_left_image_view);
        toolbarBackButton.setImageResource(R.drawable.ic_back);
        toolbarTitle.setText("Club Detail");
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
        schoolClubName = findViewById(R.id.school_club_name);
        progressBar = findViewById(R.id.detail_progress_bar);
        lastEventsProgressBar = findViewById(R.id.last_events_progress_bar);
        noLastEventsTextView = findViewById(R.id.no_last_events_text);

        events = new ArrayList<>();
    }

    private void setUpWithAdapter() {
        lastEventsRecyclerAdapter = new LastEventsRecyclerAdapter(events, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lastEventsRecyclerView.setLayoutManager(linearLayoutManager);
        lastEventsRecyclerView.setHasFixedSize(true);
        lastEventsRecyclerView.setNestedScrollingEnabled(true);
        lastEventsRecyclerView.setItemViewCacheSize(30);
        lastEventsRecyclerView.setAdapter(lastEventsRecyclerAdapter);
    }
}
