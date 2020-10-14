package com.postPc.moodlePlus.CourseInformation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.CourseInformation.CourseEvents.CourseEventsActivity;
import com.postPc.moodlePlus.CourseInformation.CourseForums.ForumInfo;
import com.postPc.moodlePlus.CourseInformation.CourseGrades.GradesInfoActivity;
import com.postPc.moodlePlus.MainMenu.UserCourse;
import com.postPc.moodlePlus.MoodleApi;
import com.postPc.moodlePlus.R;
import com.postPc.moodlePlus.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CourseInfoActivity extends AppCompatActivity {
    private RecyclerView fatherRecyclerView;
    private RecyclerView.LayoutManager fatherLayoutManager;
    private RecyclerView.Adapter fatherAdapter;
    private TextView gradesText;
    private TextView eventsText;
    private String token;
    private UserCourse userCourse;
    private UserInfo userInfo;
    private ProgressBar progressBar;

    private Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private MoodleApi moodleApi = retrofit.create(MoodleApi.class);
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ArrayList<CourseSection> courseSections = new ArrayList<>();
        eventsText = findViewById(R.id.events);
        Intent intent = getIntent();
        final TextView courseName = findViewById(R.id.course_name_text_view);
        token = intent.getStringExtra(Constants.TOKEN);
        userCourse = gson.fromJson(intent.getStringExtra(Constants.COURSE_SECTION),
                UserCourse.class);
        courseName.setText(userCourse.getDisplayname());
        userInfo = gson.fromJson(intent.getStringExtra(Constants.USER_INFO),
                UserInfo.class);
        String jsoned = intent.getStringExtra(Constants.COURSE_SECTION_ARR);
        if ((!"[]".equals(jsoned))) {
            Type type = new TypeToken<ArrayList<CourseSection>>() {
            }.getType();
            courseSections = gson.fromJson(jsoned, type);
        }
        gradesText = findViewById(R.id.grades);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        getForums(userCourse.getId(), courseSections);
        fatherRecyclerView = findViewById(R.id.recycler_view_course_info);
        fatherLayoutManager = new LinearLayoutManager(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        gradesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseInfoActivity.this, GradesInfoActivity.class);
                intent.putExtra(Constants.COURSE_SECTION, gson.toJson(userCourse));
                intent.putExtra(Constants.TOKEN, token);
                intent.putExtra(Constants.USER_INFO, gson.toJson(userInfo));
                startActivity(intent);
            }
        });
        eventsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseInfoActivity.this, CourseEventsActivity.class);
                intent.putExtra(Constants.COURSE_SECTION, gson.toJson(userCourse));
                intent.putExtra(Constants.TOKEN, token);
                intent.putExtra(Constants.USER_INFO, gson.toJson(userInfo));
                startActivity(intent);
            }
        });
    }

    private void getForums(String courseid, ArrayList<CourseSection> courseSections) {
        HashMap<String, ForumInfo> forumInfoHashMap = new HashMap<>();
        Call<List<ForumInfo>> call = moodleApi.getForumsForCourses(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_forum_get_forums_by_courses", courseid);
        call.enqueue(new Callback<List<ForumInfo>>() {
            @Override
            public void onResponse(Call<List<ForumInfo>> call, Response<List<ForumInfo>> response) {
                for (ForumInfo forum : response.body()) {
                    forumInfoHashMap.put(forum.getCmid(), forum);
                }
                fatherAdapter = new CourseSectionViewAdapter(courseSections, CourseInfoActivity.this,
                        token, forumInfoHashMap,courseid);
                progressBar.setVisibility(View.INVISIBLE);
                fatherRecyclerView.setLayoutManager(fatherLayoutManager);
                fatherRecyclerView.setAdapter(fatherAdapter);
            }

            @Override
            public void onFailure(Call<List<ForumInfo>> call, Throwable t) {
                Log.e(CourseInfoActivity.class.getName(), "mod_forum_get_forums_by_courses failed");
            }
        });
    }

}