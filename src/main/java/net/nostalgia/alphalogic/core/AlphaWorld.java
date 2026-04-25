package net.nostalgia.alphalogic.core;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.nostalgia.block.AlphaBlocks;

import java.util.ArrayList;
import java.util.List;

public class AlphaWorld {
    public final Level mcLevel;

    public List<AlphaEntity> loadedEntities = new ArrayList<>();

    public AlphaWorld(Level level) {
        this.mcLevel = level;
    }

    public int getBlockId(int x, int y, int z) {
        if (y < mcLevel.getMinY() || y >= mcLevel.getMaxY()) {
            return 0; 
        }
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = mcLevel.getBlockState(pos);

        if (state.isAir()) return 0;
        if (state.is(Blocks.WATER)) return 8;
        if (state.is(Blocks.LAVA)) return 10;
        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) return 50;
        if (state.is(Blocks.FIRE)) return 51;

        if (state.is(AlphaBlocks.ALPHA_CACTUS) || state.is(Blocks.CACTUS)) return 81;

        if (state.getCollisionShape(mcLevel, pos).isEmpty()) return 0;

        return 1;
    }

    public void setBlockId(int x, int y, int z, int id) {
        
    }

    public List<AlphaAABB> getCollidingBoundingBoxes(AlphaEntity entity, AlphaAABB aabb) {
        List<AlphaAABB> list = new ArrayList<>();
        
        int minX = AlphaMathHelper.floor(aabb.minX);
        int maxX = AlphaMathHelper.floor(aabb.maxX + 1.0);
        int minY = AlphaMathHelper.floor(aabb.minY);
        int maxY = AlphaMathHelper.floor(aabb.maxY + 1.0);
        int minZ = AlphaMathHelper.floor(aabb.minZ);
        int maxZ = AlphaMathHelper.floor(aabb.maxZ + 1.0);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY - 1; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    int blockId = this.getBlockId(x, y, z);
                    if (blockId > 0) {
                        
                        if (blockId != 8 && blockId != 9 && blockId != 10 && blockId != 11 && blockId != 50 && blockId != 51) {
                            if (blockId == 81) {
                                float f = 0.0625f; 
                                list.add(AlphaAABB.create(x + f, y, z + f, x + 1.0 - f, y + 1.0 - f, z + 1.0 - f));
                            } else {
                                list.add(AlphaAABB.create(x, y, z, x + 1.0, y + 1.0, z + 1.0));
                            }
                        }
                    }
                }
            }
        }

        net.minecraft.world.phys.AABB mcBox = new net.minecraft.world.phys.AABB(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
        List<net.minecraft.world.entity.Entity> modernEntities = mcLevel.getEntities((net.minecraft.world.entity.Entity) null, mcBox, e -> {
            return e.canBeCollidedWith((net.minecraft.world.entity.Entity) null) || e.isPushable() || e instanceof net.nostalgia.entity.AlphaBoatEntity;
        });

        for (net.minecraft.world.entity.Entity modernEntity : modernEntities) {
            if (modernEntity instanceof net.minecraft.world.entity.player.Player) continue;
            net.minecraft.world.phys.AABB boundingBox = modernEntity.getBoundingBox();
            list.add(AlphaAABB.create(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ));
        }

        return list;
    }

    public void spawnEntityInWorld(AlphaEntity entity) {
        this.loadedEntities.add(entity);
    }

    public void tickUnits() {
        for (int i = 0; i < loadedEntities.size(); i++) {
            AlphaEntity e = loadedEntities.get(i);
            if (!e.isDead) {
                e.onUpdate();
            } else {
                loadedEntities.remove(i--);
            }
        }
    }
}
