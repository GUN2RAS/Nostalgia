package net.nostalgia.mixin.client.ritual;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.renderer.entity.EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private <E extends Entity> void isolationDuringRitual(E entity, net.minecraft.client.renderer.culling.Frustum frustum, double x, double y, double z, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Boolean> cir) {
        net.nostalgia.alphalogic.ritual.event.ClientTransitionView t = net.nostalgia.client.ritual.ClientRitualEventRegistry.activeTransition();
        if (!net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.participants().isEmpty() &&
            t != null && t.currentPhase() >= 2 &&
            (t.visualTime() - t.phase2StartTime() >= 1000)) {

            if (entity instanceof net.minecraft.world.entity.item.ItemEntity) return;

            Entity cameraEntity = net.minecraft.client.Minecraft.getInstance().getCameraEntity();
            if (cameraEntity != null) {
                boolean cameraIsParticipant = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipant(cameraEntity);
                boolean entityIsParticipant = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipant(entity);
                if (cameraIsParticipant != entityIsParticipant) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
