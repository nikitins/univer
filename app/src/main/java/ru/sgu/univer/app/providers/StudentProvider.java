package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.Student;

public class StudentProvider {

    private static List<Student> students = new ArrayList<Student>();
    private static Map<Integer, List<Student>> studentsMap = new HashMap<Integer, List<Student>>();
    private static int uid = 0;

    private static int getUid() {
        return uid++;
    }

    public static Student add(String name, String lastName, String secondName, int groupId) {
        Student student = new Student(getUid(), name, secondName, lastName, groupId);
        if(!studentsMap.containsKey(groupId)) {
            studentsMap.put(groupId, new ArrayList<Student>());
        }
        studentsMap.get(groupId).add(student);
        students.add(student);
        return student;
    }

    public static List<Student> getStudentsByGroupId(int groupId) {
        if(!studentsMap.containsKey(groupId)) {
            studentsMap.put(groupId, new ArrayList<Student>());
        }
        return studentsMap.get(groupId);
    }

    public static Student getById(int id) {
        for (Student student : students) {
            if(student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public static void removeStudent(int id) {
        Student student = getById(id);
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                studentsMap.get(student.getGroupId()).remove(student);
                students.remove(i);
            }
        }
    }
}
