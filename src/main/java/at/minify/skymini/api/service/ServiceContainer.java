package at.minify.skymini.api.service;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.util.MiniCommand;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.core.data.Priority;
import at.minify.skymini.core.data.Server;
import com.google.common.reflect.ClassPath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ServiceContainer {

    public ServiceContainer() {
        serviceContainer = this;
    }

    public static ServiceContainer serviceContainer;
    public final List<EventApi> events = new ArrayList<>();
    public final Map<String, Object> objectEvents = new HashMap<>();
    public final Map<String, Object> services = new HashMap<>();
    public final List<Object> commands = new ArrayList<>();
    public final List<EventApi> loadedEvents = new ArrayList<>();

    public static <T> T getService(Class<T> clazz) {
        if(serviceContainer.objectEvents.containsKey(clazz.getName())) {
            return clazz.cast(serviceContainer.objectEvents.get(clazz.getName()));
        }
        if(serviceContainer.services.containsKey(clazz.getName())) {
            return clazz.cast(serviceContainer.services.get(clazz.getName()));
        }
        return null;
    }

    /*public static <T> T getService(Class<T> clazz) {
        Object clazz0 = serviceContainer.events.stream().filter(eventApi -> eventApi.getClazz().equals(clazz)).findAny().orElse(null);
        if(clazz0 != null) return clazz.cast(clazz0.getClass());
        Object clazz1 = serviceContainer.services.stream().filter(o -> o.equals(clazz)).findAny().orElse(null);
        if(clazz1 != null) return clazz.cast(clazz1.getClass());
        return null;
    }*/

    public void register() {
        try {
            serviceContainer.services.put(this.getClass().getName(), this);
            final Set<Class> classes = findAllClassesByPackageName("at.minify.skymini.core");
            Map<Class<?>, Integer> serviceList = new HashMap<>();
            for (Class<?> clazz : classes) {
                if(clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    serviceList.put(clazz, service.priority());
                }
            }
            serviceList.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> {
                try {
                    serviceContainer.services.put(entry.getKey().getName(), entry.getKey().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("registered service: " + entry.getKey().getName() + " with priority " + entry.getValue());
            });


            for (Class<?> clazz : classes) {
                boolean isMiniEvent = false;
                if(clazz.getSuperclass() != null) {
                    isMiniEvent = clazz.getSuperclass().equals(MiniEvent.class);
                }
                boolean hasAnnotation = clazz.isAnnotationPresent(MiniRegistry.class);
                if(!isMiniEvent && !hasAnnotation) continue;
                MiniEvent event = null;
                if(isMiniEvent) {
                    event = (MiniEvent) clazz.getDeclaredConstructor().newInstance();
                }
                Priority priority = Priority.DEFAULT;
                Server server = Server.SKYBLOCK;
                if(clazz.isAnnotationPresent(MiniRegistry.class)) {
                    MiniRegistry miniSubscribeEvent1 = clazz.getAnnotation(MiniRegistry.class);
                    priority = miniSubscribeEvent1.priority();
                    server = miniSubscribeEvent1.server();
                }
                EventApi api;
                if(serviceContainer.services.containsKey(clazz.getName())) {
                    api = new EventApi(serviceContainer.services.get(clazz.getName()), event, priority, server);
                } else if(WidgetService.widgetMap.containsKey(clazz.getName())) {
                    api = new EventApi(WidgetService.widgetMap.get(clazz.getName()), event, priority, server);
                } else {
                    api = new EventApi(clazz.newInstance(), event, priority, server);
                }
                serviceContainer.events.add(api);
                serviceContainer.objectEvents.put(api.clazz.getClass().getName(), api.clazz);
                if(server.equals(Server.NONE) && !serviceContainer.loadedEvents.contains(api)) {
                    serviceContainer.loadedEvents.add(api);
                    MinecraftForge.EVENT_BUS.register(api.clazz);
                    System.out.println("registered none: " + api.clazz.getClass().getName());
                }

                /*if(clazz.getSuperclass().equals(MiniEvent.class)) {
                    MiniEvent event = (MiniEvent) clazz.getDeclaredConstructor().newInstance();
                    Priority priority = Priority.DEFAULT;
                    Server server = Server.SKYBLOCK;
                    if(clazz.isAnnotationPresent(MiniRegistry.class)) {
                        MiniRegistry miniSubscribeEvent1 = clazz.getAnnotation(MiniRegistry.class);
                        priority = miniSubscribeEvent1.priority();
                        server = miniSubscribeEvent1.server();
                    }
                    EventApi api = new EventApi(clazz.newInstance(), event, priority, server);
                    events.add(api);
                    if(server.equals(Server.NONE) && !loadedEvents.contains(api)) {
                        loadedEvents.add(api);
                        MinecraftForge.EVENT_BUS.register(api.clazz);
                    }
                }*/
            }
            for (Class<?> clazz : classes) {
                if(clazz.getSuperclass() == null) continue;
                if(clazz.getSuperclass().equals(MiniCommand.class)) {
                    serviceContainer.commands.add(clazz);
                    ClientCommandHandler.instance.registerCommand((ICommand) clazz.getDeclaredConstructor().newInstance());
                    System.out.println("registered command: " + clazz.getName());
                }
            }

            //events.sort(Comparator.comparing(EventApi::getPriority));
        } catch (Exception e) {
            System.out.println("ERROR uwu");
            e.printStackTrace();
        }
    }

    public Set<Class> findAllClassesByPackageName(final String packageName) throws IOException {
        return ClassPath.from(Main.class.getClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName().startsWith(packageName))
                .map(ClassPath.ClassInfo::load)
                .collect(Collectors.toSet());
    }

    @Getter
    @AllArgsConstructor
    public static class EventApi {

        Object clazz;
        MiniEvent event;
        Priority priority;
        Server server;

    }


}
