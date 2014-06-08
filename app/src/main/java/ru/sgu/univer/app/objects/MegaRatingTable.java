package ru.sgu.univer.app.objects;

import android.util.Log;

import org.apache.poi.hssf.util.HSSFColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.providers.LessonTypeProvider;

public class MegaRatingTable {
    public List<Student> students;
    public List<Lesson> lessons;
    public Map<String, List<Integer>> rating;
    public List<String> positions;
    public Map<String, String> finMap;
    public String requestId;

    public MegaRatingTable() {
        students = new ArrayList<Student>();
        lessons = new ArrayList<Lesson>();
        rating = new HashMap<String, List<Integer>>();
        positions = new ArrayList<String>();
        finMap = new HashMap<String, String>();

        String[] les = new String[]{"Лeкции", "Лаборат. занятия", "Практ. занятия", "Самост. работа", "Другие виды учебн. деят.", "Пром. аттестация", "Итого"};
        for (String le : les) {
            addColumn(le);
        }
    }

    public void put(String studentFam, int pos, int ball) {
        if (pos == -1) {
            rating.get(studentFam).add(ball);
        } else {
            Log.d("PutLog", studentFam + ", " + pos + ", " + ball);
            Log.d("LOG", rating.keySet().toString());
            rating.get(studentFam).set(pos, ball);
        }
    }


    public void addColumn(String name) {
        Lesson lesson = new Lesson(null, name);
        lessons.add(lesson);
//        for (List<Integer> integers : rating.values()) {
//            integers.add(-2);
//        }
    }

    public void addStudent(Student student) {
        students.add(student);
        rating.put(student.lastname, new ArrayList<Integer>());
        for (int i = 0; i < lessons.size() - 1; i++) {
            rating.get(student.lastname).add(0);
        }
    }

    public List<Integer> getRatingByStudent(String fam) {
        if (!rating.containsKey(fam)) {
            rating.put(fam, new ArrayList<Integer>());
        }
        return rating.get(fam);
    }

    public void putPosition(String sPos) {
        if (!positions.contains(sPos)) {
            positions.add(sPos);
        }
    }

    public int getPositionByString(String s) {
        return positions.indexOf(s);
    }

    public String getStringByPosition(int p) {
        return positions.get(p);
    }

    public int getStudentPosByFam(String fam) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).lastname.equals(fam)) {
                return i;
            }
        }
        return -1;
    }

    public String getFinToRequest(String fam) {
        String fim = finMap.get(fam);
        if ("2".equals(fim)) return "two";
        if ("3".equals(fim)) return "three";
        if ("4".equals(fim)) return "four";
        if ("5".equals(fim)) return "five";
        if ("неявка".equals(fim)) return "absence";
        return "notEstimated";
    }
}
