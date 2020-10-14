package com.postPc.moodlePlus.CalenderData;

import java.util.List;

public class CoursesAssignmentsInfo {
    public List<CoursesData> courses;
    public static class CoursesData{
			public String id;
            public String fullname;
            public String shortname;
            public String timemodified;
            public List<Assignment> assignments;

            public static class Assignment {
                public String id ;
                public String name ;
                public String course;
                public String cmid;
                public String intro;
                public long duedate;


            }
    }
}
