package net.nostalgia.mixin.ritual;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.joml.Vector4f;
import net.minecraft.util.Mth;

@Mixin(net.minecraft.client.renderer.fog.FogRenderer.class)
public class FogTransitionMixin {
    @ModifyArgs(
        method = "setupFog",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;updateBuffer(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V")
    )
    private void modifyFogParameters(Args args) {
        float whiteoutAlpha = net.nostalgia.client.ritual.RitualVisualManager.getWhiteoutAlpha();
        if (whiteoutAlpha > 0.0f) {
            Vector4f color = args.get(2);
            Vector4f newColor = new Vector4f(color).lerp(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), whiteoutAlpha);
            args.set(2, newColor);
            
            args.set(3, Mth.lerp(whiteoutAlpha, (float)args.get(3), 0.0f)); 
            args.set(4, Mth.lerp(whiteoutAlpha, (float)args.get(4), 0.0f)); 
            args.set(5, Mth.lerp(whiteoutAlpha, (float)args.get(5), 0.0f)); 
            args.set(6, Mth.lerp(whiteoutAlpha, (float)args.get(6), 0.0f)); 
            args.set(7, Math.max(0.0f, Mth.lerp(whiteoutAlpha, (float)args.get(7), 0.0f))); 
            args.set(8, Math.max(0.0f, Mth.lerp(whiteoutAlpha, (float)args.get(8), 0.0f))); 
        }
    }
}
