package com.example.myapplication.CourseInformation.CourseForums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.Constants.moodleApi;

public class ForumViewActivity extends AppCompatActivity {
    private TextView forumTitle;
    private HtmlTextView forumDescription;
    private Gson gson = new Gson();
    private String token;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_view);
        forumDescription = findViewById(R.id.forumDescriptionTextView);
        forumTitle = findViewById(R.id.forumTitle);
        recyclerView = findViewById(R.id.discusiionsRecyclerView);
        layoutManager = new LinearLayoutManager(this);

        Intent intent = getIntent();
        ForumInfo forum = gson.fromJson(intent.getStringExtra(Constants.FORUM),
                ForumInfo.class);
        token = intent.getStringExtra(Constants.TOKEN);
        forumTitle.setText(forum.getName());
        forumDescription.setHtml(forum.getIntro());
        Call<DiscussionInfo> call = Constants.moodleApi.getDiscussionsOfForum(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_forum_get_forum_discussions", forum.getId());
        call.enqueue(new Callback<DiscussionInfo>() {
            @Override
            public void onResponse(Call<DiscussionInfo> call, Response<DiscussionInfo> response) {
                adapter = new DiscussionViewAdapter(token,response.body().discussions);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<DiscussionInfo> call, Throwable t) {

            }
        });
    }
}
