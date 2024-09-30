package at.minify.skymini.api.events;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.ArrayList;
import java.util.List;

public class MiniClickGUIEvent extends Event {

    private final ItemStack itemStack;
    @Getter
    private final int slot;

    public MiniClickGUIEvent(ItemStack itemStack, int slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public String getName() {
        // Die Anzeige des Items (displayName) abrufen
        return itemStack.getDisplayName();
    }

    public String getGUIName() {
        GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        ContainerChest container = (ContainerChest) chest.inventorySlots;
        return container.getLowerChestInventory().getDisplayName().getUnformattedText();
    }

    public String getLore() {
        List<String> lore = new ArrayList<String>();
        if(itemStack == null) return lore.toString();
        if(!itemStack.hasTagCompound()) return lore.toString();
        NBTTagCompound itemNBT = itemStack.getTagCompound();

        if(!itemNBT.hasKey("display", 10)) return lore.toString();
        NBTTagCompound displayTag = itemNBT.getCompoundTag("display");

        if(!displayTag.hasKey("Lore", 9)) return lore.toString();
        NBTTagList loreTagList = displayTag.getTagList("Lore", 8);
        for (int i = 0; i < loreTagList.tagCount(); i++) {
            lore.add(loreTagList.getStringTagAt(i));
        }
        return lore.toString();
    }

}
