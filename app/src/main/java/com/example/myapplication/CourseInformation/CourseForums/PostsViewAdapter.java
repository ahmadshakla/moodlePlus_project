package com.example.myapplication.CourseInformation.CourseForums;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.UserInformation.UserInfo;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PostsViewAdapter extends RecyclerView.Adapter<PostsViewAdapter.PostsViewHolder> implements DeletePopupDialog.DeletePopupDialogListener {


    private String token;
    private List<PostsInfo.Post> posts;
    private HashMap<String, PostsInfo.Post> idsToPostsMap;
    private Activity activity;
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private UserInfo userInfo;

    public PostsViewAdapter(String token, List<PostsInfo.Post> posts, Activity activity) {
        this.posts = posts;
        this.token = token;
        sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        userInfo = gson.fromJson(sharedPreferences.getString(Constants.USER_INFO, ""),
                UserInfo.class);
        idsToPostsMap = new HashMap<>();
        if (posts != null) {
            for (PostsInfo.Post post : posts) {
                idsToPostsMap.put(post.id, post);
            }
        }
        this.activity = activity;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_in_forum_layout, parent, false);
        return new PostsViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        PostsInfo.Post current = posts.get(posts.size() - 1 - position);
        if (!current.hasparent) {
            holder.postWriterTextView.setText(current.author.fullname);
        } else {
            holder.postWriterTextView.setText(current.author.fullname + " replying to " + Objects.requireNonNull(idsToPostsMap.get(current.parentid)).author.fullname);
        }
        holder.postTitleTextView.setText(current.subject);
        holder.postTextView.setHtml(current.message);
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ReplyToPostActivity.class);
                intent.putExtra(Constants.TOKEN, token);
                intent.putExtra(Constants.POST_INFO, gson.toJson(current));
                intent.putExtra(Constants.COURSE_ARR, gson.toJson(posts));
                activity.startActivityForResult(intent, Constants.ADD_POST_REQUEST_CODE);
            }
        });
        holder.postTitleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (current.author.id != null) {
                    if (current.author.id.equals(userInfo.getId())) {
                        handlepopUp(position);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    @Override
    public boolean getAnswer(boolean answer) {
        return answer;
    }

    private void handlepopUp(int position) {
        PostsInfo.Post current = posts.get(posts.size() - 1 - position);
        AlertDialog.Builder builder = new AlertDialog.Builder((activity));
        builder.setTitle("Delete post")
                .setMessage("are you sure you want to delete this post?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Call<DeletePostReturnInfo> call =
                        Constants.moodleApi.getReturnResOfDeletePost(Constants.MOODLE_W_REST_FORMAT,
                                token,
                                "mod_forum_delete_post", current.id);
                call.enqueue(new Callback<DeletePostReturnInfo>() {
                    @Override
                    public void onResponse(Call<DeletePostReturnInfo> call, Response<DeletePostReturnInfo> response) {
                        if (!response.body().status) {
                            Toast.makeText(activity, "Post not deleted, you have only " +
                                            "30 minutes to delete a post",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        posts.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeRemoved(position, posts.size());
                        if (!current.hasparent) {
                            activity.finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<DeletePostReturnInfo> call, Throwable t) {

                    }
                });

            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ;
    }

    static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView postWriterTextView;
        TextView postTitleTextView;
        HtmlTextView postTextView;
        ImageView reply;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            postWriterTextView = itemView.findViewById(R.id.postWriterTextView);
            postTitleTextView = itemView.findViewById(R.id.postTitleTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
            reply = itemView.findViewById(R.id.replyToPostImageview);
        }
    }
}
