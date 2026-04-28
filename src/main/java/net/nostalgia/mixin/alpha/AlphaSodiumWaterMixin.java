package net.nostalgia.mixin.alpha;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.nostalgia.client.render.NostalgiaChunkCache;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.fabric.render.FluidRendererImpl$DefaultRenderContext", remap = false)
public class AlphaSodiumWaterMixin {

    @Unique
    private FluidModel nostalgia$alphaWaterModel;

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE", 
                    target = "Lnet/minecraft/client/renderer/block/FluidStateModelSet;get(Lnet/minecraft/world/level/material/FluidState;)Lnet/minecraft/client/renderer/block/FluidModel;",
                    remap = true
            )
    )
    private FluidModel nostalgia$overrideSodiumAlphaFluidModel(FluidModel original, @Local(argsOnly = true) net.minecraft.client.renderer.block.BlockAndTintGetter level, @Local(argsOnly = true) FluidState fluidState, @Local(argsOnly = true) net.minecraft.core.BlockPos blockPos) {
        if (!fluidState.is(Fluids.WATER) && !fluidState.is(Fluids.FLOWING_WATER)) {
            return original;
        }

        boolean isAlpha = false;
        boolean inAlphaDimension = Minecraft.getInstance().level != null && Minecraft.getInstance().level.dimension().equals(ModDimensions.ALPHA_112_01_LEVEL_KEY);

        isAlpha = inAlphaDimension;

        net.nostalgia.alphalogic.ritual.event.ClientTransitionView clientT = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        if (clientT != null && !clientT.isBystander()) {
            if (clientT.ritualCenter() != null) {
                double distSq = blockPos.distSqr(clientT.ritualCenter());
                float currentRadius = clientT.alphaRadius();
                if (distSq <= currentRadius * currentRadius) {
                    if ("alpha".equals(clientT.targetDimension())) {
                        isAlpha = true;
                    } else {
                        isAlpha = false;
                    }
                }
            }
        }

        if (isAlpha) {
            if (this.nostalgia$alphaWaterModel == null) {
                TextureAtlas atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(Identifier.withDefaultNamespace("blocks"));
                TextureAtlasSprite still = atlas.getSprite(Identifier.fromNamespaceAndPath("nostalgia", "block/alpha_water_still"));
                TextureAtlasSprite flow = atlas.getSprite(Identifier.fromNamespaceAndPath("nostalgia", "block/alpha_water_flow"));
                TextureAtlasSprite overlay = atlas.getSprite(Identifier.fromNamespaceAndPath("nostalgia", "block/alpha_water_overlay"));

                Material.Baked stillMat = new Material.Baked(still, false);
                Material.Baked flowMat = new Material.Baked(flow, false);
                Material.Baked overlayMat = new Material.Baked(overlay, false);

                BlockTintSource tint = new BlockTintSource() {
                    @Override
                    public int color(BlockState state) {
                        return 0xFFFFFF; 
                    }
                };

                this.nostalgia$alphaWaterModel = new FluidModel(original.layer(), stillMat, flowMat, overlayMat, tint);
            }
            return this.nostalgia$alphaWaterModel;
        }

        return original;
    }
}
