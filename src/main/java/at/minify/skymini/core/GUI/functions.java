package at.minify.skymini.core.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class functions extends GuiScreen {

    public static int getX(float percent) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedX = scaledRes.getScaledWidth();
        return Math.round(percent/100*sclaedX);
    }
    public static int getY(float percent) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedY = scaledRes.getScaledHeight();
        return Math.round(percent/100*sclaedY);
    }

}
