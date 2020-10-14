package com.postPc.moodlePlus.CourseInformation.CourseEvents;

import android.app.Activity;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postPc.moodlePlus.CalenderData.CoursesAssignmentsInfo;
import com.postPc.moodlePlus.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseEventsAdapter extends RecyclerView.Adapter<CourseEventsAdapter.CourseEventsHolder>{
    private ArrayList<CoursesAssignmentsInfo.CoursesData.Assignment> assignments;
    private Activity activity;

    public CourseEventsAdapter(List<CoursesAssignmentsInfo.CoursesData.Assignment> assignments,
                               Activity activity) {
        this.assignments = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        for (CoursesAssignmentsInfo.CoursesData.Assignment assignment : assignments){
            if (assignment.duedate>0){
                this.assignments.add(assignment);
            }
        }
        this.activity = activity;
    }

    @NonNull
    @Override
    public CourseEventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_event_layout, parent, false);
        return new CourseEventsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseEventsAdapter.CourseEventsHolder holder, int position) {
        CoursesAssignmentsInfo.CoursesData.Assignment current = assignments.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current.duedate*1000);
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        String day = format1.format(calendar.getTime());
        String hour = format2.format(calendar.getTime());
        holder.name.setText(current.name);
        holder.dueDate.setText(day + " at " + hour);

    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
// ----------------------------- holder class -----------------------------
    public static class CourseEventsHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView dueDate;
        public CourseEventsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.courseEventName);
            dueDate = itemView.findViewById(R.id.courseEventDueDate);
        }
    }
}
