package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PanicGoal.class)
public abstract class LegacyPhysicsPanicGoalMixin {

    @Shadow
    protected PathfinderMob mob;

    @Inject(method = "shouldPanic", at = @At("HEAD"), cancellable = true)
    private void cancelPanicInAlpha(CallbackInfoReturnable<Boolean> cir) {
        if (this.mob.level() instanceof net.minecraft.server.level.ServerLevel sl) {
            if (sl.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
                cir.setReturnValue(false);
            }
        }
    }
}
