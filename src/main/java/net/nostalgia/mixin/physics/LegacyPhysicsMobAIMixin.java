package net.nostalgia.mixin.physics;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.PathType;
import net.nostalgia.alphalogic.bridge.AlphaAIEngine;
import net.nostalgia.world.rules.NostalgiaRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class LegacyPhysicsMobAIMixin {

    @Shadow
    public net.minecraft.world.entity.ai.goal.GoalSelector goalSelector;

    @Shadow
    public abstract void setPathfindingMalus(PathType type, float malus);

    @Unique
    private boolean nostalgia$aiInitialized = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectAlphaAI(CallbackInfo ci) {
        Mob self = (Mob)(Object)this;
        if (self.level() != null && !self.level().isClientSide() && NostalgiaRules.getForLevel(self.level()).legacyMobAI) {
            if (!this.nostalgia$aiInitialized) {
                this.goalSelector.removeAllGoals(goal -> {
                    String name = goal.getClass().getSimpleName();
                    return !name.equals("SwellGoal") &&
                           !name.equals("RangedBowAttackGoal") &&
                           !name.equals("LeapAtTargetGoal") &&
                           !name.equals("FloatGoal") &&
                           !name.contains("Ghast") && 
                           !name.contains("Slime");
                });

                this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 0.0F);
                this.setPathfindingMalus(PathType.FIRE, 0.0F);
                this.setPathfindingMalus(PathType.LAVA, 0.0F);
                this.setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 0.0F);
                this.setPathfindingMalus(PathType.DAMAGING, 0.0F);

                this.nostalgia$aiInitialized = true;
            }

            AlphaAIEngine.tickActivity(self);
        }
    }
}
