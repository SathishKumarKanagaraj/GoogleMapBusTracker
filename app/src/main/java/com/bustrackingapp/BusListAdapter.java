package com.bustrackingapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.BusViewHolder> {

    private List<BusModel> busModelList;

    public BusListAdapter(List<BusModel> busModels) {
        this.busModelList = busModels;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list_items, parent, false);
        return new BusListAdapter.BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        holder.placeTitle.setText(busModelList.get(position).getStopingName());
        holder.startTime.setText(busModelList.get(position).getStartTime());
        holder.endTime.setText(busModelList.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return busModelList.size();
    }

    public class BusViewHolder extends RecyclerView.ViewHolder {

        private TextView placeTitle;

        private TextView startTime;

        private TextView endTime;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            placeTitle = itemView.findViewById(R.id.place_title);
            startTime = itemView.findViewById(R.id.firsttime);
            endTime = itemView.findViewById(R.id.secondtime);

        }
    }
}
