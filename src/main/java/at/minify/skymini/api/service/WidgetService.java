package at.minify.skymini.api.service;

import at.minify.skymini.Main;
import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.api.widgets.manager.Images;
import com.google.gson.Gson;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WidgetService {

    public static Configuration config;
    private static final String file = "config/skymini/widgets.cfg";
    private static final Gson gson = new Gson();
    public static final Map<String, Widget> widgetMap = new HashMap<>();
    public static List<Widget> widgets = new ArrayList<>();

    public static <T> T getWidget(Class<T> clazz) {
        if(widgetMap.containsKey(clazz.getName())) {
            return clazz.cast(widgetMap.get(clazz.getName()));
        }
        return null;
    }


    public static void load() {
        Images.loadResources();
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


        try {
            final Set<Class> classes = Main.getServiceContainer().findAllClassesByPackageName("at.minify.skymini.core.widgets");
            for (Class<?> clazz : classes) {
                if(clazz.getSuperclass() == null) continue;
                if(clazz.getSuperclass().equals(Widget.class)) {
                    Widget widget = (Widget) clazz.getDeclaredConstructor().newInstance();
                    widgetMap.put(clazz.getName(), widget);
                    System.out.println("registered widget: " + clazz.getName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        widgets = new ArrayList<>(widgetMap.values());

        loadConfig();
    }

    public static void saveConfig() {
        /*for (Map.Entry<String, Widget> entry : widgetMap.entrySet()) {
            String input = entry.getValue().getClass().getName();
            int lastIndex = input.lastIndexOf(".");
            String name = input.substring(lastIndex + 1);
            try {
                Config.saveClass(entry.getValue().getClass().getDeclaredConstructor().getClass(), "data." + name, config);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            System.out.println(entry.getValue().getClass().getName());
        }*/
        for (Map.Entry<String, Widget> entry : widgetMap.entrySet()) {
            String name = entry.getKey();
            String widgetData = gson.toJson(entry.getValue());
            config.get("widgets", name, widgetData).set(widgetData);
        }
        if(config.hasChanged()) {
            config.save();
        }
    }

    public static void loadConfig() {
        /*for (Map.Entry<String, Widget> entry : widgetMap.entrySet()) {
            String input = entry.getValue().getClass().getName();
            int lastIndex = input.lastIndexOf(".");
            String name = input.substring(lastIndex + 1);
            Config.loadClass(entry.getValue().getClass(),"data." + name, config);
        }*/
        for (Map.Entry<String, Widget> entry : widgetMap.entrySet()) {
            String widgetData = config.get("widgets", entry.getKey(), "").getString();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(entry.getKey());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(clazz == null) continue;
            Widget oldWidget = (Widget) gson.fromJson(widgetData, clazz);
            if(oldWidget == null) continue;
            Widget widget = entry.getValue();
            widget.setText(oldWidget.getText());
            widget.setX(oldWidget.getX());
            widget.setY(oldWidget.getY());
            widget.setEnabled(oldWidget.isEnabled());
            widget.setCentered(oldWidget.isCenter());
        }
    }

}
