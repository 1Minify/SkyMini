package at.minify.skymini.core.commands;

import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.api.util.MiniCommand;
import at.minify.skymini.core.listener.chat.party.PartyEmanListener;
import at.minify.skymini.core.manager.Chat;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmanCommand extends MiniCommand {

    PartyEmanListener partyEmanListener = ServiceContainer.getService(PartyEmanListener.class);

    @Override
    public String getCommandName() {
        return "eman";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/eman";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String sub = getWord(args, 1);
        if(args.length == 0) {
            Chat.send("&6Enderslayer Carry Commands:");
            Chat.send("&e/eman set <name> <up/down> <count/limit>");
            Chat.send("&e/eman setprice <name> <price>");
            Chat.send("&e/eman setfull <name> <total>");
            Chat.send("&e/eman reset <name>");
            Chat.send("&e/eman info <name>");
            Chat.send("&e/eman update <name>");
            Chat.send("&e/eman list");
        }
        if(args.length == 1) {
            if(sub.equalsIgnoreCase("list")) {
                Chat.send("§eList of all players:");
                for(Map.Entry<String, PartyEmanListener.CarryPlayer> entry : partyEmanListener.players.entrySet()) {
                    getInfo(entry.getKey(), entry.getValue());
                }
            }
        }
        if(args.length == 2) {
            String name = args[1];
            if(!partyEmanListener.players.containsKey(name)) {
                Chat.send("§cThe player §e" + name + " §chas no counter!");
                return;
            }
            if(sub.equalsIgnoreCase("update")) {
                partyEmanListener.updatePlayer(partyEmanListener.players.get(name), name);
            }
            if(sub.equalsIgnoreCase("info")) {
                getInfo(name, partyEmanListener.players.get(name));
            }
            if(sub.equalsIgnoreCase("reset")) {
                Chat.send("§7You §cdeleted §7the player §e" + name + "§7!");
                partyEmanListener.players.remove(name);
                partyEmanListener.save();
            }
        }
        if (args.length == 3) {
            String name = getWord(args, 2);
            String value = getWord(args, 3);
            if(sub.equalsIgnoreCase("setprice")) {
                if(!partyEmanListener.players.containsKey(name)) {
                    Chat.send("§cThe player §e" + name + " §chas no counter!");
                    return;
                }
                if(value.equalsIgnoreCase("0")) {
                    Chat.send("§7you §cremoved §7the price for §e" + name + "§7!");
                    return;
                }
                Chat.send("§7Price of §e" + name + " §7set to §6" + value + "§7!");
                partyEmanListener.players.get(name).setPrice(value);
            }
            if(sub.equalsIgnoreCase("setfull")) {
                if(!partyEmanListener.players.containsKey(name)) {
                    Chat.send("§cThe player §e" + name + " §chas no counter!");
                    return;
                }
                if(partyEmanListener.players.get(name).getType().equalsIgnoreCase("up")) {
                    Chat.send("&e" + name + "&c dont has a 'up' counter!");
                    return;
                }
                if(value.equalsIgnoreCase("0")) {
                    Chat.send("&7you &cremoved &7the fullcount for &e" + name + "&7!");
                    partyEmanListener.players.get(name).setFullCount(null);
                    return;
                }
                Chat.send("&7fullcount of &e" + name + " &7set to &d" + value + "&7!");
                partyEmanListener.players.get(name).setFullCount(Integer.parseInt(value));
            }
        }

        if (args.length >= 4) {
            if(sub.equalsIgnoreCase("set")) {
                String name = getWord(args, 2);
                String type = getWord(args, 3);
                String setCount = getWord(args, 4);
                if(!setCount.contains("/")) {
                    if(type.contains("up")) {
                        partyEmanListener.players.put(name, new PartyEmanListener.CarryPlayer(Integer.parseInt(setCount), null, "up", null, null));
                        Chat.send("&7You set &e" + name + " &7to (&a" + setCount + "&7)");
                        return;
                    }
                    Chat.send("&cYou must set a limit for type 'down'!");
                    Chat.send("&7Usage: &e/eman set <name> <up/down> count/limit");
                }
                String[] parts = setCount.split("/");
                int count = Integer.parseInt(parts[0]);
                int limit = Integer.parseInt(parts[1]);
                if (!type.contains("up") && !type.contains("down")) {
                    Chat.send("&cwrong type! &7Usage: &e/eman set <name> <up/down> <count/limit>");
                    return;
                }
                if (type.contains("up")) {
                    if (args.length >= 5) {
                        partyEmanListener.players.put(name, new PartyEmanListener.CarryPlayer(count, Integer.parseInt(args[4]), "up", limit, null));
                        Chat.send("&7You set &e" + name + " &7to (&c" + count + "&7/&a" + limit + "&7 - &dtotal " + args[4] + "&7)");
                    } else {
                        partyEmanListener.players.put(name, new PartyEmanListener.CarryPlayer(count, null, "up", limit, null));
                        Chat.send("&7You set &e" + name + " &7to (&c" + count + "&7/&a" + limit + "&7)");
                    }
                } else {
                    int left = limit-count;
                    partyEmanListener.players.put(name, new PartyEmanListener.CarryPlayer(count, null, "down", limit, null));
                    Chat.send("&7You set &e" + name + " &7to (&c" + count + "&7/&a" + limit + " &7-&e " + left + " left&7)");
                }
            }
        }
    }

    public void getInfo(String name, PartyEmanListener.CarryPlayer carryPlayer) {
        if(carryPlayer.getLimit() == null) {
            sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + ")");
            return;
        }
        if(carryPlayer.getType().equals("down")) {
            int left = carryPlayer.getLimit() - carryPlayer.getCount();
            sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + "/" + carryPlayer.getLimit() + " - " + left + " left)");
            return;
        }
        if(carryPlayer.getType().equals("up")) {
            if(carryPlayer.getFullCount() != null) {
                sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + "/" + carryPlayer.getLimit() + " - total left: " + carryPlayer.getFullCount() + ")");
            } else {
                sendInfo(carryPlayer, name, "%name% has (" + carryPlayer.getCount() + "/" + carryPlayer.getLimit() + ")");
            }
        }
    }

    public void sendInfo(PartyEmanListener.CarryPlayer carryPlayer, String name, String message) {
        if(carryPlayer.getPrice() != null) {
            Chat.send("§e" + message.replaceAll("%name%", name) + " " + carryPlayer.getPrice());
            return;
        }
        Chat.send("§e" + message.replaceAll("%name%", name));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> nameList = new ArrayList<>();
        partyEmanListener.players.forEach((key, value) -> nameList.add(key));
        if(args.length == 2) {
            return getListOfStringsMatchingLastWord(args, nameList.toArray(new String[0]));
        }
        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
