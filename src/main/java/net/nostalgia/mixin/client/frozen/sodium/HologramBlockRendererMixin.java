package net.nostalgia.mixin.client.frozen.sodium;

import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.client.render.HologramRenderHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderer.class)
public abstract class HologramBlockRendererMixin {

    @Shadow @Final private ChunkVertexEncoder.Vertex[] vertices;

    @Unique private boolean nostalgia$isInvertedHologram = false;
    @Unique private float nostalgia$blockYOrigin = 0.0f;
    @Unique private float nostalgia$blockZOrigin = 0.0f;

    @Inject(method = "renderModel", at = @At("HEAD"))
    private void nostalgia$detectHologram(BlockStateModel model, BlockState state, BlockPos pos, BlockPos origin, CallbackInfo ci) {
        this.nostalgia$isInvertedHologram = HologramRenderHelper.isBlockInverted(pos);
        if (this.nostalgia$isInvertedHologram) {
            this.nostalgia$blockYOrigin = pos.getY();
            this.nostalgia$blockZOrigin = pos.getZ();
        }
    }

    @Inject(
            method = "bufferQuad",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/material/Material;bits()I")
    )
    private void nostalgia$invertGeometry(net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl quad, float[] brightnesses, net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material material, CallbackInfo ci) {
        if (!this.nostalgia$isInvertedHologram) return;

        float blockCenterY = this.nostalgia$blockYOrigin + 0.5f;

        for (int i = 0; i < 4; i++) {
            ChunkVertexEncoder.Vertex v = this.vertices[i];
            
            v.y = 2.0f * blockCenterY - v.y;
        }

        ChunkVertexEncoder.Vertex temp0 = this.vertices[0];
        this.vertices[0] = this.vertices[1];
        this.vertices[1] = temp0;

        ChunkVertexEncoder.Vertex temp2 = this.vertices[2];
        this.vertices[2] = this.vertices[3];
        this.vertices[3] = temp2;
    }

    @ModifyVariable(
            method = "bufferQuad",
            at = @At(value = "STORE", ordinal = 0),
            ordinal = 0
    )
    private net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing nostalgia$swapFace(net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing normalFace) {
        if (!this.nostalgia$isInvertedHologram || normalFace == null) return normalFace;
        if (normalFace == net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing.POS_Y) {
            return net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing.NEG_Y;
        } else if (normalFace == net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing.NEG_Y) {
            return net.caffeinemc.mods.sodium.client.model.quad.properties.ModelQuadFacing.POS_Y;
        }
        return normalFace;
    }
}
