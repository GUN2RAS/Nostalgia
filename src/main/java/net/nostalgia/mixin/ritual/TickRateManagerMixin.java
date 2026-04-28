package net.nostalgia.mixin.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.nostalgia.alphalogic.ritual.FreezeRegion;
import net.nostalgia.alphalogic.ritual.RitualActiveState;
import net.nostalgia.alphalogic.ritual.TickRateManagerAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(TickRateManager.class)
public abstract class TickRateManagerMixin implements TickRateManagerAccess {

    @Unique
    private final CopyOnWriteArrayList<FreezeRegion> nostalgia$regions = new CopyOnWriteArrayList<>();

    @Override @Unique
    public void nostalgia$addRegion(FreezeRegion region) {
        if (region == null) return;
        nostalgia$regions.removeIf(r -> r.dimension().equals(region.dimension()) && r.beaconPos().equals(region.beaconPos()));
        nostalgia$regions.add(region);
    }

    @Override @Unique
    public void nostalgia$removeRegionAt(ResourceKey<Level> dim, BlockPos beaconPos) {
        if (dim == null || beaconPos == null) return;
        nostalgia$regions.removeIf(r -> r.dimension().equals(dim) && r.beaconPos().equals(beaconPos));
    }

    @Override @Unique
    public void nostalgia$clearRegions() {
        nostalgia$regions.clear();
    }

    @Override @Unique
    public List<FreezeRegion> nostalgia$regions() {
        return Collections.unmodifiableList(nostalgia$regions);
    }

    @Override @Unique
    public boolean nostalgia$hasRegions() {
        return !nostalgia$regions.isEmpty();
    }

    @Override @Unique
    public boolean nostalgia$isChunkFrozen(ResourceKey<Level> dim, long chunkKey) {
        if (dim == null) return false;
        for (FreezeRegion r : nostalgia$regions) {
            if (r.containsChunk(dim, chunkKey)) return true;
        }
        return false;
    }

    @Override @Unique
    public boolean nostalgia$isChunkFrozen(ResourceKey<Level> dim, ChunkPos chunkPos) {
        if (dim == null || chunkPos == null) return false;
        for (FreezeRegion r : nostalgia$regions) {
            if (r.containsChunk(dim, chunkPos)) return true;
        }
        return false;
    }

    @Override @Unique
    public boolean nostalgia$isBlockFrozen(ResourceKey<Level> dim, BlockPos pos) {
        if (dim == null || pos == null) return false;
        for (FreezeRegion r : nostalgia$regions) {
            if (r.containsBlock(dim, pos)) return true;
        }
        return false;
    }

    @Inject(method = "isEntityFrozen", at = @At("HEAD"), cancellable = true)
    private void nostalgia$entityFreeze(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Display || entity instanceof Interaction) {
            cir.setReturnValue(false);
            return;
        }
        if (entity instanceof Player) return;
        if (entity.countPlayerPassengers() > 0) return;
        if (net.nostalgia.alphalogic.ritual.event.RitualEventRegistry.isParticipant(entity)) return;
        if (entity.level() == null) return;
        if (nostalgia$isChunkFrozen(entity.level().dimension(), entity.chunkPosition())) {
            cir.setReturnValue(true);
        }
    }
}
