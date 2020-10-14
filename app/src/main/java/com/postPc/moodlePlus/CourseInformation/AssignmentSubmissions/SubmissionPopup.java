package com.postPc.moodlePlus.CourseInformation.AssignmentSubmissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;

import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SubmissionPopup extends AppCompatDialogFragment implements ActivityCompat.OnRequestPermissionsResultCallback {
    Activity activity;
    SubmissionInfo.Feedback feedback;
    String token;
    TextView status;
    TextView grade;
    TextView feedbackTextView;
    ImageView download;

    SharedPreferences sharedPreferences;
    public SubmissionPopup(Activity activity, SubmissionInfo.Feedback feedback,String token) {
        this.feedback = feedback;
        this.token = token;
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREFERENCE,MODE_PRIVATE);
    }

    private EditText phoneNum;

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.submission_popup_layout, null);
        status = view.findViewById(R.id.submissionPopupStatus);
        grade = view.findViewById(R.id.submissionPopuoGrade);
        feedbackTextView = view.findViewById(R.id.submissionPopupFeedback);
        download= view.findViewById(R.id.submissionPopupDownload);
        setUI();

        builder.setView(view)
                .setTitle("Grade status")
                .setCancelable(true);
        return builder.create();
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

    private void setUI() {
        if (feedback!= null){
            status.setText(status.getText() + "Graded");

            if (!"null".equals(feedback.grade.grade)){
                grade.setText(grade.getText() + feedback.grade.grade);
            }
            if (feedback.plugins!=null){
                for (SubmissionInfo.Feedback.Plugins plugin : feedback.plugins){
                    if (plugin.fileareas!=null){
                        for (SubmissionInfo.Feedback.Plugins.FileArea fileArea:plugin.fileareas){
                            if (fileArea.files!=null){
                                for (SubmissionInfo.Feedback.Plugins.FileArea.SubmissionFile file :
                                        fileArea.files){
                                    if ("download".equals(fileArea.area)){
                                        feedbackTextView.setText(feedbackTextView.getText()+file.filename);
                                        download.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                            == PackageManager.PERMISSION_DENIED) {
                                                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                        requestPermissions(permissions, 321);
                                                    } else {
                                                        startDownload(file.fileurl + "?token="+token,file.filename);
                                                    }
                                                }

                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            status.setText(status.getText() + "Not Graded");

        }
    }

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
}
