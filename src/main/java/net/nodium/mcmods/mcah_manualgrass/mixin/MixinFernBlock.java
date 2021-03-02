package net.nodium.mcmods.mcah_manualgrass.mixin;

import net.nodium.mcmods.mcah_manualgrass.ManualGrass;
import net.nodium.mcmods.mcah_manualgrass.MovableGrass;
import net.nodium.mcmods.mcah_manualgrass.Position;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FernBlock.class)
public abstract class MixinFernBlock extends PlantBlock implements MovableGrass {
    protected MixinFernBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Vec3d getOffsetPos(BlockState state, BlockView view, BlockPos pos) {
        return ManualGrass.mcGetOffsetPos(pos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            return ActionResult.PASS;
        }

        ManualGrass.incrementOffset(new Position(pos.getX(), pos.getY(), pos.getZ()));
        ManualGrass.reloadBlock(pos);
//        ManualGrass.reloadChunks();

        return ActionResult.SUCCESS;
    }
}
