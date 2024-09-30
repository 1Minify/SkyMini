package at.minify.skymini.core.widgets;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.ServiceContainer;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.scoreboard.ScoreboardManager;
import at.minify.skymini.core.widgets.util.WidgetExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import static at.minify.skymini.core.GUI.functions.setx;

@MiniRegistry(server = Server.SKYBLOCK)
public class HUDGui {

    public static float pos1 = 0;
    static float time = 0;
    public static String season = "";
    public static String daytime = "";
    public static String area = "";
    ResourceLocation loc1 = new ResourceLocation("skymini","textures/test/bar.png");
    static ResourceLocation barrier = new ResourceLocation("skymini","textures/test/line.png");
    ResourceLocation loc3 = new ResourceLocation("skymini","textures/test/arrow1.png");

    ScoreboardManager scoreboardManager = ServiceContainer.getService(ScoreboardManager.class);

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if(!Display.customHUD) {
            return;
        }
        if(!Main.getAPI().inSkyBlock) {
            return;
        }
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int sclaedX = scaledRes.getScaledWidth();
        Minecraft.getMinecraft().getTextureManager().bindTexture(loc1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, sclaedX, 15, sclaedX, 15);

        Minecraft.getMinecraft().getTextureManager().bindTexture(loc3);
        Gui.drawModalRectWithCustomSizedTexture((int) (setx(35)+(time/100*setx(30))-5), 1, 0, 0, 10, 16, 10, 16);


        float scale = 0.8F;
        GlStateManager.scale(scale,scale,1);
        renderText("§8" + Main.getAPI().serverId,1);
        renderText(area,2);
        renderText(daytime,3);
        renderText("§fi%coin%#f3bc30" + scoreboardManager.getPurse() + "#ef6806",4);
        renderText("§fi%bit%#26bce2" + scoreboardManager.getBits() + "#22e479",5);
        renderText("§fi%capy%#30ab12" + Minecraft.getMinecraft().thePlayer.getName() + "#8df623",6);
        GlStateManager.scale(1/scale,1/scale,1);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void renderText(String text, float pos) {
        float addPos = 0;
        if(pos < 4) {
            int positions = 3;
            float pos1 = (float) setx(35) / positions;
            addPos = (pos1 * pos) * 1.2F;
            WidgetExecutor.renderWidget(text, addPos-setx(7), 5, true, false);
        }
        else {
            int positions = 3;
            float pos1 = (float) setx(33) / positions;
            addPos = (setx(65) + pos1 * (pos-3)) * 1.2F;
            WidgetExecutor.renderWidget(text, addPos, 5, true, false);
        }
    }

    @SubscribeEvent
    public void event(MiniTickEvent event) {
        if(!event.second(1)) return;
        getTime();
    }

    //Sommer 37,5 - 62,5
    //Autumn 62,5 - 87,5
    //Winter 87,5 - 12,5
    //Spring 12,5 - 37,5

    public static void getTime() {
        for(String line : Main.getAPI().scoreboardData) {
            if(line.contains("Summer") || line.contains("Autumn") || line.contains("Winter") || line.contains("Spring")) {
                HUDGui.season = line;
            }
            else if(line.contains("am ") || line.contains("pm ")) {
                HUDGui.daytime = line;
            }
            else if(line.contains("⏣")) {
                HUDGui.area = line;
            }
        }
        if(season.isEmpty()) {
            return;
        }
        float time1 = 0;
        if(season.contains("Spring")) {
            time1 += 12.5F;
        }
        else if(season.contains("Summer")) {
            time1 += 37.5F;
        }
        else if(season.contains("Autumn")) {
            time1 += 62.5F;
        }
        else if(season.contains("Winter")) {
            time1 += 87.5F;
        }
        if(!season.contains("Late") && !season.contains("Early")) {
            time1 += 8.33F;
        }
        else if(season.contains("Late")) {
            time1 += 16.66F;
        }
        int date = Integer.parseInt(Chat.uncolored(season.replaceAll("[^0-9]", "")));
        time1 += 8.33F*((float) date /30);
        if(time1 >= 100) {
            time1 -= 100;
        }
        time = time1;
    }


}
