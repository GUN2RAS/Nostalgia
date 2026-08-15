package net.nostalgia.mixin.alpha;

import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class AlphaServerValidationBypassMixin {

    @Inject(method = "isSingleplayerOwner", at = @At("HEAD"), cancellable = true)
    private void alwaysTrustClientInAlpha(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof ServerGamePacketListenerImpl gameListener) {
            ServerPlayer player = gameListener.player;
            if (player != null && (
                net.nostalgia.alphalogic.ritual.DimensionUtil.isClientGenerated(player.level().dimension().identifier().toString()) ||
                net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipantAny(player.getUUID()) ||
                net.nostalgia.alphalogic.ritual.SkyPortalManager.isAnyActive()
            )) {
                cir.setReturnValue(true);
            }
        }
    }
}
