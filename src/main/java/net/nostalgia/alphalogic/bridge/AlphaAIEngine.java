package net.nostalgia.alphalogic.bridge;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class AlphaAIEngine {

    public static void tickActivity(Mob mob) {
        boolean isMonster = mob instanceof net.minecraft.world.entity.monster.Monster;
        boolean isSkeleton = mob instanceof net.minecraft.world.entity.monster.skeleton.Skeleton;
        boolean isGhastOrSlime = mob instanceof net.minecraft.world.entity.monster.Ghast || mob instanceof net.minecraft.world.entity.monster.Slime;

        if (isGhastOrSlime) return;

        if (isMonster) {
            Player target = mob.level().getNearestPlayer(mob, 16.0D);
            if (target != null) {
                
                if (!isSkeleton) {
                    mob.getNavigation().moveTo(target, 1.0D);
                }
                mob.setTarget(target);
                
                if (mob.distanceToSqr(target) < 4.0D) {
                    if (!(mob instanceof net.minecraft.world.entity.monster.Creeper) && !isSkeleton) {
                        if (mob.level() instanceof net.minecraft.server.level.ServerLevel sl) {
                            mob.doHurtTarget(sl, target);
                        }
                    }
                }
            } else {
                if (!isSkeleton) wanderAimlessly(mob);
            }
        } else {
            wanderAimlessly(mob);
        }
    }

    private static void wanderAimlessly(Mob mob) {
        if (mob.getRandom().nextInt(60) == 0) {
            double rx = mob.getX() + (mob.getRandom().nextDouble() * 10.0D - 5.0D);
            double rz = mob.getZ() + (mob.getRandom().nextDouble() * 10.0D - 5.0D);
            double ry = mob.getY();

            mob.getNavigation().moveTo(rx, ry, rz, 0.8D);
        }
    }
}
