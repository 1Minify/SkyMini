package at.minify.skymini.core.listener.partyfinder;


import at.minify.skymini.core.widgets.util.WidgetExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import static at.minify.skymini.core.GUI.functions.setx;
import static at.minify.skymini.core.GUI.functions.sety;
import static at.minify.skymini.core.listener.partyfinder.PartyFinderListener.*;
import static at.minify.skymini.core.manager.Chat.uncolored;

public class PartyFinderGUI extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int fontheight = fontRendererObj.FONT_HEIGHT+1;
        drawbackground(setx(10),setx(90),sety(10),sety(90),true,false);
        drawbackground(setx(10)+4,setx(90)-4,sety(10)+4,sety(90)-27,false,false);
        int x = setx(10)+8;
        pos = (int) (float) ((setx(90) - 4) - (setx(10) + 4) - 20) /5;
        for (int i = 0; i < partys.size(); i++) {
            ItemStack item = partys.get(i);
            int ypos = sety(10) + 15;
            int x1 = (int) (float) i * (pos + 4);
            if(i < 5) { drawbackground(x + x1, x + x1 + pos, sety(10) + 8, sety(45) - 2, false, true); }
            else if(i < 9) {
                ypos = sety(45) + 9;
                x1 = (int) (float) (i-5)*(pos+4);
                drawbackground(x + x1, x + x1+pos, sety(45) + 2, sety(90) - 31, false, true);
            }
            drawStringCenteredScaledMaxWidth(item.getDisplayName(),fontRendererObj,x + x1 + (float) pos /2,ypos,true,pos-4,0xFFFFFF);
            int k = 1;
            String text = "";
            for(String line : PartyFinderListener.getLore(item)) {
                if(line.contains("Note:") || line.contains("Tank") || line.contains("Mage") || line.contains("Berserk") || line.contains("Archer") || line.contains("Healer") || line.contains("Empty")) {
                    String[] split = line.split(" ");
                    if(split.length == 3 && !line.contains("Note:")) {
                        drawStringCenteredScaledMaxWidth("§b" + uncolored(split[0]) + "§e" + uncolored(split[1]) + " " + setcolor(split[2]),fontRendererObj,x + x1 + (float) pos /2,ypos + (k*fontheight),true,pos-4,0xFFFFFF);
                    }
                    drawStringCenteredScaledMaxWidth(setcolor(line),fontRendererObj,x + x1 + (float) pos /2,ypos + (k*fontheight),true,pos-4,0xFFFFFF);
                    k++;
                }
                if(line.contains("Note:")) {
                    if(line.contains(" S ") || line.contains(" S/")|| line.contains(" s ")) { text = "§8(§eS§8)"; }
                    if(line.contains("S+") || line.contains("s+")) { text = "§8(§6S+§8)"; }
                    if(line.contains("s/s+") || line.contains("S/S+")) { text = "§8(§eS§7/§6S+§8)"; }
                    if(line.contains("Carry") || line.contains("carry")) {
                        text = "§8(§cCarry§8)";
                        if(line.contains("need") || line.contains("Need") || line.contains("please") || line.contains("pls") || line.contains("Please")) { text = "§8(§cNeed Carry§8)"; }
                        if(line.contains("free") || line.contains("Free")) { text = "§8(§cFree Carry§8)"; }
                    }
                }
            }
            drawStringCenteredScaledMaxWidth(text,fontRendererObj,x + x1 + (float) pos /2,ypos + (k*fontheight)+fontheight,true,pos-4,0xFFFFFF);
        }
        drawbackground(setx(10)+4,setx(24)+4,sety(90)-24,sety(90)-4,false,false); //Start Queue
        drawbackground(setx(24)+8,setx(38)+8,sety(90)-24,sety(90)-4,false,false); //Refresh
        drawbackground(setx(90)-24,setx(90)-4,sety(90)-24,sety(90)-4,false,false); //Search Settings
        drawbackground(setx(62)-8,setx(90)-28,sety(90)-24,sety(90)-4,false,false); //X
        drawStringCenteredScaledMaxWidth("Start Queue",fontRendererObj,setx(17)+4,sety(90)-14,true,100,0x298dd6);
        String refreshtext = "Refresh";
        if(refresh != 0) { refreshtext = "Refresh (" + refresh + "s)"; }
        drawStringCenteredScaledMaxWidth(refreshtext,fontRendererObj,setx(31)+8,sety(90)-14,true,100,0x77e96d);
        drawStringCenteredScaledMaxWidth("Search Settings",fontRendererObj,setx(76)-18,sety(90)-14,true,100,0x912969);
        drawStringCenteredScaledMaxWidth("X",fontRendererObj,setx(90)-14,sety(90)-14,true,100,0xb11d1d);
        drawStringCenteredScaledMaxWidth("§7Searching:",fontRendererObj,setx(50),sety(90)-19,true,100,0xb11d1d);
        drawStringCenteredScaledMaxWidth(dungeontype + " - " + dungeonfloor,fontRendererObj,setx(50),sety(90)-7,true,100,0x2e9a6c);
    }
    public int pos;

    public String setcolor(String line) {
        if(!line.contains("Note:") && !line.contains(" Party")) {
            line = line.replaceAll("(§e0|§e1|§e2|§e3|§e4|§e5|§e6|§e7|§e8|§e9|§e10|§e11|§e12|§e13|§e14)","§7$1");
            line = line.replaceAll("(§e15|§e16|§e17|§e18|§e19)","§f$1");
            line = line.replaceAll("(§e20|§e21|§e22|§e23|§e24)","§a$1");
            line = line.replaceAll("(§e25|§e26|§e27|§e28|§e29)","§9$1");
            line = line.replaceAll("(§e30|§e31|§e32|§e33|§e34)","§5$1");
            line = line.replaceAll("(§e35|§e36|§e37|§e38|§e39)","§6$1");
            line = line.replaceAll("(§e40|§e41|§e42|§e43|§e44|§e45|§e46|§e47|§e48|§e49|§e50)","§6$1");
            line = line.replaceAll("§e","").replace("(","").replace(")","");
        }
        return line;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX > setx(10)+4 && mouseX < setx(24)+4 && mouseY > sety(90)-24 && mouseY < sety(90)-4) { guiclick(45); } //Start Queue
        if(mouseX > setx(24)+8 && mouseX < setx(38)+8 && mouseY > sety(90)-24 && mouseY < sety(90)-4 && refresh == 0) { guiclick(46); refresh = 4; } //Refresh
        if(mouseX > setx(62)-8 && mouseX < setx(90)-28 && mouseY > sety(90)-24 && mouseY < sety(90)-4) { guiclick(50); } //Search Settings
        if(mouseX > setx(90)-24 && mouseX < setx(90)-4 && mouseY > sety(90)-24 && mouseY < sety(90)-4) { guiclick(48); } //X
        int x = setx(10)+8;
        pos = (int) (float) ((setx(90) - 4) - (setx(10) + 4) - 20) /5;
        int click = -1;
        for (int j = 0; j < partys.size(); j++) {
            if(j < 5) {
                int x1 = (int) (float) j * (pos + 4);
                if(mouseX > x + x1 && mouseX < x + x1 + pos && mouseY > sety(10) + 8 && mouseY < sety(45) - 2) { click = j; }
            } else if (j < 9) {
                int x1 = (int) (float) (j-5)*(pos+4);
                if(mouseX > x+x1 && mouseX < x+x1+pos && mouseY > sety(45) + 2 && mouseY < sety(90) - 31) { click = j; }
            }//10-16
        }//19-25
        if(click != -1) {
            if(click < 7) { guiclick(click+10); } else { guiclick(click+12); }
        }
    }

    public static void drawStringCenteredScaledMaxWidth(String str, FontRenderer fr, float x, float y, boolean shadow, int len, int color) {
        int strLen = fr.getStringWidth(str);
        float factor = len / (float) strLen;
        factor = Math.min(1, factor);
        int newLen = Math.min(strLen, len);

        float fontHeight = 8 * factor;

        drawscaledstring(str, fr, x - (float) newLen / 2, y - fontHeight / 2, shadow, color, factor);
    }

    public static void drawscaledstring(String str, FontRenderer fr, float x, float y, boolean shadow, int color, float factor) {
        GlStateManager.scale(factor, factor, 1);
        fr.drawString(str, x / factor, y / factor, color, shadow);
        GlStateManager.scale(1 / factor, 1 / factor, 1);
    }

    public static void drawbackground(int x, int width, int y, int height, boolean back, boolean in) {
        if(back) {
            int bgAlpha = WidgetExecutor.getalpha(50);
            int bgshader = (0) | (bgAlpha << 24);
            Gui.drawRect(x + 2, y + 2, width + 2, height + 2, bgshader);
        }
        int bgColor = -14606298;
        int leftTopColor = -13685196;
        int rightBottomColor = -15658986;
        if(in) {
            bgColor = -15263971;
            leftTopColor = -16250866;
            rightBottomColor = -14145490;
        }
        Gui.drawRect(x, y, (width-1), y+1, leftTopColor); // Top border
        Gui.drawRect(x, height, (width-1), height-1, rightBottomColor); // Bottom border
        Gui.drawRect(x, (y + 1), x+1, height, leftTopColor); // Left border

        Gui.drawRect(width-1, y, width, height, rightBottomColor); // Right border
        Gui.drawRect(x + 1, y + 1, width - 1, height - 1, bgColor); // Middle border
    }

    public static void guiclick(int slot) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.playerController.windowClick(partyinv.inventorySlots.windowId, slot, 0, 0, mc.thePlayer);
    }

}
