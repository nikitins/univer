package ru.sgu.univer.app.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.objects.Group;

public class GroupProvider {

    public static List<Group> groups = new ArrayList<Group>();
    public static Map<Integer, List<Group>> groupMap = new HashMap<Integer, List<Group>>();
    public static int uid = 0;

    public static Group getGroupById(int id) {
        for (Group group : groups) {
            if (group.getId() == id)
                return group;
        }
        return null;
    }

    public static List<Group> getGroupsByCourseId(int courseId) {
        if (!groupMap.containsKey(courseId)) {
            groupMap.put(courseId, new ArrayList<Group>());
        }
        return groupMap.get(courseId);
    }

    public static Group addGroup(int courseId, String name) {
        Group group = new Group(getUid(), name, courseId);
        groups.add(group);
        if (!groupMap.containsKey(courseId)) {
            groupMap.put(courseId, new ArrayList<Group>());
        }
        groupMap.get(courseId).add(group);
        return group;
    }

    public static void removeGroup(int id) {
        Group group = getGroupById(id);
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getId() == id) {
                groupMap.get(group.getCourseId()).remove(group);
                groups.remove(i);
            }
        }
    }

    public static void renameGroup(int id, String newName) {
        for (Group group : groups) {
            if(group.getId() == id) {
                group.setName(newName);
                for (Group group1 : groupMap.get(group.getCourseId())) {
                    if(group1.getId() == id) {
                        group1.setName(newName);
                        break;
                    }
                }
                break;
            }
        }
    }

    private static int getUid() {
        return uid++;
    }

    public static boolean hasGroup(String name) {
        for (Group group : groups) {
            if(group.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void clear(){
        groups.clear();
        groupMap.clear();
        uid = 0;
    }
}
