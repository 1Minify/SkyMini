package at.minify.skymini.core.GUI.categories;

import io.github.moulberry.moulconfig.annotations.*;

public class Garden {

    @ConfigOption(name = "1.12.2 Crop Hitboxes", desc = "Set hitboxes of §awheat§7, §epotato§7, §6carrot §7& §cnetherwart §7crops to 1.12.2")
    @ConfigEditorBoolean
    public static boolean hitboxes = false;

    @ConfigOption(name = "Disable Breaking Animation", desc = "Disable the rendering for block breaking particles")
    @ConfigEditorBoolean
    public static boolean breakani = false;

    /*@ConfigOption(name = "Hotkey", desc = "Set a hotkey to activate walking mode for farming")
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_P)
    public static int farmmodekey = Keyboard.KEY_P;*/

}
