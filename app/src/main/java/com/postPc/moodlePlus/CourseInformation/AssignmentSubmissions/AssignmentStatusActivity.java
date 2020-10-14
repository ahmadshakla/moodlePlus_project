package com.postPc.moodlePlus.CourseInformation.AssignmentSubmissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.postPc.moodlePlus.CalenderData.CoursesAssignmentsInfo;
import com.postPc.moodlePlus.Constants;
import com.postPc.moodlePlus.CourseInformation.CourseSection;
import com.postPc.moodlePlus.R;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentStatusActivity extends AppCompatActivity {
    HtmlTextView authorText;
    TextView dueDate;
    TextView submissionStatus;
    Button gradeStatusButton;
    Button addSubmissionButton;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_status);
        authorText = findViewById(R.id.assignmetAuthorText);
//        authorText.setMovementMethod(new ScrollingMovementMethod());
        dueDate = findViewById(R.id.dueDateText);
        submissionStatus = findViewById(R.id.submissionStatusText);
        gradeStatusButton = findViewById(R.id.gradeStatusButton);
        addSubmissionButton = findViewById(R.id.addSubmissionButton);
        Intent intent = getIntent();
        final Gson gson = new Gson();
        token = intent.getStringExtra(Constants.TOKEN);
        final CourseSection.CourseSubSection subSection =
                gson.fromJson(intent.getStringExtra(Constants.SUB_SECTION_DATA),
                        CourseSection.CourseSubSection.class);
        final String courseid = intent.getStringExtra(Constants.COURSE_ID);
        Call<CoursesAssignmentsInfo> call =
                Constants.moodleApi.getAssignmentsForOneCourse(Constants.MOODLE_W_REST_FORMAT,
                        token,
                        "mod_assign_get_assignments", courseid);
        call.enqueue(new Callback<CoursesAssignmentsInfo>() {
            @Override
            public void onResponse(Call<CoursesAssignmentsInfo> call, Response<CoursesAssignmentsInfo> response) {
                if (response.body().courses != null) {
                    if (response.body().courses.get(0).assignments != null) {
                        List<CoursesAssignmentsInfo.CoursesData.Assignment> assignments =
                                response.body().courses.get(0).assignments;
                        if (assignments != null) {
                            for (CoursesAssignmentsInfo.CoursesData.Assignment assignment :
                                    assignments) {
                                if (assignment.cmid.equals(subSection.id)) {
                                    Call<SubmissionInfo> infoCall =
                                            Constants.moodleApi.getReturnResOfViewSubmissions(Constants.MOODLE_W_REST_FORMAT,
                                                    token, "mod_assign_get_submission_status",
                                                    assignment.id);
                                    infoCall.enqueue(new Callback<SubmissionInfo>() {
                                        @Override
                                        public void onResponse(Call<SubmissionInfo> call, Response<SubmissionInfo> response) {
                                            authorText.setHtml(assignment.intro);
                                            if (assignment.duedate > 0) {
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTimeInMillis(assignment.duedate * 1000);
                                                SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                                                String day = dayFormat.format(calendar.getTime());
                                                String hour = hourFormat.format(calendar.getTime());
                                                dueDate.setText(dueDate.getText() + day + " at " + hour);
                                                setSubmissionStatus(response.body().lastattempt);
                                                gradeStatusButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        SubmissionPopup submissionPopup =
                                                                new SubmissionPopup(AssignmentStatusActivity.this,
                                                                        response.body().feedback, token);
                                                        submissionPopup.show(((FragmentActivity) AssignmentStatusActivity.this).getSupportFragmentManager(),
                                                                "example notification dialog");
                                                    }
                                                });

                                            }
                                            addSubmissionButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent =
                                                            new Intent(AssignmentStatusActivity.this,
                                                                    AssignmentSubmissionActivity.class);
                                                    intent.putExtra(Constants.SUB_SECTION_DATA,
                                                            gson.toJson(subSection));
                                                    intent.putExtra(Constants.TOKEN, token);
                                                    startActivity(intent);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(Call<SubmissionInfo> call, Throwable t) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CoursesAssignmentsInfo> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 321:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    private void setSubmissionStatus(SubmissionInfo.LastAttempt lastAttempt) {
        boolean submitted = false;
        String submissionUrl = "";
        String submissionName = "";
        for (SubmissionInfo.LastAttempt.Plugins plugin : lastAttempt.submission.plugins) {
            if (plugin.fileareas != null) {
                for (SubmissionInfo.LastAttempt.Plugins.FileArea fileArea : plugin.fileareas) {
                    if (fileArea.files != null) {
                        for (SubmissionInfo.LastAttempt.Plugins.FileArea.SubmissionFile submissionFile :
                                fileArea.files) {
                            if ("submission_files".equals(fileArea.area)) {
                                submissionUrl = submissionFile.fileurl;
                                submissionName = submissionFile.filename;
                            }
                            submitted = true;
                        }
                    }
                }
            }
        }
        if (submitted) {
            submissionStatus.setText("Submitted for grading");
            String finalSubmissionUrl = submissionUrl;
            String finalSubmissionName = submissionName;
            submissionStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, 321);
                        } else {
                            if (!finalSubmissionUrl.isEmpty()) {
                                startDownload(finalSubmissionUrl + "?token=" + token, finalSubmissionName);
                            }
                        }
                    }
                }
            });
        } else {
            submissionStatus.setText("Not submitted");

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
                (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
    }
}
