package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.objects.Group;

public class GroupProvider {

    private static List<Group> groups = new ArrayList<Group>();
    private static Map<String, Group> groupMap = new HashMap<String, Group>();
    private static int uid = 0;

    public static List<Group> getGroupsByCourseId(String courseId) {
        List<Group> answer = new ArrayList<Group>();
        for (Group group : groups) {
            if(group.getCourseId().equals(courseId)) {
                answer.add(group);
            }
        }
        return answer;
    }

    public static void addGroup(String courseId, String name) {
        Group group = new Group(String.valueOf(getUid()), name, courseId);
        groups.add(group);
        groupMap.put(group.getId(), group);
    }

    private static int getUid() {
        return uid++;
    }
}
