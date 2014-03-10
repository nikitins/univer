package ru.sgu.univer.app.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.sgu.univer.app.objects.Course;

public class DataBase {
    private static final String DEBUG_TAG = DataBase.class.getSimpleName();

    public static void putCourse(Context context, String name) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqdb = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CourseTable.NAME, name);
        if (sqdb != null) {
            sqdb.insert(CourseTable.TABLE_NAME, null, cv);
            sqdb.close();
        } else {
            Log.d(DEBUG_TAG, "Couldn't insert course '" + name + "' into course table");
        }
        dataBaseHelper.close();
    }

    public static List<Course> getCourses(Context context) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqdb = dataBaseHelper.getWritableDatabase();
        List<Course> courses = new ArrayList<Course>();
        if (sqdb != null) {
            Cursor cursor = sqdb.query(CourseTable.TABLE_NAME, new String[] {
                            CourseTable.ID, CourseTable.NAME },
                    null, // The columns for the WHERE clause
                    null, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CourseTable.ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(CourseTable.NAME));
                courses.add(new Course(String.valueOf(id), name));
                Log.i("LOG_TAG", "ROW " + id + " HAS NAME " + name);
            }
            cursor.close();
            sqdb.close();
        } else {
            Log.d(DEBUG_TAG, "Couldn't get courses from course table");
        }
        dataBaseHelper.close();
        return courses;
    }

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
