package com.example.myapplication.CourseInformation.CourseForums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscussionPostsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_posts);
        Intent intent = getIntent();
        String token = intent.getStringExtra(Constants.TOKEN);
        String discussionId = intent.getStringExtra(Constants.DISCUSSION_ID);
        recyclerView = findViewById(R.id.forumPostsRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        Call<PostsInfo> call = Constants.moodleApi.getPostsOfDiscussion(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_forum_get_discussion_posts", discussionId);
        call.enqueue(new Callback<PostsInfo>() {
            @Override
            public void onResponse(Call<PostsInfo> call, Response<PostsInfo> response) {
                adapter = new PostsViewAdapter(token,response.body().posts);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PostsInfo> call, Throwable t) {

            }
        });
//        adapter = new PostsViewAdapter(token,);
    }
}
