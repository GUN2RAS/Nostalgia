package net.nostalgia.mixin.alpha;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.nostalgia.world.rules.DimensionRules;
import net.nostalgia.world.rules.NostalgiaDimensionRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class AlphaDamageIndicatorMixin {

    @Inject(method = "damageStatsAndHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"), cancellable = true)
    private void nostalgia$suppressDamageIndicator(Entity entity, float oldHealth, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        DimensionRules rules = NostalgiaDimensionRules.getRules(player.level());
        if (rules != null && !rules.showDamageIndicatorParticles()) {
            ci.cancel();
        }
    }
}
