package net.nostalgia.mixin.rd132211;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.LightmapRenderState;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CardinalLighting;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
abstract class RdFlatCardinalMixin {
    private static final CardinalLighting FLAT =
        new CardinalLighting(1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);

    @Inject(method = "cardinalLighting", at = @At("HEAD"), cancellable = true)
    private void nostalgia$flatInRD(CallbackInfoReturnable<CardinalLighting> cir) {
        ClientLevel self = (ClientLevel) (Object) this;
        if (self.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            cir.setReturnValue(FLAT);
        }
    }
}

@Mixin(EntityRenderer.class)
abstract class RdNoShadowMixin {
    @Inject(method = "extractShadow", at = @At("HEAD"), cancellable = true)
    private void nostalgia$killShadowInRD(EntityRenderState state, Minecraft mc, net.minecraft.world.level.Level level, CallbackInfo ci) {
        if (level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            state.shadowPieces.clear();
            state.shadowRadius = 0.0F;
            ci.cancel();
        }
    }
}

@Mixin(EntityRenderer.class)
abstract class RdFullBrightEntityMixin {
    @Inject(method = "getPackedLightCoords", at = @At("HEAD"), cancellable = true)
    private void nostalgia$fullBrightEntityInRD(Entity entity, float partialTickTime, CallbackInfoReturnable<Integer> cir) {
        if (entity.level().dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            cir.setReturnValue(LightCoordsUtil.pack(15, 15));
        }
    }
}

@Mixin(Lightmap.class)
abstract class RdFullBrightLightmapMixin {
    @Shadow @Final private GpuTexture texture;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void nostalgia$fullBrightInRD(LightmapRenderState renderState, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension().equals(ModDimensions.RD_132211_LEVEL_KEY)) {
            RenderSystem.getDevice().createCommandEncoder().clearColorTexture(this.texture, -1);
            ci.cancel();
        }
    }
}

@Mixin(LevelSlice.class)
abstract class RdSodiumAOMixin {
    @Shadow(remap = false)
    private ClientLevel level;

    @Inject(method = "useAmbientOcclusion", at = @At("HEAD"), cancellable = true, remap = false)
    private void nostalgia$disableAOInRD(CallbackInfoReturnable<Boolean> cir) {
        if (this.level != null && this.level.dimension() == ModDimensions.RD_132211_LEVEL_KEY) {
            cir.setReturnValue(false);
        }
    }
}
