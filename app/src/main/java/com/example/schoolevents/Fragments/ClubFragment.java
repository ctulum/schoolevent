package com.example.schoolevents.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.schoolevents.Adapters.SchoolClubsAdapter;
import com.example.schoolevents.Models.SchoolClub;
import com.example.schoolevents.R;

import java.util.ArrayList;

public class ClubFragment extends Fragment {

    private ArrayList<SchoolClub> schoolClubs;

    private ListView schoolListView;

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

        for(int i = 0; i < 5; i++) {
            SchoolClub schoolClub = new SchoolClub();
            schoolClub.setAbout("About");
            schoolClub.setName("SauSiber");
            schoolClub.setDescription("Sakarya Üniversitesi Siber Güvenlik Kulübü");
            schoolClubs.add(schoolClub);
        }

        setUpWithAdapter();

        return view;
    }

    private void setUpWithAdapter() {
        SchoolClubsAdapter schoolClubsAdapter = new SchoolClubsAdapter(schoolClubs, getContext());
        schoolListView.setAdapter(schoolClubsAdapter);
    }

    private void initFragment(View view) {
        schoolListView = view.findViewById(R.id.school_list_view);
    }

}
