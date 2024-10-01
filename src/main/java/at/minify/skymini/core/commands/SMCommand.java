package at.minify.skymini.core.commands;

import at.minify.skymini.Main;
import at.minify.skymini.api.util.MiniCommand;
import at.minify.skymini.core.listener.LowBalling;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.updater.UpdateManager;
import at.minify.skymini.util.stats;
import io.github.moulberry.moulconfig.gui.GuiScreenElementWrapper;
import io.github.moulberry.moulconfig.gui.MoulConfigEditor;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;

import static at.minify.skymini.Main.GUIConfigMoulConfigProcessor;

public class SMCommand extends MiniCommand {

    boolean trade = false;

    @Override
    public String getCommandName() {
        return "sm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/sm";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        String sub = getWord(args, 1);
        String sub2 = getWord(args, 2);
        String name = getWord(args, 3);
        if(args.length == 0) {
            Main.screenToOpen = new GuiScreenElementWrapper(new MoulConfigEditor<>(GUIConfigMoulConfigProcessor));
        }
        if(sub.equalsIgnoreCase("updatee")) {
            UpdateManager.checkUpdate();
        }
        if(sub.equalsIgnoreCase("server")) {
            Chat.send("Aktueller Server: §e" + Main.getAPI().server.name());
        }
        if(sub.equalsIgnoreCase("help")) {
            Chat.send("&6SkyMini Commands:");
            Chat.send("&e/sm price");
        }
        if(sub.equalsIgnoreCase("price")) {
            if(sub2.isEmpty()) {
                Chat.send("&6Price Commands:");
                Chat.send("&e/sm price <value>");
                Chat.send("&e/sm price percent 0/100");
                return;
            }
            if(sub2.equalsIgnoreCase("percent")) {
                if(name.isEmpty()) return;
                int value = Integer.parseInt(name);
                stats.put("lowball.percent", value);
                stats.savepaths("lowball.percent");
                Chat.send("You set the item value percent to &a" + value + "%");
                return;
            }
            int percent = (int) stats.get("lowball.percent");
            int price = Chat.getInt(sub2);
            float newPrice = (float) percent / 100 * price;
            Chat.sendclear("&8&m----------&6 " + Chat.formatNumber(price) + " &8&m----------");
            Chat.sendclear("&8» &7Lowball Price: &6" + Chat.formatNumber((int) newPrice) + " &8(&c-" + Chat.formatNumber((int) (price-newPrice)) + " &8- &b" + percent + "%&8)");
            float AuctionHousePrice;
            if(price < 1000000) { AuctionHousePrice = (float) 99 /100*price; } else { AuctionHousePrice = (float) 98 /100*price; }
            Chat.sendclear("&8» &7AH Profit: &6" + Chat.formatNumber((int) AuctionHousePrice) + " &8(&7lost &c-" + Chat.formatNumber((int) (price-AuctionHousePrice)) + "&8)");
            Chat.sendclear("&8» &7Lowball Profit: &a+" + Chat.formatNumber((int) (AuctionHousePrice-newPrice)));
            Chat.sendclear("&8&m----------&6 " + Chat.formatNumber(price) + " &8&m----------");
        }
        if(sub.equalsIgnoreCase("lang")) {
            if(sub2.isEmpty()) return;
            Main.getAPI().language = sub2;
            Chat.send("You set the language to §e" + sub2);
        }
        if(sub.equalsIgnoreCase("trade")) {
            trade = !trade;
            if(trade) {
                MinecraftForge.EVENT_BUS.register(LowBalling.class);
                Chat.send("&aActivated saving Trade-Data");
            } else {
                MinecraftForge.EVENT_BUS.unregister(LowBalling.class);
                Chat.send("&cDeactivated saving Trade-Data");
            }
        }
        /*if(sub.equalsIgnoreCase("setx")) {
            if(args[1] != null) {
                float count1 = Float.parseFloat(args[1]);
                HUDGui.pos1 = count1;
            }
        }*/
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
