package com.example.myapplication.CourseInformation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CourseSectionViewAdapter extends RecyclerView.Adapter<CourseSectionViewAdapter.CourseViewHolder> {

    private List<CourseSection> courseSectionList;
    private Activity activity;

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;
        private RecyclerView childRecyclerView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.section_name_text_view);
            childRecyclerView = itemView.findViewById(R.id.subsection_recyclerView);
        }
    }

    public CourseSectionViewAdapter(ArrayList<CourseSection> courseList, Activity activity) {
        this.courseSectionList = courseList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_section, parent, false);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseSection current = courseSectionList.get(position);
        holder.courseTextView.setText(current.getName());
        List<CourseSection.CourseSubSection> subSections = current.getModules();
        CourseSubSectionViewAdapter childAdapter = new CourseSubSectionViewAdapter(subSections,activity);
        holder.childRecyclerView.setAdapter(childAdapter);

    }

    @Override
    public int getItemCount() {
        return courseSectionList.size();
    }


}
