package ru.sgu.univer.app.providers;

public class DataBase {



    public static class StudentTable {
        public static final String TABLE_NAME = "student_table";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LAST_NAME = "lastname";
        public static final String SECOND_NAME = "secondname";
        public static final String GROUP_ID = "groupid";
    }

    public static class GroupTable {
        public static final String TABLE_NAME = "group_table";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String COURSE_ID = "courseid";
    }

    public static class CourseTable {
        public static final String TABLE_NAME = "course_table";
        public static final String ID = "_id";
        public static final String NAME = "name";
    }
}
