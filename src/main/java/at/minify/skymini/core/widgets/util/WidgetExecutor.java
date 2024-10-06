package at.minify.skymini.core.widgets.util;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.api.annotations.ServerWidget;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.api.widgets.Widget;
import at.minify.skymini.api.widgets.manager.Gradient;
import at.minify.skymini.api.widgets.manager.Images;
import at.minify.skymini.api.widgets.manager.LocationManager;
import at.minify.skymini.api.widgets.manager.WidgetManager;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.widgets.scoreboard.ScoreboardManager;
import at.minify.skymini.core.widgets.scoreboard.ScoreboardWidget;
import at.minify.skymini.core.widgets.scoreboard.data.DungeonScoreboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.client.gui.Gui.drawRect;

@Service(priority = 20)
@MiniRegistry(server = Server.NONE)
public class WidgetExecutor {

    ScoreboardManager scoreboardManager = ServiceContainer.getService(ScoreboardManager.class);
    DungeonScoreboard dungeonScoreboard = ServiceContainer.getService(DungeonScoreboard.class);
    ScoreboardWidget scoreboardWidget = ServiceContainer.getService(ScoreboardWidget.class);
    ServiceContainer serviceContainer = ServiceContainer.getService(ServiceContainer.class);

    public static int ani = 0;

    @SubscribeEvent
    public void tick(MiniTickEvent event) {
        if(Minecraft.getMinecraft().theWorld == null) {
            return;
        }
        ani += Display.guiSpeed;
        if (ani >= 400) {
            ani = 0;
        }

        Gradient.tick(event);
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        scoreboardWidget.render();
        if ((Minecraft.getMinecraft().currentScreen instanceof LocationManager)) {
            return;
        }
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        float x = scaledRes.getScaledWidth();
        float y = scaledRes.getScaledHeight();
        if(!Main.getAPI().inSkyBlock) {
            return;
        }
        for(Widget widget : WidgetService.widgets) {
            if(!widget.isEnabled()) continue;
            if(widget.getText().equals("&c")) continue;
            if(widget.getText().isEmpty()) continue;
            if(widget.getClass().isAnnotationPresent(ServerWidget.class)) {
                ServerWidget serverWidget = widget.getClass().getAnnotation(ServerWidget.class);
                Server server = serverWidget.server();
                if(!server.equals(Server.SKYBLOCK)) {
                    if(!server.equals(Main.getAPI().server)) continue;
                }
            }
            renderWidget(widget.getText(), (x / 100) * widget.getX(), (y / 100) * widget.getY(), widget.isCenter(),true);
        }
    }

    public static void renderWidget(String text, float x2, float y2, boolean centered, boolean background, float... scale) {
        if(scale != null && scale.length == 1) {
            GlStateManager.scale(scale[0],scale[0],1);
        }
        text = text.replace("#","0x").replace("&","§");
        int x = Math.round(x2);
        int y = Math.round(y2);
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        if(!text.contains("\n")) {
            int textlength = WidgetManager.stringlength(text);
            if(centered) { x = x-textlength/2; }
            if(background) {
                drawbackground(x,x+textlength,y,y+fontRenderer.FONT_HEIGHT);
            }
            if(drawimageline(text, fontRenderer, x, y)) {
                if(drawgradientline(text, fontRenderer, x, y, ani / 2)) {
                    if(drawchromaline(colored(text), x, y)) {
                        fontRenderer.drawStringWithShadow(colored(text), x, y, 0xFFFFFF);
                    }
                }
            }
            /*if(drawgradientline(text, fontRenderer, x, y, ani / 2)) {
                if(drawchromaline(colored(text), x, y)) {
                    fontRenderer.drawStringWithShadow(colored(text), x, y, 0xFFFFFF);
                }
            }*/
        } else {
            String[] lines = text.split("\n");
            int longest = WidgetManager.longest(Arrays.asList(lines));
            if(centered) { x = x-WidgetManager.stringlength(lines[0])/2; }
            int x1 = x;
            if(background) {
                drawbackground(x,x+longest,y,y+Arrays.asList(lines).size()*(fontRenderer.FONT_HEIGHT+1)-1);
            }
            for (String line : lines) {
                line = line + "§f";
                if(line.contains("center:true")) { line = line.replaceAll("center:true",""); x1 = x+(longest/2)-(WidgetManager.stringlength(line)/2); }
                if(drawimageline(line, fontRenderer, x1, y)) {
                    if (drawgradientline(line, fontRenderer, x1, y, ani / 2)) {
                        if (drawchromaline(line, x1, y)) {
                            fontRenderer.drawStringWithShadow(colored(line), x1, y, 0xFFFFFF);
                        }
                    }
                }
                y += fontRenderer.FONT_HEIGHT + 1;
                x1 = x;
            }
        }
        if(scale != null && scale.length == 1) {
            GlStateManager.scale(1/scale[0],1/scale[0],1);
        }
    }

    public static boolean drawchromaline(String line,float x, float y) {
        if(Gradient.chromacolor == null) {
            return true;
        }
        if(line.contains("§u")) {
            Gradient.renderchroma(line.replaceAll("0x[0-9A-Fa-f]{6}",""),Math.round(x),Math.round(y),Gradient.chromacolor.getEffectiveColour().getRGB(), Display.chromaProgress);
            return false;
        }
        return true;
    }

    public static boolean drawimageline(String line, FontRenderer fontRenderer, float x1, float y1) {
        if(!line.contains("i%")) {
            return true;
        }
        String[] split = line.split("%");
        int x = Math.round(x1);
        int y = Math.round(y1);
        ResourceLocation image = Images.getImage(split[1]);
        if(image == null) {
            return true;
        }
        int i = 0;
        if(split[0].length() > 1) {
            String first = split[0].substring(0, split[0].length() - 1);
            i = WidgetManager.stringlength(first)+2;
            if(drawgradientline(first, fontRenderer,x, y, ani / 2)) {
                fontRenderer.drawStringWithShadow(first, x, y, 0xFFFFFF);
            }
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        GlStateManager.scale(1,1,1);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Gui.drawModalRectWithCustomSizedTexture(x+i, y-1, 0, 0, 10, 10, 10, 10);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        if(split.length != 3) {
            return false;
        }
        if(drawgradientline(split[2], fontRenderer,x+12+i, y, ani / 2)) {
            fontRenderer.drawStringWithShadow(split[2], x+12+i, y, 0xFFFFFF);
        }
        return false;
    }

    public static boolean drawgradientline(String line, FontRenderer fontRenderer,float x1, float y1, int ani) {
        List<Integer> Colors = getcolors(line);
        if(Colors.size() != 2) {
            return true;
        }
        String[] split = line.split("0x[0-9A-Fa-f]{6}");
        int width = 0;
        if (split.length == 1) {
            Gradient.rendergradient(colored(split[0]), Math.round(x1), Math.round(y1), ani, 0, Colors.get(0), Colors.get(1));
        } else if (split.length >= 2) {
            fontRenderer.drawStringWithShadow(colored(split[0]), Math.round(x1), Math.round(y1), 0xf98038);
            x1 += fontRenderer.getStringWidth(colored(split[0]));
            width = Gradient.rendergradient(colored(split[1]), Math.round(x1), Math.round(y1), ani, 0, Colors.get(0), Colors.get(1));
        }
        if (split.length == 3) {
            x1 += width;
            fontRenderer.drawStringWithShadow(colored(split[2]), Math.round(x1), Math.round(y1), 0xf98038);
        }
        return false;
    }

    private static List<Integer> getcolors(String inputString) {
        List<Integer> colorCodes = new ArrayList<>();
        Pattern pattern = Pattern.compile("0x[0-9A-Fa-f]{6}");;
        Matcher matcher = pattern.matcher(inputString);
        while (matcher.find()) {
            String colorCode = matcher.group();
            colorCodes.add(Integer.decode(colorCode));
        }
        return colorCodes;
    }

    public static String colored(String text) {
        return text.replace('&', '§');
    }

    public static void drawbackground(int x, int width, int y, int height) {
        y = y-2;
        int bgAlpha = getalpha(Display.widgetBGOpacity);
        int bgColor = (0) | (bgAlpha << 24);
        drawRect(x-4,y-2,width+4,height+3,bgColor);
        drawRect(x-4,y-1,x-5,height+2,bgColor);
        drawRect(width+4,y-1,width+5,height+2,bgColor);
    }

    public static int getalpha(int opacity) {
        opacity = Math.max(0, Math.min(100, opacity));
        int alpha = (int) (opacity / 100.0 * 255);
        String alphaHex = String.format("%02X", alpha);
        return Integer.parseInt(alphaHex, 16);
    }

}
