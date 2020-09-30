package com.example.myapplication.CourseInformation.CourseForums;

import java.util.List;

public class DiscussionInfo {
    public List<Discussion> discussions;
    public static class Discussion{
        String id;
        String name;
        String groupid;
        String parent;
        String userfullname;
        String message;
    }
}
