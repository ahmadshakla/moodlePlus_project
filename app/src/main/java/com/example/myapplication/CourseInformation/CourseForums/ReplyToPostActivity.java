package com.example.myapplication.CourseInformation.CourseForums;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyToPostActivity extends AppCompatActivity {
    TextView title;
    EditText message;
    Button cancel;
    Button reply;
    List<PostsInfo.Post> posts;
    Intent newIntent;
    String token;
    PostsInfo.Post post;
    Gson gson = new Gson();
    UserInfo userInfo;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_to_post);
        title = findViewById(R.id.replyToPostTitle);
        message = findViewById(R.id.addPostMessage);
        cancel = findViewById(R.id.cancelAddPostButton);
        reply = findViewById(R.id.addPostButton);
        Intent intent = getIntent();
        String jsoned = intent.getStringExtra(Constants.POST_INFO);
        post = gson.fromJson(jsoned,
                PostsInfo.Post.class);
        title.setText(post.replysubject);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE,MODE_PRIVATE);
        userInfo = gson.fromJson(sharedPreferences.getString(Constants.USER_INFO,""),
                UserInfo.class);
        token = intent.getStringExtra(Constants.TOKEN);
        String jsonedArr = intent.getStringExtra(Constants.COURSE_ARR);
        if (("[]".equals(jsonedArr))) {
            posts = new ArrayList<>();
        } else {
            Type type = new TypeToken<ArrayList<PostsInfo.Post>>() {
            }.getType();
            posts = gson.fromJson(jsonedArr, type);
        }
        newIntent = new Intent();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newIntent.putExtra(Constants.COURSE_ARR, gson.toJson(posts));
                setResult(RESULT_OK, newIntent);
                finish();
            }
        });
        reply.setOnClickListener(handleNormalClick());

    }

    private View.OnClickListener handleNormalClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = message.getText().toString();
                String t = title.getText().toString();
                if (m.length() == 0){
                    Toast.makeText(ReplyToPostActivity.this,"You didn't enter a message",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Call<AddPostReturnInfo> call =
                        Constants.moodleApi.getReturnResOfNewPost(Constants.MOODLE_W_REST_FORMAT,
                                token,
                                "mod_forum_add_discussion_post", post.id, t, m);

                call.enqueue(new Callback<AddPostReturnInfo>() {
                    @Override
                    public void onResponse(Call<AddPostReturnInfo> call, Response<AddPostReturnInfo> response) {
                        AddPostReturnInfo newPost = response.body();
                        posts.add(0,newPost.post);
                        newIntent.putExtra(Constants.COURSE_ARR, gson.toJson(posts));
                        setResult(RESULT_OK, newIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<AddPostReturnInfo> call, Throwable t) {

                    }
                });
            }


        };
    }
}
