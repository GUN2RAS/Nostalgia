package net.nostalgia.mixin.alpha;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import net.nostalgia.world.dimension.ModDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public abstract class AlphaNaturalSpawnerBypassMixin {

    @Inject(method = "spawnForChunk", at = @At("HEAD"), cancellable = true)
    private static void suppressModernSpawning(ServerLevel level, LevelChunk chunk, NaturalSpawner.SpawnState spawnState, java.util.List<?> categories, CallbackInfo ci) {
        if (level.dimension() == ModDimensions.ALPHA_112_01_LEVEL_KEY) {
            
            ci.cancel();
        }
    }
}
