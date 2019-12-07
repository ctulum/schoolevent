package com.example.schoolevents.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.schoolevents.Models.UserSession;

import java.util.HashMap;
import java.util.Map;

public class AppHelper {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 900;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 901;
    private static int REQUEST_CODE_EXIT = 100;

    private static AppHelper appHelper;
    private static String user;

    private static String SERVER_URL = "http://192.168.1.36:3000";
    //private static AlertDialog userDialog;

    // User details from the service
    private static UserSession currSession;

    public static void init(Context context) {

        if (appHelper != null) {
            return;
        }

        appHelper = new AppHelper();
    }

    public static int getRequestCodeExit() {
        return REQUEST_CODE_EXIT;
    }

    public static void setCurrSession(UserSession session) {
        currSession = session;
    }

    public static UserSession getCurrSession() {
        return currSession;
    }

    public static String getServerUrl() {
        return SERVER_URL;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String newUser) {
        user = newUser;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

   /*public static void showDialogMessage(String title, String body, Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                } catch (Exception ignored) {}
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }*/

    public static boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showPermissionDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public static boolean checkPermissionToWriteExternalStorage(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showPermissionDialog("External storage", context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    private static void showPermissionDialog(final String msg, final Context context,
                                             final String permission, final int requestCode) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                requestCode);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public static Map<String, String> getHeader() {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", getCurrSession().getAccessToken().getJWTToken());
        params.put("Platform", "ANDROID");
        return params;
    }
}
