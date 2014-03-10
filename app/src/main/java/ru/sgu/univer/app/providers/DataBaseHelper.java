package ru.sgu.univer.app.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "university_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DEBUG_TAG = DataBaseHelper.class.getSimpleName();

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + DataBase.StudentTable.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DataBase.GroupTable.TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DataBase.CourseTable.TABLE_NAME + ";");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStudentsTable = "CREATE TABLE " + DataBase.StudentTable.TABLE_NAME + " ("
                + DataBase.StudentTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DataBase.StudentTable.NAME + " TEXT NOT NULL, "
                + DataBase.StudentTable.LAST_NAME + " TEXT NOT NULL, "
                + DataBase.StudentTable.SECOND_NAME + " TEXT NOT NULL, "
                + DataBase.StudentTable.GROUP_ID + " INTEGER NOT NULL);";
        String createGroupsTable = "CREATE TABLE " + DataBase.GroupTable.TABLE_NAME + " ("
                + DataBase.GroupTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DataBase.GroupTable.NAME + " TEXT NOT NULL, "
                + DataBase.GroupTable.COURSE_ID + " INTEGER NOT NULL);";
        String createCoursesTable = "CREATE TABLE " + DataBase.CourseTable.TABLE_NAME + " ("
                + DataBase.CourseTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + DataBase.CourseTable.NAME + " TEXT NOT NULL);";
        db.execSQL(createCoursesTable);
        Log.d(DEBUG_TAG, "Courses table created");
        db.execSQL(createGroupsTable);
        Log.d(DEBUG_TAG, "Groups table created");
        db.execSQL(createStudentsTable);
        Log.d(DEBUG_TAG, "Students table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(DEBUG_TAG, "onUpgrade called");
    }
}
