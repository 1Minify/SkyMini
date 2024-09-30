package at.minify.skymini.core.listener.partyfinder;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.core.GUI.categories.Dungeon;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@MiniRegistry(server = Server.DUNGEON_HUB)
public class PartyFinderListener {

    public static List<ItemStack> partys = new ArrayList<>();
    public static String dungeontype = "";
    public static String dungeonfloor = "";
    public static int refresh = 0;

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if(!event.second(1)) {
            return;
        }
        if(refresh != 0) {
            refresh -= 1;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void chat(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if (!(Minecraft.getMinecraft().currentScreen instanceof PartyFinderGUI)) {
            return;
        }
        if(message.contains("Refreshing...")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void render(RenderGameOverlayEvent.Text event) {
        if(!Dungeon.partyfinder) {
            return;
        }
        if(!getname().contains("Party Finder")) {
            return;
        }
        partyinv = (GuiChest) Minecraft.getMinecraft().currentScreen;
        if (Minecraft.getMinecraft().currentScreen instanceof PartyFinderGUI) { return; }
        ItemStack item = partyinv.inventorySlots.getSlot(22).getStack();
        if(item != null && item.getDisplayName() != null && item.getDisplayName().contains("§cNone")) { return; }
        item = partyinv.inventorySlots.getSlot(50).getStack();
        if(item != null && item.getDisplayName() != null && item.getDisplayName().contains("Search Settings")) {
            partys = getpartys();
            Minecraft.getMinecraft().displayGuiScreen(new PartyFinderGUI());
        }
    }

    public static GuiChest partyinv;
    public List<ItemStack> getpartys() {
        List<ItemStack> getpartys = new ArrayList<>();
        for (int i = 0; i < 54; i++) {
            ItemStack item = partyinv.inventorySlots.getSlot(i).getStack();
            if(item == null) {
                return getpartys;
            }
            if(item.getDisplayName() == null) {
                return getpartys;
            }
            if(item.getDisplayName().contains("Party") && !item.getDisplayName().contains("Your Party")) {
                getpartys.add(item);
            }
            else if(item.getDisplayName().contains("Search Settings")) {
                for(String line : getLore(item)) {
                    if(line.contains("Dungeon:") && line.contains("Master")) { dungeontype = "Master Mode"; }
                    else if(line.contains("Dungeon:") && !line.contains("Master")) { dungeontype = "Catacombs"; }
                    else if(line.contains("Floor:")) { String[] split = line.split(" "); if(split.length == 3) { dungeonfloor = split[1] + " " + split[2]; } }
                    dungeontype = Chat.uncolored(dungeontype);
                    dungeonfloor = Chat.uncolored(dungeonfloor);
                }
            }
        }
        return getpartys;
    }

    public static String getname() {
        if(Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) Minecraft.getMinecraft().currentScreen).inventorySlots).getLowerChestInventory().getName();
        } else {
            return "";
        }
    }

    public static List<String> getLore(ItemStack itemStack) {
        List<String> lore = new ArrayList<String>();
        if(itemStack == null) {
            return lore;
        }
        if(!itemStack.hasTagCompound()) {
            return lore;
        }
        NBTTagCompound itemNBT = itemStack.getTagCompound();
        if(!itemNBT.hasKey("display",10)) {
            return lore;
        }
        NBTTagCompound displayTag = itemNBT.getCompoundTag("display");
        if (!displayTag.hasKey("Lore", 9)) {
            return lore;
        }
        NBTTagList loreTagList = displayTag.getTagList("Lore", 8);
        for (int i = 0; i < loreTagList.tagCount(); i++) {
            lore.add(loreTagList.getStringTagAt(i));
        }
        return lore;
    }

}
