package com.example.myapplication.CourseInformation;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.AssignmentSubmissions.AssignmentStatusActivity;
import com.example.myapplication.CourseInformation.CourseForums.ForumInfo;
import com.example.myapplication.CourseInformation.CourseForums.ForumViewActivity;
import com.example.myapplication.R;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseSubSectionViewAdapter extends RecyclerView.Adapter<CourseSubSectionViewAdapter.CourseSubSectionViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback {

    private List<CourseSection.CourseSubSection> courseSubSectionList;
    private Activity activity;
    private Map<String, Integer> iconNames;
    private String token;
    private HashMap<String, ForumInfo> forumInfoHashMap;
    private Gson gson = new Gson();
    private String courseid;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 321:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }


    public CourseSubSectionViewAdapter(List<CourseSection.CourseSubSection> courseList,
                                       Activity activity, String token, HashMap<String,
            ForumInfo> forumInfoHashMap, String courseid) {
        this.courseSubSectionList = new ArrayList<>();
        this.activity = activity;
        this.token = token;
        this.forumInfoHashMap = forumInfoHashMap;
        this.courseid = courseid;
        initHashMap();

        for (CourseSection.CourseSubSection courseSubSection : courseList) {
            if ("false".equals(courseSubSection.noviewlink)) {
                this.courseSubSectionList.add(courseSubSection);
            } else {
                if (!courseSubSection.description.contains("src")) {
                    this.courseSubSectionList.add(courseSubSection);
                }
            }
        }
    }

    private void initHashMap() {
        iconNames = new HashMap<>();
        iconNames.put("assign", R.drawable.assign);
        iconNames.put("assignment", R.drawable.assignment);
        iconNames.put("book", R.drawable.book);
        iconNames.put("chat", R.drawable.chat);
        iconNames.put("choice", R.drawable.choice);
        iconNames.put("data", R.drawable.data);
        iconNames.put("feedback", R.drawable.feedback);
        iconNames.put("folder", R.drawable.folder);
        iconNames.put("forum", R.drawable.forum);
        iconNames.put("glossary", R.drawable.glossary);
        iconNames.put("imscp", R.drawable.imscp);
        iconNames.put("label", R.drawable.label);
        iconNames.put("lesson", R.drawable.lesson);
        iconNames.put("lti", R.drawable.lti);
        iconNames.put("page", R.drawable.page);
        iconNames.put("paypal", R.drawable.paypal);
        iconNames.put("quiz", R.drawable.quiz);
        iconNames.put("resource", R.drawable.resource);
        iconNames.put("scorm", R.drawable.scorm);
        iconNames.put("survey", R.drawable.survey);
        iconNames.put("url", R.drawable.url);
        iconNames.put("wiki", R.drawable.wiki);
    }

    @NonNull
    @Override
    public CourseSubSectionViewAdapter.CourseSubSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_sub_section, parent
                , false);
        return new CourseSubSectionViewAdapter.CourseSubSectionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSubSectionViewHolder holder, int position) {
        final CourseSection.CourseSubSection current = courseSubSectionList.get(position);
        if ("false".equals(current.noviewlink)) {
            holder.courseTextView.setText(current.name);

        } else {
            holder.courseTextView.setVisibility(View.GONE);
            holder.iconImage.setVisibility(View.GONE);
        }
        if (current.description != null && current.description.length() > 0) {
            holder.descriptionTextView.setVisibility(View.VISIBLE);
            holder.descriptionTextView.setHtml(current.description);
        }
        if (current.modicon != null) {
            handleSubSection(holder, current);
        }
        if (current.contents != null && !current.contents.isEmpty()) {
            if (current.contents.size() == 1) {
                final CourseSection.CourseSubSection.CourseSubSectionContents contents =
                        current.contents.get(0);
                FilesAndUrlsHandler filesAndUrlsHandler = new FilesAndUrlsHandler(activity, token);
                holder.courseTextView.setOnClickListener(filesAndUrlsHandler.handleUrlsAndFilesClick(contents));

            }
        }

    }

    private void handleSubSection(@NonNull CourseSubSectionViewHolder holder,
                                  CourseSection.CourseSubSection current) {
        if (!current.modicon.contains("icon") || !"false".equals(current.noviewlink)) {
            Glide.with(activity).load(current.modicon).into(holder.iconImage);
        } else {
            if (iconNames.containsKey(current.modname)) {
                holder.iconImage.setImageResource(iconNames.get(current.modname));
            }
        }
        if ("forum".equals(current.modname)) {
            //TODO
            holder.courseTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (forumInfoHashMap.containsKey(current.id)) {
                        ForumInfo forum = forumInfoHashMap.get(current.id);
                        Intent intent = new Intent(activity, ForumViewActivity.class);
                        intent.putExtra(Constants.TOKEN, token);
                        intent.putExtra(Constants.FORUM, gson.toJson(forum));
                        activity.startActivity(intent);
                    }
                }
            });

        } else if ("folder".equals(current.modname)) {
            holder.courseTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, CourseFolderViewActivity.class);
                    intent.putExtra(Constants.TOKEN, token);
                    intent.putExtra(Constants.CONTENTS_ARR, gson.toJson(current.contents));
                    intent.putExtra(Constants.FOLDER_NAME, current.name);
                    activity.startActivity(intent);
                }
            });
        } else if ("lti".equals(current.modname) || "quiz".equals(current.modname)) {
            holder.courseTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(current.url));
                    activity.startActivity(browserIntent);
                }
            });
        } else if ("assign".equals(current.modname)) {
            //TODO
            holder.courseTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, AssignmentStatusActivity.class);
                    intent.putExtra(Constants.SUB_SECTION_DATA, gson.toJson(current));
                    intent.putExtra(Constants.COURSE_ID,courseid);
                    intent.putExtra(Constants.TOKEN, token);
                    activity.startActivity(intent);
                }


            });

        }
    }

    @Override
    public int getItemCount() {
        return courseSubSectionList.size();
    }


    public static class CourseSubSectionViewHolder extends RecyclerView.ViewHolder {
        public TextView courseTextView;
        public HtmlTextView descriptionTextView;
        public ImageView iconImage;

        public CourseSubSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            courseTextView = itemView.findViewById(R.id.sub_section_textView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            iconImage = itemView.findViewById(R.id.imageView);
        }
    }

}
