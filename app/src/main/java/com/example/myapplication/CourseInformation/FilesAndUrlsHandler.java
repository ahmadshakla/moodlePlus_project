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
import android.view.View;

public class FilesAndUrlsHandler {
    private Activity activity;
    private String token;

    public FilesAndUrlsHandler(Activity activity, String token) {
        this.activity = activity;
        this.token = token;
    }

    public View.OnClickListener handleUrlsAndFilesClick(CourseSection.CourseSubSection.CourseSubSectionContents contents) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFilesAndUrls(contents);
            }
        };
    }
    public void handleFilesAndUrls(CourseSection.CourseSubSection.CourseSubSectionContents contents){
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
            } else if ("url".equals(contents.type)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents.fileurl));
                activity.startActivity(browserIntent);
            }
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
}
