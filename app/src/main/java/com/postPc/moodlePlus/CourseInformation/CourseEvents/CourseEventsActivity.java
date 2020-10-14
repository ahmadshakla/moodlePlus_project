package com.postPc.moodlePlus.CourseInformation.CourseEvents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.postPc.moodlePlus.CalenderData.CoursesAssignmentsInfo;
import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.MainMenu.UserCourse;
import com.postPc.moodlePlus.R;
import com.postPc.moodlePlus.UserInformation.UserInfo;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseEventsActivity extends AppCompatActivity {
    RecyclerView eventsRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;
    TextView courseNameText;
    private static final String TAG = "CourseEventsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_events);
        progressBar = findViewById(R.id.progressBar6);
        progressBar.setVisibility(View.VISIBLE);
        eventsRecyclerView = findViewById(R.id.courseEventsRecyclerView);
        courseNameText = findViewById(R.id.courseNameInEvent);
        Intent intent = getIntent();
        final Gson gson = new Gson();
        final String token = intent.getStringExtra(Constants.TOKEN);
        final UserCourse userCourse = gson.fromJson(intent.getStringExtra(Constants.COURSE_SECTION),
                UserCourse.class);
        final UserInfo userInfo = gson.fromJson(intent.getStringExtra(Constants.USER_INFO),
                UserInfo.class);
        courseNameText.setText(userCourse.getDisplayname());
        Call<CoursesAssignmentsInfo> call =
                Constants.moodleApi.getAssignmentsForOneCourse(Constants.MOODLE_W_REST_FORMAT,
                        token,
                        "mod_assign_get_assignments", userCourse.getId());
        call.enqueue(new Callback<CoursesAssignmentsInfo>() {
            @Override
            public void onResponse(Call<CoursesAssignmentsInfo> call, Response<CoursesAssignmentsInfo> response) {
                if (response.body() != null) {
                    if (response.body().courses != null) {
                        if (response.body().courses.get(0).assignments != null) {
                            List<CoursesAssignmentsInfo.CoursesData.Assignment> assignments =
                                    response.body().courses.get(0).assignments;
                            Collections.sort(assignments, (a1, a2) -> {
                                if (a1.duedate == a2.duedate) {
                                    return 0;
                                }
                                return a1.duedate - a2.duedate > 0 ? 1 : -1;
                            });
                            adapter = new CourseEventsAdapter(assignments, CourseEventsActivity.this);
                            layoutManager = new LinearLayoutManager(CourseEventsActivity.this);
                            eventsRecyclerView.setLayoutManager(layoutManager);
                            progressBar.setVisibility(View.INVISIBLE);
                            eventsRecyclerView.setAdapter(adapter);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CoursesAssignmentsInfo> call, Throwable t) {
                Log.e(TAG, "onFailure: mod_assign_get_assignments failed");
            }
        });
    }
}
