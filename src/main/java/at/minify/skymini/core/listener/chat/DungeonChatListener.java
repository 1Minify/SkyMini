package at.minify.skymini.core.listener.chat;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.core.GUI.categories.Dungeon;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.scoreboard.data.DungeonScoreboard;
import at.minify.skymini.util.EntityTracker;
import at.minify.skymini.util.stats;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MiniRegistry(server = Server.SKYBLOCK)
public class DungeonChatListener {

    DungeonScoreboard dungeonScoreboard = ServiceContainer.getService(DungeonScoreboard.class);

    public String time = "";
    public String mile = "";
    public boolean sscore = false;
    public boolean ssscore = false;
    public int dungeonscore = 0;
    public int rushtime = 0;
    public int stime = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        message = Chat.uncolored(message);
        if(Dungeon.dungeonmsg) {
            if (message.contains("You picked up a ") || message.contains("Inventory full? Don") || message.contains("Press DROP to activate it!") || message.contains("have enough space in your inventory") || message.contains("There are blocks in the way!") || message.contains("Kill Combo")) {
                event.setCanceled(true);
            }
        }
        if (message.contains("Starting in 1 second.")) {
            System.out.println("reset of dungeon data successfull");
            dungeonScoreboard.milestone = "&b-";
            dungeonScoreboard.deaths = 0;
            dungeonScoreboard.key = "&c&lNot Found§r";
            dungeonScoreboard.keys = 0;
            dungeonScoreboard.bloodtime = 0;
            dungeonscore = 0;
            sscore = false;
            ssscore = false;
            stats.remove("gui.hidden1.dungeon");
        }
        else if (message.contains(" Milestone ") && message.contains("so far!")) {
            dungeonScoreboard.milestone = "&b" + getBetween(message, "Milestone ", ": You");
            event.setCanceled(true);
        }
        else if (message.contains("became a ghost.")) {
            dungeonScoreboard.deaths++;
        }
        else if (message.contains("has obtained") && message.contains("Wither Key") || message.contains("Wither Key was picked up!")) {
            dungeonScoreboard.key = "&a&lCollected§r";
            dungeonScoreboard.keys++;
        }
        else if (message.contains("has obtained") && message.contains("Blood Key") || message.contains("Blood Key was picked up!")) {
            dungeonScoreboard.key = "&2&lCollected Blood§r";
        }
        else if (message.contains("BLOOD DOOR has been opened")) {
            dungeonScoreboard.key = "";
            dungeonScoreboard.bloodtime = Dungeon.brtime;
            rushtime = formatTime(dungeonScoreboard.time);
            if(Dungeon.sendbr && !playingSolo()) {
                Chat.sendChat("/pchat " + Dungeon.brmessage.replaceAll("%time%", showTime(formatTime(time))).replaceAll("%keys%",dungeonScoreboard.keys + ""));
            }
        }
        else if (message.contains("opened a WITHER door")) {
            dungeonScoreboard.key = "&c&lNot Found§r";
        }
    }


    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if (dungeonscore >= 270 && !sscore && Dungeon.sendS && !playingSolo()) {
            sscore = true;
            Chat.sendChat("/pchat " + Dungeon.Smessage.replaceAll("%time%", showTime(formatTime(time))).replaceAll("%needed%", showTime(formatTime(time) - rushtime)));
            stime = formatTime(time);
        }
        if (dungeonscore >= 300 && !ssscore && Dungeon.sendSS && !playingSolo()) {
            ssscore = true;
            Chat.sendChat("/pchat " + Dungeon.SSmessage.replaceAll("%time%", showTime(formatTime(time))).replaceAll("%rush%", showTime(formatTime(time) - rushtime)).replaceAll("%270%", showTime(formatTime(time) - stime)));
        }
        if(!event.second(1)) {
            return;
        }
        if (Minecraft.getMinecraft().thePlayer != null) {
            //entitys = EntityTracker.searchCount("✯", 40);
            if (EntityTracker.searchEntity("Wither Key", 40, null) != null) {
                if (!dungeonScoreboard.key.contains("Collected")) {
                    dungeonScoreboard.key = "&e&lDropped§r";
                }
            }
            if (dungeonScoreboard.key.isEmpty()) {
                if (dungeonScoreboard.bloodtime > 0) {
                    dungeonScoreboard.bloodtime--;
                    stats.put("gui.text.dungeon", /*"&7Mobs: 0xdb42c7" + entitys + " Found0x955ac9 &8┃ */"&7Blood: &c" + dungeonScoreboard.bloodtime + "s &8┃ &7Milestone: " + dungeonScoreboard.milestone + mile);
                } else {
                    stats.put("gui.text.dungeon", /*"&7Mobs: 0xdb42c7" + entitys + " Found0x955ac9 &8┃ */"&7Milestone: " + dungeonScoreboard.milestone + mile);
                }
            } else {
                stats.put("gui.text.dungeon", /*"&7Mobs: 0xdb42c7" + entitys + " Found0x955ac9 &8┃ */"&7Key: " + dungeonScoreboard.key + " &8┃ &7Milestone: " + dungeonScoreboard.milestone + mile);
            }
        }
    }

    public int formatTime(String value) {
        String[] split = value.split(" ");
        int time = Integer.parseInt(split[0].replaceAll("[^0-9]",""));
        if(split[0].contains("m")) { time = time*60; }
        if(split.length > 1) { time += (Integer.parseInt(split[1].replaceAll("[^0-9]",""))); }
        return time;
    }

    public String showTime(int value) {
        int min = value / 60;
        int sec = value % 60;
        if(Dungeon.timeformat == 2) {
            if(min > 0) {
                return min + "m " + sec + "s";
            }
            return sec + "s";
        }
        if (min < 1) {
            return sec + "s";
        }
        double min1 = (double) value / 60.0;
        return String.format("%.1fm", min1).replaceAll(",",".");
    }

    public boolean playingSolo() {
        for(String line : Main.getAPI().scoreboardData) {
            if (line.contains("Solo")) {
                return true;
            }
        }
        return false;
    }

    public String getBetween(String text, String startWord, String endWord) {
        int startIndex = text.indexOf(startWord);
        int endIndex = text.indexOf(endWord);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return text.substring(startIndex + startWord.length(), endIndex).trim();
        }
        return "";
    }

}
