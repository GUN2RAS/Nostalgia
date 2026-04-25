package net.nostalgia.mixin.alpha;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class AlphaMobLootMixin {

    @Inject(method = "dropFromLootTable", at = @At("HEAD"), cancellable = true)
    private void injectAlphaLoot(ServerLevel level, DamageSource damageSource, boolean hitByPlayer, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        if (level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            
            ci.cancel();

            int amount = entity.getRandom().nextInt(3); 
            
            if (entity instanceof Zombie) {
                if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.FEATHER, amount));
            } else if (entity instanceof Skeleton) {
                if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.ARROW, amount));
                int bones = entity.getRandom().nextInt(3);
                if (bones > 0) entity.spawnAtLocation(level, new ItemStack(Items.BONE, bones));
            } else if (entity instanceof Creeper) {
                if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.GUNPOWDER, amount));
            } else if (entity instanceof Spider) {
                if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.STRING, amount));
            } else if (entity instanceof Pig) {
                if (amount > 0) {
                    if (entity.isOnFire()) {
                        entity.spawnAtLocation(level, new ItemStack(Items.COOKED_PORKCHOP, amount));
                    } else {
                        entity.spawnAtLocation(level, new ItemStack(Items.PORKCHOP, amount));
                    }
                }
            } else if (entity instanceof Cow) {
                if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.LEATHER, amount));
            } else if (entity instanceof Chicken) {
                if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.FEATHER, amount));
            } else if (entity instanceof Slime slime) {
                if (slime.getSize() == 1) {
                    if (amount > 0) entity.spawnAtLocation(level, new ItemStack(Items.SLIME_BALL, amount));
                }
            } else if (entity instanceof Sheep sheep) {
                if (!sheep.isSheared()) {
                    entity.spawnAtLocation(level, new ItemStack(Items.WHITE_WOOL, 1));
                }
            }
        }
    }
}
