package net.nostalgia.mixin.client.frozen.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.client.frozen.FrozenSpriteRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class BlockRendererMixin {

    @Shadow @Final private ChunkVertexEncoder.Vertex[] vertices;

    @Unique private boolean nostalgia$inZone = false;

    @Inject(method = "renderModel", at = @At("HEAD"))
    private void nostalgia$detectZone(BlockStateModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
        this.nostalgia$inZone = false;
        if (!FrozenSpriteRegistry.hasAnyMappings()) return;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        if (!(level.tickRateManager() instanceof TickRateManagerAccess access)) return;
        if (!access.nostalgia$hasRegions()) return;
        long chunkKey = ChunkPos.pack(pos.getX() >> 4, pos.getZ() >> 4);
        this.nostalgia$inZone = access.nostalgia$isChunkFrozen(level.dimension(), chunkKey);
    }

    @ModifyVariable(
            method = "bufferQuad",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private TextureAtlasSprite nostalgia$swapAndRemap(TextureAtlasSprite sprite) {
        if (!this.nostalgia$inZone || sprite == null) return sprite;
        TextureAtlasSprite frozen = FrozenSpriteRegistry.getFrozenFor(sprite);
        if (frozen == null) return sprite;
        float oU0 = sprite.getU0(), oU1 = sprite.getU1();
        float oV0 = sprite.getV0(), oV1 = sprite.getV1();
        float du = oU1 - oU0;
        float dv = oV1 - oV0;
        if (Math.abs(du) < 1e-8f || Math.abs(dv) < 1e-8f) return sprite;
        float fU0 = frozen.getU0(), fU1 = frozen.getU1();
        float fV0 = frozen.getV0(), fV1 = frozen.getV1();
        float fdu = fU1 - fU0;
        float fdv = fV1 - fV0;
        for (int i = 0; i < 4; i++) {
            ChunkVertexEncoder.Vertex v = this.vertices[i];
            v.u = fU0 + (v.u - oU0) / du * fdu;
            v.v = fV0 + (v.v - oV0) / dv * fdv;
        }
        return frozen;
    }
}
