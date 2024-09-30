package at.minify.skymini.core.listener.chat;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.api.api.TranslateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static at.minify.skymini.core.GUI.categories.Chat.*;

@MiniRegistry(server = Server.SKYBLOCK)
public class MSGListener extends MiniEvent {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void chat(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if(message.contains("Inventory full?")) {
            event.setCanceled(true);
        }
        setupTranslation(event, event.message.getUnformattedText());
        changeMSG(event, message);
        changePartyChat(event, message);
        changeGuildChat(event, message);
    }

    private void setupTranslation(ClientChatReceivedEvent event, String message) {
        if(!at.minify.skymini.core.GUI.categories.Chat.translobby) return;
        if(!message.startsWith("[")) return;
        if(!message.contains(":")) return;
        if(!message.contains("]")) return;
        String[] split = message.split(":");
        TranslateAPI.send(split[0] + "§7: §f",split[1],"");
        event.setCanceled(true);
    }

    private void changeMSG(ClientChatReceivedEvent event, String message) {
        if(!cmsg) return;
        Matcher matcher = Pattern.compile("(.*) (.*?): (.*)").matcher(message);
        if(!matcher.find()) return;
        if(!matcher.group(1).contains("From") && !matcher.group(1).contains("To")) return;
        boolean own = matcher.group(1).contains("To");
        String name = Chat.getName(matcher.group(2));
        String client = Minecraft.getMinecraft().thePlayer.getName();
        String text = matcher.group(3);
        if(own) {
            send("§7[§dMSG§7] §c" + client + " §7» " + name + "§7: §e" + text);
        } else {
            send("§7[§dMSG§7] §7" + name + " §7» §c" + client + "§7: §e" + text);
        }
        event.setCanceled(true);
    }

    private void changePartyChat(ClientChatReceivedEvent event, String message) {
        if(!cpchat) return;
        Matcher matcher = Pattern.compile("Party > (.*?): (.*)").matcher(message);
        if(!matcher.find()) return;
        String pchat = pchatformat;
        if(!pchat.contains("%name%") || !pchat.contains("%text%")) {
            Chat.send("&7Custom Chat has been &cdisabled &7for reason:");
            Chat.send("&c%name% or %text% not found");
            cpchat = !cpchat;
            return;
        }
        String nameElement = matcher.group(1);
        String name = Chat.getName(nameElement);
        pchat = pchat.replace("%name%", name).replace("%text%", matcher.group(2)).replace("&", "§");
        event.setCanceled(true);
        if(at.minify.skymini.core.GUI.categories.Chat.transparty && !name.contains(Minecraft.getMinecraft().thePlayer.getName())) {
            TranslateAPI.send("",pchat,"");
        } else {
            send(pchat);
        }
    }

    private void changeGuildChat(ClientChatReceivedEvent event, String message) {
        if(!gchat) return;
        Matcher matcher = Pattern.compile("Guild > (.*?): (.*)").matcher(message);
        if(!matcher.find()) return;
        String pchat = gchatformat;
        if(!pchat.contains("%name%") || !pchat.contains("%text%")) {
            Chat.send("&7Guild Chat has been &cdisabled &7for reason:");
            Chat.send("&c%name% or %text% not found");
            cpchat = !cpchat;
            return;
        }
        String nameElement = matcher.group(1);
        String name = Chat.getName(nameElement);
        pchat = pchat.replace("%name%", name).replace("%text%", matcher.group(2)).replace("&","§");
        send(pchat);
        event.setCanceled(true);
    }

    public void send(String text) {
        text = text.replaceAll("&&", "§");
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
    }

}
