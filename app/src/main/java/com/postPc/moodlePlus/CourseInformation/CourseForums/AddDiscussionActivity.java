package com.postPc.moodlePlus.CourseInformation.CourseForums;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.R;
import com.postPc.moodlePlus.UserInformation.UserInfo;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDiscussionActivity extends AppCompatActivity {
    EditText newSubject;
    EditText newMessage;
    SharedPreferences sharedPreferences;
    UserInfo userInfo;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discussion);
        gson = new Gson();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE,MODE_PRIVATE);
        UserInfo userInfo = gson.fromJson(sharedPreferences.getString(Constants.USER_INFO,""),
                UserInfo.class);
        Intent intent1 = getIntent();
        newSubject = findViewById(R.id.addDiscussionTopic);
        newMessage = findViewById(R.id.addDiscussionMessage);
        ForumInfo forum = gson.fromJson(intent1.getStringExtra(Constants.FORUM),
                ForumInfo.class);
        String token = intent1.getStringExtra(Constants.TOKEN);
        if (token != null && token.startsWith("\"")) {
            token = token.substring(1,token.length()-1);
        }
        Button cancel = findViewById(R.id.cancelAddDiscussionButton);
        Button postDiscussion = findViewById(R.id.postDiscussionButton);
        Intent intent = new Intent();
        intent.putExtra(Constants.FORUM,gson.toJson(forum));
        intent.putExtra(Constants.TOKEN,token);
        String jsoned = intent1.getStringExtra(Constants.DISCUSSION_INFO);
        DiscussionInfo discussionInfo = gson.fromJson(jsoned,DiscussionInfo.class);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra(Constants.DISCUSSION_INFO, gson.toJson(discussionInfo));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        String finalToken = token;
        postDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topic = newSubject.getText().toString();
                String message = newMessage.getText().toString();
                if (topic.length() == 0 || message.length() == 0){
                    Toast.makeText(AddDiscussionActivity.this,"You should enter a topic and a " +
                                    "message!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //TODO fix the name
                DiscussionInfo.Discussion disc = new DiscussionInfo.Discussion(topic, message, true);
                Call<PostDiscussionReturnInfo> call =
                        Constants.moodleApi.getReturnResOfNewDiscussion(disc
                                ,Constants.MOODLE_W_REST_FORMAT,
                                finalToken,
                        "mod_forum_add_discussion", forum.getId(),
                                topic,
                                message);
                call.enqueue(new Callback<PostDiscussionReturnInfo>() {
                    @Override
                    public void onResponse(Call<PostDiscussionReturnInfo> call,
                                           Response<PostDiscussionReturnInfo> response) {
                        disc.discussion = response.body().discussionid;
                        disc.userfullname = userInfo.getFullname();
                        discussionInfo.discussions.add(0,disc);
                        intent.putExtra(Constants.DISCUSSION_INFO, gson.toJson(discussionInfo));
                        setResult(RESULT_OK,intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<PostDiscussionReturnInfo> call, Throwable t) {

                    }
                });
            }
        });

    }
}
