package at.minify.skymini.core.widgets.scoreboard.data;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.core.GUI.categories.Dungeon;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.scoreboard.ScoreboardManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(priority = 2)
@MiniRegistry(server = Server.CATACOMBS)
public class DungeonScoreboard {

    ScoreboardManager scoreboardManager = ServiceContainer.getService(ScoreboardManager.class);

    public int score = 0;
    public int needed = 0;
    public int keys = 0;
    public String key = "";
    public String grade = "";
    public String time = "";
    public int bloodtime = 0;
    public String milestone = "";
    public int deaths = 0;

    public void getScoreboard() {
        List<String> lines = new ArrayList<>();
        lines.add(scoreboardManager.lines.get(0));
        lines.add("§1");
        String key = "§fKeys: §a" + keys + "x";
        if (!this.key.isEmpty()) {
            key += " §7(" + this.key + "§7)";
        }
        lines.add(key);
        time = scoreboardManager.lines.get(6).replaceAll("Time Elapsed: §a§a", "");
        String time = "§fTime: 0x26bce2" + Objects.requireNonNull(scoreboardManager.getvalue(scoreboardManager.lines, "Time Elapsed")).replaceAll("Time Elapsed: §a§a", "") + "0x22e479";
        if (bloodtime != 0) {
            time += " &8┃ §c" + bloodtime + "s";
        }
        lines.add(time);
        //lines1.add(lines.get(7) + grade);
        if (Dungeon.DisplayNeededScore) {
            lines.add("§fScore: §b" + score + " §7(" + grade + "§7) &c+" + needed);
        } else {
            lines.add("§fScore: §b" + score + " §7(" + grade + "§7)");
        }
        lines.add("§2");
        lines.add("§fDeaths: 0xc82626" + deaths + "✗0xc82626"); //✘✗
        lines.add("§fMilestone: " + milestone);
        lines.add("§3");
        lines.add("players");
        lines.add("§4");
        lines.add("§ewww.hypixel.net");
        for (int i : Dungeon.scoreboardList) {
            if (i <= lines.size() - 1) {
                String value = lines.get(i);
                if (value.equals("players")) {
                    for (String line : scoreboardManager.lines) {
                        if (line.contains("[") || line.contains("Solo") || line.contains("Healthy") || line.contains("forced") || line.contains("Laser") || line.contains("Chaos")) {
                            scoreboardManager.lines1.add(line);
                        }
                    }
                } else {
                    scoreboardManager.lines1.add(value);
                }
            }
        }
    }

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if(!event.second(1)) {
            return;
        }
        if(Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        if(!(scoreboardManager.lines.size() >= 8)) {
            return;
        }
        if(!Main.getAPI().currentScoreboard.equals("dungeon")) {
            return;
        }
        String line = scoreboardManager.lines.stream().filter(s -> s.contains("Cleared")).findAny().orElse(null);
        if(line == null) {
            return;
        }
        Matcher matcher = Pattern.compile("Cleared: .*% \\((.*)\\)").matcher(Chat.uncolored(line));
        if(!matcher.find()) {
            return;
        }
        score = Integer.parseInt(matcher.group(1));
        if(score < 100) { grade = "§4&lD"; needed = 100-score; }
        if(score > 99 && score < 160) { grade = "§9&lC"; needed = 160-score; }
        else if(score > 159 && score < 230) { grade = "§2&lB"; needed = 230-score; }
        else if(score > 229 && score < 270) { grade = "§5&lA"; needed = 270-score; }
        else if(score > 269 && score < 300) { grade = "§e&lS"; needed = 300-score; }
        else if(score > 300) { grade = "§6&lS+"; needed = 0; }
    }

}
