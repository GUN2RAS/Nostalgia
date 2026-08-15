package net.nostalgia.mixin.alpha;

import net.minecraft.client.Minecraft;

import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.renderer.LightmapRenderStateExtractor.class)
public abstract class AlphaLightTextureMixin {

    @Redirect(
        method = "extract(Lnet/minecraft/client/renderer/state/LightmapRenderState;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;"
        )
    )
    private Object stepSkyLightFactor(EnvironmentAttributeProbe probe, EnvironmentAttribute<?> key, float partialTick) {
        Object value = probe.getValue(key, partialTick);
        Minecraft client = Minecraft.getInstance();
        if (client.level != null && client.level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            if (key == EnvironmentAttributes.SKY_LIGHT_FACTOR) {
                float floatVal = (Float) value;
                return Math.round(floatVal * 15.0F) / 15.0F;
            }
            if (key == EnvironmentAttributes.BLOCK_LIGHT_TINT) {

               return net.minecraft.util.ARGB.color(34, 107, 255);
            }
        }
        return value;
    }
}
