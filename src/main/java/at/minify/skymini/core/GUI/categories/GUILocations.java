package at.minify.skymini.core.GUI.categories;

import at.minify.skymini.Main;
import at.minify.skymini.api.widgets.manager.LocationManager;
import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigEditorInfoText;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import net.minecraft.client.Minecraft;

public class GUILocations {

    @ConfigEditorButton(buttonText = "Edit")
    @ConfigOption(name = "Edit Locations", desc = "Edit the positions of gui elements")
    public Runnable doRun = () -> Minecraft.getMinecraft().displayGuiScreen(new LocationManager());
    //public Runnable doRun = () -> Minecraft.getMinecraft().displayGuiScreen(new Locations());

    @ConfigEditorInfoText(infoTitle = "§a§l" + Main.VERSION)
    @ConfigOption(name = "Version", desc = "Version of SkyMini")
    public boolean infoText;

}
