package com.example.myapplication.CourseInformation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class CourseSectionViewAdapter extends RecyclerView.Adapter<CourseSectionViewAdapter.CourseViewHolder> {

    private List<CourseSection> courseSectionList;
    private Activity activity;

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;
        public HtmlTextView summeryTextView;
        private RecyclerView childRecyclerView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.section_name_text_view);
            summeryTextView = itemView.findViewById(R.id.summeryTextView);
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
        if (current.getSummary()!=null && current.getSummary().length()>0){
            String summery = current.getSummary();
            if (summery.contains("src=\"")) {
                int start = summery.indexOf("src=\"") + 5;
                int end = summery.indexOf("\"", start);
                String first = summery.substring(0, start);
                String img = summery.substring(start, end);
                String last = summery.substring(end);
                img += "?token=690cb20e0e50c5ffd76cd3ab4e8cd797";
                summery = first+img+last;
            }
            holder.summeryTextView.setVisibility(View.VISIBLE);
            holder.summeryTextView.setHtml(summery,
                    new HtmlHttpImageGetter(holder.summeryTextView));
        }
        List<CourseSection.CourseSubSection> subSections = current.getModules();
        CourseSubSectionViewAdapter childAdapter = new CourseSubSectionViewAdapter(subSections,activity);
        holder.childRecyclerView.setAdapter(childAdapter);

    }

    @Override
    public int getItemCount() {
        return courseSectionList.size();
    }


}
