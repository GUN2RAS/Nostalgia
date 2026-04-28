package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public class CloudSpeedMixin {

    @ModifyVariable(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private int nostalgia$fadeCloudAlpha(int color) {
        float alphaMultiplier = 1.0f;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            alphaMultiplier *= net.nostalgia.alphalogic.core.AlphaRenderState.getCelestialAlpha();
        }

        if (alphaMultiplier >= 1.0f) return color;

        int originalAlpha = (color >> 24) & 0xFF;
        int newAlpha = Math.max((int)(originalAlpha * alphaMultiplier), 0);

        return (color & 0x00FFFFFF) | (newAlpha << 24);
    }

    @Inject(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nostalgia$cancelFullyFadedClouds(int color, CloudStatus cloudStatus, float bottomY, int range, Vec3 cameraPosition, long gameTime, float partialTicks, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.dimension() == net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            if (net.nostalgia.alphalogic.core.AlphaRenderState.getCelestialAlpha() <= 0.01f) {
                ci.cancel();
            }
        }
    }

    @ModifyVariable(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        ordinal = 0,
        argsOnly = true
    )
    private long nostalgia$accelerateCloudTime(long originalTime) {
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        if (t == null || t.isBystander()) {

            long permanentOffset = (long) net.nostalgia.client.ritual.RitualVisualManager.getDynamicCloudOffset(0, false);
            return originalTime - permanentOffset;
        }

        long offset = (long) net.nostalgia.client.ritual.RitualVisualManager.getDynamicCloudOffset(originalTime, true);
        return originalTime - offset;
    }

    @ModifyVariable(
        method = "render(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V",
        at = @At("HEAD"),
        ordinal = 1,
        argsOnly = true
    )
    private float nostalgia$accelerateCloudPartialTick(float originalPartialTick) {
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        if (t == null || t.isBystander()) {

            double exactOffset = net.nostalgia.client.ritual.RitualVisualManager.getDynamicCloudOffset(0, false);
            long intOffset = (long) exactOffset;
            return (float) (originalPartialTick - (exactOffset - intOffset));
        }

        double exactOffset = net.nostalgia.client.ritual.RitualVisualManager.getDynamicCloudOffset(0, false);
        long intOffset = (long) exactOffset;
        double fracOffset = exactOffset - intOffset;

        return (float) (originalPartialTick - fracOffset);
    }

}
