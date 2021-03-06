package ru.sgu.univer.app.objects;

import java.io.Serializable;

public class Student implements Serializable {

    public int id;
    public String name;
    public String surname;
    public String lastname;
    public String telefon;
    public String email;
    public int groupId;
    public String link;

    public Student(int id, String name, String surname, String lastname, String telefon, String email, int groupId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.telefon = telefon;
        this.email = email;
        this.groupId = groupId;
    }

    public Student(String name, String surname, String lastname, String link) {
        this.id = -1;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Student student = (Student) o;

        if (groupId != student.groupId) return false;
        if (id != student.id) return false;
        if (lastname != null ? !lastname.equals(student.lastname) : student.lastname != null)
            return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        if (surname != null ? !surname.equals(student.surname) : student.surname != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + groupId;
        return result;
    }

    @Override
    public String toString() {
        return lastname + " " + name;
    }
}
