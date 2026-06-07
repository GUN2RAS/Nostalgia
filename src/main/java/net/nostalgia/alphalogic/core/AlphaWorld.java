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
        if (state.is(Blocks.COBWEB)) return 30;
        if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) return 50;
        if (state.is(Blocks.FIRE)) return 51;
        if (state.is(AlphaBlocks.ALPHA_ICE) || state.is(Blocks.ICE)) return 79;

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
        this.pushEntities();
    }

    public void pushEntities() {
        for (int i = 0; i < loadedEntities.size(); i++) {
            AlphaEntity e1 = loadedEntities.get(i);
            if (e1.isDead) continue;
            for (int j = i + 1; j < loadedEntities.size(); j++) {
                AlphaEntity e2 = loadedEntities.get(j);
                if (e2.isDead) continue;

                if (e1.riddenByEntity == e2 || e1.ridingEntity == e2) continue;
                
                double dx = e2.posX - e1.posX;
                double dz = e2.posZ - e1.posZ;
                double distSq = AlphaMathHelper.absMax(dx, dz);
                if (distSq >= 0.01) {
                    distSq = AlphaMathHelper.sqrt(distSq);
                    dx /= distSq;
                    dz /= distSq;
                    double strength = 1.0 / distSq;
                    if (strength > 1.0) strength = 1.0;
                    dx *= strength;
                    dz *= strength;
                    dx *= 0.05;
                    dz *= 0.05;
                    
                    e1.motionX -= dx;
                    e1.motionZ -= dz;
                    e2.motionX += dx;
                    e2.motionZ += dz;
                }
            }
        }
    }

    public boolean handleMaterialAcceleration(AlphaAABB aabb, int materialId, AlphaEntity entity) {
        int minX = AlphaMathHelper.floor(aabb.minX);
        int maxX = AlphaMathHelper.floor(aabb.maxX + 1.0);
        int minY = AlphaMathHelper.floor(aabb.minY);
        int maxY = AlphaMathHelper.floor(aabb.maxY + 1.0);
        int minZ = AlphaMathHelper.floor(aabb.minZ);
        int maxZ = AlphaMathHelper.floor(aabb.maxZ + 1.0);

        boolean inMaterial = false;
        AlphaVec3D pushVec = AlphaVec3D.create(0.0, 0.0, 0.0);

        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    int blockId = this.getBlockId(x, y, z);
                    if (blockId == materialId || (materialId == 8 && blockId == 9) || (materialId == 10 && blockId == 11)) {
                        BlockPos pos = new BlockPos(x, y, z);
                        net.minecraft.world.level.material.FluidState fs = mcLevel.getFluidState(pos);
                        double height = y + 1 - (1.0 / 9.0);
                        if (maxY >= height) {
                            inMaterial = true;
                            if (materialId == 8 || materialId == 10) {
                                net.minecraft.world.phys.Vec3 modernFlow = fs.getFlow(mcLevel, pos);
                                pushVec.x += modernFlow.x;
                                pushVec.y += modernFlow.y;
                                pushVec.z += modernFlow.z;
                            }
                        }
                    }
                }
            }
        }

        if (pushVec.length() > 0.0 && inMaterial) {
            pushVec = pushVec.normalize();
            double strength = 0.014;
            entity.motionX += pushVec.x * strength;
            entity.motionY += pushVec.y * strength;
            entity.motionZ += pushVec.z * strength;
        }

        return inMaterial;
    }
}
