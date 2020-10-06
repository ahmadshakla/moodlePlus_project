package com.example.myapplication.CourseInformation;

import java.util.List;

public class CourseSection {
    private String hiddenbynumsections;
    private String id;
    private List<CourseSubSection> modules;
    private String name;
    private String section;
    private String summary;
    private String summaryformat;
    private String uservisible;
    private String visible;

    public String getHiddenbynumsections() {
        return hiddenbynumsections;
    }

    public String getId() {
        return id;
    }

    public List<CourseSubSection> getModules() {
        return modules;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public String getSummary() {
        return summary;
    }

    public String getSummaryformat() {
        return summaryformat;
    }

    public String getUservisible() {
        return uservisible;
    }

    public String getVisible() {
        return visible;
    }

    public static class CourseSubSection{
            public String id;
            public String url;
            public String name;
            public String instance;
            public String description;
            public String visible;
            public String uservisible;
            public String visibleoncoursepage;
            public String modicon;
            public String modname;
            public String modplural;
            public String indent;
            public String onclick;
            public String afterlink;
            public String customdata;
            public String noviewlink;
            public String completion;
            public List<CourseSubSectionContents> contents;

            public static class CourseSubSectionContents{
               public String type;
               public String filename;
               public String filepath;
               public String filesize;
               public String fileurl;
               public String timecreated;
               public String timemodified;
               public String sortorder;
               public String mimetype;
               public String isexternalfile;
               public String userid;
               public String author;
               public String license;
            }
    }



}
