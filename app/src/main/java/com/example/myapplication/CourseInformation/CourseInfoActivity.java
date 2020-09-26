package com.example.myapplication.CourseInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.MainMenu.CoursesViewAdapter;
import com.example.myapplication.MainMenu.UserCourses;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CourseInfoActivity extends AppCompatActivity {
    private RecyclerView fatherRecyclerView;
    private RecyclerView.LayoutManager fatherLayoutManager;
    private RecyclerView.Adapter fatherAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        Gson gson = new Gson();
        ArrayList<CourseSection> courseSections = new ArrayList<>();
        Intent intent = getIntent();
        TextView courseName = findViewById(R.id.course_name_text_view);
        courseName.setText(gson.fromJson(intent.getStringExtra(Constants.COURSE_SECTION),
                UserCourses.class).getDisplayname());
        String jsoned = intent.getStringExtra(Constants.COURSE_SECTION_ARR);
        if ((!"[]".equals(jsoned))) {
            Type type = new TypeToken<ArrayList<CourseSection>>() {
            }.getType();
            courseSections = gson.fromJson(jsoned, type);
        }
        fatherRecyclerView = findViewById(R.id.recycler_view_course_info);
        fatherLayoutManager = new LinearLayoutManager(this);
        fatherAdapter = new CourseSectionViewAdapter(courseSections,this);
        fatherRecyclerView.setLayoutManager(fatherLayoutManager);
        fatherRecyclerView.setAdapter(fatherAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
