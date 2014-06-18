package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.List;

import ru.sgu.univer.app.objects.LessonType;
import ru.sgu.univer.app.objects.Student;

public class LessonTypeProvider {
    private static int uid = 0;
    public static List<LessonType> lessonTypes = new ArrayList<LessonType>();

    static {
        add("Лекция");
        add("Семинар");
        add("Контрольная");
        add("Лабораторная");
    }

    public static LessonType getById(int id) {
        for (LessonType lessonType : lessonTypes) {
            if(lessonType.id == id) {
                return lessonType;
            }
        }
        return null;
    }

    public static LessonType getByName(String name) {
        for (LessonType lessonType : lessonTypes) {
            if(lessonType.name.equals(name)) {
                return lessonType;
            }
        }
        return null;
    }

    public static LessonType add(String name) {
        LessonType lessonType = new LessonType(getUid(), name);
        lessonTypes.add(lessonType);
        return lessonType;
    }

    private static int getUid() {
        return uid++;
    }
}
