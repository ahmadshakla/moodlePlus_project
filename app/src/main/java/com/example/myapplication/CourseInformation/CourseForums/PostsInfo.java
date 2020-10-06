package com.example.myapplication.CourseInformation.CourseForums;

import java.util.List;

public class PostsInfo {
    public List<Post> posts;
    public static class Post {
        public String id;
        public String subject;
        public String replysubject;
        public String message;
        public String discussionid;
        public boolean hasparent;
        public String parentid;
        public Author author;
        public static class Author {
            public String id;
            public String fullname;
        }
    }

}
