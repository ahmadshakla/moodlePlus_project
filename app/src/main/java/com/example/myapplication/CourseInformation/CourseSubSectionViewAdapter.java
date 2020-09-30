package com.example.myapplication.CourseInformation;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseForums.ForumInfo;
import com.example.myapplication.CourseInformation.CourseForums.ForumViewActivity;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.pixplicity.sharp.OnSvgElementListener;
import com.pixplicity.sharp.Sharp;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseSubSectionViewAdapter extends RecyclerView.Adapter<CourseSubSectionViewAdapter.CourseSubSectionViewHolder> implements ActivityCompat.OnRequestPermissionsResultCallback {

    private List<CourseSection.CourseSubSection> courseSubSectionList;
    private Activity activity;
    private Map<String,Integer> iconNames;
    private String token;
    private HashMap<String, ForumInfo> forumInfoHashMap;
    private Gson gson = new Gson();
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
                                       Activity activity,String token,HashMap<String, ForumInfo> forumInfoHashMap) {
        this.courseSubSectionList = new ArrayList<>();
        this.activity = activity;
        this.token = token;
        this.forumInfoHashMap = forumInfoHashMap;
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
        iconNames.put("assign",R.drawable.assign);
        iconNames.put("assignment",R.drawable.assignment);
        iconNames.put("book",R.drawable.book);
        iconNames.put("chat",R.drawable.chat);
        iconNames.put("choice",R.drawable.choice);
        iconNames.put("data",R.drawable.data);
        iconNames.put("feedback",R.drawable.feedback);
        iconNames.put("folder",R.drawable.folder);
        iconNames.put("forum",R.drawable.forum);
        iconNames.put("glossary",R.drawable.glossary);
        iconNames.put("imscp",R.drawable.imscp);
        iconNames.put("label",R.drawable.label);
        iconNames.put("lesson",R.drawable.lesson);
        iconNames.put("lti",R.drawable.lti);
        iconNames.put("page",R.drawable.page);
        iconNames.put("paypal",R.drawable.paypal);
        iconNames.put("quiz",R.drawable.quiz);
        iconNames.put("resource",R.drawable.resource);
        iconNames.put("scorm",R.drawable.scorm);
        iconNames.put("survey",R.drawable.survey);
        iconNames.put("url",R.drawable.url);
        iconNames.put("wiki",R.drawable.wiki);
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
        Log.i("HBD", current.name);
        if ( "false".equals(current.noviewlink)) {
            holder.courseTextView.setText(current.name);

        }
        else {
            holder.courseTextView.setVisibility(View.GONE);
            holder.iconImage.setVisibility(View.GONE);
        }
        if (current.description != null && current.description.length() >0){
            holder.descriptionTextView.setVisibility(View.VISIBLE);
            holder.descriptionTextView.setHtml(current.description);
        }
        if (current.modicon != null) {
            if (!current.modicon.contains("icon") || !"false".equals(current.noviewlink)) {
                Glide.with(activity).load(current.modicon).into(holder.iconImage);
//                holder.iconImage.setScaleType(ImageView.ScaleType.);
            }
            else{
                if (iconNames.containsKey(current.modname)) {
                    holder.iconImage.setImageResource(iconNames.get(current.modname));
                }
            }
            if ("forum".equals(current.modname)){
                //TODO
                holder.courseTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (forumInfoHashMap.containsKey(current.id)){
                            ForumInfo forum = forumInfoHashMap.get(current.id);
                            Intent intent = new Intent(activity, ForumViewActivity.class);
                            intent.putExtra(Constants.TOKEN,token);
                            intent.putExtra(Constants.FORUM,gson.toJson(forum));
                            activity.startActivity(intent);
                        }
                    }
                });

            }

        }
        if (current.contents != null && !current.contents.isEmpty()) {
            final CourseSection.CourseSubSection.CourseSubSectionContents contents =
                    current.contents.get(0);
            holder.courseTextView.setOnClickListener(handleUrlsAndFiles(contents));
        }

    }
    private View.OnClickListener handleUrlsAndFiles(CourseSection.CourseSubSection.CourseSubSectionContents contents){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("file".equals(contents.type)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            activity.requestPermissions(permissions, 321);
                        } else {
                            startDownload(contents.fileurl + "&token=" +
                                    token, contents.filename);
                        }
                    }
                }
                else if ("url".equals(contents.type)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents.fileurl));
                    activity.startActivity(browserIntent);
                }
            }
        };
    }

    private void startDownload(String url, String name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(name);
        request.setDescription("downloading file... ");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                name);
        DownloadManager downloadManager =
                (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
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
