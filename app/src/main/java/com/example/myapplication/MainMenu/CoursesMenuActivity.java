package com.example.myapplication.MainMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.SettingsMenu.SettingsButtonHandler;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.myapplication.CalenderData.CalenderViewActivity;

public class CoursesMenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Button settingsButton;
    private Button voiceCommandButton;
    private Button calenderButton;
    UserInfo userInfo;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_menu);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        settingsButton = findViewById(R.id.menu_button);
        voiceCommandButton = findViewById(R.id.voice_command_button);
        calenderButton = findViewById(R.id.calenderMainButton);
        Gson gson = new Gson();
        ArrayList<UserCourses> userCourses = new ArrayList<>();
        Intent intent = getIntent();
        token = intent.getStringExtra(Constants.TOKEN);
        String jsoned = intent.getStringExtra(Constants.COURSE_ARR);
        userInfo = gson.fromJson(intent.getStringExtra(Constants.USER_INFO), UserInfo.class);
        if (("[]".equals(jsoned))) {
            userCourses = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<UserCourses>>() {
            }.getType();
            userCourses = gson.fromJson(jsoned, type);
        }
        adapter = new CoursesViewAdapter(userCourses, this, intent.getStringExtra(Constants.TOKEN));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
//        Glide.with(this).load("https://moodle2.cs.huji.ac.il/nu19/theme/image" +
//                ".php/classic/core/1600652210/f/pdf-24").into(voiceCommandButton);

    }

    @Override
    protected void onResume() {
        super.onResume();
        final SettingsButtonHandler settingsButtonHandler = new SettingsButtonHandler(this, this, userInfo);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsButtonHandler.showPopup(view);
            }
        });

        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CoursesMenuActivity.this, CalenderViewActivity.class);
                intent.putExtra(Constants.TOKEN,token);
                startActivity(intent);
            }
        });
    }




}
