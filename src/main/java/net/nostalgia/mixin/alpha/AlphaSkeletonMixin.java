package net.nostalgia.mixin.alpha;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.nostalgia.world.gen.AlphaSounds;
import net.nostalgia.world.rules.NostalgiaRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSkeleton.class)
public abstract class AlphaSkeletonMixin extends Monster implements RangedAttackMob {
    @Shadow @Final private MeleeAttackGoal meleeGoal;
    @Shadow @Final private RangedBowAttackGoal<AbstractSkeleton> bowGoal;

    protected AlphaSkeletonMixin() {
        super(null, null);
    }

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void nostalgia$clearSkeletonBow(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        if (this.level() != null && NostalgiaRules.getForLevel(this.level()).legacySkeleton) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    private void nostalgia$clearFinalizeBow(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (NostalgiaRules.getForLevel(this.level()).legacySkeleton) {
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Inject(method = "reassessWeaponGoal", at = @At("HEAD"), cancellable = true)
    private void nostalgia$setLegacyRangedGoal(CallbackInfo ci) {
        if (this.level() != null && !this.level().isClientSide() && NostalgiaRules.getForLevel(this.level()).legacySkeleton) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ci.cancel();
        }
    }

    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void nostalgia$performLegacyRangedAttack(LivingEntity target, float power, CallbackInfo ci) {
        if (NostalgiaRules.getForLevel(this.level()).legacySkeleton) {
            AbstractArrow arrow = new Arrow(this.level(), this, new ItemStack(Items.ARROW), null);
            arrow.setSoundEvent(AlphaSounds.RANDOM_DRR.value());
            double xd = target.getX() - this.getX();
            double yd = target.getY(0.3333333333333333) - arrow.getY();
            double zd = target.getZ() - this.getZ();
            double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
            if (this.level() instanceof ServerLevel serverLevel) {
                Projectile.spawnProjectileUsingShoot(
                    arrow, serverLevel, new ItemStack(Items.ARROW), xd, yd + distanceToTarget * 0.2, zd, 1.6F, 14 - serverLevel.getDifficulty().getId() * 4
                );
            }
            this.playSound(AlphaSounds.RANDOM_BOW.value(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            ci.cancel();
        }
    }
}
