package ru.sgu.univer.app.objects;

public class Student {

    private String id;
    private String name;
    private String surname;
    private String lastname;
    private String groupId;

    public Student(String id, String name, String surname, String lastname, String groupId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.groupId = groupId;
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

    public String getGroupId() {
        return groupId;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (id != null ? !id.equals(student.id) : student.id != null) return false;
        if (lastname != null ? !lastname.equals(student.lastname) : student.lastname != null)
            return false;
        if (name != null ? !name.equals(student.name) : student.name != null) return false;
        if (surname != null ? !surname.equals(student.surname) : student.surname != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        return result;
    }
}
