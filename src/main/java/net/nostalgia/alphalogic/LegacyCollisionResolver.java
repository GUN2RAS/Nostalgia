package net.nostalgia.alphalogic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LegacyCollisionResolver {

    private static final double EPSILON = 1.0E-7;
    private static final int MAX_ITERATION_DEPTH = 4;

    private final Level level;
    private final AABB entityBounds;

    public LegacyCollisionResolver(Level level, AABB entityBounds) {
        this.level = level;
        this.entityBounds = entityBounds;
    }

    public Vec3 resolveMovement(Vec3 requestedMotion) {
        if (level == null) {
            return requestedMotion;
        }

        double mx = requestedMotion.x;
        double my = requestedMotion.y;
        double mz = requestedMotion.z;

        List<AABB> colliders = gatherNearbyColliders(entityBounds.expandTowards(mx, my, mz));

        if (colliders.isEmpty()) {
            return requestedMotion;
        }

        double adjustedY = my;
        for (AABB box : colliders) {
            adjustedY = clipYCollide(entityBounds, box, adjustedY);
        }

        AABB movedY = entityBounds.move(0.0, adjustedY, 0.0);

        double adjustedX = mx;
        for (AABB box : colliders) {
            adjustedX = clipXCollide(movedY, box, adjustedX);
        }

        AABB movedXY = movedY.move(adjustedX, 0.0, 0.0);

        double adjustedZ = mz;
        for (AABB box : colliders) {
            adjustedZ = clipZCollide(movedXY, box, adjustedZ);
        }

        return new Vec3(adjustedX, adjustedY, adjustedZ);
    }

    private List<AABB> gatherNearbyColliders(AABB searchArea) {
        List<AABB> result = new ArrayList<>();

        int minX = (int) Math.floor(searchArea.minX) - 1;
        int maxX = (int) Math.ceil(searchArea.maxX) + 1;
        int minY = (int) Math.floor(searchArea.minY) - 1;
        int maxY = (int) Math.ceil(searchArea.maxY) + 1;
        int minZ = (int) Math.floor(searchArea.minZ) - 1;
        int maxZ = (int) Math.ceil(searchArea.maxZ) + 1;

        for (int bx = minX; bx < maxX; bx++) {
            for (int by = minY; by < maxY; by++) {
                for (int bz = minZ; bz < maxZ; bz++) {
                    BlockPos pos = new BlockPos(bx, by, bz);
                    BlockState state = level.getBlockState(pos);

                    if (!state.isAir() && state.canOcclude()) {
                        double x0 = bx;
                        double y0 = by;
                        double z0 = bz;
                        result.add(new AABB(x0, y0, z0, x0 + 1.0, y0 + 1.0, z0 + 1.0));
                    }
                }
            }
        }

        return result;
    }

    private double clipYCollide(AABB entity, AABB block, double motion) {
        if (entity.maxX > block.minX && entity.minX < block.maxX
                && entity.maxZ > block.minZ && entity.minZ < block.maxZ) {
            if (motion > 0.0 && entity.maxY <= block.minY + EPSILON) {
                double gap = block.minY - entity.maxY;
                if (gap < motion) {
                    motion = gap;
                }
            } else if (motion < 0.0 && entity.minY >= block.maxY - EPSILON) {
                double gap = block.maxY - entity.minY;
                if (gap > motion) {
                    motion = gap;
                }
            }
        }
        return motion;
    }

    private double clipXCollide(AABB entity, AABB block, double motion) {
        if (entity.maxY > block.minY && entity.minY < block.maxY
                && entity.maxZ > block.minZ && entity.minZ < block.maxZ) {
            if (motion > 0.0 && entity.maxX <= block.minX + EPSILON) {
                double gap = block.minX - entity.maxX;
                if (gap < motion) {
                    motion = gap;
                }
            } else if (motion < 0.0 && entity.minX >= block.maxX - EPSILON) {
                double gap = block.maxX - entity.minX;
                if (gap > motion) {
                    motion = gap;
                }
            }
        }
        return motion;
    }

    private double clipZCollide(AABB entity, AABB block, double motion) {
        if (entity.maxX > block.minX && entity.minX < block.maxX
                && entity.maxY > block.minY && entity.minY < block.maxY) {
            if (motion > 0.0 && entity.maxZ <= block.minZ + EPSILON) {
                double gap = block.minZ - entity.maxZ;
                if (gap < motion) {
                    motion = gap;
                }
            } else if (motion < 0.0 && entity.minZ >= block.maxZ - EPSILON) {
                double gap = block.maxZ - entity.minZ;
                if (gap > motion) {
                    motion = gap;
                }
            }
        }
        return motion;
    }
}
