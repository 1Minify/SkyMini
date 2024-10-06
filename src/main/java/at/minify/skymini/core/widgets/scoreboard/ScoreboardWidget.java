package at.minify.skymini.core.widgets.scoreboard;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.api.widgets.manager.LocationManager;
import at.minify.skymini.api.widgets.manager.WidgetManager;
import at.minify.skymini.core.GUI.categories.Crimson;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.widgets.KuudraWidget;
import at.minify.skymini.core.widgets.scoreboard.data.DungeonScoreboard;
import at.minify.skymini.core.widgets.scoreboard.data.EmanScoreboard;
import at.minify.skymini.core.widgets.util.WidgetExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Collections;

@Service(priority = 11)
public class ScoreboardWidget {

    ScoreboardManager scoreboardManager = ServiceContainer.getService(ScoreboardManager.class);
    EmanScoreboard emanScoreboard = ServiceContainer.getService(EmanScoreboard.class);
    DungeonScoreboard dungeonScoreboard = ServiceContainer.getService(DungeonScoreboard.class);
    Bossbar bossbar = ServiceContainer.getService(Bossbar.class);

    public static String Sidebar = "null";
    public static String oldtitle;

    public void render() {
        scoreboardManager.lines = Main.getAPI().scoreboardData;
        if (Minecraft.getMinecraft().currentScreen instanceof LocationManager) { return; }
        scoreboardManager.updatePurse(scoreboardManager.lines);
        scoreboardManager.updateBits(scoreboardManager.lines);
        if(!Display.customScoreboard) { return; }
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        if(scoreboardManager.lines.isEmpty()) { return; }
        scoreboardManager.lines1.clear();
        if(Display.islandTitle) {
            addTitle();
        } else {
            scoreboardManager.lines1.add("center:true" + Display.customTitle.replaceAll("#","0x").replaceAll("&","§"));
        }
        //scoreboardManager.lines1.add("0xdb42c7§lSKYMINI0x955ac9");
        String customSB = "null";
        for(String line : scoreboardManager.lines) {
            line = line.replaceAll("§[0-9a-fA-Fklmnor]", "");
            if (line.contains("Zealot Bruiser Hideout") || line.contains("Void Sepulture")) {
                if(Display.emanScoreboard) {
                    customSB = "eman";
                    emanScoreboard.getScoreboard();
                    break;
                }
            }
            if(line.contains("Cleared:")) {
                customSB = "dungeon";
                dungeonScoreboard.getScoreboard();
                break;
            }

        }
        Main.getAPI().currentScoreboard = customSB;
        if(Display.customBossBar) {
            bossbar.drawBossBar(scoreboardManager.lines);
        }
        int i = 1;
        if(customSB.equals("null")) {
            for(String line : scoreboardManager.lines) {
                if(line.contains("Purse:") || line.contains("Piggy:")) {
                    scoreboardManager.lines1.add(scoreboardManager.getPurse(Display.purseFormat));
                    i++;
                }
                else if(line.contains("Bits:")) {
                    scoreboardManager.lines1.add(scoreboardManager.getBits(Display.bitsFormat));
                    i++;
                } else {
                    scoreboardManager.lines1.add(line);
                    if(Display.customBossBar) {
                        if(line.contains("Summer") || line.contains("Autumn") || line.contains("Winter") || line.contains("Spring")) {
                            scoreboardManager.lines1.remove(i);
                            scoreboardManager.lines1.remove(i-1);
                            i -= 2;
                        }
                        else if(line.contains("am ") || line.contains("pm ") || line.contains("⏣")) {
                            scoreboardManager.lines1.remove(i);
                            i -= 1;
                        }
                    }
                    if(Crimson.displayKuudraWidget && Main.getAPI().server == Server.KUUDRA) {
                        if(line.contains("Protect Elle") || line.contains("Rescue")) {
                            //stats.put("gui.text.kuudra",line);
                            WidgetService.getWidget(KuudraWidget.class).setText(line);
                            scoreboardManager.lines1.remove(i); scoreboardManager.lines1.remove(i-1); scoreboardManager.lines1.remove(i-2);
                            i -= 3;
                        }
                        if(line.contains("defeat Kuudra")) {
                            scoreboardManager.lines1.remove(i); scoreboardManager.lines1.remove(i-1); scoreboardManager.lines1.remove(i-2); scoreboardManager.lines1.remove(i-3);
                            i -= 4;
                        }
                    }
                    i++;
                }
            }
        }
        scoreboardManager.lines1.removeAll(Collections.singleton(null));
        int longest = WidgetManager.longest(scoreboardManager.lines1);
        float texty = scoreboardManager.lines1.size()*(fontRenderer.FONT_HEIGHT+1);
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedx = scaledRes.getScaledWidth();
        int scaledy = scaledRes.getScaledHeight();
        float x = sclaedx-longest/*-5*/-10;
        float y = ((float) scaledy /2)-(texty/2)-1;
        StringBuilder sb = new StringBuilder();
        for(String line : scoreboardManager.lines1) { sb.append(line).append("\n"); }
        String sb1 = sb.substring(0, sb.length() - 1);
        WidgetExecutor.renderWidget(sb1, x,y,false,true);
    }

    public void addTitle() {
        Server server = Main.getAPI().server;
        switch (server) {
            case CRIMSON_ISLE: oldtitle = ("0xdd1515§lCrimson Isle0xce6c24"); break;
            case KUUDRA: oldtitle = ("0xdd1515§lKuudra0xce6c24"); break;
            case THE_END: oldtitle = ("0x7e04ef§lThe End0xdc0cd5"); break;
            case PRIVATE_ISLAND: oldtitle = ("0x1add15§lPrivate Island0x24cebc"); break;
            case SPIDERS_DEN: oldtitle = ("0xef7e04§lSpider's Den0x6e4829"); break;
            case THE_PARK: oldtitle = ("0x10d71e§lThe Park0x2e622e"); break;
            case DUNGEON_HUB: oldtitle = ("0xd71010§lDungeon Hub0xe317a2"); break;
            case FARMING_ISLANDS: oldtitle = ("0x10d72c§lFarming Islands0xc4df0b"); break;
            case GOLD_MINE: oldtitle = ("0xd7af10§lGold Mine0xdb7406"); break;
            case DEEP_CAVERNS: oldtitle = ("0xdb42c7§lDeep Caverns0x955ac9"); break;
            case DWARVEN_MINES: oldtitle = ("0x18c375§lDwarven Mines0x008fbf"); break;
            case CRYSTAL_HOLLOWS: oldtitle = ("0xdb42c7§lCrystal Hollows0x955ac9"); break;
            case GARDEN: oldtitle = ("0x1add15§lGarden0x24cebc"); break;
            case HUB: oldtitle = ("0xc5ef04§lHub Island0x23cc38"); break;
        }
        String region = Main.getAPI().region;
        if(region.replaceAll("§.", "").contains("Catacombs")) {
            oldtitle = ("0xd71010§lDungeon0xe317a2");
        }
        if(!Main.getAPI().inSkyBlock || oldtitle == null) { scoreboardManager.lines1.add("center:true" + Display.customTitle.replaceAll("#","0x").replaceAll("&","§")); return; }
        scoreboardManager.lines1.add("center:true" + oldtitle);
    }

}
