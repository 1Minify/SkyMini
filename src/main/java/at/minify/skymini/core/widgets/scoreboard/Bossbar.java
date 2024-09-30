package at.minify.skymini.core.widgets.scoreboard;

import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.annotations.Service;
import at.minify.skymini.core.widgets.util.WidgetExecutor;
import at.minify.skymini.api.widgets.manager.WidgetManager;
import at.minify.skymini.core.GUI.categories.Display;
import at.minify.skymini.core.GUI.functions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MiniRegistry
@Service(priority = 3)
public class Bossbar {

    private String realBossBar = "";

    public void drawBossBar(List<String> all) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        int y = 7;
        if(Display.customHUD) {
            if(!realBossBar.isEmpty() && !realBossBar.contains("[§c0.3%")) {
                int fontheight = fontRenderer.FONT_HEIGHT;
                float x1 = functions.setx(50)-((float) WidgetManager.stringlength(realBossBar)/2);
                float y1 = y+fontheight+2;
                WidgetExecutor.drawbackground((int) x1, (int) (x1+WidgetManager.stringlength(realBossBar)), (int) y1, (int) (y1+fontheight));
                fontRenderer.drawStringWithShadow(realBossBar, x1, y1, 0xFFFFFF);
            }
            return;
        }
        Map<String, String> bosslist = new HashMap<>();
        for(String line : all) {
            if(line.contains("Summer") || line.contains("Spring") || line.contains("Winter") || line.contains("Autumn")) { bosslist.put("year","§f" + line); }
            if(line.contains("am ") || line.contains("pm ")) { bosslist.put("time",line); }
            if(line.contains("⏣")) { bosslist.put("area",line.replaceFirst(" ","")); }
        }
        StringBuilder bossbar = new StringBuilder();
        bossbar.append(bosslist.get("area"));
        bossbar.append(" §8┃" + bosslist.get("year"));
        if(bosslist.get("time") != null) {
            bossbar.append(" §8┃" + bosslist.get("time"));
        }
        if(bossbar.toString().contains("null")) { return; }
        int length = WidgetManager.stringlength(bossbar.toString());
        int x = functions.setx(50)-(length/2);
        if(realBossBar.isEmpty() || realBossBar.contains("[§c0.3%")) {
            int fontheight = fontRenderer.FONT_HEIGHT;
            WidgetExecutor.drawbackground(x,x+length,y,y+fontheight);
            fontRenderer.drawStringWithShadow(bossbar.toString(), x, y, 0xFFFFFF);
        } else {
            int fontheight = fontRenderer.FONT_HEIGHT;
            WidgetExecutor.drawbackground(x,x+length,y,y+fontheight*2+2);
            fontRenderer.drawStringWithShadow(bossbar.toString(), x, y, 0xFFFFFF);
            fontRenderer.drawStringWithShadow(realBossBar, functions.setx(50)-((float) WidgetManager.stringlength(realBossBar)/2), y+fontheight+2, 0xFFFFFF);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.BOSSHEALTH && event.isCancelable()) {
            if(Display.sbbossbar) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        World world = Minecraft.getMinecraft().theWorld;
        world.loadedEntityList.stream();
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityWither) {
                String name = entity.getCustomNameTag();
                if(name.contains("§") && !name.contains("Objective:")) {
                    EntityWither wither = (EntityWither) entity;
                    float percent = (wither.getHealth()/wither.getMaxHealth())*100;
                    if(percent != 100) {
                        realBossBar = name + " §8[§c" + String.format("%.1f", percent).replaceAll(",",".") + "%§8]";
                    } else {
                        realBossBar = "";
                    }
                } else {
                    realBossBar = "";
                }
            }
        }
    }

}
