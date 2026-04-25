package net.nostalgia.mixin.client.frozen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.ChunkPos;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import net.nostalgia.client.frozen.FrozenSpriteRegistry;
import net.nostalgia.client.ritual.ClientFreezeRegions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Quaternionf;
import net.minecraft.client.resources.model.sprite.AtlasManager;

@Mixin(FlameFeatureRenderer.class)
public class FlameFeatureRendererMixin {

    @Unique
    private static final ThreadLocal<EntityRenderState> nostalgia$currentState = new ThreadLocal<>();

    @Inject(method = "renderFlame", at = @At("HEAD"))
    private void nostalgia$captureState(PoseStack.Pose pose, MultiBufferSource bufferSource, EntityRenderState state, Quaternionf rotation, AtlasManager atlasManager, CallbackInfo ci) {
        nostalgia$currentState.set(state);
    }

    @ModifyVariable(method = "renderFlame", at = @At("STORE"), ordinal = 0)
    private TextureAtlasSprite nostalgia$swapFire1(TextureAtlasSprite sprite) {
        return nostalgia$swap(sprite);
    }

    @ModifyVariable(method = "renderFlame", at = @At("STORE"), ordinal = 1)
    private TextureAtlasSprite nostalgia$swapFire2(TextureAtlasSprite sprite) {
        return nostalgia$swap(sprite);
    }

    @Unique
    private TextureAtlasSprite nostalgia$swap(TextureAtlasSprite sprite) {
        if (sprite == null) return null;
        EntityRenderState state = nostalgia$currentState.get();
        if (state == null) return sprite;
        if (!FrozenSpriteRegistry.hasAnyMappings()) return sprite;
        
        TickRateManagerAccess access = ClientFreezeRegions.access();
        if (access == null || !access.nostalgia$hasRegions()) return sprite;
        
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return sprite;
        
        long chunkKey = ChunkPos.pack((int) Math.floor(state.x) >> 4, (int) Math.floor(state.z) >> 4);
        if (access.nostalgia$isChunkFrozen(level.dimension(), chunkKey)) {
            TextureAtlasSprite frozen = FrozenSpriteRegistry.getFrozenFor(sprite);
            if (frozen != null) return frozen;
        }
        return sprite;
    }
}
