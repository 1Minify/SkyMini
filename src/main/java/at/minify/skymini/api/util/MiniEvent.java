package at.minify.skymini.api.util;

import at.minify.skymini.Main;
import at.minify.skymini.api.api.ModAPI;
import net.minecraft.client.Minecraft;

public abstract class MiniEvent {

    public boolean isInGame() {
        return findPlayer() && findWorld();
    }

    public boolean findPlayer() {
        return Minecraft.getMinecraft().thePlayer != null;
    }

    public boolean findWorld() {
        return Minecraft.getMinecraft().theWorld != null;
    }

    public ModAPI api() {
        return Main.getAPI();
    }

}
