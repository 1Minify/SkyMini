package at.minify.skymini.mixins;

import at.minify.skymini.core.GUI.categories.Garden;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class BlockBreak {

    @Inject(method = "addBlockDestroyEffects", at = @At("HEAD"), cancellable = true)
    public void addBlockDestroyEffects(BlockPos pos, IBlockState state, CallbackInfo ci) {
        if(Garden.disabledBreakAnimation) {
            ci.cancel();
        }
    }

}
