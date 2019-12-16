package com.example.schoolevents.Activities;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.Models.Event;
import com.example.schoolevents.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.Calendar;

public class EventDetailActivity extends AppCompatActivity {

    private static final String TAG = "EventDetailActivity";

    private ImageView eventDetailImageIV;
    private TextView eventDetailNameTV;
    private TextView eventDetailDescriptionTV;
    private TextView eventDetailInstructorName;
    private TextView eventDetailRequirementTextTV;
    private TextView eventDetailRequirementListTV;
    private TextView eventDetailEventTypeTV;
    private TextView eventDetailPlaceTV;
    private TextView eventDetailDateTV;
    private TextView eventDetailTimeTV;
    private Button calendarButton;
    private ProgressBar progressBar;
    private ConstraintLayout infoContainer;
    private ImageView infoCloseButton;

    private String eventDetailId;
    private Event event;
    private boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("event_id"))
                eventDetailId = extras.getString("event_id");
        }
        initActionBar();
        initActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!status)
            getEvent();
    }

    private void getEvent() {
        progressBar.setVisibility(View.VISIBLE);
        String url = AppHelper.getServerUrl() + "/events?id=" + eventDetailId;
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: response -> " + response);
                progressBar.setVisibility(View.GONE);
                status = true;
                event = new Gson().fromJson(response.toString(), Event.class);
                setActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EventDetailActivity.this, "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setActivity() {
        ImageLoader.getInstance().displayImage(event.getBannerUrl(), eventDetailImageIV);
        eventDetailNameTV.setText(event.getName());
        eventDetailDescriptionTV.setText(event.getDescription());
        eventDetailInstructorName.setText(event.getInstructorName());
        if(event.getEventType() == 0) {
            eventDetailRequirementTextTV.setVisibility(View.GONE);
            eventDetailRequirementListTV.setVisibility(View.GONE);
            eventDetailEventTypeTV.setText("Seminer");
        } else {
            eventDetailRequirementTextTV.setVisibility(View.VISIBLE);
            eventDetailRequirementListTV.setVisibility(View.VISIBLE);
            eventDetailEventTypeTV.setText("Workshop");
            if(event.getRequirement() != null) {
                eventDetailRequirementListTV.setText(event.getRequirement());
            } else eventDetailRequirementTextTV.setText("Belirtilmemiş.");
        }
        eventDetailPlaceTV.setText(event.getPlace());
        eventDetailDateTV.setText(event.getDate());
        eventDetailTimeTV.setText(event.getTime());
    }

    private void initActivity() {
        eventDetailImageIV = findViewById(R.id.event_detail_image);
        eventDetailNameTV = findViewById(R.id.event_detail_name);
        eventDetailDescriptionTV = findViewById(R.id.event_detail_description);
        eventDetailInstructorName = findViewById(R.id.event_instructor_name);
        eventDetailRequirementTextTV = findViewById(R.id.requirement_text);
        eventDetailRequirementListTV = findViewById(R.id.requirement_list);
        eventDetailEventTypeTV = findViewById(R.id.event_type);
        eventDetailPlaceTV = findViewById(R.id.event_place);
        eventDetailDateTV = findViewById(R.id.event_detail_date);
        eventDetailTimeTV = findViewById(R.id.event_detail_time);
        calendarButton = findViewById(R.id.event_detail_calendar_button);
        progressBar = findViewById(R.id.event_detail_progress_bar);
        infoCloseButton = findViewById(R.id.info_close_button);
        infoContainer = findViewById(R.id.brand_info_container);

        infoCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoContainer.setVisibility(View.GONE);
            }
        });

        eventDetailImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bigImageIntent = new Intent(EventDetailActivity.this, EventBigPhotoActivity.class);
                bigImageIntent.putExtra("url", event.getBannerUrl());
                startActivity(bigImageIntent);
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
    }

    private void openCalendar() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(event.getYear()), Integer.parseInt(event.getMonth()),
                Integer.parseInt(event.get_date()), Integer.parseInt(event.getHourOfDay()),
                Integer.parseInt(event.getMinute()));
        Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.parseInt(event.getYear()), Integer.parseInt(event.getMonth()),
                Integer.parseInt(event.get_date()), Integer.parseInt(event.getHourOfDay()),
                Integer.parseInt(event.getMinute()) + 60);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, event.getName())
                .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getPlace())
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        startActivity(intent);
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
}
