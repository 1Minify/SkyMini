package at.minify.skymini.api.widgets.manager;

import at.minify.skymini.Main;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.core.widgets.util.WidgetExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Arrays;

import static at.minify.skymini.core.GUI.functions.setx;

public class LocationManager extends GuiScreen {

    private final ServiceContainer serviceContainer;

    public LocationManager() {
        serviceContainer = Main.getServiceContainer();
    }

    private int distanceX, distanceY;
    private Widget dragging = null;
    boolean magnet = false;
    private int magnetx;
    private int magnety;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, 0x80000000);
        renderWidgets();
        renderInfo();
        if(dragging != null) {
            ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
            int sclaedX = scaledRes.getScaledWidth();
            int sclaedY = scaledRes.getScaledHeight();
            float x = (float) (mouseX - distanceX) / sclaedX * 100;
            float y = (float) (mouseY - distanceY) / sclaedY * 100;
            if(magnet) {
                float x1 = getxpercent(mouseX);
                float y1 = getypercent(mouseY);
                if(x1 > 48 && x1 < 52) {x = 50;}
                if(y1 > 48 && y1 < 52) {y = 49;}
            }
            dragging.setX(x);
            dragging.setY(y);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for(Widget widget : WidgetService.widgets) {
            if (widget.getText().isEmpty()) {
                continue;
            }
            String name = widget.getClass().getName();
            String text = widget.getText();
            String[] lines = text.split("\n");
            int length = 0;
            text = text.replaceAll("0x[0-9A-Fa-f]{6}","").replaceAll("&[0-9a-fA-Fklmnor]", "");
            if(text.equalsIgnoreCase("&c")) {
                text = "§7" + name;
            } else {
                text = text.replaceAll("&","§");
            }
            if(widget.isCenter()) {
                length -= (int) ((float) (getlength(text)/2)*-1);
            }
            int x = getlocx(widget);
            int y = getlocy(widget);
            int length1 = fontRendererObj.getStringWidth(text.replaceAll("&[0-9a-fA-Fklmnor]", ""));
            if(text.contains("\n")) { length1 = WidgetManager.longest(Arrays.asList(text.split("\n"))); }
            int height = fontRendererObj.FONT_HEIGHT; //9
            if(lines != null) {
                height = (fontRendererObj.FONT_HEIGHT+1)*lines.length;
            }
            if (mouseX >= x-length && mouseX <= (x-length + length1) && mouseY >= y && mouseY <= (y + height)) {
                if(mouseButton == 0) {
                    distanceX = mouseX - x;
                    distanceY = mouseY - y;
                    dragging = widget;
                    break;
                }
                if(mouseButton == 1) {
                    widget.setCentered(!widget.isCenter());
                }
            }
        }
        int length = WidgetManager.stringlength("Click here");
        if(mouseX >= magnetx && mouseX <= magnetx+length && mouseY >= magnety && mouseY <= magnety+9) {
            magnet = !magnet;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(dragging == null) {
            return;
        }
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedX = scaledRes.getScaledWidth();
        int sclaedY = scaledRes.getScaledHeight();
        float x = (float) (mouseX - distanceX) / sclaedX * 100;
        float y = (float) (mouseY - distanceY) / sclaedY * 100;
        if(magnet) {
            float x1 = getxpercent(mouseX);
            float y1 = getypercent(mouseY);
            if(x1 > 48 && x1 < 52) {x = 50;}
            if(y1 > 48 && y1 < 52) {y = 49;}
        }
        dragging.setX(x);
        dragging.setY(y);
        dragging = null;
    }



    public void renderInfo() {
        int fontheight = fontRendererObj.FONT_HEIGHT+1;
        String text;
        if(magnet) {
            text = "§eClick here §7to §cdeactivate §7the §9Magnet mode";
        } else {
            text = "§eClick here §7to §aactivate §7the §9Magnet mode";
        }
        int length = WidgetManager.stringlength(text);
        magnetx = setx(50)-length/2;
        magnety = 10+fontheight*2;
        WidgetExecutor.drawbackground(magnetx,setx(50)+length/2,10,10+fontheight*3-1);
        drawCenteredString(fontRendererObj,"§dSkyMini Location Editor",setx(50),10,0xFFFFFF);
        drawCenteredString(fontRendererObj,"§7Center a text with §bRight-Click",setx(50),10+fontheight,0xFFFFFF);
        drawCenteredString(fontRendererObj,text,setx(50),10+fontheight*2,0xFFFFFF);
    }

    public void renderWidgets() {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        float x = scaledRes.getScaledWidth();
        float y = scaledRes.getScaledHeight();
        for(Widget widget : WidgetService.widgets) {
            if (widget.getText().isEmpty()) {
                continue;
            }
            WidgetExecutor.renderWidget(widget.getText(), (x / 100) * widget.getX(), (y / 100) * widget.getY(), widget.isCenter(),true);
        }
    }

    public static int getlocx(Widget widget) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedX = scaledRes.getScaledWidth();
        float percent = widget.getX();
        percent = percent/100*sclaedX;
        return Math.round(percent);
    }

    public static int getlocy(Widget widget) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedY = scaledRes.getScaledHeight();
        float percent = widget.getY();
        percent = percent/100*sclaedY;
        return Math.round(percent);
    }

    public static int getlength(String text) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        if (!text.contains("\n")) {
            return fontRenderer.getStringWidth(text.replaceAll("&[0-9a-fA-Fklmnor]", ""));
        } else {
            String[] lines = text.split("\n");
            return fontRenderer.getStringWidth(lines[0].replaceAll("&[0-9a-fA-Fklmnor]", ""));
        }
    }

    public static float getxpercent(int value) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedY = scaledRes.getScaledWidth();
        return (float) value/sclaedY*100;
    }
    public static float getypercent(int value) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedY = scaledRes.getScaledHeight();
        return (float) value/sclaedY*100;
    }

}
