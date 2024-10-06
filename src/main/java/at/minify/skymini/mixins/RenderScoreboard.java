package at.minify.skymini.mixins;

import at.minify.skymini.core.GUI.categories.Display;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class RenderScoreboard {
    @Shadow public static boolean renderObjective;
    @Shadow public static boolean renderAir;

    @Inject(method = "renderGameOverlay", at = @At("HEAD"))
    private void onRenderGameOverlay(float partialTicks, CallbackInfo ci) {
        renderObjective = !Display.customScoreboard;
    }

    /*@Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.renderObjective = false;
    }*/

    /*@Inject(method = "renderGameOverlay", at = @At("HEAD"))
    private void onRenderGameOverlay(float partialTicks, CallbackInfo ci) {
        if (Main.inSkyblock && ScoreboardConfig.customsb) {
            this.renderObjective = false;
        } else {
            this.renderObjective = true;
        }
    }*/

    /*@Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.renderObjective = false;
        //if(Main.inSkyblock) {
        //    this.renderObjective = false;
        //} else {
        //    this.renderObjective = true;
        //}
    }*/
}