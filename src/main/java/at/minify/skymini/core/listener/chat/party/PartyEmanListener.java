package at.minify.skymini.core.listener.chat.party;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.events.MiniChatEvent;
import at.minify.skymini.api.util.MiniEvent;
import at.minify.skymini.api.events.MiniPartyChatEvent;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.util.Config;
import at.minify.skymini.util.EntityTracker;
import at.minify.skymini.util.stats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

import static at.minify.skymini.util.Config.config;

@Service(priority = 1)
@MiniRegistry
public class PartyEmanListener extends MiniEvent {

    public PartyEmanListener() {
        load();
    }

    public Map<String, CarryPlayer> players = new HashMap<>();

    @SubscribeEvent
    public void onChat(MiniChatEvent event) {
        String message = event.getMessage();
        String name = "null";
        if (message.contains("LOOT SHARE")) {
            name = event.getWord(message,8);
            for (Map.Entry<String, CarryPlayer> entry : players.entrySet()) {
                String playerName = entry.getKey();
                if(name.contains(playerName)) {
                    name = playerName;
                }
            }
            if(!players.containsKey(name)) return;
            CarryPlayer carryPlayer = players.get(name);
            String allSpawned = EntityTracker.searchEntityNames("Spawned by",30,null);
            if(allSpawned.contains(name)) {
                String type = carryPlayer.getType();
                if (type == null) {
                    return;
                }
                updatePlayer(carryPlayer, name);
                int carrys = 0;
                if(stats.get("end.carry.count") != null) {
                    carrys = (Integer) stats.get("end.carry.count");
                }
                carrys++;
                stats.put("end.carry.count", carrys);
                stats.savepaths("end.carry.count");
            }
        }
        if (message.contains("was killed by Voidgloom Seraph")) {
            for (Map.Entry<String, CarryPlayer> entry : players.entrySet()) {
                String name1 = entry.getKey();
                if(message.contains(name1)) {
                    Chat.sendChat("/pchat " + name1 + " failed the Quest! No counter update.");
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPartyChat(MiniPartyChatEvent event) {
        String message = event.getMessage();
        if(message.contains("!math")) {
            String math = event.getWord(message, 2);
            String[] calc = math.split("\\*");
            int value = Chat.getInt(calc[0])* Chat.getInt(calc[1]);
            Chat.sendChat("/pchat Value: " + Chat.formatNumber(value));
        }
        if(message.contains("!total")) {
            Chat.sendChat("Total Carry's done: " + stats.get("end.carry.count"));
        }
        if(message.contains("!need")) {
            String countId = event.getWord(message, 2);
            if(countId == null) {
                Chat.sendChat("/pchat Usage: !need <count>");
                return;
            }
            int count = 0;
            try {
                count = Integer.parseInt(countId);
            } catch (NumberFormatException e) {
                Chat.sendChat("/pchat You must set a valid number!");
                return;
            }
            if(count < 1) {
                Chat.sendChat("/pchat You can't set the counter under 1!");
                return;
            }
            if(!players.containsKey(event.getName())) {
                players.put(event.getName(), new CarryPlayer(0, count, "down", null, null));
                Chat.sendChat("/pchat Created a new counter for " + event.getName() + "! (0/" + count + ")");
                save();
                return;
            }
            CarryPlayer carryPlayer = players.get(event.getName());
            if(carryPlayer.getType().equals("up")) {
                if(carryPlayer.getLimit() == null) return;
                if(carryPlayer.getFullCount() == null) return;
                Chat.sendChat("/pchat Updated needed counter of " + event.getName() + " to " + count);
                save();
                return;
            }
            if(carryPlayer.getType().equals("down")) {
                int oldCount = carryPlayer.getCount();
                int total1 = carryPlayer.getLimit() - count;
                total1 = total1*-1;
                if (count > carryPlayer.getLimit()) {
                    carryPlayer.setLimit(carryPlayer.getLimit() + total1 + oldCount);
                } else {
                    carryPlayer.setLimit(carryPlayer.getLimit() + count);
                }
                Chat.sendChat("/pchat Updated needed counter of " + event.getName() + " to " + count);
                save();
                return;
            }
        }
        if(message.contains("!info")) {
            String name = event.getWord(message, 2);
            if(name == null) {
                Chat.sendChat("/pchat Usage: !info <name>");
                return;
            }
            if(!players.containsKey(name)) {
                Chat.sendChat("/pchat This player has no counter");
                return;
            }
            CarryPlayer carryPlayer = players.get(name);
            if(carryPlayer.getLimit() == null) {
                sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + ")", false);
                return;
            }
            if(carryPlayer.getType().equals("down")) {
                int left = carryPlayer.getLimit() - carryPlayer.getCount();
                sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + "/" + carryPlayer.getLimit() + " - " + left + " left)", false);
                return;
            }
            if(carryPlayer.getType().equals("up")) {
                if(carryPlayer.getFullCount() != null) {
                    sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + "/" + carryPlayer.getLimit() + " - total left: " + carryPlayer.getFullCount() + ")", false);
                } else {
                    sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + "/" + carryPlayer.getLimit() + ")", false);
                }
            }
        }
        if(event.getName().equals(Minecraft.getMinecraft().thePlayer.getName())) {
            if(message.contains("!reset")) {
                String name = event.getWord(message, 2);
                if(!players.containsKey(name)) {
                    Chat.sendChat("/pchat Counter for " + name + " not found!");
                    return;
                }
                Chat.sendChat("/pchat Reset of " + name + " successfully!");
                players.remove(name);
                save();
            }
            if(message.contains("!update")) {
                String name = event.getWord(message, 2);
                if(players.containsKey(name)) {
                    Chat.sendChat("/pchat Counter for " + name + " not found!");
                    return;
                }
                updatePlayer(players.get(name), name);
            }
        }
    }

    public void updatePlayer(CarryPlayer carryPlayer, String name) {
        carryPlayer.setCount(carryPlayer.getCount() + 1);
        int count = carryPlayer.getCount();
        if(carryPlayer.getLimit() == null) {
            sendInfo(carryPlayer, name, "+1 to %name%! (Now: " + count + ")", true);
            return;
        }
        int limit = carryPlayer.getLimit();
        if(carryPlayer.getType().equals("down")) {
            int left = limit-count;
            if (left < 1) {
                Chat.sendChat("/pchat " + name + " finished all! (" + count + "/" + limit + ")");
                players.remove(name);
                save();
                return;
            }
            sendInfo(carryPlayer, name, "+1 to %name%! (Now: " + count + "/" + limit + " - " + left + " left)", true);
            return;
        }
        if(carryPlayer.getFullCount() == null) {
            if (count >= limit) {
                carryPlayer.setCount(0);
                Chat.sendChat("/pchat Reset score of " + name + " (" + count + "/" + limit + ")");
                save();
            } else {
                sendInfo(carryPlayer, name, "+1 to %name%! (Now: " + count + "/" + limit + ")", true);
            }
            return;
        }
        int full = carryPlayer.getFullCount()-1;
        carryPlayer.setFullCount(full);
        if (full > 0) {
            if (count >= limit) {
                carryPlayer.setCount(0);
                sendInfo(carryPlayer, name, "Reset score of %name% (" + count + "/" + limit + " - total left: " + full + ")", true);
            } else {
                Chat.sendChat("/pchat +1 to " + name + "! (Now: " + count + "/" + limit + " - total left: " + full + ")");
                save();
                return;
            }
        } else {
            if (count >= limit) {
                sendInfo(carryPlayer, name, "%name% finished all! (" + count + "/" + limit + " - total left: " + full + ")", true);
            } else {
                Chat.sendChat("/pchat " + name + " finished all! (" + count + "/" + limit + " - total left: " + full + ")");
            }
            players.remove(name);
            save();
        }


    }

    public void sendInfo(CarryPlayer carryPlayer, String name, String message, boolean payout) {
        if(carryPlayer.getPrice() != null) {
            if(payout) {
                Chat.sendChat("/pchat " + message.replaceAll("%name%", name) + " Payout: " + carryPlayer.getPrice());
                return;
            }
            Chat.sendChat("/pchat " + message.replaceAll("%name%", name) + " " + carryPlayer.getPrice());
            return;
        }
        Chat.sendChat("/pchat " + message.replaceAll("%name%", name));
    }

    public void load() {
        if(!config.hasCategory("eman.type")) return;
        for (Map.Entry<String, Property> entry : config.getCategory("eman.type").getValues().entrySet()) {
            String name = entry.getKey();
            Integer count = config.getCategory("eman.count").get(name).getInt();
            Integer fullCount = config.getCategory("eman.fullcount").get(name).getInt();
            String type = config.getCategory("eman.type").get(name).getString();
            Integer limit = config.getCategory("eman.limit").get(name).getInt();
            String price = config.getCategory("eman.price").get(name).getString();
            CarryPlayer carryPlayer = new CarryPlayer(count, fullCount, type, limit, price);
            players.put(name, carryPlayer);
        }
    }

    public void save() {
        Config.clearCategory("eman.count");
        Config.clearCategory("eman.fullcount");
        Config.clearCategory("eman.type");
        Config.clearCategory("eman.limit");
        Config.clearCategory("eman.price");
        for(Map.Entry<String, CarryPlayer> entry : players.entrySet()) {
            String name = entry.getKey();
            CarryPlayer carryPlayer = entry.getValue();
            Config.setInt("eman.count", name, carryPlayer.getCount());
            Config.setInt("eman.fullcount", name, carryPlayer.getFullCount());
            Config.setString("eman.type", name, carryPlayer.getType());
            Config.setInt("eman.limit", name, carryPlayer.getLimit());
            Config.setString("eman.price", name, carryPlayer.getPrice());
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CarryPlayer {

        private Integer count;
        private Integer fullCount;
        private String type;
        private Integer limit;
        private String price;

    }

}
