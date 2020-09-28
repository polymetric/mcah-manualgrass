package net.fabricmc.example.mixin;

import net.fabricmc.example.ManualGrass;
import net.fabricmc.example.MovableGrass;
import net.fabricmc.example.Offset;
import net.fabricmc.example.Position;
import net.minecraft.block.BlockState;
import net.minecraft.block.FernBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Arrays;

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

        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            System.out.println(e);
        }
        ManualGrass.incrementOffset(new Position(pos.getX(), pos.getY(), pos.getZ()));
        ManualGrass.reloadChunks();

        return ActionResult.SUCCESS;
    }
}
