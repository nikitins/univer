package ru.sgu.univer.app.utils;

import android.util.Log;

import java.util.List;

import ru.sgu.univer.app.objects.MegaRatingTable;
import ru.sgu.univer.app.objects.Student;

public class Parser {

    public static final String BALL_TAG = "studentStandatrTableTd studentStandatrTableExamin studentStandatrTableTdPaddingTeacher\"><input type=\"text\" name=\"table";
    public static final String SELECTED = "selected=\"selected\">";
    public static final String STUDENT_TAG = "linkToStudentsProfile";
    public static final String ID_TAG = "name=\"javax.faces.ViewState\" id=\"javax.faces.ViewState\" value=\"";

    public static final String LINK = "href=\"";
    public static final String CELL = "<input type=\"text\" name=\"table:";
    public static final String VALUE = "value=\"";

    public static MegaRatingTable parseRating(List<String> strings) {
        MegaRatingTable table = new MegaRatingTable();
        Student lastStudent = null;
        for (String s : strings) {
            Log.d("Log", s);
            if(s.contains(STUDENT_TAG)) {
                String link = s.substring(s.indexOf(LINK) + LINK.length()).split("\"")[0];
                String name = s.split(">")[2].substring(2);
                name = name.substring(0, name.length() - 3);
                String[] fio = name.split(" ");
                Student student = new Student(fio[1], fio[2], fio[0], link);
                lastStudent = student;
                table.addStudent(student);
            }

            if(s.contains(BALL_TAG)) {
                String[] cell = s.split(CELL)[1].split("\"")[0].split(":");
                table.putPosition(cell[1]);

                int val = Integer.valueOf(s.split(VALUE)[1].split("\"")[0]);
                table.put(lastStudent.lastname, table.getPositionByString(cell[1]), val);
            }

            if(s.contains(SELECTED)) {
                String cell = s.split(SELECTED)[1].split("<")[0];
                table.finMap.put(lastStudent.lastname, cell);
            }

            if(s.contains(ID_TAG)) {
                table.requestId = s.split(ID_TAG)[1].split("\"")[0];
            }
        }
        return table;
    }
}
