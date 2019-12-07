package com.example.schoolevents.Activities;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.schoolevents.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EventBigPhotoActivity extends AppCompatActivity {

    private ConstraintLayout eventDetailBigPhotoContainer;
    private ImageView eventDetailBigPhotoIV;
    private ImageView eventDetailBackButton;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_big_photo);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("url"))
                url = extras.getString("url");
        }
        initActivity();
        getPhoto();
    }

    private void getPhoto() {
        ImageLoader.getInstance().displayImage(url, eventDetailBigPhotoIV);
    }

    private void initActivity() {
        eventDetailBigPhotoContainer = findViewById(R.id.event_detail_big_photo_container);
        eventDetailBigPhotoIV = findViewById(R.id.event_detail_big_photo);
        eventDetailBackButton = findViewById(R.id.event_Detail_big_photo_back);

        eventDetailBigPhotoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventDetailBackButton.getVisibility() == View.VISIBLE)
                    eventDetailBackButton.setVisibility(View.INVISIBLE);
                else
                    eventDetailBackButton.setVisibility(View.VISIBLE);
            }
        });
        eventDetailBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
