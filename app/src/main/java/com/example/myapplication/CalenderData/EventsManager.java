package com.example.myapplication.CalenderData;

import android.util.Log;

import com.example.myapplication.Constants;
import com.example.myapplication.MoodleApi;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsManager {
    private Retrofit retrofit;
    private MoodleApi moodleApi;
    private String Token;
    private HashMap<CoursesAssignmentsInfo.CoursesData, List<CoursesAssignmentsInfo.CoursesData.Assignment>> assignmentsByCourse;

    public EventsManager(String token) {
        assignmentsByCourse = new HashMap<>();
        Token = token;
        retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        moodleApi = retrofit.create(MoodleApi.class);
    }


    public HashMap<CoursesAssignmentsInfo.CoursesData, List<CoursesAssignmentsInfo.CoursesData.Assignment>> getAssignmentsByCourses(List<CoursesAssignmentsInfo.CoursesData> courses) {
        HashMap<CoursesAssignmentsInfo.CoursesData, List<CoursesAssignmentsInfo.CoursesData.Assignment>> assignmentsByCourse =
                new HashMap<>();
        for (CoursesAssignmentsInfo.CoursesData course : courses) {
            assignmentsByCourse.put(course, course.assignments);
        }
        return assignmentsByCourse;
    }


    public Callback<CoursesAssignmentsInfo> createCallBack() {
        return new Callback<CoursesAssignmentsInfo>() {
            @Override
            public void onResponse(Call<CoursesAssignmentsInfo> call, Response<CoursesAssignmentsInfo> response) {
                final List<CoursesAssignmentsInfo.CoursesData> courses = response.body().courses;
                assignmentsByCourse = getAssignmentsByCourses(courses);
            }

            @Override
            public void onFailure(Call<CoursesAssignmentsInfo> call, Throwable t) {
                Log.e("CalenderViewActivity", "mod_assign_get_assignments failed");
            }
        };
    }


    public HashMap<CoursesAssignmentsInfo.CoursesData, List<CoursesAssignmentsInfo.CoursesData.Assignment>> getAssignmentsByCourse() {
        return assignmentsByCourse;
    }
}
