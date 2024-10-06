package at.minify.skymini.core.listener.chat.party;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.core.manager.Chat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@MiniRegistry(server = Server.SKYBLOCK)
public class PartyWarpListener extends MiniEvent {

    private boolean hideList = false;
    private int ticks = 0;
    private int lobbySwitch = -1;
    private int warpTime = 6;
    private boolean warpCooldown = false;
    private boolean hideAll = false;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void chat(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        Matcher matcher = Pattern.compile("Party > (.*?): (.*)").matcher(message);
        if(matcher.find()) {
            String nameElement = matcher.group(1);
            String name = Chat.getName(nameElement);
            if(name.equals(Minecraft.getMinecraft().thePlayer.getName())) {
                if(matcher.group(2).contains("!newlobby")) {
                    Chat.sendChat("/pchat Switching Lobby in 5 seconds! Type 'cancel' to cancel");
                    lobbySwitch = 0;
                }
                return;
            }
            if(at.minify.skymini.core.GUI.categories.Chat.partyAutoWarp) {
                if(matcher.group(2).contains("cancel") && !name.equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getName())) {
                    Chat.sendChat("/pchat Lobby switch canceled!");
                    lobbySwitch = -1;
                }
                if (message.contains("Party") && message.contains("warp") && !message.contains("is full") && !warpCooldown) {
                    hideList = true;
                    //sendChat("/p list");
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/p list");
                }
            }
        }
        if(message.contains("Party Leader:") && hideList) {
            if(message.contains(Minecraft.getMinecraft().thePlayer.getName())) {
                Chat.sendChat("/pchat Warping in 5 seconds! Type 'cancel' to cancel");
                warpCooldown = true;
            }
        }
        if(message.contains("-------------") && hideList) {
            hideAll = !hideAll;
            event.setCanceled(true);
            if(!hideAll) {
                hideList = false;
            }
        }
        if(hideList) {
            if(hideAll) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void startWarp(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && isInGame()) {
            ticks++;
            if (ticks >= 201) { ticks = 0; }
            if (!warpCooldown) {
                warpTime = 6;}
            if (ticks % 20 == 0) {
                if(warpCooldown) {
                    warpTime = warpTime -1;
                    if(warpTime == 0) {
                        warpCooldown = false;
                        warpTime = 5;
                        Chat.sendChat("/p warp");
                    }
                }
                if(lobbySwitch != -1) lobbySwitch++;
                if(lobbySwitch == 6) Chat.sendChat("/is");
                if(lobbySwitch == 8) Chat.sendChat("/p warp");
                if(lobbySwitch == 12) Chat.sendChat("/warp drag");
                if(lobbySwitch == 14) {
                    Chat.sendChat("/p warp");
                    lobbySwitch = -1;
                }
            }
        }
    }

}
