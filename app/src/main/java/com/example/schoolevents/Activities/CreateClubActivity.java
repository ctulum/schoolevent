package com.example.schoolevents.Activities;

import android.content.ContentResolver;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.Glide4Engine;
import com.example.schoolevents.Helper.UniversalImageLoader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateClubActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "CreateClubActivity";

    private ImageView clubLogoIV;
    private ImageView clubLogoChangeIV;
    private ImageView clubCoverPhotoIV;
    private ImageView clubCoverPhotoChangeIV;
    private TextInputLayout clubNameET;
    private TextInputLayout clubAboutET;
    private Button createButton;
    private ProgressBar progressBar;

    private ContentResolver contentResolver;
    private Bitmap logoBitmap;
    private Bitmap coverBitmap;

    private List<Uri> mSelectedLogo;
    private List<Uri> mSelectedCover;
    private List<Uri> logoUris;
    private List<Uri> coverUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        initActionBar();
        initApp();
        initImageLoader();
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
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
        progressBar = findViewById(R.id.progress_bar);

        logoUris = new ArrayList<>();
        coverUris = new ArrayList<>();
        contentResolver = getContentResolver();

        createButton.setOnClickListener(this);
        clubLogoChangeIV.setOnClickListener(this);
        clubCoverPhotoChangeIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.open_gallery_icon:
                openGallery(0);
                break;
            case R.id.open_gallery_cover_icon:
                openGallery(1);
                break;
            case R.id.create_club_button:
                Log.d(TAG, "onClick: ");
                try {
                    if(logoUris.size() > 0 && coverUris.size() >0) {
                        if(!clubNameET.getEditText().getText().toString().isEmpty() &&
                        !clubAboutET.getEditText().getText().toString().isEmpty())
                            createClub();
                        else
                            Toast.makeText(this, "Lütfen alanları boş bırakmayınız.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Lütfen fotoğraf seçiniz.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }


    private void openGallery(int whichIcon) {
        if(AppHelper.checkPermissionREAD_EXTERNAL_STORAGE(this) && AppHelper.checkPermissionToWriteExternalStorage(this)) {
            if(whichIcon == 0) {
                Matisse.from(this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .imageEngine(new Glide4Engine(CreateClubActivity.this))
                        .forResult(1);
            } else {
                Matisse.from(this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .imageEngine(new Glide4Engine(CreateClubActivity.this))
                        .forResult(2);
            }
        }
    }


    private void createClub() throws JSONException, IOException {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "createClub: ");
        JSONObject jsonObject = new JSONObject();

        Uri logoPath = logoUris.get(0);
        logoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), logoPath);
        String logoDataUri = "data:image/png;base64," + getStringImage(logoBitmap);
        Uri coverPath = coverUris.get(0);
        coverBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), coverPath);
        String coverDataUri = "data:image/png;base64," + getStringImage(coverBitmap);

        jsonObject.put("club_title", clubNameET.getEditText().getText().toString());
        jsonObject.put("club_description", clubAboutET.getEditText().getText().toString());
        jsonObject.put("logo", logoDataUri);
        jsonObject.put("cover", coverDataUri);
        final String requestBody = jsonObject.toString();
        Log.d(TAG, "createClub: requestBody -> " + requestBody);
        String url = AppHelper.getServerUrl() + "/clubs/new";
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateClubActivity.this, "Kulup kaydi basarıyla olusturulmustur.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if(networkResponse != null) {
                    final String data = new String(networkResponse.data);
                    if(networkResponse.statusCode == 400) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateClubActivity.this);
                        builder.setTitle("Hata")
                                .setMessage(data)
                                .setNeutralButton("Tamam", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                        builder.create().show();
                    }
                } else {
                    Toast.makeText(CreateClubActivity.this, "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onErrorResponse: error -> " + error);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                return AppHelper.getHeader();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
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
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: RESULT_OK");
                    if(data != null) {
                        mSelectedLogo = Matisse.obtainResult(data);
                        for (int i = 0; i < mSelectedLogo.size(); i++) {
                            if(isImage(mSelectedLogo.get(i))) {
                                logoUris.add(mSelectedLogo.get(i));
                            }
                        }
                        ImageLoader.getInstance().displayImage(logoUris.get(0).toString(), clubLogoIV);
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: RESULT_OK");
                    if(data != null) {
                        mSelectedCover = Matisse.obtainResult(data);
                        for (int i = 0; i < mSelectedCover.size(); i++) {
                            if(isImage(mSelectedCover.get(i))) {
                                coverUris.add(mSelectedCover.get(i));
                            }
                        }
                        ImageLoader.getInstance().displayImage(coverUris.get(0).toString(), clubCoverPhotoIV);
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean isImage(Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(contentResolver.getType(uri));
        return type.contains("jpeg") || type.contains("png") || type.contains("jpg");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 900:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery(0);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}
