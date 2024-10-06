package at.minify.skymini.api.GUI;

import net.minecraft.client.gui.Gui;

public class MiniRenderUtils {

    public static void drawFloatingRectDark(int x, int y, int width, int height, boolean shadow) {
        int bgColor = -15263971;
        int leftTopColor = -16250866;
        int rightBottomColor = -14145490;
        Gui.drawRect(x, y, x + 1, y + height,leftTopColor); //Left
        Gui.drawRect(x + 1, y, x + width, y + 1, leftTopColor); //Top
        Gui.drawRect(x + width - 1, y + 1, x + width, y + height, rightBottomColor); //Right
        Gui.drawRect(x + 1, y + height - 1, x + width - 1, y + height, rightBottomColor); //Bottom
        Gui.drawRect(x + 1, y + 1, x + width - 1, y + height - 1, bgColor); //Middle
        if (shadow) {
            Gui.drawRect(x + width, y + 2, x + width + 2, y + height + 2, 0x70000000); //Right shadow
            Gui.drawRect(x + 2, y + height, x + width, y + height + 2, 0x70000000); //Bottom shadow
        }
    }

}
