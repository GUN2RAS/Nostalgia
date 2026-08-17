package net.nostalgia.client.render;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.nostalgia.client.events.caches.UniversalHologramCache;
import net.nostalgia.client.events.core.IHologramContext;

public final class HologramMeshTransformBridge {

  private HologramMeshTransformBridge() {}

  public static Long getTransformedSeed(BlockPos pos) {
    if (!UniversalHologramCache.INSTANCE.isActive()) {
      return null;
    }

    int worldX = pos.getX();
    int y = pos.getY();
    int worldZ = pos.getZ();

    for (IHologramContext ctx : UniversalHologramCache.ACTIVE_CONTEXTS) {
      if (ctx.isActive() && ctx.isTerrainActive() && !ctx.isSkyInverted() && ctx.contains(worldX, y, worldZ)) {
        int sourceX = worldX + ctx.getOffsetX();
        int sourceZ = worldZ + ctx.getOffsetZ();
        int sourceY = y - ctx.getOffsetY();
        return Mth.getSeed(sourceX, sourceY, sourceZ);
      }
    }

    return null;
  }

  public static Vec3 getTransformedOffset(BlockState state, BlockPos pos, BlockBehaviour.OffsetFunction function) {
    if (!UniversalHologramCache.INSTANCE.isActive() || function == null) {
      return null;
    }

    int worldX = pos.getX();
    int y = pos.getY();
    int worldZ = pos.getZ();

    for (IHologramContext ctx : UniversalHologramCache.ACTIVE_CONTEXTS) {
      if (ctx.isActive() && ctx.isTerrainActive() && !ctx.isSkyInverted() && ctx.contains(worldX, y, worldZ)) {
        int sourceX = worldX + ctx.getOffsetX();
        int sourceZ = worldZ + ctx.getOffsetZ();
        int sourceY = y - ctx.getOffsetY();
        return function.evaluate(state, new BlockPos(sourceX, sourceY, sourceZ));
      }
    }

    return null;
  }
}
