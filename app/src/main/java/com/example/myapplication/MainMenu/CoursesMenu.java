package com.example.myapplication.MainMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;
import com.example.myapplication.Constants;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SettingsMenu.SettingsButtonHandler;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.sharp.Sharp;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoursesMenu extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Button settingsButton;
    private Button voiceCommandButton;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_menu);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        settingsButton = findViewById(R.id.menu_button);
        voiceCommandButton = findViewById(R.id.voice_command_button);
        Gson gson = new Gson();
        ArrayList<UserCourses> userCourses = new ArrayList<>();
        Intent intent = getIntent();
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
    }




}
