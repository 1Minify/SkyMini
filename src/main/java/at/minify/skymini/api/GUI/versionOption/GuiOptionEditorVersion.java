package at.minify.skymini.api.GUI.versionOption;

import at.minify.skymini.Main;
import at.minify.skymini.api.GUI.MiniRenderUtils;
import at.minify.skymini.updater.UpdateManager;
import io.github.moulberry.moulconfig.gui.GuiOptionEditor;
import io.github.moulberry.moulconfig.internal.TextRenderUtils;
import io.github.moulberry.moulconfig.processor.ProcessedOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class GuiOptionEditorVersion extends GuiOptionEditor {

    public static String text;
    public static String buttonText;

    public static void resetButtonText(String text) {
        new Thread(() -> {
            try {
                for(int i = 5; i > 0; i--) {
                    buttonText = text + " (" + i + ")";
                    Thread.sleep(1000);
                }
                buttonText = "Check Update";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public GuiOptionEditorVersion(ProcessedOption option) {
        super(option);
        text = "§a" + Main.VERSION;
        buttonText = "Check Update";
    }

    @Override
    public void renderOverlay(int x, int y, int width) {
        super.renderOverlay(x, y, width);
    }

    @Override
    public void render(int x, int y, int width) {
        super.render(x, y, width);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int height = getHeight();

        int distance = width / 8;
        int rectWidth = (int) (distance * 2.5F);
        int xCoordinate = (int) x + width-distance-rectWidth;
        MiniRenderUtils.drawFloatingRectDark(xCoordinate, y + height/2 - 8, rectWidth, 16, false);
        TextRenderUtils.drawStringCenteredScaledMaxWidth(buttonText, fontRenderer,
                xCoordinate + (float) rectWidth / 2, y + (float) height/2,
                false, rectWidth-10, 0xc0c0c0
        );
        int availableWidth = width-distance-rectWidth;

        GlStateManager.pushMatrix(); // savind old data
        GlStateManager.scale(2F, 2F, 1F);
        TextRenderUtils.drawStringCenteredScaledMaxWidth(text, fontRenderer,
                /*x + (float) width / 6*/ (x + (float) availableWidth / 2) / 2, (y + (float) height / 2) / 2,
                true, availableWidth - 40, -1);
        GlStateManager.popMatrix(); // loading old data
    }

    @Override
    public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY) {
        if(!Mouse.getEventButtonState()) return false;
        int height = getHeight();
        int distance = width / 8;
        int rectWidth = (int) (distance * 2.5F);
        int xCoordinate = (int) x + width-distance-rectWidth;
        int yCoordinate = y + height/2 - 8;
        if(mouseX <= xCoordinate) return false;
        if(mouseX >= xCoordinate+rectWidth) return false;
        if(mouseY <= yCoordinate) return false;
        if(mouseY >= yCoordinate+16) return false;
        if(!buttonText.equals("Check Update")) return false;
        UpdateManager.checkUpdate();
        //Chat.send("§7Checking for updates...");
        return true;
    }

    @Override
    public boolean keyboardInput() {
        return false;
    }

}
