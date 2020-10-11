package com.example.myapplication.CourseInformation.AssignmentSubmissions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.CalenderData.CoursesAssignmentsInfo;
import com.example.myapplication.Constants;
import com.example.myapplication.CourseInformation.CourseSection;
import com.example.myapplication.R;
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
        final String token = intent.getStringExtra(Constants.TOKEN);
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
                                            if (assignment.duedate>0){
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTimeInMillis(assignment.duedate*1000);
                                                SimpleDateFormat dayFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                                                String day = dayFormat.format(calendar.getTime());
                                                String hour = hourFormat.format(calendar.getTime());
                                                dueDate.setText(dueDate.getText() + day + " at " + hour);

                                            }
                                            addSubmissionButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent =
                                                            new Intent(AssignmentStatusActivity.this,
                                                            AssignmentSubmissionActivity.class);
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
}
