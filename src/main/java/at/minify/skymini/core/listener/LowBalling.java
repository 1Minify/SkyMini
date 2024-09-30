package at.minify.skymini.core.listener;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.event.HoverEvent;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@MiniRegistry
public class LowBalling {

    public String item = "";
    Map<String, String> trades = new HashMap<>();

    @SubscribeEvent
    public void MiniTick(MiniTickEvent event) {
        if(!event.second(1)) {
            return;
        }
        ItemStack item = getslot(5);
        if(item == null) {
            return;
        }
        String name = getname().replace("You","").replaceAll(" ","");
        String item1 = trades.get(name);
        String itemdata = item.serializeNBT().toString();
        if(item1 == null) {
            send("&7" + name + ": &aSaved Data &8[",item,"&8]");
            trades.put(name,itemdata);
        }
        else if(!item1.equals(itemdata)) {
            send("&7" + name + ": &eUpdated Data to &8[",item,"&8]");
            trades.put(name,itemdata);
        }
    }

    public static void send(String prefix,ItemStack item, String suffix) {
        prefix = "&8» &dSkyMini &8❘ &7" + prefix;
        IChatComponent hoverComponent = new ChatComponentText(item.getDisplayName()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(item.serializeNBT().toString()))));
        IChatComponent message = new ChatComponentText(prefix.replace("&", "§")).appendSibling(hoverComponent).appendSibling(new ChatComponentText(suffix.replace("&", "§")));
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
    }

    public ItemStack getslot(int slot) {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            return null;
        }
        ContainerChest chest = (ContainerChest) ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots;
        if(chest.getLowerChestInventory().getName().contains("You  ")) {
            return chest.getSlot(slot).getStack();
        }
        return null;
    }
    public String getname() {
        if(!(Minecraft.getMinecraft().currentScreen instanceof GuiChest)) {
            return "";
        }
        return ((ContainerChest) ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots).getLowerChestInventory().getName();
    }

}
