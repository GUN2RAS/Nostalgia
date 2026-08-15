package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class HologramInteractionHandler {
  public HologramInteractionHandler() {
  }

  public static HologramInteractionHandler.HologramMatch checkHologramInteraction(ServerLevel sourceLevel, BlockPos pos, ServerPlayer player) {
    String currentDim = DimensionUtil.normalize(sourceLevel.dimension().identifier().toString());
    EchoRitualEventInstance echo = RitualEventRegistry.findInstanceForParticipant(player.getUUID());
    if (echo != null) {
      HologramInteractionHandler.HologramMatch match = resolveEchoRitual(sourceLevel, pos, currentDim, echo);
      if (match != null) {
        return match;
      }
    }

    for (SkyPortalEventInstance portal : SkyPortalManager.allPortals()) {
      if (portal != null && portal.isActive()) {
        HologramInteractionHandler.HologramMatch match = resolveSkyPortal(sourceLevel, pos, currentDim, portal);
        if (match != null) {
          return match;
        }
      }
    }

    return null;
  }

  private static HologramInteractionHandler.HologramMatch resolveSkyPortal(
    ServerLevel sourceLevel, BlockPos pos, String currentDim, SkyPortalEventInstance portal
  ) {
    String sourceDim = DimensionUtil.normalize(portal.sourceDimension());
    String targetDim = DimensionUtil.normalize(portal.targetDimension());
    String oppositeDim;
    boolean isOnSourceSide;
    if (currentDim.equals(sourceDim)) {
      oppositeDim = targetDim;
      isOnSourceSide = true;
    } else {
      if (!currentDim.equals(targetDim)) {
        return null;
      }

      oppositeDim = sourceDim;
      isOnSourceSide = false;
    }

    if (!isHologramBlockForPortal(pos, portal, isOnSourceSide)) {
      return null;
    } else if (!isWithinPortalRadius(pos, portal, isOnSourceSide)) {
      return null;
    } else {
      ServerLevel oppositeLevel = sourceLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(oppositeDim)));
      if (oppositeLevel == null) {
        return null;
      } else {
        BlockPos targetPos;
        if (isOnSourceSide) {
          targetPos = CoordinateMapper.forward(portal.geometry(), pos);
        } else {
          targetPos = CoordinateMapper.inverse(portal.geometry(), pos);
        }

        return new HologramInteractionHandler.HologramMatch(oppositeLevel, targetPos);
      }
    }
  }

  private static boolean isHologramBlockForPortal(BlockPos pos, SkyPortalEventInstance portal, boolean isOnSourceSide) {
    int crackPlane = isOnSourceSide ? portal.crackPlaneY() : portal.crackPlaneYTarget();
    return portal.inverted() ? pos.getY() > crackPlane : pos.getY() <= crackPlane;
  }

  private static boolean isWithinPortalRadius(BlockPos pos, SkyPortalEventInstance portal, boolean isOnSourceSide) {
    BlockPos checkPos;
    if (isOnSourceSide) {
      checkPos = pos;
    } else {
      checkPos = CoordinateMapper.inverse(portal.geometry(), pos);
    }

    double dx = checkPos.getX() - portal.center().getX();
    double dz = checkPos.getZ() - portal.center().getZ();
    return dx * dx + dz * dz <= 82944.0;
  }

  private static HologramInteractionHandler.HologramMatch resolveEchoRitual(
    ServerLevel sourceLevel, BlockPos pos, String currentDim, EchoRitualEventInstance echo
  ) {
    String dim1 = echo.dimension() != null ? DimensionUtil.normalize(echo.dimension().identifier().toString()) : currentDim;
    String dim2 = null;
    if (echo.targetDimensionId() != null && !echo.targetDimensionId().isEmpty()) {
      dim2 = DimensionUtil.normalize(echo.targetDimensionId());
    } else if (echo.targetServerLevel() != null) {
      dim2 = DimensionUtil.normalize(echo.targetServerLevel().dimension().identifier().toString());
    }

    if (dim2 == null) {
      return null;
    } else if (!currentDim.equals(dim1) && !currentDim.equals(dim2)) {
      return null;
    } else {
      String oppositeDim = currentDim.equals(dim1) ? dim2 : dim1;
      BlockPos sourcePos;
      if (currentDim.equals(dim1)) {
        sourcePos = pos;
      } else {
        sourcePos = CoordinateMapper.inverse(echo.geometry(), pos);
      }

      if (echo.beaconPos() != null && sourcePos.closerThan(echo.beaconPos(), 288.0)) {
        ServerLevel targetLevel = sourceLevel.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(oppositeDim)));
        if (targetLevel == null) {
          return null;
        } else {
          BlockPos targetPos;
          if (oppositeDim.equals(dim2)) {
            targetPos = CoordinateMapper.forward(echo.geometry(), pos);
          } else {
            targetPos = CoordinateMapper.inverse(echo.geometry(), pos);
          }

          return new HologramInteractionHandler.HologramMatch(targetLevel, targetPos);
        }
      } else {
        return null;
      }
    }
  }

  public static class HologramMatch {
    public final ServerLevel targetLevel;
    public final BlockPos targetPos;

    public HologramMatch(ServerLevel targetLevel, BlockPos targetPos) {
      this.targetLevel = targetLevel;
      this.targetPos = targetPos;
    }
  }
}
