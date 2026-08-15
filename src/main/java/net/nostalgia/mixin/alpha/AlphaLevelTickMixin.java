package net.nostalgia.mixin.alpha;

import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.bridge.AlphaSpawner;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class AlphaLevelTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void invokeAlphaSpawns(java.util.function.BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        
        if (level.dimension().equals(ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            AlphaSpawner.performSpawns(level);
        }
    }
}
