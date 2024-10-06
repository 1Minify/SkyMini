package at.minify.skymini.core.widgets.manager;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.core.GUI.categories.Crimson;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.CrimsonBossesWidget;
import at.minify.skymini.core.widgets.CrimsonQuestsWidget;
import at.minify.skymini.util.stats;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@MiniRegistry
public class CrimsonWidgetManager {

    CrimsonQuestsWidget crimsonQuestsWidget = WidgetService.getWidget(CrimsonQuestsWidget.class);
    CrimsonBossesWidget crimsonBossesWidget = WidgetService.getWidget(CrimsonBossesWidget.class);

    public List<String> dailys = new ArrayList<>();
    int dif = 0;

    @SubscribeEvent
    public void second(MiniTickEvent event) {
        if(!event.second(1)) return;
        Date newDate = new Date();
        Date update = updatedate(newDate);
        dif = datedifference(newDate,update);

        for (int i = 0; i < Main.getAPI().tabListData.size(); i++) {
            String line = Main.getAPI().tabListData.get(i);
            if(line.contains("Faction Quests")) {
                dailys.clear();
                for(int i1 = 1; i1 < 6; i1++) {
                    dailys.add(Main.getAPI().tabListData.get(i + i1));
                }
            }
        }
        StringBuilder quests = new StringBuilder();
        quests.append("&5Daily Quests &8(&7").append(formatDuration(dif)).append("&8)");
        for(String quest : dailys) {
            quest = quest.replaceFirst(" ","");
            if(quest.contains("§d")) {
                quests.append("\n0xcc2a2a").append(Chat.uncolored(quest)).append("0x761e1e");}
            else if(quest.contains("§e")) {
                quests.append("\n0xcc982a").append(Chat.uncolored(quest)).append("0x8d6c26");}
            else if(quest.contains("§a") && !Crimson.displayAll) {
                quests.append("\n0x4ccc2a").append(Chat.uncolored(quest)).append("0x2c661c");}
        }
        if(crimsonQuestsWidget != null) crimsonQuestsWidget.setText(quests.toString());
        StringBuilder bosses = new StringBuilder();
        if(stats.get("crimson.boss.bladesoul") == null) { stats.put("crimson.boss.bladesoul","null"); } //EVTL PATHLISTE ERSTELLEN UND FÜR ALLE AUF ALTES DATUM SETZEN: 2023-09-09 19:50:35
        bosses.append("&5Daily Bosses &8(&7").append(formatDuration(dif)).append("&8)");
        for(String path : bosslist) {
            String olddate = (String) stats.get("crimson.boss." + path);
            String name = getboss(path);
            if(olddate != null && olddate.equalsIgnoreCase("null")) { bosses.append("\n&c0xcc2a2a").append(name).append("0x761e1e"); }
            else if(olddate != null && datedifference(newDate,stringToDate(olddate)) > 0) {
                if(!Crimson.displayAll) {
                    bosses.append("\n&a0x4ccc2a").append(name).append("0x2c661c");
                }
            } else {
                bosses.append("\n&c0xcc2a2a").append(name).append("0x761e1e");
            }
        }
        crimsonBossesWidget.setText(bosses.toString());
    }

    private final List<String> bosslist = new ArrayList<>(Arrays.asList("barbarian", "bladesoul", "ashfang", "magma", "mage"));
    private String getboss(String path) {
        if(path.contains("barbarian")) { return "Barbarian Duke X"; }
        else if(path.contains("bladesoul")) { return "Bladesoul"; }
        else if(path.contains("ashfang")) { return "Ashfang"; }
        else if(path.contains("magma")) { return "Magma Boss"; }
        else if(path.contains("mage")) { return "Mage Outlaw"; }
        return "not found";
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        if(message.contains("Find a way to reach the top of the stomach!")) {
            String newDate = dateToString(new Date());
            stats.put("crimson.heavy",newDate);
            stats.savepaths("crimson.heavy");
        }
        if(message.contains("DOWN!") && Main.getAPI().server.equals(Server.CRIMSON_ISLE)) {
            String newDate = dateToString(updatedate(new Date()));
            if(message.contains("BARBARIAN")) {stats.put("crimson.boss.barbarian", newDate);}
            if(message.contains("BLADESOUL")) {stats.put("crimson.boss.bladesoul", newDate);}
            if(message.contains("ASHFANG")) {stats.put("crimson.boss.ashfang", newDate);}
            if(message.contains("MAGMA")) {stats.put("crimson.boss.magma", newDate);}
            if(message.contains("OUTLAW")) {stats.put("crimson.boss.mage", newDate);}
            stats.savepaths("crimson.boss");
        }
    }





    public Date updatedate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (date.after(cal.getTime())) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return cal.getTime();
    }

    public int datedifference(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {return -1;}
        long timeinMillis = endDate.getTime() - startDate.getTime();
        return (int) (timeinMillis / 1000);
    }

    public String dateToString(Date date) {
        return dateFormat.format(date);
    }
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Date stringToDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public String formatDuration(int s) {
        int days = s / (24 * 60 * 60);
        int hours = (s % (24 * 60 * 60)) / (60 * 60);
        int minutes = (s % (60 * 60)) / 60;
        int seconds = s % 60;

        StringBuilder duration = new StringBuilder();
        if (days > 0) { duration.append(days).append("d "); }
        if (hours > 0) { duration.append(hours).append("h "); }
        if (minutes > 0) { duration.append(minutes).append("m"); }
        if(hours == 0 && minutes == 0) duration.append(seconds).append("s");

        return duration.toString();
    }

}
