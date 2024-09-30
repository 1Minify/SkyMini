package at.minify.skymini.util;

import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.Map;

import static at.minify.skymini.util.Config.config;

public class stats {
    public static Map<String, Object> list = new HashMap<String, Object>();

    public static void put(String path, Object value) {
        list.put(path, value);
    }

    public static Object get(String path) {
        return list.get(path);
    }

    public static void remove(String path) {
        list.remove(path);
    }

    /*public static List<String> pathlist(String contains) {
        List<String> dataList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : list.entrySet()) {
            String path = entry.getKey();
            if(path.contains(contains) && entry.getValue() instanceof String) {
                dataList.add((String) entry.getValue());
            }
        }
        return dataList;
    }

    public static List<String> pathsget(String contains) {
        List<String> dataList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : list.entrySet()) {
            String path = entry.getKey();
            if(path.contains(contains) && entry.getValue() instanceof String) {
                dataList.add(entry.getKey().replace(contains,""));
            }
        }
        return dataList;
    }*/

    public static void savepaths(String contains) {
        String category = "stats";
        //clearCategory(category);
        for (Map.Entry<String, Object> entry : list.entrySet()) {
            String path = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof Float) {
                value = Float.toString((Float) value);
            }
            if(path.contains(contains)) {
                Config.set(path, value, category);
            }
        }
        Map<String, Property> propertyMap = config.getCategory(category).getValues();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            if(stats.get(name) == null && name.contains(contains)) {
                Config.remove(name);
            }
        }
    }

    public static void loadconfig() {
        String category = "stats";
        Map<String, Property> propertyMap = config.getCategory(category).getValues();
        for (Map.Entry<String, Property> entry : propertyMap.entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();
            switch (property.getType()) {
                case INTEGER: {
                    int value = property.getInt();
                    stats.put(name, value);
                    break;
                }
                case BOOLEAN: {
                    Boolean value = property.getBoolean();
                    stats.put(name, value);
                    break;
                }
                case STRING: {
                    String value = property.getString();
                    if(value.matches("[0-9.]+")) {
                        float put = Float.parseFloat(value);
                        stats.put(name,put);
                    } else {
                        stats.put(name, value);
                    }
                }
            }
        }
    }

}
