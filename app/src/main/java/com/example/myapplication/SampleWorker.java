package com.example.myapplication;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.WorkerParameters;

import com.example.myapplication.CalenderData.CoursesAssignmentsInfo;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.Constants.moodleApi;

public class SampleWorker extends androidx.work.Worker {
    private static final String TAG = "SampleWorker";
    private SharedPreferences sharedPreferences;
    private NotificationManagerCompat notificationManager;
    private Context context;
    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context  = context;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        notificationManager = NotificationManagerCompat.from(context);

    }

    @NonNull
    @Override
    public Result doWork() {
        Call<CoursesAssignmentsInfo> call;

        String token = sharedPreferences.getString(Constants.TOKEN, "");
        if (token.length()==0){
            Log.d(TAG, "invalid token" );
            return Result.failure();

        }
        call = moodleApi.getAssignmentsForCourses(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_assign_get_assignments");
        call.enqueue(new Callback<CoursesAssignmentsInfo>() {
            @Override
            public void onResponse(Call<CoursesAssignmentsInfo> call, Response<CoursesAssignmentsInfo> response) {
                Log.d(TAG, "doWork: onResponse");
                Calendar calendar2 = Calendar.getInstance();
                int id=1;
                if (response.body().courses != null){
                    for (CoursesAssignmentsInfo.CoursesData course : response.body().courses) {
                        for (CoursesAssignmentsInfo.CoursesData.Assignment assignment : course.assignments) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(assignment.duedate*1000);
                            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
                            String day = format1.format(calendar.getTime());
                            String hour = format2.format(calendar.getTime());
                            String today = format1.format(calendar2.getTime());
                            if (day.equals(today)){
                                Notification notification = new NotificationCompat.Builder(context,
                                        MoodleApp.CHANNEL_ID)
                                        .setSmallIcon(R.drawable.logo3)
                                        .setContentTitle(assignment.name)
                                        .setContentText(hour)
                                        .build();
                                notificationManager.notify(id++, notification);
                            }

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CoursesAssignmentsInfo> call, Throwable t) {
                Log.d(TAG, "doWork: onFailure");

            }
        });
        return Result.success();
    }
}
