package net.fabricmc.example;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements GettableChunkBuilder {
    @Shadow ChunkBuilder chunkBuilder;

    public ChunkBuilder getChunkBuilder() {
        return chunkBuilder;
    }
}
