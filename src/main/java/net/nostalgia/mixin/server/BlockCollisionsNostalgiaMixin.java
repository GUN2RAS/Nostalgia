package net.nostalgia.mixin.server;

import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockCollisions.class)
public class BlockCollisionsNostalgiaMixin {
    @Shadow @Final private CollisionGetter collisionGetter;

    @Inject(method = "computeNext", at = @At("HEAD"))
    private void onComputeNextHead(CallbackInfoReturnable<Object> cir) {
        if (this.collisionGetter instanceof net.minecraft.world.level.Level level) {
            net.nostalgia.alphalogic.ritual.event.EchoRitualEvent t = net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.activeTransition();
            boolean isRitualDim = (t != null && level.dimension() == t.dimension());
            net.nostalgia.alphalogic.ritual.NostalgiaServerCollisionBypassProvider.IS_OVERWORLD.set(isRitualDim);
        }
    }

    @Inject(method = "computeNext", at = @At("RETURN"))
    private void onComputeNextReturn(CallbackInfoReturnable<Object> cir) {
        NostalgiaServerCollisionBypassProvider.IS_OVERWORLD.remove();
    }
}
