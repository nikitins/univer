package ru.sgu.univer.app.objects;

import java.io.Serializable;

public class LessonType implements Serializable {
    public int id;
    public String name;

    public LessonType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        LessonType lessonType = (LessonType) o;

        if (id != lessonType.id) return false;
        if (name != null ? !name.equals(lessonType.name) : lessonType.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
