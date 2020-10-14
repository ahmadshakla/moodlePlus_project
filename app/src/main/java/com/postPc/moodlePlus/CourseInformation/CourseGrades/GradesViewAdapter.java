package com.postPc.moodlePlus.CourseInformation.CourseGrades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postPc.moodlePlus.R;
import com.google.gson.Gson;

public class GradesViewAdapter extends RecyclerView.Adapter<GradesViewAdapter.GradesViewHolder> {
    private GradesTable.UserGrades.GradesItem[] gradeSheet;
    private Gson gson;

    public static class GradesViewHolder extends RecyclerView.ViewHolder {
        public TextView grade_item;
        public TextView grade;

        public GradesViewHolder(@NonNull View itemView) {
            super(itemView);
            grade_item = itemView.findViewById(R.id.grade_item_text);
            grade = itemView.findViewById(R.id.grade);
        }
    }

    public GradesViewAdapter(GradesTable.UserGrades.GradesItem[] grades) {
        gradeSheet = grades;
    }


    @NonNull
    @Override
    public GradesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new GradesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesViewHolder holder, int position) {
        final GradesTable.UserGrades.GradesItem grade = gradeSheet[position];
        holder.grade_item.setText(grade.getItemName());
        String text;
        if (grade.getGrade().equals("-")) {
            text = "-";
        } else {
            if (grade.getMaxGrade().equals("null") || grade.getGrade().equals("Pass") || grade.getGrade().equals("Fail")) {
                text = grade.getGrade();
            } else {
                text = grade.getGrade() + "/" + grade.getMaxGrade();
            }
        }
        holder.grade.setText(text);
    }


    @Override
    public int getItemCount() {
        return gradeSheet.length;
    }
}
