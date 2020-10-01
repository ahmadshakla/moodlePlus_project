package com.example.myapplication.CourseInformation.CourseForums;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PostsViewAdapter extends RecyclerView.Adapter<PostsViewAdapter.PostsViewHolder> {


    private String token;
    private List<PostsInfo.Post> posts;
    private HashMap<String, PostsInfo.Post> idsToPostsMap;

    public PostsViewAdapter(String token, List<PostsInfo.Post> posts) {
        this.posts = posts;
        this.token = token;
        idsToPostsMap = new HashMap<>();
        if (posts!=null) {
            for (PostsInfo.Post post : posts) {
                idsToPostsMap.put(post.id, post);
            }
        }
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
        if (!current.hasparent){
            holder.postWriterTextView.setText(current.author.fullname);
        }
        else {
            holder.postWriterTextView.setText(current.author.fullname +" replying to " + Objects.requireNonNull(idsToPostsMap.get(current.parentid)).author.fullname);
        }
        holder.postTitleTextView.setText(current.subject);
        holder.postTextView.setHtml(current.message);
    }

    @Override
    public int getItemCount() {
        return posts == null? 0 : posts.size();
    }

    static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView postWriterTextView;
        TextView postTitleTextView;
        HtmlTextView postTextView;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            postWriterTextView = itemView.findViewById(R.id.postWriterTextView);
            postTitleTextView = itemView.findViewById(R.id.postTitleTextView);
            postTextView = itemView.findViewById(R.id.postTextView);
        }
    }
}
