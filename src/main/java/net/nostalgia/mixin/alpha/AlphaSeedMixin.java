package net.nostalgia.mixin.alpha;

import net.minecraft.server.level.ServerLevel;
import net.nostalgia.alphalogic.bridge.AlphaEngineManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class AlphaSeedMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void captureWorldSeed(CallbackInfo ci) {
        AlphaEngineManager.setWorldSeed(((ServerLevel) (Object) this).getSeed());
    }
}
