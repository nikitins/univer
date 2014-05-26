package ru.sgu.univer.app.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.providers.LessonTypeProvider;

public class RatingTable implements Serializable {
    public List<Lesson> lessons = new ArrayList<Lesson>();
    public Map<Integer, List<Integer>> rating = new HashMap<Integer, List<Integer>>();
    public Map<Integer, Integer> sumMap = new HashMap<Integer, Integer>();

    public RatingTable(List<Student> students) {
        for (Student student : students) {
            rating.put(student.id, new ArrayList<Integer>());
            sumMap.put(student.id, 0);
        }
    }

    public void put(int studentId, int pos, int ball) {
        if(rating.containsKey(studentId)) {
            int dif = 0;
            if(pos == -1) {
                rating.get(studentId).add(ball);
            } else {
                List<Integer> marks = rating.get(studentId);
                dif = Math.max(marks.get(pos), 0);
                rating.get(studentId).set(pos, ball);
            }
            sumMap.put(studentId, sumMap.get(studentId) - dif);
            if(ball > 0) {
                sumMap.put(studentId, sumMap.get(studentId) + ball);
            }
        }
    }

    public String getColumnTitle(int pos) {
        return lessons.get(pos).toString();
    }

    public void addColumn(int typeId, String date) {
        Lesson lesson = new Lesson(LessonTypeProvider.getById(typeId), date);
        lessons.add(lesson);
        for (List<Integer> integers : rating.values()) {
            integers.add(-2);
        }
    }

    public List<Integer> getRatingByStudentId(int id) {
        if(!rating.containsKey(id)) {
            rating.put(id, new ArrayList<Integer>());
        }
        return rating.get(id);
    }

    public int getSumStudentId(int id) {
        if(!sumMap.containsKey(id)) {
            sumMap.put(id, 0);
        }
        return sumMap.get(id);
    }

}
