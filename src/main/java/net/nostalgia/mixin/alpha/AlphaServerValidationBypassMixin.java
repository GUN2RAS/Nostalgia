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
            if (player != null && (player.level().dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY || net.nostalgia.command.ModCommands.portalDebugState)) {
                cir.setReturnValue(true);
            }
        }
    }
}
