package net.nostalgia.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.nostalgia.alphalogic.ritual.RitualManager;
import net.nostalgia.client.render.FPVTrailManager;
import net.nostalgia.client.render.GlowNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.util.Mth;

@Mixin(ItemInHandRenderer.class)
public class FPVHandRendererMixin {

    @WrapOperation(
            method = "renderHandsWithItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V"
            )
    )
    private void renderWithTrailsAndGlow(
            ItemInHandRenderer instance,
            AbstractClientPlayer player,
            float frameInterp,
            float xRot,
            InteractionHand hand,
            float attack,
            ItemStack itemStack,
            float inverseArmHeight,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            Operation<Void> original
    ) {
        
        original.call(instance, player, frameInterp, xRot, hand, attack, itemStack, inverseArmHeight, poseStack, submitNodeCollector, lightCoords);

        if (player.level() == null || !itemStack.isEmpty()) return;

        RitualManager.ActiveZone zone = RitualManager.findZoneContaining(player.level().dimension(), player.blockPosition());
        if (zone == null) return;

        int bx = zone.beaconPos().getX() >> 4;
        int bz = zone.beaconPos().getZ() >> 4;
        double minX = (bx - zone.radiusChunks()) * 16.0;
        double maxX = (bx + zone.radiusChunks() + 1) * 16.0;
        double minZ = (bz - zone.radiusChunks()) * 16.0;
        double maxZ = (bz + zone.radiusChunks() + 1) * 16.0;

        double dxMin = player.getX() - minX;
        double dxMax = maxX - player.getX();
        double dzMin = player.getZ() - minZ;
        double dzMax = maxZ - player.getZ();

        double dist = Math.min(Math.min(dxMin, dxMax), Math.min(dzMin, dzMax));
        float alpha = (float) Mth.clamp(dist / 2.0, 0.0, 1.0);
        if (alpha <= 0.01f) return;

        
        if (attack > 0.0f && attack < 1.0f) {
            FPVTrailManager.isRenderingTrail = true;
            for (int i = 1; i <= 4; i++) {
                float trailAttack = attack - (i * 0.12f);
                if (trailAttack > 0.0f) {
                    float trailAlpha = alpha * (0.5f - (i * 0.1f));
                    if (trailAlpha > 0.0f) {
                        GlowNodeCollector trailCollector = new GlowNodeCollector(submitNodeCollector, trailAlpha);
                        original.call(instance, player, frameInterp, xRot, hand, trailAttack, itemStack, inverseArmHeight, poseStack, trailCollector, lightCoords);
                    }
                }
            }
            FPVTrailManager.isRenderingTrail = false;
        }
    }
}
