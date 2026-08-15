package net.nostalgia.world.rules;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.nostalgia.world.gen.AlphaSounds;
import org.jspecify.annotations.Nullable;

public class Alpha112DimensionRules implements DimensionRules {

    @Override
    public boolean isMobAllowed(EntityType<?> type) {
        return type == EntityType.PIG || type == EntityType.SHEEP || type == EntityType.COW || type == EntityType.CHICKEN ||
               type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.SPIDER || type == EntityType.CREEPER;
    }

    @Override
    public @Nullable SoundEvent getRedirectedSound(EntityType<?> type, SoundEvent original) {
        String path = original.location().getPath();
        
        if (type == EntityType.COW) {
            if (path.equals("entity.cow.ambient")) {
                return AlphaSounds.MOB_COW_SAY.value();
            }
            if (path.equals("entity.cow.hurt") || path.equals("entity.cow.death")) {
                return AlphaSounds.MOB_COW_HURT.value();
            }
        } else if (type == EntityType.PIG) {
            if (path.equals("entity.pig.ambient") || path.equals("entity.pig.hurt")) {
                return AlphaSounds.MOB_PIG_SAY.value();
            }
            if (path.equals("entity.pig.death")) {
                return AlphaSounds.MOB_PIG_DEATH.value();
            }
        } else if (type == EntityType.SHEEP) {
            if (path.equals("entity.sheep.ambient") || path.equals("entity.sheep.hurt") || path.equals("entity.sheep.death")) {
                return AlphaSounds.MOB_SHEEP_SAY.value();
            }
        } else if (type == EntityType.CHICKEN) {
            if (path.equals("entity.chicken.ambient")) {
                return AlphaSounds.MOB_CHICKEN_SAY.value();
            }
            if (path.equals("entity.chicken.hurt") || path.equals("entity.chicken.death")) {
                return AlphaSounds.MOB_CHICKEN_HURT.value();
            }
            if (path.equals("entity.chicken.egg")) {
                return AlphaSounds.MOB_CHICKEN_PLOP.value();
            }
        } else if (type == EntityType.ZOMBIE) {
            if (path.equals("entity.zombie.ambient")) {
                return AlphaSounds.MOB_ZOMBIE_SAY.value();
            }
            if (path.equals("entity.zombie.hurt")) {
                return AlphaSounds.MOB_ZOMBIE_HURT.value();
            }
            if (path.equals("entity.zombie.death")) {
                return AlphaSounds.MOB_ZOMBIE_DEATH.value();
            }
        } else if (type == EntityType.SKELETON) {
            if (path.equals("entity.skeleton.ambient")) {
                return AlphaSounds.MOB_SKELETON_SAY.value();
            }
            if (path.equals("entity.skeleton.hurt")) {
                return AlphaSounds.MOB_SKELETON_HURT.value();
            }
            if (path.equals("entity.skeleton.death")) {
                return AlphaSounds.MOB_SKELETON_DEATH.value();
            }
        } else if (type == EntityType.SPIDER) {
            if (path.equals("entity.spider.ambient") || path.equals("entity.spider.hurt")) {
                return AlphaSounds.MOB_SPIDER_SAY.value();
            }
            if (path.equals("entity.spider.death")) {
                return AlphaSounds.MOB_SPIDER_DEATH.value();
            }
        } else if (type == EntityType.CREEPER) {
            if (path.equals("entity.creeper.hurt")) {
                return AlphaSounds.MOB_CREEPER_SAY.value();
            }
            if (path.equals("entity.creeper.death")) {
                return AlphaSounds.MOB_CREEPER_DEATH.value();
            }
        } else if (type == EntityType.PLAYER) {
            if (path.equals("entity.player.hurt") || path.equals("entity.player.death")) {
                return AlphaSounds.RANDOM_HURT.value();
            }
        } else if (type == EntityType.ARROW) {
            if (path.equals("entity.arrow.hit") || path.equals("entity.arrow.hit_player")) {
                return AlphaSounds.RANDOM_DRR.value();
            }
        }
        
        return null;
    }

    @Override
    public @Nullable Identifier getOverriddenTexture(EntityType<?> type, Identifier original) {
        String path = original.getPath();
        if (path.contains("pig")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/pig/pig_alpha.png");
        }
        if (path.contains("cow")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/cow/cow_alpha.png");
        }
        if (path.contains("sheep") && !path.contains("fur")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/sheep/sheep_alpha.png");
        }
        if (path.contains("chicken")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/chicken_alpha.png");
        }
        if (path.contains("zombie")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/zombie/zombie_alpha.png");
        }
        if (path.contains("skeleton")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/skeleton/skeleton_alpha.png");
        }
        if (path.contains("spider")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/spider/spider_alpha.png");
        }
        if (path.contains("creeper")) {
            return Identifier.fromNamespaceAndPath("nostalgia", "textures/entity/creeper/creeper_alpha.png");
        }
        return null;
    }

    @Override
    public boolean showDamageIndicatorParticles() {
        return false;
    }
}
