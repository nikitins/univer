package ru.sgu.univer.app.objects;

import java.io.Serializable;

public class Lesson implements Serializable {
    public LessonType type;
    public String date;

    public Lesson(LessonType type, String date) {
        this.type = type;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Lesson lesson = (Lesson) o;

        if (date != null ? !date.equals(lesson.date) : lesson.date != null) return false;
        if (type != null ? !type.equals(lesson.type) : lesson.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return type.toString() + " " + date;
    }
}
