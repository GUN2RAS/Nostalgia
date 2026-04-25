package net.nostalgia.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.nostalgia.NostalgiaMod;

public class AlphaSounds {
    public static final SoundEvent STEP_GRASS = registerSoundEvent("alpha.step.grass");
    public static final SoundEvent STEP_GRAVEL = registerSoundEvent("alpha.step.gravel");
    public static final SoundEvent STEP_SAND = registerSoundEvent("alpha.step.sand");
    public static final SoundEvent STEP_STONE = registerSoundEvent("alpha.step.stone");
    public static final SoundEvent STEP_WOOD = registerSoundEvent("alpha.step.wood");

    public static final SoundType ALPHA_GRASS_SOUND = new SoundType(1.0f, 1.0f, STEP_GRASS, STEP_GRASS, STEP_GRASS, STEP_GRASS, STEP_GRASS);
    public static final SoundType ALPHA_GRAVEL_SOUND = new SoundType(1.0f, 1.0f, STEP_GRAVEL, STEP_GRAVEL, STEP_GRAVEL, STEP_GRAVEL, STEP_GRAVEL);
    public static final SoundType ALPHA_SAND_SOUND = new SoundType(1.0f, 1.0f, STEP_SAND, STEP_SAND, STEP_SAND, STEP_SAND, STEP_SAND);
    public static final SoundType ALPHA_STONE_SOUND = new SoundType(1.0f, 1.0f, STEP_STONE, STEP_STONE, STEP_STONE, STEP_STONE, STEP_STONE);
    public static final SoundType ALPHA_WOOD_SOUND = new SoundType(1.0f, 1.0f, STEP_WOOD, STEP_WOOD, STEP_WOOD, STEP_WOOD, STEP_WOOD);
    
    public static final SoundEvent RANDOM_BOW = registerSoundEvent("alpha.random.bow");
    public static final SoundEvent RANDOM_POP = registerSoundEvent("alpha.random.pop");
    public static final SoundEvent RANDOM_DOOR_OPEN = registerSoundEvent("alpha.random.door_open");
    public static final SoundEvent RANDOM_DOOR_CLOSE = registerSoundEvent("alpha.random.door_close");
    public static final SoundEvent RANDOM_CHEST_OPEN = registerSoundEvent("alpha.random.chestopen");
    public static final SoundEvent RANDOM_CHEST_CLOSE = registerSoundEvent("alpha.random.chestclosed");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(NostalgiaMod.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
    }
}
