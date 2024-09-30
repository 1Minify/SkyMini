package at.minify.skymini.util;


import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static Configuration config;
    private static final String file = "config/skymini.cfg";

    public static void load() {
        config = new Configuration(new File(file));
        try {
            config.load();
        } catch (Exception e) {
            System.out.println("Couldn't load the config.");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void clearCategory(String categoryName) {
        if(config == null) {
            return;
        }
        config.removeCategory(config.getCategory(categoryName));
        config.save();
    }

    public static void saveConfig() {
        if(!config.hasChanged()) {
            return;
        }
        config.save();
    }

    public static String getString(String category, String key) {
        return config.get(category, key, "").getString();
    }

    public static void setString(String category, String key, String value) {
        config.get(category, key, value).set(value);
        saveConfig();
    }

    public static void remove(String key) {
        config.getCategory("stats").remove(key);
    }

    /*public static Object get(String key, Object value, String category) {
        if (category == null) {
            category = "stats";
        }
        if (value instanceof Integer) { return config.get(category, key, 0).getInt(); }
        if (value instanceof String) { return config.get(category, key, "").getString(); }
        if (value instanceof Boolean) { return config.get(category, key, false).getBoolean(); }
        return null;
    }*/

    public static void set(String key, Object value, String category) {
        if (category == null) { category = "stats"; }
        if (value instanceof Integer) { config.get(category, key, (Integer)value).set((Integer)value); }
        else if (value instanceof String) { config.get(category, key, (String)value).set((String)value); }
        else if (value instanceof Boolean) { config.get(category, key, (Boolean)value).set((Boolean)value); }
        //if (value instanceof Float) { config.get(category, key, (Float)value).set((Float)value);}
        saveConfig();
    }





    public static int getInt(String category, String key) {
        return config.get(category, key, 0).getInt();
    }

    public static void setInt(String category, String key, int value) {
        config.get(category, key, value).set(value);
        saveConfig();
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        saveConfig();
    }

    public static void saveClass(Class<?> clazz, String category, Configuration config) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                if (field.getType() == float.class) {
                    Property property = config.get(category, field.getName(), field.getFloat(null));
                    property.set(field.getFloat(null));
                } else if (field.getType() == int.class) {
                    Property property = config.get(category, field.getName(), field.getInt(null));
                    property.set(field.getInt(null)); // Setze den Wert in die Config
                } else if (field.getType() == boolean.class) {
                    Property property = config.get(category, field.getName(), field.getBoolean(null));
                    property.set(field.getBoolean(null)); // Setze den Wert in die Config
                } else if (field.getType() == double.class) {
                    Property property = config.get(category, field.getName(), field.getDouble(null));
                    property.set(field.getDouble(null)); // Setze den Wert in die Config
                } else if (field.getType() == String.class) {
                    Property property = config.get(category, field.getName(), field.get(null).toString());
                    property.set(field.get(null).toString()); // Setze den Wert in die Config
                }  else if (field.getType() == List.class) {
                    Property property = config.get(category, field.getName(), field.get(null).toString());
                    property.set(field.get(null).toString()); // Setze den Wert in die Config
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (config.hasChanged()) {
            config.save();
        }
    }



    public static void loadClass(Class<?> clazz, String category, Configuration config) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            try {
                if (field.getType() == float.class) {
                    Property property = config.get(category, field.getName(), field.getFloat(null));
                    field.set(null, (float) property.getDouble());
                }
                if (field.getType() == int.class) {
                    Property property = config.get(category, field.getName(), field.getInt(null));
                    field.set(null, property.getInt());
                } else if (field.getType() == boolean.class) {
                    Property property = config.get(category, field.getName(), field.getBoolean(null));
                    field.set(null, property.getBoolean());
                } else if (field.getType() == double.class) {
                    Property property = config.get(category, field.getName(), field.getDouble(null));
                    field.set(null, property.getDouble());
                } else if (field.getType() == String.class) {
                    Property property = config.get(category, field.getName(), field.get(null).toString());
                    field.set(null, property.getString());
                } else if (field.getType() == List.class) {
                    Property property = config.get(category, field.getName(), field.get(null).toString());
                    String[] stringList = property.getString().replace("[","").replace("]","").split(",\\s*");
                    List<Integer> intList = new ArrayList<>();
                    for (String str : stringList) { intList.add(Integer.parseInt(str)); }
                    field.set(null, intList);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
