package net.nostalgia.alphalogic.bridge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nostalgia.world.gen.AlphaSounds;

public class AlphaAIEngine {
  public AlphaAIEngine() {
  }

  public static void tickActivity(Mob mob) {
    boolean isMonster = mob instanceof Monster;
    boolean isSkeleton = mob instanceof Skeleton;
    boolean isGhastOrSlime = mob instanceof Ghast || mob instanceof Slime;
    if (isGhastOrSlime) {
      return;
    }

    if (isMonster) {
      LivingEntity currentTarget = mob.getTarget();
      Player target = null;
      if (currentTarget instanceof Player p && p.isAlive() && !p.isCreative() && !p.isSpectator() && mob.distanceToSqr(p) < 256.0) {
        target = p;
      } else if (mob.tickCount % 10 == 0 || currentTarget == null) {
        target = mob.level().getNearestPlayer(mob, 16.0);
      }

      if (target != null && target.isAlive() && !target.isCreative() && !target.isSpectator()) {
        mob.setTarget(target);
        double distSq = mob.distanceToSqr(target);

        if (isSkeleton) {
          mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
          double dx = target.getX() - mob.getX();
          double dz = target.getZ() - mob.getZ();
          double dist = Math.sqrt(distSq);

          if (dist > 10.0) {
            if (mob.tickCount % 10 == 0 || mob.getNavigation().isDone()) {
              mob.getNavigation().moveTo(target, 1.0);
            }
          } else if (dist < 4.5) {
            double backX = mob.getX() - (dx / (dist > 0.001 ? dist : 1.0)) * 4.0;
            double backZ = mob.getZ() - (dz / (dist > 0.001 ? dist : 1.0)) * 4.0;
            if (mob.tickCount % 10 == 0 || mob.getNavigation().isDone()) {
              mob.getNavigation().moveTo(backX, mob.getY(), backZ, 1.0);
            }
          } else {
            if (mob.tickCount % 20 == 0 || mob.getNavigation().isDone()) {
              double side = (mob.getId() % 2 == 0 ? 1.0 : -1.0);
              if (mob.getRandom().nextFloat() < 0.25F) {
                side = -side;
              }
              double perpX = -dz / dist * side;
              double perpZ = dx / dist * side;
              double strafeX = mob.getX() + perpX * 3.5;
              double strafeZ = mob.getZ() + perpZ * 3.5;
              mob.getNavigation().moveTo(strafeX, mob.getY(), strafeZ, 1.0);
            }
          }

          if (dist <= 10.0 && mob.level() instanceof ServerLevel sl) {
            if (mob.tickCount % 30 == 0) {
              shootAlphaArrow(mob, target, sl);
            }
          }
        } else {
          mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
          if (mob.tickCount % 10 == 0 || mob.getNavigation().isDone()) {
            mob.getNavigation().moveTo(target, 1.0);
          }
          if (distSq < 4.0 && !(mob instanceof Creeper) && mob.level() instanceof ServerLevel sl) {
            mob.doHurtTarget(sl, target);
          }
        }
      } else {
        wanderAimlessly(mob);
      }
    } else {
      wanderAimlessly(mob);
    }
  }

  private static void shootAlphaArrow(Mob mob, Player target, ServerLevel serverLevel) {
    Arrow arrow = new Arrow(serverLevel, mob, new ItemStack(Items.ARROW), ItemStack.EMPTY);
    arrow.setSoundEvent(AlphaSounds.RANDOM_DRR.value());
    arrow.setPos(mob.getX(), mob.getY() + 1.4, mob.getZ());
    double dx = target.getX() - mob.getX();
    double dy = target.getY(0.3333333333333333) - arrow.getY();
    double dz = target.getZ() - mob.getZ();
    double distanceToTarget = Math.sqrt(dx * dx + dz * dz);

    Projectile.spawnProjectileUsingShoot(
      arrow, serverLevel, new ItemStack(Items.ARROW), dx, dy + distanceToTarget * 0.2, dz, 1.6F, 3.0F
    );
    mob.playSound(AlphaSounds.RANDOM_BOW.value(), 1.0F, 1.0F / (mob.getRandom().nextFloat() * 0.4F + 0.8F));
  }

  private static void wanderAimlessly(Mob mob) {
    if (mob.getRandom().nextInt(60) == 0) {
      double rx = mob.getX() + (mob.getRandom().nextDouble() * 10.0 - 5.0);
      double rz = mob.getZ() + (mob.getRandom().nextDouble() * 10.0 - 5.0);
      double ry = mob.getY();
      mob.getNavigation().moveTo(rx, ry, rz, 0.8);
    }
  }
}
