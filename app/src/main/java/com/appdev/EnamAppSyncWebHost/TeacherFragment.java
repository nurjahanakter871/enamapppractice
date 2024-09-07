package com.appdev.EnamAppSyncWebHost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TeacherFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentRVAdapter adapter;
    private List<ParticipantModal> teacherParticipants;
    private DBHandler dbHandler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View teacherfragment = inflater.inflate(R.layout.fragment_teacher, container, false);
        recyclerView = teacherfragment.findViewById(R.id.rvTeacherParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHandler = new DBHandler(getContext());
        teacherParticipants = dbHandler.getParticipantsByOccupation("Teacher");

        adapter = new FragmentRVAdapter(getContext(),teacherParticipants);
        recyclerView.setAdapter(adapter);

        return teacherfragment;
    }
}