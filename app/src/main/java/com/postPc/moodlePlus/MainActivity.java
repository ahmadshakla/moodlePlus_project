package com.postPc.moodlePlus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String token = "null";
    private SharedPreferences sharedPreferences;
    private String idNum;
    private TextView textView;
    private EditText username;
    private EditText password;
    private Button nextButton;
    //    LoadingDialog loadingDialog = new LoadingDialog(this);
    private ProgressBar progressBar;
    Gson gson = new Gson();
    Retrofit retrofit;
    PeriodicWorkRequest periodicWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.userIdNum);
        nextButton = findViewById(R.id.next_button);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        retrofit = new Retrofit.Builder().baseUrl(Constants.MOODLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    @Override
    protected void onResume() {
        super.onResume();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MoodleApi moodleApi = retrofit.create(MoodleApi.class);
//                Call<LoginReturn> call = moodleApi.getToken(Constants.MOODLE_SERVICE, Constants.USERNAME,
//                        Constants.PASSWORD);
//                getToken(call);
                if (username.getText().length()>0) {
                    Intent intent = new Intent(MainActivity.this, UserLoginActivity.class);
                    intent.putExtra(Constants.ID_NUMBER, username.getText().toString());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"please enter a valid ID number",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }

}
