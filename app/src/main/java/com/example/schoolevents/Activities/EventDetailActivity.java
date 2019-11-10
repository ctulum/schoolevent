package com.example.schoolevents.Activities;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schoolevents.R;

import java.util.Calendar;

public class EventDetailActivity extends AppCompatActivity {

    private Button calendarButton;
    private ImageView eventImageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        initActionBar();
        initActivity();
    }

    private void initActivity() {
        calendarButton = findViewById(R.id.event_detail_calendar_button);
        eventImageIV = findViewById(R.id.event_detail_image);

        eventImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bigImageIntent = new Intent(EventDetailActivity.this, EventBigPhotoActivity.class);
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
        beginTime.set(2019, 10, 6, 18, 40);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2019, 10, 6, 18, 45);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, "Test")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Test")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, "Test")
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, "cahittulum@gmail.com");
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
