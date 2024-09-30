package at.minify.skymini.mixins;

import at.minify.skymini.api.events.MiniClickGUIEvent;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.inventory.GuiContainer.class)

public class GuiContainer {
    @Inject(at = @At("HEAD"), method = "handleMouseClick")
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (slotIn != null && slotIn.getStack() != null) {
            ItemStack clickedItem = slotIn.getStack();
            MiniClickGUIEvent event = new MiniClickGUIEvent(clickedItem, slotId);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }
}
