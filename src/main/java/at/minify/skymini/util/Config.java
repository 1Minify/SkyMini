package at.minify.skymini.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;

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


    public static void saveClass1(Class<?> clazz) {
        Gson gson = new Gson();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

        }
    }

    public static void loadClass(Class<?> clazz, String category, Configuration config) {
        Gson gson = new Gson();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> fieldMap;

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Property property = config.get(category, field.getName(), (String) null);
                if (property != null) {
                    String jsonValue = property.getString();
                    Type fieldType = TypeToken.get(field.getType()).getType();
                    Object value = gson.fromJson(jsonValue, fieldType);
                    System.out.println("loaded: " + value);
                    field.set(null, value);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void saveClass(Class<?> clazz, String category, Configuration config) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(null);  // Statische Felder verwenden keinen Instanzbezug
                if (value != null) {
                    String jsonValue = gson.toJson(value);  // Objekt in JSON umwandeln
                    Property property = config.get(category, field.getName(), jsonValue);
                    property.set(jsonValue);  // JSON-Wert in die Config setzen
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        if (config.hasChanged()) {
            config.save();
        }
    }



    /*public static void saveClass(Class<?> clazz, String category, Configuration config) {
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
                    field.setAccessible(true); // Zugriff auf das Feld ermöglichen
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
    }*/


}
