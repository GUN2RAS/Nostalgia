package net.nostalgia.alphalogic.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.nostalgia.alphalogic.ritual.event.RitualEventRegistry;

public class HologramInteractionHandler {

    public static class HologramMatch {
        public final ServerLevel targetLevel;
        public final BlockPos targetPos;

        public HologramMatch(ServerLevel targetLevel, BlockPos targetPos) {
            this.targetLevel = targetLevel;
            this.targetPos = targetPos;
        }
    }

    public static HologramMatch checkHologramInteraction(ServerLevel sourceLevel, BlockPos pos, ServerPlayer player) {
        String currentDim = DimensionUtil.normalize(sourceLevel.dimension().identifier().toString());

        SkyPortalEventInstance portal = SkyPortalManager.getActive();
        if (portal != null && portal.isActive()) {
            HologramMatch match = resolveSkyPortal(sourceLevel, pos, currentDim, portal);
            if (match != null) return match;
        }

        EchoRitualEventInstance echo = RitualEventRegistry.findInstanceForParticipant(player.getUUID());
        if (echo != null) {
            HologramMatch match = resolveEchoRitual(sourceLevel, pos, currentDim, echo);
            if (match != null) return match;
        }

        return null;
    }

    private static HologramMatch resolveSkyPortal(ServerLevel sourceLevel, BlockPos pos, String currentDim, SkyPortalEventInstance portal) {
        String sourceDim = DimensionUtil.normalize(portal.sourceDimension());
        String targetDim = DimensionUtil.normalize(portal.targetDimension());

        String oppositeDim;
        boolean isOnSourceSide;
        if (currentDim.equals(sourceDim)) {
            oppositeDim = targetDim;
            isOnSourceSide = true;
        } else if (currentDim.equals(targetDim)) {
            oppositeDim = sourceDim;
            isOnSourceSide = false;
        } else {
            return null;
        }

        if (!isHologramBlockForPortal(pos, portal, isOnSourceSide)) {
            return null;
        }

        if (!isWithinPortalRadius(pos, portal, isOnSourceSide)) {
            return null;
        }

        ServerLevel oppositeLevel = sourceLevel.getServer().getLevel(
            ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(oppositeDim)));
        if (oppositeLevel == null) return null;

        int maxY = isOnSourceSide ? sourceLevel.getMaxY() : oppositeLevel.getMaxY();
        BlockPos targetPos;
        if (isOnSourceSide) {
            targetPos = CoordinateMapper.sourceToTarget(pos, 0, 0, 0, portal.inverted(), maxY);
        } else {
            targetPos = CoordinateMapper.targetToSource(pos, 0, 0, 0, portal.inverted(), maxY);
        }

        return new HologramMatch(oppositeLevel, targetPos);
    }

    private static boolean isHologramBlockForPortal(BlockPos pos, SkyPortalEventInstance portal, boolean isOnSourceSide) {
        int crackPlane = isOnSourceSide ? portal.crackPlaneY() : portal.crackPlaneYTarget();
        if (portal.inverted()) {
            return pos.getY() > crackPlane;
        } else {
            return pos.getY() <= crackPlane;
        }
    }

    private static boolean isWithinPortalRadius(BlockPos pos, SkyPortalEventInstance portal, boolean isOnSourceSide) {
        BlockPos checkPos;
        if (isOnSourceSide) {
            checkPos = pos;
        } else {
            ServerLevel oppositeLevel = null;
            checkPos = CoordinateMapper.targetToSource(pos, 0, 0, 0, portal.inverted(),
                portal.crackPlaneY() + portal.crackPlaneYTarget());
        }
        double dx = checkPos.getX() - portal.center().getX();
        double dz = checkPos.getZ() - portal.center().getZ();
        return dx * dx + dz * dz <= 288.0 * 288.0;
    }

    private static HologramMatch resolveEchoRitual(ServerLevel sourceLevel, BlockPos pos, String currentDim, EchoRitualEventInstance echo) {
        String dim1 = echo.dimension() != null
            ? DimensionUtil.normalize(echo.dimension().identifier().toString())
            : currentDim;
        String dim2 = null;
        if (echo.targetDimensionId() != null && !echo.targetDimensionId().isEmpty()) {
            dim2 = DimensionUtil.normalize(echo.targetDimensionId());
        } else if (echo.targetServerLevel() != null) {
            dim2 = DimensionUtil.normalize(echo.targetServerLevel().dimension().identifier().toString());
        }
        if (dim2 == null) return null;

        if (!currentDim.equals(dim1) && !currentDim.equals(dim2)) return null;

        String oppositeDim = currentDim.equals(dim1) ? dim2 : dim1;

        ServerLevel dim1Level = sourceLevel.getServer().getLevel(
            ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(dim1)));
        int dim1MaxY = dim1Level != null ? dim1Level.getMaxY() : 320;

        BlockPos sourcePos;
        if (currentDim.equals(dim1)) {
            sourcePos = pos;
        } else {
            sourcePos = CoordinateMapper.targetToSource(pos, echo.offsetX(), echo.yOffset(), echo.offsetZ(), false, dim1MaxY);
        }

        if (echo.beaconPos() == null || !sourcePos.closerThan(echo.beaconPos(), 288.0)) {
            return null;
        }

        ServerLevel targetLevel = sourceLevel.getServer().getLevel(
            ResourceKey.create(Registries.DIMENSION, Identifier.tryParse(oppositeDim)));
        if (targetLevel == null) return null;

        BlockPos targetPos;
        if (oppositeDim.equals(dim2)) {
            targetPos = CoordinateMapper.sourceToTarget(pos, echo.offsetX(), echo.yOffset(), echo.offsetZ(), false, dim1MaxY);
        } else {
            targetPos = CoordinateMapper.targetToSource(pos, echo.offsetX(), echo.yOffset(), echo.offsetZ(), false, dim1MaxY);
        }

        return new HologramMatch(targetLevel, targetPos);
    }
}
