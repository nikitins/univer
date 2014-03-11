package ru.sgu.univer.app.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    private int id;
    private String name;
    private int courseId;
    private List<Student> students = new ArrayList<Student>();
    private Map<String, Student> studentMap = new HashMap<String, Student>();

    public Group(int id, String name, int courseId) {
        this.id = id;
        this.name = name;
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCourseId() {
        return courseId;
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (courseId != group.courseId) return false;
        if (id != group.id) return false;
        if (name != null ? !name.equals(group.name) : group.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + courseId;
        return result;
    }

    @Override
    public String toString() {
        return name;
    }
}
