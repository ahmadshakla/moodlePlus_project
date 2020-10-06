package com.example.myapplication.CourseInformation.CourseForums;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumViewActivity extends AppCompatActivity {
    private TextView forumTitle;
    private HtmlTextView forumDescription;
    private TextView addNewDiscussion;
    private Gson gson = new Gson();
    private ForumInfo forum;
    private String token;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_view);
        intent1 = new Intent(ForumViewActivity.this, AddDiscussionActivity.class);

        forumDescription = findViewById(R.id.forumDescriptionTextView);
        forumTitle = findViewById(R.id.forumTitle);
        recyclerView = findViewById(R.id.discusiionsRecyclerView);
        addNewDiscussion = findViewById(R.id.addNewDescussionTextView);
        layoutManager = new LinearLayoutManager(this);
        progressBar = findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        forum = gson.fromJson(intent.getStringExtra(Constants.FORUM),
                ForumInfo.class);
        if (!forum.getCancreatediscussions()) {
            addNewDiscussion.setVisibility(View.GONE);
        }
        token = intent.getStringExtra(Constants.TOKEN);
        forumTitle.setText(forum.getName());
        if (forum.getIntro() == null || forum.getIntro().length() == 0) {
            forumDescription.setVisibility(View.GONE);
        }
        forumDescription.setHtml(forum.getIntro());
        Call<DiscussionInfo> call = Constants.moodleApi.getDiscussionsOfForum(Constants.MOODLE_W_REST_FORMAT,
                token,
                "mod_forum_get_forum_discussions", forum.getId());
        call.enqueue(getDiscussionsCallBack());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.ADD_DISCUSSION_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                forum = gson.fromJson(data.getStringExtra(Constants.FORUM),
                        ForumInfo.class);
                token = data.getStringExtra(Constants.TOKEN);
                DiscussionInfo discussions = gson.fromJson(data.getStringExtra(Constants.DISCUSSION_INFO),
                        DiscussionInfo.class);
                adapter = new DiscussionViewAdapter(token, discussions.discussions,
                        ForumViewActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private  Callback<DiscussionInfo> getDiscussionsCallBack(){
        return new Callback<DiscussionInfo>() {
            @Override
            public void onResponse(Call<DiscussionInfo> call, Response<DiscussionInfo> response) {
                adapter = new DiscussionViewAdapter(token, response.body().discussions,
                        ForumViewActivity.this);
                addNewDiscussion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent1.putExtra(Constants.FORUM, gson.toJson(forum));
                        intent1.putExtra(Constants.TOKEN, gson.toJson(token));
                        intent1.putExtra(Constants.DISCUSSION_INFO, gson.toJson(response.body()));
                        startActivityForResult(intent1, Constants.ADD_DISCUSSION_REQUEST_CODE);

                    }
                });

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<DiscussionInfo> call, Throwable t) {
                Log.e(ForumViewActivity.class.getName(), "mod_forum_get_forum_discussions failed");
                progressBar.setVisibility(View.INVISIBLE);

            }
        };
    }
 }
