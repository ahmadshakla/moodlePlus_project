package com.postPc.moodlePlus.CourseInformation.CourseGrades;

public class GradesTable {
    UserGrades[] usergrades;



    public static class UserGrades{
        public GradesItem[] gradeitems;

        public static class GradesItem{
            public String id;
            public String itemname;
            public String gradeformatted;
            public String grademax;


            public String getItemName(){
                return itemname;
            }
            public String getGrade(){
                return gradeformatted;
            }
            public String getMaxGrade(){
                return grademax;
            }
        }


    }

}
