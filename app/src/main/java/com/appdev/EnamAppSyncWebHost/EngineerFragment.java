package com.appdev.EnamAppSyncWebHost;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class EngineerFragment extends Fragment {


    private RecyclerView recyclerView;
    private FragmentRVAdapter adapter;
    private List<ParticipantModal> engineerParticipants;
    private DBHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View engfragment = inflater.inflate(R.layout.fragment_engineer, container, false);
        recyclerView = engfragment.findViewById(R.id.rvEngineerParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHandler = new DBHandler(getContext());
        engineerParticipants = dbHandler.getParticipantsByOccupation("Engineer");

        adapter = new FragmentRVAdapter(getContext(),engineerParticipants);
        recyclerView.setAdapter(adapter);

        return engfragment;
    }






}