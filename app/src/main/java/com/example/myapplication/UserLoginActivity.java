package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainMenu.CoursesMenuActivity;
import com.example.myapplication.MainMenu.UserCourse;
import com.example.myapplication.UserInformation.LoginReturn;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserLoginActivity extends AppCompatActivity {
    EditText csUserName;
    EditText password;
    CheckBox checkBox;
    private String idNum;
    private String token = "null";
    private SharedPreferences sharedPreferences;
    private Button loginButton;
    //    LoadingDialog loadingDialog = new LoadingDialog(this);
    PeriodicWorkRequest periodicWorkRequest;

    private ProgressBar progressBar;
    Gson gson = new Gson();
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        csUserName = findViewById(R.id.csUsername);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkBox);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE,MODE_PRIVATE);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    csUserName.setVisibility(View.VISIBLE);
                    password.setHint("CS password");
                }
                else {
                    csUserName.setVisibility(View.GONE);
                }
            }
        });
        Intent intent = getIntent();
        idNum = intent.getStringExtra(Constants.ID_NUMBER);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoodleApi moodleApi = retrofit.create(MoodleApi.class);
                Call<LoginReturn> call;
                if (checkBox.isChecked()) {
                    call = moodleApi.getToken(Constants.MOODLE_SERVICE, csUserName.getText().toString(),
                            password.getText().toString());
                }
                else{
                    call = moodleApi.getToken(Constants.MOODLE_SERVICE, idNum,
                            password.getText().toString());
                }
                getToken(call);

            }
        });
    }


    /**
     * gets the token when the user inserts his username and password
     */
    private void getToken(Call<LoginReturn> call) {
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<LoginReturn>() {
            @Override
            public void onResponse(Call<LoginReturn> call, Response<LoginReturn> response) {
                token = response.body().getToken();
                sharedPreferences.edit().putString(Constants.TOKEN, token).apply();
                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                Data data = new Data.Builder()
                        .build();
                periodicWorkRequest = new PeriodicWorkRequest.Builder(SampleWorker.class,
                        1, TimeUnit.DAYS)
                        .setInitialDelay(3, TimeUnit.HOURS)
                        .setInputData(data)
                        .addTag("periodic")
                        .build();
//                if (sharedPreferences.getBoolean("added work",false)){
//                    WorkManager.getInstance(MainActivity.this).enqueue(periodicWorkRequest);
                sharedPreferences.edit().putBoolean("added work",
                        sharedPreferences.getBoolean("added work", false)).apply();
//                }

//        WorkManager.getInstance(MainActivity.this).cancelWorkById(periodicWorkRequest.getId());
                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                getInfo( token);
                return;
            }

            @Override
            public void onFailure(Call<LoginReturn> call, Throwable t) {
                token = t.getMessage();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserLoginActivity.this,Constants.WRONG_LOGIN,Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

    private void getInfo( final String token) {
        MoodleApi moodleApi = retrofit.create(MoodleApi.class);
        Call<List<UserInfo>> call = moodleApi.getUserInfo(Constants.MOODLE_W_REST_FORMAT,
                token,
                "core_user_get_users_by_field", "username", idNum);
        call.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body().size() != 0) {
                    sharedPreferences.edit().putString(Constants.USER_INFO,
                            gson.toJson(response.body().get(0))).apply();
                    getUserCourses(token, response.body().get(0));
                } else {
                    Toast.makeText(UserLoginActivity.this,Constants.WRONG_LOGIN,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserLoginActivity.this,Constants.WRONG_LOGIN,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * gets the list of the user's courses
     *
     * @param token    the login token
     * @param userInfo the information of the user
     * @return the list of all the user's courses
     */
    private List<UserCourse> getUserCourses(final String token, final UserInfo userInfo) {
        final ArrayList<UserCourse> courses = new ArrayList<>();
        MoodleApi moodleApi = retrofit.create(MoodleApi.class);
        Call<List<UserCourse>> call = moodleApi.getUserCourses(Constants.MOODLE_W_REST_FORMAT, token,
                "core_enrol_get_users_courses", userInfo.getId());
        call.enqueue(new Callback<List<UserCourse>>() {
            @Override
            public void onResponse(Call<List<UserCourse>> call, Response<List<UserCourse>> response) {
                progressBar.setVisibility(View.VISIBLE);
                courses.addAll(response.body());
                Intent intent = new Intent(UserLoginActivity.this, CoursesMenuActivity.class);
                intent.putExtra(Constants.WORK_MANAGER, gson.toJson(periodicWorkRequest));
                intent.putExtra(Constants.COURSE_ARR, gson.toJson(courses));
                intent.putExtra(Constants.TOKEN, token);
                intent.putExtra(Constants.USER_INFO, gson.toJson(userInfo));
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<List<UserCourse>> call, Throwable t) {
                Toast.makeText(UserLoginActivity.this,Constants.WRONG_LOGIN,Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        return courses;
    }
}
