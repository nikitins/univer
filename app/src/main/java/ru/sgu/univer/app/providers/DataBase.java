package ru.sgu.univer.app.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.objects.Group;

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

    public static void removeCourse(Context context, int id) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqdb = dataBaseHelper.getWritableDatabase();
        if (sqdb != null) {
            int count = sqdb.delete(CourseTable.TABLE_NAME, CourseTable.ID + "='" + String.valueOf(id) + "'", null);
            sqdb.close();
            Log.d(DEBUG_TAG, "Deleted " + count +  " courses with id '" + id + "'");
        } else {
            Log.d(DEBUG_TAG, "Couldn't delete course with id '" + id + "'");
        }
        dataBaseHelper.close();
    }

    public static void renameCourse(Context context, int id, String name) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqdb = dataBaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CourseTable.NAME, name);
        if (sqdb != null) {
            sqdb.update(CourseTable.TABLE_NAME, cv, CourseTable.ID, new String[]{String.valueOf(id)});
            sqdb.close();
        } else {
            Log.d(DEBUG_TAG, "Couldn't rename course with id '" + id + "' to " + name);
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

    public static List<Group> getGroupsByCourseId(Context context, int courseId) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqdb = dataBaseHelper.getWritableDatabase();
        List<Group> courses = new ArrayList<Group>();
        if (sqdb != null) {
            Cursor cursor = sqdb.query(GroupTable.TABLE_NAME, new String[] {
                            GroupTable.ID, GroupTable.NAME },
                    GroupTable.COURSE_ID, // The columns for the WHERE clause
                    new String[]{String.valueOf(courseId)}, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(GroupTable.ID));
                String name = cursor.getString(cursor
                        .getColumnIndex(GroupTable.NAME));
                courses.add(new Group(String.valueOf(id), name, String.valueOf(courseId)));
                Log.i("LOG_TAG", "ROW " + id + " HAS NAME " + name);
            }
            cursor.close();
            sqdb.close();
        } else {
            Log.d(DEBUG_TAG, "Couldn't get groups from course table");
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
