package com.example.myapplication.CourseInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.MainMenu.UserCourse;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CourseFolderViewActivity extends AppCompatActivity {
    TextView title;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_folder_view);
        title = findViewById(R.id.folderName);
        recyclerView = findViewById(R.id.folderContentsRecyclerView);
        Gson gson = new Gson();
        Intent intent = getIntent();
        List<CourseSection.CourseSubSection.CourseSubSectionContents> contents;
        String jsoned = intent.getStringExtra(Constants.CONTENTS_ARR);
        if (("[]".equals(jsoned))) {
            contents = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<CourseSection.CourseSubSection.CourseSubSectionContents>>() {
            }.getType();
            contents = gson.fromJson(jsoned, type);
        }
        String token = intent.getStringExtra(Constants.TOKEN);
        String title = intent.getStringExtra(Constants.FOLDER_NAME);
        this.title.setText(title);
        adapter = new CourseFoldersAdapter(contents,this,token);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
