package at.minify.skymini.api.util;

import at.minify.skymini.Main;
import at.minify.skymini.api.api.ModAPI;
import at.minify.skymini.api.events.MiniChatEvent;
import at.minify.skymini.api.events.MiniTickEvent;
import at.minify.skymini.core.data.Server;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public abstract class MiniEvent {

    public void tick(MiniTickEvent event) {}

    public void chat(MiniChatEvent event) {}

    public void render(RenderGameOverlayEvent event) {}

    public void renderWorldLast(RenderWorldLastEvent event) {}

    public void logOut(PlayerEvent.PlayerLoggedOutEvent event) {}

    public void changeServer(Server server) {}

    public boolean isInGame() {
        return findPlayer() && findWorld();
    }

    public boolean findPlayer() {
        return Minecraft.getMinecraft().thePlayer != null;
    }

    public boolean findWorld() {
        return Minecraft.getMinecraft().theWorld != null;
    }

    public ModAPI api() {
        return Main.getAPI();
    }

}
