package at.minify.skymini.core.listener.data;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@MiniRegistry(server = Server.NONE)
public class Scoreboard extends MiniEvent {

    private static final List<String> SPLIT_ICONS = Arrays.asList("\uD83C\uDF6B", "\uD83D\uDCA3", "\uD83D\uDC7D", "\uD83D\uDD2E", "\uD83D\uDC0D", "\uD83D\uDC7E", "\uD83C\uDF20", "\uD83C\uDF6D", "⚽", "\uD83C\uDFC0", "\uD83D\uDC79", "\uD83C\uDF81", "\uD83C\uDF89", "\uD83C\uDF82");
    private int seconds = 0;

    //private static List<String> sidebarLines = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void tick(MiniTickEvent event) {
        List<String> list = getscoreboard(event);
        Collections.reverse(list);
        /*List<String> semiFormatted = list.stream().map(this::cleanSB).collect(Collectors.toList());
        if (!semiFormatted.equals(sidebarLines)) {
            sidebarLines = semiFormatted;
        }*/
        List<String> newFormatted = list.stream()
                .map(line -> SPLIT_ICONS.stream().filter(line::contains).findFirst()
                        .map(separator -> line.replaceFirst(Pattern.quote(separator), ""))
                        .orElse(line))
                .collect(Collectors.toList());
        if (!newFormatted.equals(list)) {
            list = newFormatted;
        }
        for(String line : list) {
            if(line.contains("⏣")) {
                api().region = line.replaceAll(" §7⏣ ","");
            }
            if(line.contains("/") && line.contains("§8") && line.contains("§7")) {
                String[] split = line.split("§8");
                String[] split1 = split[1].split(" ");
                //api().serverId = split1[0];
            }
        }
        api().scoreboardData = list;
        if(event.second(1)) {
            if(!isInGame()) {
                seconds++;
                if(seconds == 10) {
                    api().inSkyBlock = false;
                }
            } else {
                seconds = 0;
            }
        }
    }

    private List<String> getscoreboard(MiniTickEvent event) {
        net.minecraft.scoreboard.Scoreboard scoreboard = Minecraft.getMinecraft().theWorld != null ? Minecraft.getMinecraft().theWorld.getScoreboard() : null;
        if (scoreboard == null) {
            return Collections.emptyList();
        }
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) {
            return Collections.emptyList();
        }

        loadInSkyBlock(Chat.uncolored1(objective.getDisplayName()).contains("SKYBLOCK"));

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = scores.stream().filter(s -> s != null && s.getPlayerName() != null && !s.getPlayerName().startsWith("#")).collect(Collectors.toList());
        scores = list.size() > 15 ? list.subList(15, list.size()) : list;
        return scores.stream().map(s -> ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(s.getPlayerName()), s.getPlayerName())).collect(Collectors.toList());
    }

    private void loadInSkyBlock(boolean value) {
        if(value && api().server == Server.NONE) {
            api().server = Server.SKYBLOCK;
            api().inSkyBlock = true;
        }
        if(!value && api().server != Server.NONE) {
            api().inSkyBlock = false;
        }
    }

}
