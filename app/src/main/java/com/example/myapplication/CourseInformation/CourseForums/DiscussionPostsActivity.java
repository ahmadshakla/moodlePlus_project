package com.example.myapplication.CourseInformation.CourseForums;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

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

public class DiscussionPostsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Gson gson = new Gson();
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_posts);
        Intent intent = getIntent();
        token = intent.getStringExtra(Constants.TOKEN);
        String discussionId = intent.getStringExtra(Constants.DISCUSSION_ID);
        recyclerView = findViewById(R.id.forumPostsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        Call<PostsInfo> call = Constants.moodleApi.getPostsOfDiscussion(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_forum_get_discussion_posts", discussionId);
        call.enqueue(new Callback<PostsInfo>() {
            @Override
            public void onResponse(Call<PostsInfo> call, Response<PostsInfo> response) {
                adapter = new PostsViewAdapter(token,response.body().posts,
                        DiscussionPostsActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PostsInfo> call, Throwable t) {

            }
        });
//        adapter = new PostsViewAdapter(token,);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_POST_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null){
                List<PostsInfo.Post> posts;
                String jsonedArr = data.getStringExtra(Constants.COURSE_ARR);
                if (("[]".equals(jsonedArr))) {
                    posts = new ArrayList<>();
                } else {
                    Type type = new TypeToken<ArrayList<PostsInfo.Post>>() {
                    }.getType();
                    posts = gson.fromJson(jsonedArr, type);
                }
                adapter = new PostsViewAdapter(token,posts,
                        DiscussionPostsActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}
