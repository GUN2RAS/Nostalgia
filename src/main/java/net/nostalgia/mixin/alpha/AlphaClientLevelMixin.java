package net.nostalgia.mixin.alpha;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.gamerules.GameRules;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class AlphaClientLevelMixin {

    @org.spongepowered.asm.mixin.Shadow
    private boolean tickDayTime;

    @org.spongepowered.asm.mixin.Shadow
    private ClientLevel.ClientLevelData clientLevelData;

    @Inject(method = "tickTime", at = @At("TAIL"))
    private void forceAlphaTime(CallbackInfo ci) {
        ClientLevel level = (ClientLevel) (Object) this;
        if (level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            if (!this.tickDayTime) {
                
            }
        }
    }
}
