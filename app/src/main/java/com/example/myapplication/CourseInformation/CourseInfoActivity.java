package com.example.myapplication.CourseInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseGrades.GradesInfoActivity;
import com.example.myapplication.MainMenu.UserCourses;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CourseInfoActivity extends AppCompatActivity {
    private RecyclerView fatherRecyclerView;
    private RecyclerView.LayoutManager fatherLayoutManager;
    private RecyclerView.Adapter fatherAdapter;
    private TextView gradesText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        final Gson gson = new Gson();
        ArrayList<CourseSection> courseSections = new ArrayList<>();
        Intent intent = getIntent();
        final TextView courseName = findViewById(R.id.course_name_text_view);
        final String token = intent.getStringExtra(Constants.TOKEN);
        final UserCourses userCourses = gson.fromJson(intent.getStringExtra(Constants.COURSE_SECTION),
                UserCourses.class);
        courseName.setText(userCourses.getDisplayname());
        final UserInfo userInfo = gson.fromJson(intent.getStringExtra(Constants.USER_INFO),
                UserInfo.class);
        String jsoned = intent.getStringExtra(Constants.COURSE_SECTION_ARR);
        if ((!"[]".equals(jsoned))) {
            Type type = new TypeToken<ArrayList<CourseSection>>() {
            }.getType();
            courseSections = gson.fromJson(jsoned, type);
        }
        fatherRecyclerView = findViewById(R.id.recycler_view_course_info);
        fatherLayoutManager = new LinearLayoutManager(this);
        fatherAdapter = new CourseSectionViewAdapter(courseSections,CourseInfoActivity.this);
        fatherRecyclerView.setLayoutManager(fatherLayoutManager);
        fatherRecyclerView.setAdapter(fatherAdapter);
        gradesText = findViewById(R.id.grades);
        gradesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseInfoActivity.this, GradesInfoActivity.class);
                intent.putExtra(Constants.COURSE_SECTION, gson.toJson(userCourses));
                intent.putExtra(Constants.TOKEN, token);
                intent.putExtra(Constants.USER_INFO, gson.toJson(userInfo));
                startActivity(intent);
            }
        });

    }

}