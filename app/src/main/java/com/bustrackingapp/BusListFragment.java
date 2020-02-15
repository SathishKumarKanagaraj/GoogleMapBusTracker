package com.bustrackingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusListFragment extends Fragment {

    private String selectedbus;

    RecyclerView busListView;

    BusListAdapter busListAdapter;

    private List<BusModel> busStopingList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_buslist,container,false);
        /*
         *initialize value from selected bus id
         */
        selectedbus=getArguments().getString("route_value");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         busListView=view.findViewById(R.id.busrecyclerview);
         addStopList();
         busListAdapter=new BusListAdapter(busStopingList);
         setList();

    }

    private void setList(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        busListView.setLayoutManager(linearLayoutManager);
        busListView.setAdapter(busListAdapter);

    }

    private void addStopList() {
        busStopingList = new ArrayList<>();

        if(selectedbus.equalsIgnoreCase("Tiruvotriyur to Poonamallee")){
            busStopingList.add(new BusModel(13.1322, 80.2920, "Tondiarpet","101","9:30 AM","12:30 PM"));
            busStopingList.add(new BusModel(13.1137, 80.2954, "Kalmandapam","101","10:30 AM","10:50 AM"));
            busStopingList.add(new BusModel(13.0950, 80.2926, "Beach","101","11:00 AM","11:45 AM"));
            busStopingList.add(new BusModel(13.0822, 80.2756, "Central","101","12:00 PM","12:30 PM"));
        }else if(selectedbus.equalsIgnoreCase("Tiruvotriyur to CMBT")){
            busStopingList.add(new BusModel(13.0418, 80.2341, "Tnagar","159 A","9:30 AM","12:30 PM"));
            busStopingList.add(new BusModel(13.0706, 80.2279, "Aminjakarai","159 A","10:30 AM","10:50 AM"));
            busStopingList.add(new BusModel(13.0500, 80.2121, "vadapalani","159 A","11:00 AM","11:45 AM"));
            busStopingList.add(new BusModel(13.0694, 80.1948, "CMBT","159 A","12:00 PM","12:30 PM"));
        }else if(selectedbus.equalsIgnoreCase("potheri to tambaram")){
            busStopingList.add(new BusModel(12.8259, 80.0395, "Potheri","39","2:30 AM","3:30 AM"));
            busStopingList.add(new BusModel(12.8913,  80.0810, "Vandalur","39","3:30 AM","4:30 AM"));
        }

    }
}
