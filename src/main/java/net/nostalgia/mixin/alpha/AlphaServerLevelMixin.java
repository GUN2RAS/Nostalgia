package net.nostalgia.mixin.alpha;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRules;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class AlphaServerLevelMixin {

    @Inject(method = "tickTime", at = @At("TAIL"))
    private void forceAlphaTime(CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        
        if (level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            if (!level.getGameRules().get(GameRules.ADVANCE_TIME)) {

            }
        }
    }
}
