package com.example.schoolevents.Activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolevents.R;

public class CreateClubActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView clubLogoIV;
    private ImageView clubLogoChangeIV;
    private ImageView clubCoverPhotoIV;
    private ImageView clubCoverPhotoChangeIV;
    private TextInputLayout clubNameET;
    private TextInputLayout clubAboutET;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        initActionBar();
        initApp();
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView toolbarBackButton = findViewById(R.id.toolbar_left_image_view);
        toolbarBackButton.setImageResource(R.drawable.ic_back);
        toolbarTitle.setText("Kulüp Oluştur");
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
        clubLogoIV = findViewById(R.id.club_logo);
        clubLogoChangeIV = findViewById(R.id.open_gallery_icon);
        clubCoverPhotoIV = findViewById(R.id.club_cover_photo);
        clubCoverPhotoChangeIV = findViewById(R.id.open_gallery_cover_icon);
        clubNameET = findViewById(R.id.club_name);
        clubAboutET = findViewById(R.id.club_about);
        createButton = findViewById(R.id.create_club_button);

        createButton.setOnClickListener(this);
        clubLogoChangeIV.setOnClickListener(this);
        clubCoverPhotoChangeIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.open_gallery_icon:
                Toast.makeText(this, "Soon..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.open_gallery_cover_icon:
                Toast.makeText(this, "Soon..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.create_club_button:
                Toast.makeText(this, "Soon..", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
