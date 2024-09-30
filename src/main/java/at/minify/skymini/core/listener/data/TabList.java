package at.minify.skymini.core.listener.data;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.core.manager.Chat;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@MiniRegistry(server = Server.NONE)
public class TabList extends MiniEvent {
    private final Ordering<NetworkPlayerInfo> playerOrdering = Ordering.from(new PlayerComparator());

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if(!event.second(1)) return;

        if(!isInGame()) return;
        List<NetworkPlayerInfo> players = playerOrdering.sortedCopy(Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap());
        List<String> tabListData = new ArrayList<>();
        for (NetworkPlayerInfo info : players) {
            String name = Minecraft.getMinecraft().ingameGUI.getTabList().getPlayerName(info);
            if(!tabListData.contains(name.replace("§r",""))) {
                tabListData.add(name.replace("§r",""));
            }
        }
        for(String name : tabListData) {
            if(name.contains("Server:")) {
                api().serverId = Chat.uncolored(name.replace("Server: ", ""));
            }
        }
        api().tabListData = tabListData;
    }

    private static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        @Override
        public int compare(NetworkPlayerInfo o1, NetworkPlayerInfo o2) {
            ScorePlayerTeam team1 = o1.getPlayerTeam();
            ScorePlayerTeam team2 = o2.getPlayerTeam();
            return ComparisonChain.start()
                    .compareTrueFirst(
                            o1.getGameType() != WorldSettings.GameType.SPECTATOR,
                            o2.getGameType() != WorldSettings.GameType.SPECTATOR
                    )
                    .compare(
                            team1 != null ? team1.getRegisteredName() : "",
                            team2 != null ? team2.getRegisteredName() : ""
                    )
                    .compare(o1.getGameProfile().getName(), o2.getGameProfile().getName())
                    .result();
        }
    }
}
