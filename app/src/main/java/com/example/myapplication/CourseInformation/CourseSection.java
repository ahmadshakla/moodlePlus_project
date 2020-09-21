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

    static class CourseSubSection{
            String id;
            String url;
            String name;
            String instance;
            String description;
            String visible;
            String uservisible;
            String visibleoncoursepage;
            String modicon;
            String modname;
            String modplural;
            String indent;
            String onclick;
            String afterlink;
            String customdata;
            String noviewlink;
            String completion;
            List<CourseSubSectionContents> contents;

            static class CourseSubSectionContents{
                String type;
                String filename;
                String filepath;
                String filesize;
                String fileurl;
                String timecreated;
                String timemodified;
                String sortorder;
                String mimetype;
                String isexternalfile;
                String userid;
                String author;
                String license;
            }
    }



}
