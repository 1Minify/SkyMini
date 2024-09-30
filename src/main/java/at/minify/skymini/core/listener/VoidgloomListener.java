package at.minify.skymini.core.listener;

import at.minify.skymini.Main;
import at.minify.skymini.api.annotations.MiniRegistry;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.api.service.WidgetService;
import at.minify.skymini.core.GUI.categories.Slayer;
import at.minify.skymini.core.data.Server;
import at.minify.skymini.core.manager.Chat;
import at.minify.skymini.core.widgets.BossWidget;
import at.minify.skymini.core.widgets.MiniBossWidget;
import at.minify.skymini.util.EntityTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

import static at.minify.skymini.util.EntityTracker.searchEntity;

@MiniRegistry
public class VoidgloomListener {

    MiniBossWidget miniBossWidget = WidgetService.getWidget(MiniBossWidget.class);
    BossWidget bossWidget = WidgetService.getWidget(BossWidget.class);

    private EntityArmorStand Entity = null;
    private EntityArmorStand Entity1 = null;
    private String spawnedByName = null;
    private String time = "";
    private int dominusTime = 0;
    private boolean dominus10 = false;
    private int sbzTime = 0;
    private int guardian;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChatReceived1(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        //String message = event.message.getUnformattedText();
        if(message.contains("Dominus")) {
            int number = Integer.parseInt(Chat.uncolored(message.replaceAll("[^0-9]", "")));
            dominus10 = number == 10;
        }
    }

    @SubscribeEvent
    public void onClientTick(MiniTickEvent event) {
        if(Minecraft.getMinecraft().thePlayer == null) {
            return;
        }
        if(!Main.getAPI().server.equals(Server.THE_END) && !Main.getAPI().server.equals(Server.CRIMSON_ISLE)) {
            if(!event.second(1)) {
                return;
            }
            Entity = searchEntity("Spawned by: " + Minecraft.getMinecraft().thePlayer.getName(), 50, null);
            Entity1 = searchEntity("Sven Packmaster", 1, Entity);
            if(Entity1 == null) { Entity1 = searchEntity("Broodfather", 1, Entity); }
            if(Entity1 == null) { Entity1 = searchEntity("Revenant", 1, Entity); }
            if (Entity1 != null) { spawnedByName = Entity1.getCustomNameTag(); }
            if(Entity1 != null) {
                String arrow = EntityTracker.getYaw(Minecraft.getMinecraft().thePlayer, Entity1);
                int distance = EntityTracker.getDistance(Minecraft.getMinecraft().thePlayer, Entity1);
                bossWidget.setText("&7Boss: " + spawnedByName + time + " &8(&c" + arrow + " &8-&c " + distance + "m&8)");
            }
            return;
        }
        String name;
        if(Main.getAPI().server.equals(Server.THE_END)) {
            if(event.second(1)) {
                Entity1 = null;
                spawnedByName = null;
                Entity = searchEntity("Voidgloom Seraph", Slayer.voidradius, null);
                if(Entity == null) { Entity = searchEntity("Voidling Radical",30,null); }
                if(Entity == null) { Entity = searchEntity("Voidcrazed Maniac",30,null); }
                if(Entity == null) { Entity = searchEntity("Voidling Devotee",30,null); }
                if(Entity == null) {
                    miniBossWidget.setText("&7Miniboss: &c-");
                }
                if(Entity != null && Entity.getCustomNameTag().contains("Voidgloom")) {
                    miniBossWidget.setText("&7Miniboss: &c-");
                }
                if(Entity != null) { if(Entity.isDead) { Entity = null; } }
                bossWidget.setEnabled(Entity != null && Entity.getCustomNameTag().contains("Voidgloom"));
                if(Entity != null && Entity.getCustomNameTag().contains("Voidgloom Seraph")) {
                    if(searchEntity("3:", 2, Entity) != null) { time = " " + Objects.requireNonNull(searchEntity("3:", 2, Entity)).getCustomNameTag(); }
                    if(searchEntity("2:", 2, Entity) != null) { time = " " + Objects.requireNonNull(searchEntity("2:", 2, Entity)).getCustomNameTag(); }

                    Entity1 = searchEntity("Spawned by", 1, Entity);
                    if(Entity1 != null) {
                        spawnedByName = Entity1.getCustomNameTag();
                        spawnedByName = spawnedByName.replaceAll("Spawned by:","");
                    }
                }
            }
            if (Entity != null) {
                name = Entity.getCustomNameTag();
                String arrow = EntityTracker.getYaw(Minecraft.getMinecraft().thePlayer, Entity);
                int distance = EntityTracker.getDistance(Minecraft.getMinecraft().thePlayer, Entity);
                if(name.contains("Voidling Radical") || name.contains("Voidcrazed")  || name.contains("Voidling Devotee"))  {
                    miniBossWidget.setText("&7Miniboss: " + name + " &8(&c" + arrow + " &8-&c " + distance + "m&8)");
                }
                if(name.contains("Voidgloom Seraph")) {
                    name = name.replaceAll("Voidgloom Seraph ", "");
                    if(searchGuardian(5, Entity) != null) {
                        guardian--;
                        name = name + " &8(&c" + formatSecond(guardian) + "s&8)";
                    } else { guardian = 142; }
                    if(Entity1 != null) {
                        bossWidget.setText("&7Voidgloom: " + name + " &7by" + spawnedByName);
                        sbzTime++;
                        if(dominus10) {
                            dominusTime++;
                        } else {
                            dominusTime = 0;
                        }
                        //dominostime
                        miniBossWidget.setText("§7(§dSBZ Purge§7) Carry Timer: #f3bc30&l" + formatSecond(sbzTime) + "s#ef6806");
                        //MinibossWidget.text = "§7(§dSBZ Purge§7) Carry Timer: #f3bc30&l" + formatsecond(sbztime) + "s#ef6806"/*§r §8┃ §7Dominus: §d§l" + formatsecond(dominustime) + "s"*/;
                    } else {
                        bossWidget.setText("&7Voidgloom: " + name);
                    }
                }
            } else {
                sbzTime = 0;
            }
        }
    }

    public String formatSecond(int tick) {
        return String.format("%.1f", tick/20.0).replaceAll(",",".");
    }

    public EntityGuardian searchGuardian(float radius, net.minecraft.entity.Entity entitypos) {
        net.minecraft.entity.Entity mob = entitypos;
        if (entitypos == null) {mob = Minecraft.getMinecraft().thePlayer;}
        AxisAlignedBB box = mob.getEntityBoundingBox().expand(radius, radius, radius);
        for (Entity entity : Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(mob, box)) {
            if (entity instanceof EntityGuardian) {
                return (EntityGuardian) entity;
            }
        }
        return null;
    }

}
