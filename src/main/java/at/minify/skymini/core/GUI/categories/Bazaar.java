package at.minify.skymini.core.GUI.categories;

import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.core.widgets.manager.BazaarWidgetManager;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import net.minecraft.client.Minecraft;

public class Bazaar {

    @ConfigOption(name = "Show Bazaar always", desc = "Show the gui with the bazaar flipping statistics always")
    @ConfigEditorBoolean
    public static boolean bzshow = false;

    @ConfigOption(name = "Show unit price", desc = "Show unit prices at Bazaar Order/Offer value")
    @ConfigEditorBoolean
    public static boolean unitprice = false;

    @ConfigOption(name = "Show item coins", desc = "Show coins at every flipped items")
    @ConfigEditorBoolean
    public static boolean bzitemcoins = true;

    @ConfigEditorButton(buttonText = "Reset")
    @ConfigOption(name = "Clear Flipping", desc = "Delete all data from bazaar flipping")
    public Runnable doRun = this::resetFlip;

    public void resetFlip() {
        ServiceContainer.getService(BazaarWidgetManager.class).resetBazaarData();
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
