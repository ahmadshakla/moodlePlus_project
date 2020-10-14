package com.postPc.moodlePlus.CalenderData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postPc.moodlePlus.R;

import java.util.ArrayList;

public class CalenderEventsAdapter extends RecyclerView.Adapter<CalenderEventsAdapter.CalenderEventsHolder> {

    private ArrayList<String> events;
    public CalenderEventsAdapter(ArrayList<String> events){
        this.events = events;
    }
    @NonNull
    @Override
    public CalenderEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_line_white, parent, false);
        return new CalenderEventsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderEventsHolder holder, int position) {
        final String current =events.get(position);
        holder.textView.setText(current);
    }

    @Override
    public int getItemCount() {
        return events == null? 0 : events.size();
    }

    // ---------------------------------- Holder class ----------------------------------
    public static class CalenderEventsHolder extends RecyclerView.ViewHolder {
            TextView textView;
        public CalenderEventsHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.single_line_white_textView);

        }
    }
}
