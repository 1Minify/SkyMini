package at.minify.skymini.core.widgets.scoreboard.data;

import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.listener.chat.party.PartyEmanListener;
import at.minify.skymini.core.widgets.scoreboard.ScoreboardManager;
import at.minify.skymini.util.stats;

import java.util.Map;

@Service(priority = 2)
public class EmanScoreboard {

    ScoreboardManager scoreboardManager = ServiceContainer.getService(ScoreboardManager.class);
    PartyEmanListener partyEmanListener = ServiceContainer.getService(PartyEmanListener.class);

    public void getScoreboard() {
        scoreboardManager.lines1.add(scoreboardManager.getvalue(scoreboardManager.lines,"/"));
        scoreboardManager.lines1.add("§1");
        //lines1.add("§fTotal: §a" + stats.get("end.carry.count") + " Kills");
        //lines1.add("§2");
        int i = 0;
        for (Map.Entry<String, PartyEmanListener.CarryPlayer> entry : partyEmanListener.players.entrySet()) {
            scoreboardManager.lines1.add(getCarryInfo(entry.getKey(), entry.getValue()));
            i++;
        }
        if(i != 0) {
            scoreboardManager.lines1.add("§3");
        }
        String kills = (String) stats.get("end.carry.count");
        if(kills == null) {
            kills = "0";
        }
        scoreboardManager.lines1.add("§fTotal: §a" + kills + " Kills");
        if(Display.displayCustomHUD) {
            scoreboardManager.lines1.add(scoreboardManager.getPurse(Display.purseFormat));
            scoreboardManager.lines1.add(scoreboardManager.getBits(Display.bitsFormat));
        }
        i = 0;
        for(String line : scoreboardManager.lines) {
            if(line.contains("Slayer Quest")) {
                scoreboardManager.lines1.add("§4");
                scoreboardManager.lines1.add(scoreboardManager.lines.get(i));
                scoreboardManager.lines1.add(scoreboardManager.lines.get(i+1));
                scoreboardManager.lines1.add(scoreboardManager.lines.get(i+2));
            }
            i++;
        }
        scoreboardManager.lines1.add("§5");
        scoreboardManager.lines1.add("§ewww.hypixel.net");
    }

    public String getCarryInfo(String name, PartyEmanListener.CarryPlayer carryPlayer) {
        if(carryPlayer.getCount() == null) return "not found";
        int count = carryPlayer.getCount();
        if(carryPlayer.getLimit() == null) {
            return  "§f" + name + " &a" + count + getPrice(carryPlayer);
        }
        int limit = carryPlayer.getLimit();
        if(carryPlayer.getType().equals("down")) {
            int left = limit - count;
            return "§f" + name + " §c" + count + "§7/§a" + limit + " §7(§e" + left + "§7)" + getPrice(carryPlayer);
        }
        if (carryPlayer.getFullCount() != null) {
            return "§f" + name + " §c" + count + "§7/§a" + limit + " §7(§d" + carryPlayer.getFullCount() + "§7)" + getPrice(carryPlayer);
        }
        return  "§f" + name + " §c" + count + "§7/§a" + limit +getPrice(carryPlayer);
    }

    public String getPrice(PartyEmanListener.CarryPlayer carryPlayer) {
        if(carryPlayer.getPrice() == null) return "";
        return "&6" + carryPlayer.getPrice();
    }

}
