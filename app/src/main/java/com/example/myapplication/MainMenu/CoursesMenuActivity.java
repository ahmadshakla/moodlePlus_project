package com.example.myapplication.MainMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseInfoActivity;
import com.example.myapplication.CourseInformation.CourseSection;
import com.example.myapplication.MoodleApi;
import com.example.myapplication.R;
import com.example.myapplication.SettingsMenu.SettingsButtonHandler;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.myapplication.CalenderData.CalenderViewActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoursesMenuActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Button settingsButton;
    private Button voiceCommandButton;
    private Button calenderButton;
    UserInfo userInfo;
    String token;
    ArrayList<UserCourses> userCourses;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_menu);
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        settingsButton = findViewById(R.id.menu_button);
        voiceCommandButton = findViewById(R.id.voice_command_button);
        calenderButton = findViewById(R.id.calenderMainButton);
        voiceCommandButton = findViewById(R.id.voice_command_button);
        Gson gson = new Gson();
      userCourses = new ArrayList<>();
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
                intent.putExtra(Constants.TOKEN, token);
                startActivity(intent);
            }
        });
        voiceCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "speech to text");
                startActivityForResult(speechIntent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            if (data != null) {
                ArrayList<String> match = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (match != null) {
                    String text = match.get(0);
                    if (text.matches("[oO]pen course number \\d+")){
                        String courseNum = text.substring(text.lastIndexOf(" ")+1);
                        int index=  isCourseAvailable(courseNum,userCourses);
                        if (index>-1){
                            Toast.makeText(this,match.get(0),Toast.LENGTH_LONG).show();
                            openCourseInfoActivity(courseNum,index);
                        }
                        else {
                            Toast.makeText(this, "You are not taking course " + courseNum,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(this,
                                "You should say something like: \"open course number 67100\"",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openCourseInfoActivity(String courseNum,int index){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoodleApi moodleApi = retrofit.create(MoodleApi.class);
        Call<List<CourseSection>> call = moodleApi.getCourseInfo(Constants.MOODLE_W_REST_FORMAT,
                token,
                "core_course_get_contents", courseNum);
        call.enqueue(new Callback<List<CourseSection>>() {
            @Override
            public void onResponse(Call<List<CourseSection>> call,
                                   Response<List<CourseSection>> response) {
                List<CourseSection> sections = response.body();
                Intent intent = new Intent(CoursesMenuActivity.this,
                        CourseInfoActivity.class);
                intent.putExtra(Constants.TOKEN, token);
                intent.putExtra(Constants.COURSE_SECTION_ARR, gson.toJson(sections));
                intent.putExtra(Constants.COURSE_SECTION,
                        gson.toJson(userCourses.get(index)));
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<CourseSection>> call, Throwable t) {
                Log.e(Constants.TAG, Constants.ON_FAILURE_COURSE_CONTENTS);
            }
        });
    }

    private int isCourseAvailable(String courseId, ArrayList<UserCourses> userCourses){
        for (int i = 0; i < userCourses.size(); i++) {
            UserCourses userCourse = userCourses.get(i);
            if (userCourse.getId().equals(courseId)) {
                return i;
            }
        }
        return -1;
    }
}