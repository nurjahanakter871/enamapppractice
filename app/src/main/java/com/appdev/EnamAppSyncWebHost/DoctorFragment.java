package com.appdev.EnamAppSyncWebHost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class DoctorFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentRVAdapter adapter;
    private List<ParticipantModal> doctorParticipants;
    private DBHandler dbHandler;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_doctor, container, false);

        View docfragment = inflater.inflate(R.layout.fragment_doctor, container, false);
        recyclerView = docfragment.findViewById(R.id.rvDoctorParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHandler = new DBHandler(getContext());
        doctorParticipants = dbHandler.getParticipantsByOccupation("Doctor");

        adapter = new FragmentRVAdapter(getContext(),doctorParticipants);
        recyclerView.setAdapter(adapter);

        return docfragment;
    }
}