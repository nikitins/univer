package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.Student;

public class StudentProvider {

    public static List<Student> students = new ArrayList<Student>();
    public static Map<Integer, List<Student>> studentsMap = new HashMap<Integer, List<Student>>();
    public static int uid = 0;

    private static int getUid() {
        return uid++;
    }

    public static Student add(String name, String lastName, String secondName, String tel, String email, int groupId) {
        Student student = new Student(getUid(), name, secondName, lastName, tel, email, groupId);
        if(!studentsMap.containsKey(groupId)) {
            studentsMap.put(groupId, new ArrayList<Student>());
        }
        studentsMap.get(groupId).add(student);
        students.add(student);
        return student;
    }

    public static Student putIfNotExist(Student student, int groupId) {
        student.groupId = groupId;
        if(!studentsMap.containsKey(groupId)) {
            studentsMap.put(groupId, new ArrayList<Student>());
        }
        boolean exist = false;
        for (Student s : studentsMap.get(groupId)) {
            if(s.lastname.equals(student.lastname)) {
                exist = true;
                break;
            }
        }

        if (!exist) {
            if(student.id == -1) {
                student.id = getUid();
            }
            studentsMap.get(groupId).add(student);
            students.add(student);
        }
        return student;
    }

    public static Student edit(int id, String name, String lastName, String secondName, String tel, String email, int groupId) {
        for (Student student : students) {
            if(student.getId() == id) {
                student.name = name;
                student.lastname = lastName;
                student.surname = secondName;
                student.telefon = tel;
                student.email = email;
                break;
            }
        }
        for (Student student : studentsMap.get(groupId)) {
            if(student.getId() == id) {
                student.name = name;
                student.lastname = lastName;
                student.surname = secondName;
                student.telefon = tel;
                student.email = email;
                return student;
            }
        }
        return null;
    }

    public static List<Student> getStudentsByGroupId(int groupId) {
        if(!studentsMap.containsKey(groupId)) {
            studentsMap.put(groupId, new ArrayList<Student>());
        }
        return studentsMap.get(groupId);
    }

    public static void removeStudentsByGroupId(int groupId) {
        studentsMap.put(groupId, new ArrayList<Student>());
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

    public static void removeStudentByGroupId(int groupId) {
        List<Student> forRemoving = new ArrayList<Student>();
        for (Student student : students) {
            if(student.getGroupId() == groupId) {
                forRemoving.add(student);
            }
        }
        students.removeAll(forRemoving);
        studentsMap.remove(groupId);
    }

    public static void clear(){
        students.clear();
        studentsMap.clear();
        uid = 0;
    }
}
