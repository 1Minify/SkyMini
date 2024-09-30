package at.minify.skymini.util;

import at.minify.skymini.Main;
import at.minify.skymini.core.GUI.categories.Garden;
import at.minify.skymini.mixins.Crops.BlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class CropUtilities {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static final AxisAlignedBB[] CARROT_POTATO_BOX = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D) };

    public static final AxisAlignedBB[] WHEAT_BOX = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D) };

    public static final AxisAlignedBB[] NETHER_WART_BOX = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D) };

    public static void updateCropsMaxY(World world, BlockPos pos, Block block) {
        IBlockState blockState = world.getBlockState(pos);
        Integer ageValue = (Integer) blockState.getValue((IProperty) BlockCrops.AGE);
        BlockAccessor accessor = (BlockAccessor)block;
        if (Main.getAPI().inSkyBlock && Garden.hitboxes || mc.isSingleplayer() && Garden.hitboxes) {
            accessor.setMaxY((blockState
                    .getBlock() instanceof net.minecraft.block.BlockPotato || blockState.getBlock() instanceof net.minecraft.block.BlockCarrot) ?
                    (CARROT_POTATO_BOX[ageValue.intValue()]).maxY :
                    (WHEAT_BOX[ageValue.intValue()]).maxY);
            return;
        }
        accessor.setMaxY(0.25D);
    }

    public static void updateWartMaxY(World world, BlockPos pos, Block block) {
        ((BlockAccessor)block).setMaxY(Main.getAPI().inSkyBlock && Garden.hitboxes || mc.isSingleplayer() && Garden.hitboxes ? NETHER_WART_BOX[((Integer)world.getBlockState(pos).getValue((IProperty) BlockNetherWart.AGE))].maxY : 0.25D);
    }

}
