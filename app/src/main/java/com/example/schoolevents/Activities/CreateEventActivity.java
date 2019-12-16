package com.example.schoolevents.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.Glide4Engine;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CreateEventActivity";

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
    private ProgressBar progressBar;

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

    private List<Uri> mSelectedBrand;
    private Bitmap brandBitmap;

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
        progressBar = findViewById(R.id.create_event_progress_bar);

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
                    try {
                        if(mSelectedBrand != null) {
                            if(mSelectedBrand.size() > 0)
                                createEvent();
                        } else
                            Toast.makeText(this, "Lütfen bir afiş seçiniz.", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showError();
                }
                break;
            case R.id.open_gallery_icon:
                openGallery();
                break;
            default:
                Toast.makeText(this, "Bir hata oluştu...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void openGallery() {
        if(AppHelper.checkPermissionREAD_EXTERNAL_STORAGE(this) && AppHelper.checkPermissionToWriteExternalStorage(this)) {
            Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .imageEngine(new Glide4Engine(CreateEventActivity.this))
                    .forResult(1);
        }
    }

    private void createEvent() throws IOException, JSONException {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        Uri brandPath = mSelectedBrand.get(0);
        brandBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), brandPath);
        String brandDataUri = "data:image/png;base64," + getStringImage(brandBitmap);
        jsonObject.put("title", eventName);
        jsonObject.put("description", eventDescription);
        jsonObject.put("event_type", eventType);
        jsonObject.put("banner", brandDataUri);
        jsonObject.put("date", eventDate);
        jsonObject.put("time", eventTime);
        jsonObject.put("place", eventPlace);
        jsonObject.put("instructor_name", eventInstructor);
        jsonObject.put("requirement", eventTools);
        String url = AppHelper.getServerUrl() + "/events/new";
        final String requestBody = jsonObject.toString();
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: response -> " + response);
                progressBar.setVisibility(View.GONE);
                Intent detailEvent = new Intent(CreateEventActivity.this, EventDetailActivity.class);
                try {
                    detailEvent.putExtra("event_id", response.getString("insertedId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(detailEvent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if(networkResponse != null) {
                    String data = new String(networkResponse.data);
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
                    builder.setTitle("Hata")
                            .setMessage(data)
                            .setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                } else {
                    Toast.makeText(CreateEventActivity.this, "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onErrorResponse: error -> " + error);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return AppHelper.getHeader();
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
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
        eventName = eventNameET.getEditText().getText().toString();
        eventDescription = eventDescriptionET.getEditText().getText().toString();
        eventType = eventTypeSpinner.getSelectedItemPosition();
        eventTools = eventToolsET.getEditText().getText().toString();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 900:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: RESULT_OK");
                    if(data != null) {
                        mSelectedBrand = Matisse.obtainResult(data);
                        ImageLoader.getInstance().displayImage(mSelectedBrand.get(0).toString(), eventImageIV);
                    }
                }
                break;
            default:
                break;
        }
    }
}
