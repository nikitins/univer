package ru.sgu.univer.app.objects;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.providers.LessonTypeProvider;

public class RatingTable {
    public static List<Lesson> lessons = new ArrayList<Lesson>();
    public static Map<Integer, List<Integer>> rating = new HashMap<Integer, List<Integer>>();

    public RatingTable(List<Student> students) {
        for (Student student : students) {
            rating.put(student.id, new ArrayList<Integer>());
        }
    }

    public static void put(int studentId, int pos, int ball) {
        if(rating.containsKey(studentId)) {
            rating.get(studentId).set(pos, ball);
        }
    }

    public static void addColumn(int typeId, String date) {
        Lesson lesson = new Lesson(LessonTypeProvider.getById(typeId), date);
        lessons.add(lesson);
        for (List<Integer> integers : rating.values()) {
            integers.add(0);
        }
    }

    public static List<Integer> getRatingByStudentId(int id) {
        if(rating.containsKey(id)) {
            return rating.get(id);
        }
        return new ArrayList<Integer>();
    }
}
