package at.minify.skymini.core.GUI.categories;

import io.github.moulberry.moulconfig.annotations.*;

public class Slayer {

    @ConfigOption(name = "Voidgloom", desc = "")
    @ConfigEditorAccordion(id = 1)
    public boolean accordion = false;

    @ConfigOption(name = "Display Radius", desc = "Set how many blocks the voidgloom display should go")
    @ConfigEditorSlider(minValue = 0, maxValue = 15, minStep = 1)
    @ConfigAccordionId(id = 1)
    public static int voidGloomDisplayRadius = 0;

    @ConfigOption(name = "Miniboss tracker", desc = "Show Enderman minibosses on the end island")
    @ConfigEditorBoolean
    @ConfigAccordionId(id = 1)
    public static boolean displayMiniBoss = true;



}
