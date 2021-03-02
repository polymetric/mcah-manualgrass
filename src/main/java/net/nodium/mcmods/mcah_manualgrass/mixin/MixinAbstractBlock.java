package net.nodium.mcmods.mcah_manualgrass.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.nodium.mcmods.mcah_manualgrass.ManualGrass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinAbstractBlock {
    @Shadow
    public abstract Block getBlock();

    @Inject(method = "getModelOffset", at = @At("HEAD"), cancellable = true)
    private void getModelOffsetOverride(BlockView world, BlockPos pos, CallbackInfoReturnable ci) {
        if (getBlock() != Blocks.GRASS)
            return;

        ci.cancel();
        ci.setReturnValue(ManualGrass.mcGetOffsetPos(pos));
    }
}