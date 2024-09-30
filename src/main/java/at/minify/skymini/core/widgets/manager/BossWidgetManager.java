package at.minify.skymini.core.widgets.manager;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.BossWidget;
import at.minify.skymini.util.EntityTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.minify.skymini.util.EntityTracker.searchEntity;

@MiniRegistry(server = Server.CRIMSON_ISLE)
public class BossWidgetManager {

    BossWidget bossWidget = WidgetService.getWidget(BossWidget.class);

    private final List<String> bosslist = new ArrayList<String>(Arrays.asList("Bladesoul", "Mage Outlaw", "Barbarian Duke X", "Ashfang"));
    private int ticks = 0;
    private EntityArmorStand Entity = null;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Main.getAPI().inSkyBlock) {
            ticks++;
            if (ticks >= 200) {
                ticks = 0;
            }
            if (ticks % 20 == 0) {
                Entity = null;
                for(String bossName : bosslist) {
                    if(Entity == null) { Entity = searchEntity(bossName,30,null); }
                }
                if (Entity != null) { if (Entity.isDead) { Entity = null; Chat.send("is dead");} }
                if(Entity == null) { Entity = searchEntity("ASHEN",30,null); }
                if(Entity == null) { Entity = searchEntity("AURIC",30,null); }
                if(Entity == null) { Entity = searchEntity("CRYSTAL",30,null); }
                if(Entity == null) { Entity = searchEntity("SPIRIT",30,null); }
                bossWidget.setEnabled(Entity != null);
            }
            if (Entity != null) {
                String name = Entity.getCustomNameTag();
                String arrow = EntityTracker.getYaw(Minecraft.getMinecraft().thePlayer, Entity);
                int distance = EntityTracker.getDistance(Minecraft.getMinecraft().thePlayer, Entity);
                bossWidget.setText(name + " &8(&c" + arrow + " &8-&c " + distance + "m&8)");
            }
        }
    }

}
