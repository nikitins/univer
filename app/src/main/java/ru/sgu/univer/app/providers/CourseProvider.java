package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.objects.Course;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class CourseProvider {

    private static List<Course> items = new ArrayList<Course>();
    private static Map<String, Course> itemMap = new HashMap<String, Course>();
    private static int uid = 0;

    static {
        // Add 3 sample items.
        add("Курс 1");
        add("Курс 2");
        add("Курс 3");
        add("Курс 4");
        add("Курс 5");
    }

    private static int getUid() {
        return uid++;
    }

    public static void add(String name) {
        Course item = new Course(String.valueOf(getUid()), name);
        itemMap.put(item.id, item);
        items.add(item);
    }

    public static List<Course> getItems() {
        return items;
    }

    public static Course getById(String id) {
        return itemMap.get(id);
    }

    public static Course getByPosition(int position) {
        return items.get(position);
    }

    public static void removeById(String id) {
        Course course = itemMap.remove(id);
        items.remove(course);
    }

    public static void renameById(String id, String newName) {
        itemMap.get(id).name = newName;
        for (Course item : items) {
            if (item.id.equals(id)) {
                item.name = newName;
                break;
            }
        }
    }

    public static boolean hasCourse(String name) {
        for (Course item : items) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
