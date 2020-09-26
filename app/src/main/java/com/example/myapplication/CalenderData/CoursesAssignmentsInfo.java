package com.example.myapplication.CalenderData;

import java.util.List;

public class CoursesAssignmentsInfo {
    List<CoursesData> courses;
    public static class CoursesData{
			String id;
            String fullname;
            String shortname;
            String timemodified;
            List<Assignments> assignments;

            public static class Assignments{
                String id ;
                String name ;
                String course;
                long duedate;

            }
    }
}
