package net.nostalgia.mixin.client.ritual;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Level.class)
public interface LevelRainFieldAccessor {
    @Accessor("rainLevel") float nostalgia$getRainLevelField();
    @Accessor("thunderLevel") float nostalgia$getThunderLevelField();
}
