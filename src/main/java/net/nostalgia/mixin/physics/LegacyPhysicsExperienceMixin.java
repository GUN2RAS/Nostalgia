package net.nostalgia.mixin.physics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public class LegacyPhysicsExperienceMixin {

    @Inject(method = "award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V", at = @At("HEAD"), cancellable = true)
    private static void cancelAward(ServerLevel level, Vec3 pos, int amount, CallbackInfo ci) {
        if (level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "awardWithDirection(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;I)V", at = @At("HEAD"), cancellable = true)
    private static void cancelAwardDirection(ServerLevel level, Vec3 pos, Vec3 dir, int amount, CallbackInfo ci) {
        if (level.dimension().equals(net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY)) {
            ci.cancel();
        }
    }
}
