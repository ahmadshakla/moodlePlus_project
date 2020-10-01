package com.example.myapplication.CourseInformation.CourseForums;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDiscussionActivity extends AppCompatActivity {
    EditText newSubject;
    EditText newMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discussion);
        Gson gson = new Gson();
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
                //TODO fix the name
                DiscussionInfo.Discussion disc = new DiscussionInfo.Discussion(topic, message, true);
                Call<PostDiscussionReturnInfo> call =
                        Constants.moodleApi.getReturnResOfNewPost(disc
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
