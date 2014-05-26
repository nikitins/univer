package ru.sgu.univer.app.providers;

import android.widget.TableLayout;

import java.util.HashMap;
import java.util.Map;

import ru.sgu.univer.app.objects.RatingTable;

public class RatingProvider {
    public static Map<Integer, Map<Integer, RatingTable>> map = new HashMap<Integer, Map<Integer, RatingTable>>();

    public static RatingTable getById(int courseId, int groupId) {
        if (!map.containsKey(courseId)) {
            map.put(courseId, new HashMap<Integer, RatingTable>());
        }
        if (!map.get(courseId).containsKey(groupId)) {
            map.get(courseId).put(groupId, new RatingTable(StudentProvider.getStudentsByGroupId(groupId)));
        }
        return map.get(courseId).get(groupId);
    }
    public static void add(int courseId, int groupId) {
        Map<Integer, RatingTable> temp = new HashMap<Integer, RatingTable>();
        temp.put(groupId, new RatingTable(StudentProvider.getStudentsByGroupId(groupId)));
        map.put(courseId, temp);
    }

    public static void clear() {
        map.clear();
    }
}
