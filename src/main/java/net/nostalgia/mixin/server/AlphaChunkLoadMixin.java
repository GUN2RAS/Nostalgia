package net.nostalgia.mixin.server;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public class AlphaChunkLoadMixin {

    @Inject(method = "setLoaded", at = @At("RETURN"))
    private void onChunkLoaded(boolean loaded, CallbackInfo ci) {
        if (!loaded) return;
        LevelChunk chunk = (LevelChunk) (Object) this;
        if (!(chunk.getLevel() instanceof ServerLevel sl)) return;
        if (sl.dimension() != net.nostalgia.world.dimension.ModDimensions.ALPHA_112_01_LEVEL_KEY) return;

        net.nostalgia.alphalogic.ritual.HologramWorldData data = net.nostalgia.alphalogic.ritual.HologramWorldData.get(sl);
        int chunkMinX = chunk.getPos().getMinBlockX();
        int chunkMinZ = chunk.getPos().getMinBlockZ();
        int chunkMaxX = chunkMinX + 15;
        int chunkMaxZ = chunkMinZ + 15;

        for (it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry<BlockState> entry : data.deltas.long2ObjectEntrySet()) {
            BlockPos pos = BlockPos.of(entry.getLongKey());
            if (pos.getX() >= chunkMinX && pos.getX() <= chunkMaxX && pos.getZ() >= chunkMinZ && pos.getZ() <= chunkMaxZ) {
                if (pos.getY() < sl.getMinY() || pos.getY() >= sl.getMinY() + sl.dimensionType().height()) continue;
                BlockState currentState = chunk.getBlockState(pos);
                if (currentState != entry.getValue()) {
                    chunk.setBlockState(pos, entry.getValue(), 0);
                }
            }
        }
    }
}
