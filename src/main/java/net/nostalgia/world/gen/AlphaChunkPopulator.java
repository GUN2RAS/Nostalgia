package net.nostalgia.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.nostalgia.block.AlphaBlocks;

import java.util.Random;

public class AlphaChunkPopulator {

    public static void populate(WorldGenLevel level, ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x();
        int chunkZ = chunkPos.z();
        int startX = chunkX * 16;
        int startZ = chunkZ * 16;
        
        Random rand = new Random(level.getSeed());
        long xSeed = rand.nextLong() / 2L * 2L + 1L;
        long zSeed = rand.nextLong() / 2L * 2L + 1L;
        rand.setSeed((long)chunkX * xSeed + (long)chunkZ * zSeed ^ level.getSeed());
        
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < 20; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(AlphaBlocks.ALPHA_DIRT.defaultBlockState(), 32).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(128), startZ + rand.nextInt(16));
        }
        for (int i = 0; i < 10; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(AlphaBlocks.ALPHA_GRAVEL.defaultBlockState(), 32).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(128), startZ + rand.nextInt(16));
        }
        for (int i = 0; i < 20; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(AlphaBlocks.ALPHA_COAL_ORE.defaultBlockState(), 16).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(128), startZ + rand.nextInt(16));
        }
        for (int i = 0; i < 20; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(AlphaBlocks.ALPHA_IRON_ORE.defaultBlockState(), 8).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(64), startZ + rand.nextInt(16));
        }
        for (int i = 0; i < 2; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(AlphaBlocks.ALPHA_GOLD_ORE.defaultBlockState(), 8).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(32), startZ + rand.nextInt(16));
        }
        for (int i = 0; i < 8; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(net.minecraft.world.level.block.Blocks.REDSTONE_ORE.defaultBlockState(), 7).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(16), startZ + rand.nextInt(16));
        }
        for (int i = 0; i < 1; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenMinable(AlphaBlocks.ALPHA_DIAMOND_ORE.defaultBlockState(), 7).generate(level, rand, startX + rand.nextInt(16), rand.nextInt(16), startZ + rand.nextInt(16));
        }

        int treeCount = (int)(rand.nextDouble() * rand.nextDouble() * 10.0 + 1.0);
        for (int i = 0; i < treeCount; i++) {
            int x = startX + rand.nextInt(16) + 8;
            int z = startZ + rand.nextInt(16) + 8;
            int y = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE_WG, new BlockPos(x, 0, z)).getY();
            
            if (y > 0 && y < 128) {
                generateAlphaTree(level, pos, x, y, z, rand);
            }
        }

        for (int i = 0; i < 10; ++i) {
            int rx = startX + rand.nextInt(16) + 8;
            int rz = startZ + rand.nextInt(16) + 8;
            int ry = rand.nextInt(128);
            
            for (int r = 0; r < 20; ++r) {
                int dx = rx + rand.nextInt(4) - rand.nextInt(4);
                int dz = rz + rand.nextInt(4) - rand.nextInt(4);
                int dy = ry + rand.nextInt(4) - rand.nextInt(4);
                pos.set(dx, dy, dz);
                
                if (level.isEmptyBlock(pos) || level.getBlockState(pos).is(AlphaBlocks.ALPHA_SUGAR_CANE)) {
                    BlockPos down = pos.below();
                    BlockState belowState = level.getBlockState(down);
                    if (belowState.is(AlphaBlocks.ALPHA_GRASS_BLOCK) || belowState.is(AlphaBlocks.ALPHA_DIRT) || belowState.is(AlphaBlocks.ALPHA_SAND)) {
                        boolean adjacentWater = false;
                        for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                            if (level.getBlockState(down.relative(dir)).is(Blocks.WATER) || level.getBlockState(down.relative(dir)).is(Blocks.WATER)) {
                                adjacentWater = true;
                                break;
                            }
                        }
                        if (adjacentWater) {
                            int height = 2 + rand.nextInt(rand.nextInt(3) + 1);
                            for (int h = 0; h < height; h++) {
                                BlockPos canePos = pos.above(h);
                                if (level.isEmptyBlock(canePos)) {
                                    level.setBlock(canePos, AlphaBlocks.ALPHA_SUGAR_CANE.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 2; ++i) {
            generatePlant(level, AlphaBlocks.ALPHA_YELLOW_FLOWER.defaultBlockState(), startX + rand.nextInt(16) + 8, rand.nextInt(128), startZ + rand.nextInt(16) + 8, rand);
        }
        if (rand.nextInt(2) == 0) {
            generatePlant(level, AlphaBlocks.ALPHA_RED_FLOWER.defaultBlockState(), startX + rand.nextInt(16) + 8, rand.nextInt(128), startZ + rand.nextInt(16) + 8, rand);
        }

        for (int i = 0; i < 10; ++i) {
            generatePlant(level, AlphaBlocks.ALPHA_SUGAR_CANE.defaultBlockState(), startX + rand.nextInt(16) + 8, rand.nextInt(128), startZ + rand.nextInt(16) + 8, rand);
        }

        if (rand.nextInt(4) == 0) {
            generatePlant(level, AlphaBlocks.ALPHA_BROWN_MUSHROOM.defaultBlockState(), startX + rand.nextInt(16) + 8, rand.nextInt(128), startZ + rand.nextInt(16) + 8, rand);
        }
        if (rand.nextInt(8) == 0) {
            generatePlant(level, AlphaBlocks.ALPHA_RED_MUSHROOM.defaultBlockState(), startX + rand.nextInt(16) + 8, rand.nextInt(128), startZ + rand.nextInt(16) + 8, rand);
        }

        for (int i = 0; i < 10; ++i) {
            int cx = startX + rand.nextInt(16) + 8;
            int cy = rand.nextInt(128);
            int cz = startZ + rand.nextInt(16) + 8;
            for (int k = 0; k < 10; ++k) {
                int tx = cx + rand.nextInt(8) - rand.nextInt(8);
                int ty = cy + rand.nextInt(4) - rand.nextInt(4);
                int tz = cz + rand.nextInt(8) - rand.nextInt(8);
                pos.set(tx, ty, tz);
                if (level.isEmptyBlock(pos)) {
                    int height = 1 + rand.nextInt(rand.nextInt(3) + 1);
                    for (int h = 0; h < height; ++h) {
                        BlockState below = level.getBlockState(pos.below());
                        if (below.is(AlphaBlocks.ALPHA_SAND) || below.is(AlphaBlocks.ALPHA_CACTUS)) {
                            boolean isValid = true;
                            for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                                if (level.getBlockState(pos.relative(dir)).isSolid()) {
                                    isValid = false; break;
                                }
                            }
                            if (isValid) level.setBlock(pos, AlphaBlocks.ALPHA_CACTUS.defaultBlockState(), 2);
                        }
                        pos.set(tx, ty + h + 1, tz);
                    }
                }
            }
        }

        if (rand.nextInt(64) == 0) {
            int px = startX + rand.nextInt(16) + 8;
            int py = rand.nextInt(128);
            int pz = startZ + rand.nextInt(16) + 8;
            for (int k = 0; k < 64; ++k) {
                int tx = px + rand.nextInt(8) - rand.nextInt(8);
                int ty = py + rand.nextInt(4) - rand.nextInt(4);
                int tz = pz + rand.nextInt(8) - rand.nextInt(8);
                pos.set(tx, ty, tz);
                if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()).is(AlphaBlocks.ALPHA_GRASS_BLOCK)) {
                    level.setBlock(pos, Blocks.PUMPKIN.defaultBlockState(), 2);
                }
            }
        }

        for (int i = 0; i < 50; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenLiquids(Blocks.WATER.defaultBlockState()).generate(level, rand, startX + rand.nextInt(16) + 8, rand.nextInt(rand.nextInt(120) + 8), startZ + rand.nextInt(16) + 8);
        }
        for (int i = 0; i < 20; ++i) {
            new net.nostalgia.alphalogic.gen.AlphaWorldGenLiquids(Blocks.LAVA.defaultBlockState()).generate(level, rand, startX + rand.nextInt(16) + 8, rand.nextInt(rand.nextInt(120) + 8), startZ + rand.nextInt(16) + 8);
        }
    }

    private static void generatePlant(WorldGenLevel level, BlockState plant, int rx, int ry, int rz, Random rand) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 64; ++i) {
            int x = rx + rand.nextInt(8) - rand.nextInt(8);
            int y = ry + rand.nextInt(4) - rand.nextInt(4);
            int z = rz + rand.nextInt(8) - rand.nextInt(8);
            pos.set(x, y, z);
            
            if (level.isEmptyBlock(pos) && y > 0 && y < 128) {
                BlockState below = level.getBlockState(pos.below());
                if (plant.is(AlphaBlocks.ALPHA_YELLOW_FLOWER) || plant.is(AlphaBlocks.ALPHA_RED_FLOWER)) {
                    if (below.is(AlphaBlocks.ALPHA_GRASS_BLOCK) || below.is(AlphaBlocks.ALPHA_DIRT)) {
                        level.setBlock(pos, plant, 2);
                    }
                } else if (plant.is(AlphaBlocks.ALPHA_BROWN_MUSHROOM) || plant.is(AlphaBlocks.ALPHA_RED_MUSHROOM)) {
                    if ((below.is(AlphaBlocks.ALPHA_STONE) || below.is(AlphaBlocks.ALPHA_DIRT) || below.is(AlphaBlocks.ALPHA_GRAVEL) || below.is(AlphaBlocks.ALPHA_GRASS_BLOCK) || below.is(AlphaBlocks.ALPHA_COBBLESTONE)) && !below.is(AlphaBlocks.ALPHA_LEAVES)) {
                        level.setBlock(pos, plant, 2);
                    }
                }
            }
        }
    }

    private static boolean generateAlphaTree(WorldGenLevel level, BlockPos.MutableBlockPos pos, int x, int y, int z, Random rand) {
        int height = rand.nextInt(3) + 4;
        boolean canGrow = true;

        if (y < 1 || y + height + 1 > 128) return false;

        for (int curY = y; curY <= y + 1 + height; ++curY) {
            int radius = (curY == y) ? 0 : (curY >= y + 1 + height - 2 ? 2 : 1);
            for (int curX = x - radius; curX <= x + radius && canGrow; ++curX) {
                for (int curZ = z - radius; curZ <= z + radius && canGrow; ++curZ) {
                    if (curY >= 0 && curY < 256) {
                        BlockState state = level.getBlockState(pos.set(curX, curY, curZ));
                        if (!state.isAir() && !state.is(AlphaBlocks.ALPHA_LEAVES)) {
                            canGrow = false;
                        }
                    } else {
                        canGrow = false;
                    }
                }
            }
        }

        if (!canGrow) return false;

        BlockState below = level.getBlockState(pos.set(x, y - 1, z));
        if ((below.is(AlphaBlocks.ALPHA_GRASS_BLOCK) || below.is(AlphaBlocks.ALPHA_DIRT)) && y < 128 - height - 1) {
            level.setBlock(pos.set(x, y - 1, z), AlphaBlocks.ALPHA_DIRT.defaultBlockState(), 2);

            for (int leafY = y - 3 + height; leafY <= y + height; ++leafY) {
                int distY = leafY - (y + height);
                int leafRadius = 1 - distY / 2;
                for (int leafX = x - leafRadius; leafX <= x + leafRadius; ++leafX) {
                    int distX = leafX - x;
                    for (int leafZ = z - leafRadius; leafZ <= z + leafRadius; ++leafZ) {
                        int distZ = leafZ - z;
                        if (Math.abs(distX) != leafRadius || Math.abs(distZ) != leafRadius || (rand.nextInt(2) != 0 && distY != 0)) {
                            pos.set(leafX, leafY, leafZ);
                            if (level.getBlockState(pos).isAir() || level.getBlockState(pos).is(AlphaBlocks.ALPHA_LEAVES)) {
                                level.setBlock(pos, AlphaBlocks.ALPHA_LEAVES.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }

            for (int logY = 0; logY < height; ++logY) {
                pos.set(x, y + logY, z);
                BlockState state = level.getBlockState(pos);
                if (state.isAir() || state.is(AlphaBlocks.ALPHA_LEAVES)) {
                    level.setBlock(pos, AlphaBlocks.ALPHA_OAK_LOG.defaultBlockState(), 2);
                }
            }
            return true;
        }
        return false;
    }
}
