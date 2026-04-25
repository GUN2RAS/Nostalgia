package net.nostalgia.world.gen;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.nostalgia.NostalgiaMod;
import net.minecraft.world.level.block.SoundType;

public class AlphaSounds {

        public static final Holder<SoundEvent> MOB_CHICKEN_SAY = register("mob.chicken");
        public static final Holder<SoundEvent> MOB_CHICKEN_HURT = register("mob.chickenhurt");
        public static final Holder<SoundEvent> MOB_CHICKEN_PLOP = register("mob.chickenplop");
        public static final Holder<SoundEvent> MOB_COW_SAY = register("mob.cow");
        public static final Holder<SoundEvent> MOB_COW_HURT = register("mob.cowhurt");
        public static final Holder<SoundEvent> MOB_CREEPER_SAY = register("mob.creeper");
        public static final Holder<SoundEvent> MOB_CREEPER_DEATH = register("mob.creeperdeath");
        public static final Holder<SoundEvent> MOB_PIG_SAY = register("mob.pig");
        public static final Holder<SoundEvent> MOB_PIG_DEATH = register("mob.pigdeath");
        public static final Holder<SoundEvent> MOB_SHEEP_SAY = register("mob.sheep");
        public static final Holder<SoundEvent> MOB_SKELETON_SAY = register("mob.skeleton");
        public static final Holder<SoundEvent> MOB_SKELETON_DEATH = register("mob.skeletondeath");
        public static final Holder<SoundEvent> MOB_SKELETON_HURT = register("mob.skeletonhurt");
        public static final Holder<SoundEvent> MOB_SLIME_SAY = register("mob.slime");
        public static final Holder<SoundEvent> MOB_SPIDER_SAY = register("mob.spider");
        public static final Holder<SoundEvent> MOB_SPIDER_DEATH = register("mob.spiderdeath");
        public static final Holder<SoundEvent> MOB_ZOMBIE_SAY = register("mob.zombie");
        public static final Holder<SoundEvent> MOB_ZOMBIE_DEATH = register("mob.zombiedeath");
        public static final Holder<SoundEvent> MOB_ZOMBIE_HURT = register("mob.zombiehurt");

        public static final Holder<SoundEvent> RANDOM_HURT = register("random.hurt");
        public static final Holder<SoundEvent> RANDOM_BOW = register("random.bow");
        public static final Holder<SoundEvent> RANDOM_EXPLODE = register("random.explode");
        public static final Holder<SoundEvent> RANDOM_SPLASH = register("random.splash");
        public static final Holder<SoundEvent> RANDOM_CLICK = register("random.click");
        public static final Holder<SoundEvent> RANDOM_POP = register("random.pop");

        public static final Holder<SoundEvent> STEP_GRASS = register("step.grass");
        public static final Holder<SoundEvent> STEP_GRAVEL = register("step.gravel");
        public static final Holder<SoundEvent> STEP_SAND = register("step.sand");
        public static final Holder<SoundEvent> STEP_SNOW = register("step.snow");
        public static final Holder<SoundEvent> STEP_STONE = register("step.stone");
        public static final Holder<SoundEvent> STEP_WOOD = register("step.wood");
        public static final Holder<SoundEvent> STEP_CLOTH = register("step.cloth");

        public static final SoundType ALPHA_GRASS = new SoundType(1.0F, 1.0F, STEP_GRASS.value(), STEP_GRASS.value(),
                        STEP_GRASS.value(),
                        STEP_GRASS.value(), STEP_GRASS.value());
        public static final SoundType ALPHA_STONE = new SoundType(1.0F, 1.0F, STEP_STONE.value(), STEP_STONE.value(),
                        STEP_STONE.value(),
                        STEP_STONE.value(), STEP_STONE.value());
        public static final SoundType ALPHA_WOOD = new SoundType(1.0F, 1.0F, STEP_WOOD.value(), STEP_WOOD.value(),
                        STEP_WOOD.value(), STEP_WOOD.value(),
                        STEP_WOOD.value());
        public static final SoundType ALPHA_GRAVEL = new SoundType(1.0F, 1.0F, STEP_GRAVEL.value(), STEP_GRAVEL.value(),
                        STEP_GRAVEL.value(),
                        STEP_GRAVEL.value(), STEP_GRAVEL.value());
        public static final SoundType ALPHA_SAND = new SoundType(1.0F, 1.0F, STEP_SAND.value(), STEP_SAND.value(),
                        STEP_SAND.value(), STEP_SAND.value(),
                        STEP_SAND.value());
        public static final SoundType ALPHA_CLOTH = new SoundType(1.0F, 1.0F, STEP_CLOTH.value(), STEP_CLOTH.value(),
                        STEP_CLOTH.value(),
                        STEP_CLOTH.value(), STEP_CLOTH.value());

        private static Holder<SoundEvent> register(String name) {
                Identifier id = Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, name);
                SoundEvent event = SoundEvent.createVariableRangeEvent(id);
                return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, id, event);
        }

        public static void registerSounds() {
        }
}
