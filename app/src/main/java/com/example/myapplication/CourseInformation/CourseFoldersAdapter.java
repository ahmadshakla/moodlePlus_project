package com.example.myapplication.CourseInformation;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class CourseFoldersAdapter extends RecyclerView.Adapter<CourseFoldersAdapter.CourseFoldersHolder>{
    private List<CourseSection.CourseSubSection.CourseSubSectionContents> contents;
    private Activity activity;
    private String token;

    public CourseFoldersAdapter(List<CourseSection.CourseSubSection.CourseSubSectionContents> contents, Activity activity,String token) {
        this.contents = contents;
        this.activity = activity;
        this.token = token;
    }

    @NonNull
    @Override
    public CourseFoldersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_in_folder_layout, parent, false);
        return new CourseFoldersHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseFoldersHolder holder, int position) {
        CourseSection.CourseSubSection.CourseSubSectionContents content = contents.get(position);
        holder.textView.setText(content.filename);
        holder.imageView.setOnClickListener(handleUrlsAndFiles(content));
    }

    @Override
    public int getItemCount() {
        return contents==null?0:contents.size();
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


    //--------------------------------- holder class -----------------------------------
    public static class CourseFoldersHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        public CourseFoldersHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.fileTextView);
            imageView = itemView.findViewById(R.id.fileDownlaodImageView);
        }
    }
}
