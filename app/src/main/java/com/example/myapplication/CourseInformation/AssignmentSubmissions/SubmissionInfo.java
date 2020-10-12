package com.example.myapplication.CourseInformation.AssignmentSubmissions;

import java.util.List;

public class SubmissionInfo {
    public LastAttempt lastattempt;
    public Feedback feedback;

    public static class LastAttempt {
        Submissions submission;
        public static class Submissions{

            public String id;
            public String userid;
            public String attemptnumber;
            public String timecreated;
            public String timemodified;
            public String status;
            public String groupid;
            public String assignment;
            public String latest;
            public List<LastAttempt.Plugins> plugins;
        }

        public static class Plugins {
            public String type;
            public List<FileArea> fileareas;

            public static class FileArea {
                public List<SubmissionFile> files;
                public String area;
                public static class SubmissionFile {
                    public String filename;
                    public String filepath;
                    public String filesize;
                    public String fileurl;
                    public String timemodified;
                    public String mimetype;
                    public String isexternalfile;
                }
            }
        }
    }

    public static class Feedback {
        public Grade grade;
        public String gradefordisplay;
        public String gradeddate;
        public List<Feedback.Plugins> plugins;
        public static class Grade {
            public String id;
            public String assignment;
            public String userid;
            public String attemptnumber;
            public String timecreated;
            public String timemodified;
            public String grader;
            public String grade;
        }
        public static class Plugins {
            public String type;
            public List<Feedback.Plugins.FileArea> fileareas;

            public static class FileArea {
                public List<Feedback.Plugins.FileArea.SubmissionFile> files;
                public String area;

                public static class SubmissionFile {
                    public String filename;
                    public String filepath;
                    public String filesize;
                    public String fileurl;
                    public String timemodified;
                    public String mimetype;
                    public String isexternalfile;
                }
            }
        }
    }
}
