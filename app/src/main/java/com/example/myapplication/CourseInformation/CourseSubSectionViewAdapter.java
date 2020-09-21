package com.example.myapplication.CourseInformation;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class CourseSubSectionViewAdapter extends RecyclerView.Adapter<CourseSubSectionViewAdapter.CourseSubSectionViewHolder> {

    private List<CourseSection.CourseSubSection> courseSubSectionList;

    public static class CourseSubSectionViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;

        public CourseSubSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.sub_section_textView);

        }
    }

    public CourseSubSectionViewAdapter(List<CourseSection.CourseSubSection> courseList) {
        this.courseSubSectionList = courseList;
    }

    @NonNull
    @Override
    public CourseSubSectionViewAdapter.CourseSubSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_sub_section, parent
                , false);
        return new CourseSubSectionViewAdapter.CourseSubSectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSubSectionViewHolder holder, int position) {
        CourseSection.CourseSubSection current = courseSubSectionList.get(position);

            holder.courseTextView.setText(current.name);


    }

    @Override
    public int getItemCount() {
        return courseSubSectionList.size();
    }


}
