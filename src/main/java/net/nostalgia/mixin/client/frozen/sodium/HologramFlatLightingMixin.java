package net.nostalgia.mixin.client.frozen.sodium;

import net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext;
import net.nostalgia.client.render.HologramRenderHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.caffeinemc.mods.sodium.client.model.light.LightMode;

@Mixin(AbstractBlockRenderContext.class)
public abstract class HologramFlatLightingMixin {

    @Shadow protected net.minecraft.core.BlockPos pos;
    @Shadow protected LightMode defaultLightMode;
    @Shadow protected boolean useAmbientOcclusion;

    @Inject(method = "prepareAoInfo", at = @At("TAIL"))
    private void nostalgia$forceFlatLightingForHolograms(boolean modelAo, CallbackInfo ci) {
        if (HologramRenderHelper.isBlockInverted(this.pos)) {
            this.defaultLightMode = LightMode.FLAT;
            this.useAmbientOcclusion = false;
        }
    }

    @Inject(method = "shadeQuad", at = @At("RETURN"))
    private void nostalgia$forceLightmapForHolograms(net.caffeinemc.mods.sodium.client.render.model.MutableQuadViewImpl quad, LightMode lightMode, boolean emissive, net.caffeinemc.mods.sodium.client.render.model.SodiumShadeMode shadeMode, CallbackInfo ci) {
        if (HologramRenderHelper.isBlockInverted(this.pos)) {
            // Принудительно устанавливаем максимальное освещение (SkyLight 15, BlockLight 15)
            // Это решает проблему черных теней, возникающих из-за того, что перевернутая
            // геометрия острова в Оверворлде отбрасывает тень сама на себя снизу вверх.
            for (int i = 0; i < 4; i++) {
                quad.setLight(i, 15728880);
            }
        }
    }
}
