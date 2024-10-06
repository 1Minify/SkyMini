package at.minify.skymini.api.GUI.chatFilter;

import at.minify.skymini.api.GUI.MiniRenderUtils;
import io.github.moulberry.moulconfig.GuiTextures;
import io.github.moulberry.moulconfig.gui.GuiOptionEditor;
import io.github.moulberry.moulconfig.gui.elements.GuiElementTextField;
import io.github.moulberry.moulconfig.internal.LerpUtils;
import io.github.moulberry.moulconfig.internal.RenderUtils;
import io.github.moulberry.moulconfig.internal.TextRenderUtils;
import io.github.moulberry.moulconfig.processor.ProcessedOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiOptionEditorChatFiler extends GuiOptionEditor {

    private GuiElementTextField textField;
    public static List<String> inputs;
    private final Map<String, Long> hoverMillis = new HashMap<>();
    private String selectedId = "";
    private final String oldSelectedId = "";

    public GuiOptionEditorChatFiler(ProcessedOption option) {
        super(option);
        inputs = (List<String>) option.get();
        textField = new GuiElementTextField("", 0);
    }

    @Override
    public int getHeight() {
        int height = super.getHeight();
        if(!inputs.isEmpty()) {
            height += 3;
        }
        for(String input : inputs) {
            height += 16 + 3;
        }

        return height;
    }

    @Override
    public void render(int x, int y, int width) {
        super.render(x, y, width);

        int fullWidth = Math.min(width / 3 - 10, 80);

        int textFieldX = x + width / 6 - fullWidth / 2;
        if (textField.getFocus()) {
            fullWidth = Math.max(
                    fullWidth,
                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(textField.getText()) + 10
            );
        }

        textField.setSize(fullWidth, 16);
        //textField.setText((String) option.get());

        textField.render(textFieldX, y + super.getHeight() - 7 - 14);

        FontRenderer fontRenderer =  Minecraft.getMinecraft().fontRendererObj;

        int height = y + super.getHeight();
        int id = 0;
        for(String input : inputs) {
            int width1 = x + 10 + ((width - 40) / 2);
            MiniRenderUtils.drawFloatingRectDark(x + 10, height, width - 39, 16, false);
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTextures.DELETE);

            if(hoverMillis.containsKey(input)) {
                Long millis = hoverMillis.get(input);
                if (millis < 0) {
                    float greenBlue = LerpUtils.clampZeroOne((System.currentTimeMillis() + millis) / 250f);
                    GlStateManager.color(1, greenBlue, greenBlue, 1);
                } else {
                    float greenBlue = LerpUtils.clampZeroOne((250 + millis - System.currentTimeMillis()) / 250f);
                    GlStateManager.color(1, greenBlue, greenBlue, 1);
                }
            } else {
                hoverMillis.put(input, -1L);
            }


            /*if(selectedId == id) {
                float greenBlue = LerpUtils.clampZeroOne((250 + selectedMillis - System.currentTimeMillis()) / 250f);
                GlStateManager.color(1, greenBlue, greenBlue, 1);
            } else {
                GlStateManager.color(1, 1, 1, 1);
            }*/


            RenderUtils.drawTexturedRect(x + width - 20, height, 11, 14, GL11.GL_NEAREST);
            TextRenderUtils.drawStringScaledMaxWidth(input, fontRenderer, x + 15, height + 4, false, width - 44, 0xc0c0c0);
            //TextRenderUtils.drawStringCenteredScaledMaxWidth(input, Minecraft.getMinecraft().fontRendererObj, width1, height+8, false, width - 40, 0xc0c0c0);

            height += 16+3;
            id++;
        }
    }

    @Override
    public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY) {
        int height1 = y + super.getHeight();
        int posX = x + width - 20;
        int id = 0;
        boolean found = false;
        for(String input : inputs) {
            int posY = height1;
            if(mouseX > posX && mouseX < posX + 11 && mouseY > posY && mouseY < posY + 14) {
                found = true;
                if(!selectedId.equals(input)) {
                    hoverMillis.put(input, System.currentTimeMillis());
                }
                selectedId = input;
                break;
            } else {
                if(selectedId.equals(input)) {
                    hoverMillis.put(input, -System.currentTimeMillis());
                }
            }
            height1 += 16+3;
            id++;
        }
        if(!found) {
            selectedId = "";
        }
        if(Mouse.getEventButtonState() && !selectedId.isEmpty()) {
            hoverMillis.remove(selectedId);
            inputs.remove(id);
            option.set(inputs);
        }

        int height = super.getHeight();
        int fullWidth = Math.min(width / 3 - 10, 80);
        int textFieldX = x + width / 6 - fullWidth / 2;
        if (textField.getFocus()) {
            fullWidth = Math.max(
                    fullWidth,
                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(textField.getText()) + 10
            );
        }

        int textFieldY = y + height - 7 - 14;
        textField.setSize(fullWidth, 16);

        if (Mouse.getEventButtonState() && (Mouse.getEventButton() == 0 || Mouse.getEventButton() == 1)) {
            if (mouseX > textFieldX && mouseX < textFieldX + fullWidth &&
                    mouseY > textFieldY && mouseY < textFieldY + 16) {
                textField.mouseClicked(mouseX, mouseY, Mouse.getEventButton());
                return true;
            }
            textField.unfocus();
        }

        return false;
    }

    @Override
    public boolean keyboardInput() {
        if (!Keyboard.getEventKeyState()) return false;
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && textField.getFocus()) {
            textField.unfocus();
            return true;
        }
        if (textField.getFocus()) {
            if(Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
                String text = textField.getText();
                if(text.isEmpty()) return true;
                for(String input : inputs) {
                    if(input.equals(text)) {
                        return true;
                    }
                }
                inputs.add(text);
                option.set(inputs);
                textField.setText("");
                textField.setFocus(false);
                return true;
            }

            textField.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());

            try {
                textField.setCustomBorderColour(0xffffffff);
            } catch (Exception e) {
                textField.setCustomBorderColour(0xffff0000);
            }

            return true;
        }
        return false;
    }

}
