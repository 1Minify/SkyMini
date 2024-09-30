package at.minify.skymini.core.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static at.minify.skymini.core.GUI.categories.Chat.pchatrank;

public class Chat {

    private static final Pattern formattingCodePattern = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-Za-z]");

    public static String uncolored(String text) {
        return text.replaceAll("§[0-9A-Za-z]","");
    }

    public static String uncolored1(String text) {
        return (text == null) ? null : formattingCodePattern.matcher(text).replaceAll("");
    }


    public static String colored(String text) {
        return text == null ? null : text.replace("&", "§");
    }

    public static void send(String input) {
        String input1 = "&8» &dSkyMini &8❘ &7" + input;
        String coloredInput = input1.replace("&", "§");
        IChatComponent textComponent = new ChatComponentText(coloredInput);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponent);
    }

    public static void sendclear(String input) {
        String input1 = "&7" + input;
        String coloredInput = input1.replace("&", "§");
        IChatComponent textComponent = new ChatComponentText(coloredInput);
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(textComponent);
    }

    public static String getName(String element) {
        String[] firstSplit = element.split(" ");
        String rank = "";
        int i = 0;
        for(String word : firstSplit) {
            i++;
            if(i == firstSplit.length) {
                if(word.contains("[") && word.contains("]")) {
                    element = element.replace(" " + word, "");
                    rank = " §6" + word;
                }
            }
        }
        String[] split = element.split(" ");
        String name = split[0];
        String playerRank = "";
        if(split.length == 2) {
            if(split[0].startsWith("[")) {
                playerRank = split[0];
            }
            name = split[1];
        }
        if(split.length == 3) {
            playerRank = split[1];
            name = split[2];
        }
        if(pchatrank) {
            if(playerRank.contains("[VIP")) {name = "§a" + name;}
            else if(playerRank.contains("[MVP++")) {name = "§6" + name;}
            else if (playerRank.contains("[MVP")) {name = "§b" + name;}
            else {name = "§7" + name;}
        }
        return name + rank;
    }

    public static int getInt(String value) {
        int add = 1;
        if(value.contains("k")) { add = 1000; }
        if(value.contains("m")) { add = 1000000; }
        if(value.contains("b")) { add = 1000000; }
        try {
            double floatValue = Double.parseDouble(value.replaceAll("[a-zA-Z]", "").replaceAll(",","."));
            return (int) (floatValue * add);
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    public static String formatNumber(int number) {
        if(number == 0) { return "0"; }
        if(number >= 0) {
            if (number < 1000) {
                return String.format("%.1f", (float) number).replaceAll(",",".");
            } else if (number < 1000000) {
                return String.format("%.1fk", (float) number / 1000f).replaceAll(",",".");
            } else {
                return String.format("%.1fM", (float) number / 1000000f).replaceAll(",",".");
            }
        } else {
            if (number > -1000) {
                return String.format("%.1f", (float) number).replaceAll(",",".");
            } else if (number > -1000000) {
                return String.format("%.1fk", (float) number / 1000f).replaceAll(",",".");
            } else {
                return String.format("%.1fM", (float) number / 1000000f).replaceAll(",",".");
            }
        }
    }


    public static Map<String, Integer> commands = new HashMap<>();

    public static void sendChat(String message) {
        commands.put(message, 5);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if(!commands.isEmpty()) {
                List<String> commandsToRemove = new ArrayList<String>();

                for (Map.Entry<String, Integer> entry : commands.entrySet()) {
                    String command = entry.getKey();
                    int cooldown = entry.getValue();

                    if (cooldown > 0) {
                        entry.setValue(cooldown - 1);
                    } else {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
                        commandsToRemove.add(command);
                    }
                }

                for (String commandToRemove : commandsToRemove) {
                    commands.remove(commandToRemove);
                }
            }
        }
    }

}
