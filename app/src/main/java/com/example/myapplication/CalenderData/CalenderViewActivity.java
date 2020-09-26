package com.example.myapplication.CalenderData;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.myapplication.Constants;
import com.example.myapplication.MainMenu.CoursesViewAdapter;
import com.example.myapplication.MoodleApi;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CalenderViewActivity extends AppCompatActivity {
    private RecyclerView eventsInDay;
    List<EventDay> events;
    Call<CoursesAssignmentsInfo> call;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);
        eventsInDay = findViewById(R.id.eventsRecycelrView);

        events = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoodleApi moodleApi = retrofit.create(MoodleApi.class);
        Intent intent = getIntent();
        String token = intent.getStringExtra(Constants.TOKEN);
        call = moodleApi.getAssignmentsForCourses(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_assign_get_assignments");
        layoutManager = new LinearLayoutManager(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        call.enqueue(createCallBack());
    }

    private Callback<CoursesAssignmentsInfo> createCallBack() {
        return new Callback<CoursesAssignmentsInfo>() {
            @Override
            public void onResponse(Call<CoursesAssignmentsInfo> call, Response<CoursesAssignmentsInfo> response) {
                final HashMap<String, ArrayList<String>> daysOfEvents = new HashMap<>();
                final List<CoursesAssignmentsInfo.CoursesData> courses = response.body().courses;
                for (CoursesAssignmentsInfo.CoursesData courseData : courses) {
                    for (CoursesAssignmentsInfo.CoursesData.Assignments assignment : courseData.assignments) {
                        Calendar calendar = Calendar.getInstance();
                        if (assignment.duedate > 0) {
                            calendar.setTimeInMillis(assignment.duedate * 1000);
                            SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                            String day = dayFormat.format(calendar.getTime());
                            String hour = hourFormat.format(calendar.getTime());
                            if (!daysOfEvents.containsKey(day)) {
                                daysOfEvents.put(day, new ArrayList<String>());
                            }
                            daysOfEvents.get(day).add(hour + " | " + courseData.fullname + " " +
                                    assignment.name);
                            events.add(new EventDay(calendar, R.drawable.ic_calender_cicle));
                        }
                    }
                }
                CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
                calendarView.setEvents(events);
                calendarView.setOnDayClickListener(new OnDayClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDayClick(EventDay eventDay) {
                        Calendar clickedDayCalendar = eventDay.getCalendar();
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        String day = format1.format(clickedDayCalendar.getTime());
                        if (daysOfEvents.containsKey(day)) {
                            adapter = new CalenderEventsAdapter(daysOfEvents.get(day));;
                        }
                        else{
                            adapter = new CalenderEventsAdapter(null);
                            Toast.makeText(CalenderViewActivity.this,"You don't have any events " +
                                    "in " + day,Toast.LENGTH_LONG).show();
                        }
                        eventsInDay.setLayoutManager(layoutManager);
                        eventsInDay.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure(Call<CoursesAssignmentsInfo> call, Throwable t) {
                Log.e("CalenderViewActivity", "mod_assign_get_assignments failed");
            }
        };
    }
}
