package com.postPc.moodlePlus.CourseInformation.CourseForums;

import java.util.List;

public class DiscussionInfo {
    public List<Discussion> discussions;
    public static class Discussion{
        String id;
        String name;
        String groupid;
        String parent;
        String discussion;
        String userfullname;
        String message;
        boolean canreply;

        public Discussion(String name,String message, boolean canreply) {
            this.name = name;
            this.message = message;
            this.canreply = canreply;
        }
    }
}
