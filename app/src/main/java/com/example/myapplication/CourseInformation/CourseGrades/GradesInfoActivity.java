package com.example.myapplication.CourseInformation.CourseGrades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.MainMenu.UserCourse;
import com.example.myapplication.MoodleApi;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GradesInfoActivity extends AppCompatActivity {
    private RecyclerView gradeRecycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_info);
        Intent intent = getIntent();
        final Gson gson = new Gson();
        final String token = intent.getStringExtra(Constants.TOKEN);
        final UserCourse userCourse = gson.fromJson(intent.getStringExtra(Constants.COURSE_SECTION),
                UserCourse.class);
        final UserInfo userInfo = gson.fromJson(intent.getStringExtra(Constants.USER_INFO),
                UserInfo.class);
        TextView titleTextView = findViewById(R.id.grade_info_text_view);
        titleTextView.setText(userCourse.getDisplayname());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoodleApi moodleApi = retrofit.create(MoodleApi.class);
        Call<GradesTable> call = moodleApi.getGradesInfo(Constants.MOODLE_W_REST_FORMAT, token,
                "gradereport_user_get_grade_items", userCourse.getId(), userInfo.getId());
        call.enqueue(new Callback<GradesTable>() {
            @Override
            public void onResponse(Call<GradesTable> call, Response<GradesTable> response) {
                GradesTable gradesTable = response.body();
                if (gradesTable.usergrades.length > 0) {
                    GradesTable.UserGrades.GradesItem[] gradeSheet = gradesTable.usergrades[0].gradeitems;
                    gradeRecycler = findViewById(R.id.recycler_view_grade_sheet);
                    layoutManager = new LinearLayoutManager(GradesInfoActivity.this);
                    adapter = new GradesViewAdapter(gradeSheet);

                    gradeRecycler.setLayoutManager(layoutManager);
                    gradeRecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GradesTable> call, Throwable t) {


            }
        });
    }
}
