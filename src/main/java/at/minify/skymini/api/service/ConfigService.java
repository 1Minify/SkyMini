package at.minify.skymini.api.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDraggableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConfigService {

    private static JsonObject data = new JsonObject();

    public static void saveConfig() {
        setFile(FileType.CONFIG, data);
    }

    /*private static final Map<FileType, Object> jsonHolder = new HashMap<>();

    public static void load(FileType fileType) {
        try {
            jsonHolder.put(fileType, getObject(GUIConfig.class, fileType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(FileType fileType) {
        Gson gson = new Gson();
        File file = new File(fileType.getPath());
        System.out.println(jsonHolder.get(fileType));
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(gson.toJson(jsonHolder.get(fileType)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getObject(Class<?> clazz, FileType fileType) throws InstantiationException, IllegalAccessException, IOException {
        Gson gson = new GsonBuilder().create(); // Consider configuring Gson for circular references if needed
        JsonObject object = getFile(fileType);

        // Use TypeToken to correctly deserialize the object
        return gson.fromJson(object, TypeToken.get(clazz).getType());
    }*/

    public static void loadClass(Class<?> clazz) {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new Gson();
        Field[] fields = clazz.getDeclaredFields();
        JsonObject object = getFile(FileType.CONFIG);
        data = object;
        if(object == null) return;
        if(!object.has(clazz.getName())) return;
        JsonObject classObject = object.get(clazz.getName()).getAsJsonObject();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            System.out.println(field.getName());
            field.setAccessible(true);
            if(classObject.has(field.getName())) {
                Type fieldType = TypeToken.get(field.getType()).getType();
                Object value = gson.fromJson(classObject.get(field.getName()), fieldType);

                if(field.isAnnotationPresent(ConfigEditorDraggableList.class)) {
                    List<Integer> list = getListFromJson((JsonArray) classObject.get(field.getName()));
                    try {
                        field.set(null, list);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                try {
                    field.set(null, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void saveClass(Class<?> clazz) {
        //Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Gson gson = new Gson();
        Field[] fields = clazz.getDeclaredFields();

        JsonObject object = data;
        if (!object.has(clazz.getName())) {
            object.add(clazz.getName(), new JsonObject());
        }
        JsonObject classObject = object.get(clazz.getName()).getAsJsonObject();

        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object value = field.get(null);
                if (value != null) {
                    if(value instanceof List<?>) {
                        List<?> list = (List<?>) value;
                        if(!list.isEmpty() && list.get(0) instanceof Integer) {
                            JsonArray jsonArray = new JsonArray();
                            for (Object item : list) {
                                jsonArray.add(gson.toJsonTree(item, Integer.class));
                            }
                            classObject.add(field.getName(), jsonArray);
                            continue;
                        }
                    }
                    JsonElement jsonValue = gson.toJsonTree(value);
                    classObject.add(field.getName(), jsonValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        data = object;
    }

    private static JsonObject getFile(FileType fileType) {
        StringBuilder content = new StringBuilder();
        try {
            File file = new File(fileType.getPath());
            if(!file.exists()) return new JsonObject();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(content.toString(), JsonObject.class);
    }

    private static synchronized void setFile(FileType fileType, JsonObject content) {
        File file = new File(fileType.getPath());
        new Thread(() -> {
            String jsonMessage = new Gson().toJson(content);
            try {
                if(!file.exists()) {
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter(fileType.getPath());
                writer.write(jsonMessage);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public enum FileType {

        CONFIG("config.js");

        String path;

        FileType(String path) {
            this.path = path;
        }

        public String getPath() {
            return "config/skymini/" + path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public static List<Integer> getListFromJson(JsonArray jsonArray) {
        List<Integer> list = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            list.add(element.getAsInt());
        }
        return list;
    }

}
