package at.minify.skymini.api.util;

import at.minify.skymini.Main;
import at.minify.skymini.api.api.ModAPI;
import net.minecraft.command.CommandBase;

public abstract class MiniCommand extends CommandBase {

    public String getWord(String[] words, int pos) {
        if (pos > 0 && pos <= words.length) {
            return words[pos - 1];
        } else {
            return "";
        }
    }

    public ModAPI api() {
        return Main.getAPI();
    }
}
