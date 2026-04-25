package net.nostalgia.mixin.alpha;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(RenderSetup.class)
public class AlphaRenderSetupMixin {

    @Inject(method = "getTextures", at = @At("RETURN"), cancellable = true)
    private void alphaForcePixelatedDistance(CallbackInfoReturnable<Map<String, RenderSetup.TextureAndSampler>> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            Map<String, RenderSetup.TextureAndSampler> map = cir.getReturnValue();
            if (map != null && !map.isEmpty() && map.containsKey("Sampler0")) {
                try {
                    RenderSetup.TextureAndSampler original = map.get("Sampler0");
                    
                    GpuSampler nearestSampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, false);
                    map.put("Sampler0", new RenderSetup.TextureAndSampler(original.textureView(), nearestSampler));
                } catch (UnsupportedOperationException e) {
                    
                }
            }
        }
    }
}
