package at.minify.skymini.api.service;

import at.minify.skymini.Main;
import at.minify.skymini.api.api.ModAPI;
import at.minify.skymini.api.events.MiniChatEvent;
import at.minify.skymini.api.events.MiniPartyChatEvent;
import at.minify.skymini.api.events.MiniServerChangeEvent;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventExecutor {

    public ModAPI api;
    public ServiceContainer serviceContainer;
    private boolean oldSkyBlockState = false;

    public EventExecutor(ModAPI api, ServiceContainer serviceContainer) {
        this.api = api;
        this.serviceContainer = serviceContainer;
    }

    private int ticks = 0;
    private int seconds = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        ticks++;
        MiniTickEvent miniTickEvent = new MiniTickEvent(seconds, ticks);
        MinecraftForge.EVENT_BUS.post(miniTickEvent);
        //eventService.getEvents().forEach(eventApi -> eventApi.event.tick(miniTickEvent));
        if (ticks >= 20) {
            seconds++;
            ticks = 0;
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        MiniChatEvent miniChatEvent = new MiniChatEvent(message);
        MinecraftForge.EVENT_BUS.post(miniChatEvent);
        //eventService.getEvents().forEach(eventApi -> eventApi.event.chat(miniChatEvent));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChat(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        Matcher matcher = Pattern.compile("Party > (.*?): (.*)").matcher(message);
        if(matcher.find()) {
            String nameElement = matcher.group(1);
            String name = Chat.getName(nameElement);
            MinecraftForge.EVENT_BUS.post(new MiniPartyChatEvent(name, matcher.group(2)));
        }
    }

    private List<String> oldTabList = new ArrayList<>();

    @SubscribeEvent
    public void onTick(MiniTickEvent event) {
        Server server = Server.NONE;

        if(!api.inSkyBlock && oldSkyBlockState) {
            updateRegistry(server);
        }
        oldSkyBlockState = api.inSkyBlock;
        if(Main.getAPI().currentScoreboard.equals("dungeon") && api.inSkyBlock) {
            server = Server.CATACOMBS;
            updateRegistry(server);
        } else if(!oldTabList.equals(api.tabListData)) {
            for(String name : api.tabListData) {
                if(name.contains("Area")) {
                    if(name.contains("Hub")) server = Server.HUB;
                    if(name.contains("Crimson Isle")) server = Server.CRIMSON_ISLE;
                    if(name.contains("Kuudra")) server = Server.KUUDRA;
                    if(name.contains("The End")) server = Server.THE_END;
                    if(name.contains("Private Island")) server = Server.PRIVATE_ISLAND;
                    if(name.contains("Spider's Den")) server = Server.SPIDERS_DEN;
                    if(name.contains("The Park")) server = Server.THE_PARK;
                    if(name.contains("Dungeon Hub")) server = Server.DUNGEON_HUB;
                    if(name.contains("Farming Islands")) server = Server.FARMING_ISLANDS;
                    if(name.contains("Gold Mine")) server = Server.GOLD_MINE;
                    if(name.contains("Deep Caverns")) server = Server.DEEP_CAVERNS;
                    if(name.contains("Dwarven Mines")) server = Server.DWARVEN_MINES;
                    if(name.contains("Crystal Hollows")) server = Server.CRYSTAL_HOLLOWS;
                    if(name.contains("Garden")) server = Server.GARDEN;
                }
            }
            if(server != Server.NONE) {
                updateRegistry(server);
            }
        }
        /*if(api.server != null && api.server != server) {
            MiniServerChangeEvent miniServerChangeEvent = new MiniServerChangeEvent(server);
            MinecraftForge.EVENT_BUS.post(miniServerChangeEvent);
            for(ServiceContainer.EventApi api : serviceContainer.events) {
                if(api.getServer().equals(Server.NONE)) continue;

                if(!serviceContainer.loadedEvents.contains(api)) {
                    if(api.getServer().equals(server) || api.getServer().equals(Server.SKYBLOCK) && !server.equals(Server.NONE)) {
                        serviceContainer.loadedEvents.add(api);
                        MinecraftForge.EVENT_BUS.register(api.clazz);
                        System.out.println("registered: " + api.clazz.getClass().getName());
                    }
                } else {
                    if(!api.getServer().equals(server) && !api.getServer().equals(Server.SKYBLOCK) || api.getServer().equals(Server.SKYBLOCK) && server.equals(Server.NONE)) {
                        serviceContainer.loadedEvents.remove(api);
                        MinecraftForge.EVENT_BUS.unregister(api.clazz);
                        System.out.println("unregistered: " + api.clazz.getClass().getName());
                    }
                }
            }
            //eventService.getEvents().forEach(eventApi -> eventApi.event.changeServer(api.server));
        }*/
        oldTabList = api.tabListData;
    }

    public void updateRegistry(Server server) {
        if(api.server != null && api.server != server) {
            MiniServerChangeEvent miniServerChangeEvent = new MiniServerChangeEvent(server);
            MinecraftForge.EVENT_BUS.post(miniServerChangeEvent);
            for(ServiceContainer.EventApi api : serviceContainer.events) {
                if(api.getServer().equals(Server.NONE)) continue;

                if(!serviceContainer.loadedEvents.contains(api)) {
                    if(api.getServer().equals(server) || api.getServer().equals(Server.SKYBLOCK) && !server.equals(Server.NONE)) {
                        serviceContainer.loadedEvents.add(api);
                        MinecraftForge.EVENT_BUS.register(api.clazz);
                        //System.out.println("registered: " + api.clazz.getClass().getName());
                    }
                } else {
                    if(!api.getServer().equals(server) && !api.getServer().equals(Server.SKYBLOCK) || api.getServer().equals(Server.SKYBLOCK) && server.equals(Server.NONE)) {
                        serviceContainer.loadedEvents.remove(api);
                        MinecraftForge.EVENT_BUS.unregister(api.clazz);
                        //System.out.println("unregistered: " + api.clazz.getClass().getName());
                    }
                }
            }
            //eventService.getEvents().forEach(eventApi -> eventApi.event.changeServer(api.server));
        }
        api.server = server;
    }

}
