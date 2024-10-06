package at.minify.skymini.core.GUI.categories;

import at.minify.skymini.api.GUI.versionOption.ConfigEditorVersion;
import at.minify.skymini.api.widgets.manager.LocationManager;
import io.github.moulberry.moulconfig.annotations.ConfigAccordionId;
import io.github.moulberry.moulconfig.annotations.ConfigEditorAccordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorButton;
import io.github.moulberry.moulconfig.annotations.ConfigOption;
import net.minecraft.client.Minecraft;

public class GUILocations {

    @ConfigEditorVersion
    @ConfigOption(name = "", desc = "")
    public Void test = null;

    @ConfigEditorButton(buttonText = "Edit")
    @ConfigOption(name = "Edit Locations", desc = "Edit the positions of gui elements")
    public Runnable doRun = () -> Minecraft.getMinecraft().displayGuiScreen(new LocationManager());
    //public Runnable doRun = () -> Minecraft.getMinecraft().displayGuiScreen(new Locations());

    @ConfigOption(name = "Updates", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion1 = false;

    @ConfigOption(name = "Auto Update", desc = "check and install updates automatically ")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean autoUpdate = true;



}
