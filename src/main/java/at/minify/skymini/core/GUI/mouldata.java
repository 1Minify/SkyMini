package at.minify.skymini.core.GUI;

import at.minify.skymini.api.service.ConfigService;
import at.minify.skymini.core.GUI.config.GUIConfig;
import io.github.moulberry.moulconfig.annotations.Category;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class mouldata {
    private static final GUIConfig configclass = new GUIConfig();

    public static void load() {
        for (Object category : catlist(configclass)) {
            String input = category.getClass().getName();
            int lastIndex = input.lastIndexOf(".");
            String name = input.substring(lastIndex + 1);

            ConfigService.loadClass(category.getClass());
            //Config.loadClass(category.getClass(),"data." + name, Config.config);
        }
    }

    public static void save() {
        for (Object category : catlist(configclass)) {
            String input = category.getClass().getName();
            int lastIndex = input.lastIndexOf(".");
            String name = input.substring(lastIndex + 1);
            ConfigService.saveClass(category.getClass());
            //Config.saveClass(category.getClass(), "data." + name, Config.config);
        }
        ConfigService.saveConfig();
    }

    public static List<Object> catlist(GUIConfig guiConfig) {
        List<Object> categories = new ArrayList<>();
        Field[] fields = guiConfig.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Category.class)) {
                try {
                    categories.add(field.get(guiConfig));
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        return categories;
    }


}
