package com.example.schoolevents.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.schoolevents.Adapters.SchoolClubsAdapter;
import com.example.schoolevents.Helper.AppHelper;
import com.example.schoolevents.Helper.VolleySingleton;
import com.example.schoolevents.Models.SchoolClub;
import com.example.schoolevents.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClubFragment extends Fragment {

    private static final String TAG = "ClubFragment";

    private ArrayList<SchoolClub> schoolClubs;
    private SchoolClubsAdapter schoolClubsAdapter;

    private ListView schoolListView;
    private TextView noClubText;
    private ProgressBar progressBar;

    public ClubFragment() {
        // Required empty public constructor
        schoolClubs = new ArrayList<>();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_club, container, false);
        initFragment(view);

        setUpWithAdapter();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getClubData();
    }

    private void getClubData() {
        progressBar.setVisibility(View.VISIBLE);
        schoolClubs.clear();
        String url = AppHelper.getServerUrl() + "/clubs/list";
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressBar.setVisibility(View.GONE);
                if(response.length() > 0) {
                    noClubText.setVisibility(View.GONE);
                    for(int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            SchoolClub schoolClub = new Gson().fromJson(jsonObject.toString(), SchoolClub.class);
                            schoolClubs.add(schoolClub);
                            schoolClubsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    noClubText.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noClubText.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onErrorResponse: error -> " + error.toString());
                Toast.makeText(getContext(), "Bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void setUpWithAdapter() {
        schoolClubsAdapter = new SchoolClubsAdapter(schoolClubs, getContext());
        schoolListView.setAdapter(schoolClubsAdapter);
    }

    private void initFragment(View view) {
        schoolListView = view.findViewById(R.id.school_list_view);
        noClubText = view.findViewById(R.id.no_club_text);
        progressBar = view.findViewById(R.id.club_progress_bar);
    }

}
