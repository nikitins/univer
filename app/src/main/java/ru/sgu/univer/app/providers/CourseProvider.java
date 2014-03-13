package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.sgu.univer.app.objects.Course;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CourseProvider {

    private static List<Course> courses = new ArrayList<Course>();
    private static Map<Integer, Course> courseMap = new HashMap<Integer, Course>();
    private static int uid = 0;

    static {
        String[] names = new String[]{"Маша", "Петя", "Вася", "Сергей", "Оля", "Дима", "Леша", "Антон", "Иван"};
        String[] lastNames = new String[]{"Прихотько", "Гайдук", "Кравченко", "Шматко", "Солдатик", "Лепненко", "Завайчик", "Чапман"};
        add("Матан");
        add("Сети ЭВМ");
        add("Программирование");
        for(int q = 0; q < courses.size(); q++) {
            Course course = courses.get(q);
            for (int i = 0; i < 5; i++) {
                int groupid = GroupProvider.addGroup(course.id,  String.valueOf(q) + "0" + String.valueOf(i+1)).getId();
                for (int j = 0; j < 5; j++) {
                    Random random = new Random();
                    StudentProvider.add(names[random.nextInt(names.length)],
                            lastNames[random.nextInt(lastNames.length)], "ivan", "555555",
                            "email@gmail.com",  groupid);
                }

            }
        }
    }

    private static int getUid() {
        return uid++;
    }

    public static void add(String name) {
        Course item = new Course(getUid(), name);
        courseMap.put(item.id, item);
        courses.add(item);
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static Course getById(int id) {
        return courseMap.get(id);
    }

    public static Course getByPosition(int position) {
        return courses.get(position);
    }

    public static void removeById(int id) {
        Course course = courseMap.remove(id);
        courses.remove(course);
    }

    public static void renameById(int id, String newName) {
        courseMap.get(id).name = newName;
        for (Course item : courses) {
            if (item.id == id) {
                item.name = newName;
                break;
            }
        }
    }

    public static boolean hasCourse(String name) {
        for (Course item : courses) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
