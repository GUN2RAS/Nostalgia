package net.nostalgia.alphalogic.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nostalgia.block.AlphaBlocks;

import java.util.Random;

public class AlphaWorldGenDungeons {

    public boolean generate(WorldGenLevel level, Random random, int x, int y, int z) {
        int radiusX = random.nextInt(2) + 2;
        int radiusZ = random.nextInt(2) + 2;
        int height = 3;
        int solidCount = 0;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int curX = x - radiusX - 1; curX <= x + radiusX + 1; ++curX) {
            for (int curY = y - 1; curY <= y + height + 1; ++curY) {
                for (int curZ = z - radiusZ - 1; curZ <= z + radiusZ + 1; ++curZ) {
                    pos.set(curX, curY, curZ);
                    boolean isSolid = level.getBlockState(pos).isSolid();
                    
                    if (curY == y - 1 && !isSolid) return false;
                    if (curY == y + height + 1 && !isSolid) return false;
                    
                    if ((curX != x - radiusX - 1 && curX != x + radiusX + 1 && curZ != z - radiusZ - 1 && curZ != z + radiusZ + 1) || curY != y || !level.isEmptyBlock(pos) || !level.isEmptyBlock(pos.above())) {
                        continue;
                    }
                    ++solidCount;
                }
            }
        }

        if (solidCount < 1 || solidCount > 5) {
            return false;
        }

        for (int curX = x - radiusX - 1; curX <= x + radiusX + 1; ++curX) {
            for (int curY = y + height; curY >= y - 1; --curY) {
                for (int curZ = z - radiusZ - 1; curZ <= z + radiusZ + 1; ++curZ) {
                    pos.set(curX, curY, curZ);
                    
                    if (curX == x - radiusX - 1 || curY == y - 1 || curZ == z - radiusZ - 1 || curX == x + radiusX + 1 || curY == y + height + 1 || curZ == z + radiusZ + 1) {
                        if (curY >= 0 && !level.getBlockState(pos.below()).isSolid()) {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                            continue;
                        }
                        if (level.getBlockState(pos).isSolid()) {
                            if (curY == y - 1 && random.nextInt(4) != 0) {
                                level.setBlock(pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 2);
                            } else {
                                level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 2);
                            }
                        }
                    } else {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }
            }
        }

        for (int chests = 0; chests < 2; ++chests) {
            for (int attempts = 0; attempts < 3; ++attempts) {
                int cx = x + random.nextInt(radiusX * 2 + 1) - radiusX;
                int cy = y;
                int cz = z + random.nextInt(radiusZ * 2 + 1) - radiusZ;
                pos.set(cx, cy, cz);

                if (!level.isEmptyBlock(pos)) continue;

                int adjacentSolid = 0;
                if (level.getBlockState(pos.west()).isSolid()) ++adjacentSolid;
                if (level.getBlockState(pos.east()).isSolid()) ++adjacentSolid;
                if (level.getBlockState(pos.north()).isSolid()) ++adjacentSolid;
                if (level.getBlockState(pos.south()).isSolid()) ++adjacentSolid;

                if (adjacentSolid != 1) continue;

                level.setBlock(pos, Blocks.CHEST.defaultBlockState(), 2);
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof ChestBlockEntity chest) {
                    for (int i = 0; i < 8; ++i) {
                        ItemStack itemStack = generateLoot(random);
                        if (itemStack != null && !itemStack.isEmpty()) {
                            chest.setItem(random.nextInt(chest.getContainerSize()), itemStack);
                        }
                    }
                }
                break;
            }
        }

        pos.set(x, y, z);
        level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), 2);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SpawnerBlockEntity spawner) {
            EntityType<?> type = getSpawnerEntity(random);
            spawner.setEntityId(type, level.getRandom());
        }

        return true;
    }

    private ItemStack generateLoot(Random random) {
        int i = random.nextInt(11);
        if (i == 0) return new ItemStack(Items.SADDLE);
        if (i == 1) return new ItemStack(Items.IRON_INGOT, random.nextInt(4) + 1);
        if (i == 2) return new ItemStack(Items.BREAD);
        if (i == 3) return new ItemStack(Items.WHEAT, random.nextInt(4) + 1);
        if (i == 4) return new ItemStack(Items.GUNPOWDER, random.nextInt(4) + 1);
        if (i == 5) return new ItemStack(Items.STRING, random.nextInt(4) + 1);
        if (i == 6) return new ItemStack(Items.BUCKET);
        if (i == 7 && random.nextInt(100) == 0) return new ItemStack(Items.APPLE); // Original was golden apple, but there was only one apple ID in early versions, I'll use APPLE
        if (i == 8 && random.nextInt(2) == 0) return new ItemStack(Items.REDSTONE, random.nextInt(4) + 1);
        // Music discs
        if (i == 9 && random.nextInt(10) == 0) {
            return new ItemStack(random.nextInt(2) == 0 ? Items.MUSIC_DISC_13 : Items.MUSIC_DISC_CAT);
        }
        return null;
    }

    private EntityType<?> getSpawnerEntity(Random random) {
        int i = random.nextInt(4);
        if (i == 0) return EntityType.SKELETON;
        if (i == 1) return EntityType.ZOMBIE;
        if (i == 2) return EntityType.ZOMBIE;
        if (i == 3) return EntityType.SPIDER;
        return EntityType.PIG;
    }
}
