package com.example.schoolevents.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolevents.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView eventImageIV;
    private ImageView openGalleryIV;
    private TextInputLayout eventNameET;
    private TextInputLayout eventDescriptionET;
    private Spinner eventTypeSpinner;
    private TextInputLayout eventToolsET;
    private TextInputLayout eventDateET;
    private TextInputLayout eventTimeET;
    private TextInputLayout eventPlaceET;
    private TextInputLayout eventInstructorET;
    private Button createEventButton;

    private String eventImage;
    private String eventName;
    private String eventDescription;
    private int eventType;
    private String eventTools;
    private String eventDate;
    private String eventTime;
    private String eventPlace;
    private String eventInstructor;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initActionBar();
        initApp();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_container);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView toolbarBackButton = findViewById(R.id.toolbar_left_image_view);
        toolbarBackButton.setImageResource(R.drawable.ic_back);
        toolbarTitle.setText("Etkinlik Oluştur");
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
        eventImageIV = findViewById(R.id.event_image);
        openGalleryIV = findViewById(R.id.open_gallery_icon);
        eventNameET = findViewById(R.id.event_name);
        eventDescriptionET = findViewById(R.id.event_description);
        eventTypeSpinner = findViewById(R.id.event_type);
        eventToolsET = findViewById(R.id.event_tools);
        eventDateET = findViewById(R.id.event_date);
        eventTimeET = findViewById(R.id.event_time);
        eventPlaceET = findViewById(R.id.event_place);
        eventInstructorET = findViewById(R.id.event_instructor);
        createEventButton = findViewById(R.id.create_event_button);

        myCalendar = Calendar.getInstance();

        createEventButton.setOnClickListener(this);
        eventTimeET.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        eventDateET.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        openGalleryIV.setOnClickListener(this);

        eventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    eventToolsET.getEditText().setText("");
                    eventToolsET.setVisibility(View.GONE);
                } else {
                    eventToolsET.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showDatePicker() {
        new DatePickerDialog(CreateEventActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(CreateEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = selectedHour + ":" + selectedMinute;
                eventTimeET.getEditText().setText(time);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Etkinlik Saati");
        mTimePicker.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.create_event_button:
                int errorCount = validate();
                if(errorCount == 0) {
                    createEvent();
                } else {
                    showError();
                }
                break;
            case R.id.open_gallery_icon:
                Toast.makeText(this, "Soon...", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Bir hata oluştu...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void createEvent() {
        // TODO: CREATE EVENT
    }

    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Hata")
                .setMessage("Lütfen gerekli alanları doldurunuz.")
                .setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private int validate() {
        int errorCount = 0;
        // TODO: Afişi hallet base64 or multipart/form-data
        eventName = eventNameET.getEditText().getText().toString();
        eventDescription = eventDescriptionET.getEditText().getText().toString();
        eventType = eventTypeSpinner.getSelectedItemPosition();
        eventTools = eventToolsET.getEditText().toString();
        eventDate = eventDateET.getEditText().getText().toString();
        eventTime = eventTimeET.getEditText().getText().toString();
        eventPlace = eventPlaceET.getEditText().getText().toString();
        eventInstructor = eventInstructorET.getEditText().getText().toString();

        if(eventName.isEmpty()) eventNameET.setError("Etkinlik ismi boş bırakılamaz.");
        if(eventDescription.isEmpty()) eventDescriptionET.setError("Etkinlik Açıklaması boş bırakılamaz.");
        if(eventDate.isEmpty()) eventDateET.setError("Etkinlik Tarihi boş bırakılamaz.");
        if(eventTime.isEmpty()) eventTimeET.setError("Etkinlik Saati boş bırakılamaz.");
        if(eventPlace.isEmpty()) eventPlaceET.setError("Etkinlik yeri boş bırakılamaz.");
        if(eventInstructor.isEmpty()) eventInstructorET.setError("Etkinlik eğitmeni boş bırakılamaz.");

        return errorCount;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        eventDateET.getEditText().setText(sdf.format(myCalendar.getTime()));
    }
}
