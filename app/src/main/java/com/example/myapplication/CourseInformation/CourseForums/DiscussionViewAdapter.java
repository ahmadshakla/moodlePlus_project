package com.example.myapplication.CourseInformation.CourseForums;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseSectionViewAdapter;
import com.example.myapplication.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import javax.security.auth.callback.Callback;

public class DiscussionViewAdapter extends RecyclerView.Adapter<DiscussionViewAdapter.DiscussionViewHolder> {
    private String token;
    private List<DiscussionInfo.Discussion> discussions;
    Activity activity;

    public DiscussionViewAdapter(String token, List<DiscussionInfo.Discussion> discussions,
                                 Activity activity) {
        this.token = token;
        this.discussions = discussions;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_discussion_layout, parent, false);
        return new DiscussionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        DiscussionInfo.Discussion current = discussions.get(position);
        holder.nameAndTitle.setText(current.userfullname + ": " + current.name);

        holder.showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("show more".equals(holder.showMore.getText().toString())) {
                    String summery = current.message;
                    if (summery != null && summery.contains("src=\"")) {
                        int start = summery.indexOf("src=\"") + 5;
                        int end = summery.indexOf("\"", start);
                        String first = summery.substring(0, start);
                        String img = summery.substring(start, end);
                        String last = summery.substring(end);
                        if (img.contains("?")) {
                            img += ("&token=" + token);
                        } else {
                            img += ("?token=" + token);
                        }
                        summery = first + img + last;
                    }
                    if (summery != null) {
                        holder.discussionText.setHtml(summery, new HtmlHttpImageGetter(holder.discussionText));
                    }
                    holder.discussionText.setVisibility(View.VISIBLE);
                    if (current.canreply)
                        holder.reply.setVisibility(View.VISIBLE);
                    holder.showMore.setText("show less");

                } else {
                    holder.discussionText.setVisibility(View.GONE);
                    holder.reply.setVisibility(View.GONE);
                    holder.showMore.setText("show more");

                }
            }
        });
        if (current.canreply) {

            holder.nameAndTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,DiscussionPostsActivity.class);
                    intent.putExtra(Constants.TOKEN,token);
                    intent.putExtra(Constants.DISCUSSION_ID,current.discussion);
                    activity.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return discussions == null ? 0 : discussions.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        TextView nameAndTitle;
        HtmlTextView discussionText;
        Button showMore;
        ImageView reply;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameAndTitle = itemView.findViewById(R.id.discussionNameAndTitle);
            discussionText = itemView.findViewById(R.id.discussionText);
            showMore = itemView.findViewById(R.id.showMoreButton);
            reply = itemView.findViewById(R.id.replyImageView);
        }
    }
}
