package ru.sgu.univer.app.providers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MegaProvider implements Serializable{
    public Map<Class, Map<String, Object>> megaMap = new HashMap<Class, Map<String, Object>>();

    public static void clearAll(){
        CourseProvider.clear();
        GroupProvider.clear();
        PerevodProvider.clear();
        RatingProvider.clear();
        StudentProvider.clear();
    }
}
