package net.nostalgia.alphalogic.gen;

import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenTrees;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenBigTreeHologram;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenDungeonsHologram;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenClayHologram;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenFlowers;
import net.nostalgia.client.render.NostalgiaChunkCache;

public class AlphaChunkDecorator {
    private static final AlphaWorldGenTrees treeGen = new AlphaWorldGenTrees();
    private static final AlphaWorldGenBigTreeHologram bigTreeGen = new AlphaWorldGenBigTreeHologram();
    private static final AlphaWorldGenFlowers dandelionGen = new AlphaWorldGenFlowers((byte) 37);
    private static final AlphaWorldGenFlowers poppyGen = new AlphaWorldGenFlowers((byte) 38);
    private static final AlphaWorldGenFlowers brownMushroomGen = new AlphaWorldGenFlowers((byte) 39);
    private static final AlphaWorldGenFlowers redMushroomGen = new AlphaWorldGenFlowers((byte) 40);

    private static net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves treeNoise;
    private static long lastSeed = -1;

    private static void skipOre(Random random, int maxY, int amount) {
        random.nextInt(16);
        random.nextInt(maxY);
        random.nextInt(16);
        random.nextFloat();
        random.nextInt(3);
        random.nextInt(3);
        for(int l = 0; l <= amount; ++l) {
            random.nextDouble();
        }
    }

    public static void decorate(ChunkPos cp, long seed) {
        if (treeNoise == null || lastSeed != seed) {
            Random initRand = new Random(seed);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 16);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 16);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 8);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 4);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 4);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 10);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 16);
            new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 8); // depthNoise2
            treeNoise = new net.nostalgia.alphalogic.gen.AlphaNoiseGeneratorOctaves(initRand, 8);
            lastSeed = seed;
        }

        int chunkX = cp.x();
        int chunkZ = cp.z();
        int startX = chunkX * 16;
        int startZ = chunkZ * 16;
        
        Random currentRand = new Random(seed);
        long xSeed = currentRand.nextLong() / 2L * 2L + 1L;
        long zSeed = currentRand.nextLong() / 2L * 2L + 1L;
        currentRand.setSeed((long)chunkX * xSeed + (long)chunkZ * zSeed ^ seed);
        
        boolean isSnowMode = new Random(seed).nextInt(4) == 0;

        for (int i = 0; i < 8; ++i) {
            new AlphaWorldGenDungeonsHologram().generate(currentRand, startX + currentRand.nextInt(16) + 8, currentRand.nextInt(128), startZ + currentRand.nextInt(16) + 8);
        }
        for (int i = 0; i < 10; ++i) {
            new AlphaWorldGenClayHologram(32).generate(currentRand, startX + currentRand.nextInt(16), currentRand.nextInt(128), startZ + currentRand.nextInt(16));
        }

        for (int i = 0; i < 20; ++i) { skipOre(currentRand, 128, 32); } 
        for (int i = 0; i < 10; ++i) { skipOre(currentRand, 128, 32); } 
        for (int i = 0; i < 20; ++i) { skipOre(currentRand, 128, 16); } 
        for (int i = 0; i < 20; ++i) { skipOre(currentRand, 64, 8); }  
        for (int i = 0; i < 2; ++i) { skipOre(currentRand, 32, 8); }   
        for (int i = 0; i < 8; ++i) { skipOre(currentRand, 16, 7); }   
        for (int i = 0; i < 1; ++i) { skipOre(currentRand, 16, 7); }   

        double d2 = 0.5D;
        int treeCount = (int)((treeNoise.generateNoise(startX * d2, startZ * d2) / 8.0D + currentRand.nextDouble() * 4.0D + 4.0D) / 3.0D);
        if (treeCount < 0) {
            treeCount = 0;
        }
        if (currentRand.nextInt(10) == 0) {
            ++treeCount;
        }

        for (int i = 0; i < treeCount; ++i) {
            int x = startX + currentRand.nextInt(16) + 8;
            int z = startZ + currentRand.nextInt(16) + 8;
            int y = NostalgiaChunkCache.getHighestBlockY(x, z);
            
            if (y > 0 && y < 128) {
                if (currentRand.nextInt(10) == 0) {
                    bigTreeGen.generate(currentRand, x, y, z);
                } else {
                    treeGen.generate(currentRand, x, y, z);
                }
            }
        }

        for (int i = 0; i < 2; ++i) {
            int x = startX + currentRand.nextInt(16) + 8;
            int y = currentRand.nextInt(128);
            int z = startZ + currentRand.nextInt(16) + 8;
            dandelionGen.generate(currentRand, x, y, z);
        }
        if (currentRand.nextInt(2) == 0) {
            int x = startX + currentRand.nextInt(16) + 8;
            int y = currentRand.nextInt(128);
            int z = startZ + currentRand.nextInt(16) + 8;
            poppyGen.generate(currentRand, x, y, z);
        }

        if (currentRand.nextInt(4) == 0) {
            int x = startX + currentRand.nextInt(16) + 8;
            int y = currentRand.nextInt(128);
            int z = startZ + currentRand.nextInt(16) + 8;
            brownMushroomGen.generate(currentRand, x, y, z);
        }
        if (currentRand.nextInt(8) == 0) {
            int x = startX + currentRand.nextInt(16) + 8;
            int y = currentRand.nextInt(128);
            int z = startZ + currentRand.nextInt(16) + 8;
            redMushroomGen.generate(currentRand, x, y, z);
        }

        for (int i = 0; i < 10; ++i) {
            int rx = startX + currentRand.nextInt(16) + 8;
            int rz = startZ + currentRand.nextInt(16) + 8;
            int ry = currentRand.nextInt(128);
            
            for (int r = 0; r < 20; ++r) {
                int dx = rx + currentRand.nextInt(4) - currentRand.nextInt(4);
                int dz = rz + currentRand.nextInt(4) - currentRand.nextInt(4);
                int dy = ry + currentRand.nextInt(4) - currentRand.nextInt(4);
                
                if (dy >= 0 && dy < 128) {
                    byte blockId = NostalgiaChunkCache.getBlockSafely(dx, dy, dz);
                    if (blockId == 0 || blockId == 83) { // Air or Sugar Cane
                        byte belowState = NostalgiaChunkCache.getBlockSafely(dx, dy - 1, dz);
                        if (belowState == 2 || belowState == 3 || belowState == 12) { // Grass, Dirt, Sand
                            boolean adjacentWater = false;
                            int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
                            for (int[] dir : dirs) {
                                byte adjBlock = NostalgiaChunkCache.getBlockSafely(dx + dir[0], dy - 1, dz + dir[1]);
                                if (adjBlock == 8 || adjBlock == 9) { // Water
                                    adjacentWater = true;
                                    break;
                                }
                            }
                            if (adjacentWater) {
                                int height = 2 + currentRand.nextInt(currentRand.nextInt(3) + 1);
                                for (int h = 0; h < height; h++) {
                                    if (dy + h < 128 && NostalgiaChunkCache.getBlockSafely(dx, dy + h, dz) == 0) {
                                        NostalgiaChunkCache.setBlockSafely(dx, dy + h, dz, (byte) 83); // 83 is Sugar Cane
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 1; ++i) {
            int cx = startX + currentRand.nextInt(16) + 8;
            int cy = currentRand.nextInt(128);
            int cz = startZ + currentRand.nextInt(16) + 8;
            for (int k = 0; k < 10; ++k) {
                int tx = cx + currentRand.nextInt(8) - currentRand.nextInt(8);
                int ty = cy + currentRand.nextInt(4) - currentRand.nextInt(4);
                int tz = cz + currentRand.nextInt(8) - currentRand.nextInt(8);
                
                if (ty >= 0 && ty < 128 && NostalgiaChunkCache.getBlockSafely(tx, ty, tz) == 0) {
                    int height = 1 + currentRand.nextInt(currentRand.nextInt(3) + 1);
                    for (int h = 0; h < height; ++h) {
                        byte below = NostalgiaChunkCache.getBlockSafely(tx, ty + h - 1, tz);
                        if (below == 12 || below == 81) { // Sand or Cactus
                            boolean isValid = true;
                            int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
                            for (int[] dir : dirs) {
                                byte adjBlock = NostalgiaChunkCache.getBlockSafely(tx + dir[0], ty + h, tz + dir[1]);
                                if (adjBlock != 0 && adjBlock != 8 && adjBlock != 9 && adjBlock != 10 && adjBlock != 11 && adjBlock != 18 && adjBlock != 20 && adjBlock != 83) { // Solid check approximated
                                    isValid = false; break;
                                }
                            }
                            if (isValid) {
                                NostalgiaChunkCache.setBlockSafely(tx, ty + h, tz, (byte) 81); // 81 is Cactus
                            }
                        }
                    }
                }
            }
        }
        
        // Skip liquids 
        for (int i = 0; i < 50; ++i) {
            currentRand.nextInt(16);
            currentRand.nextInt(currentRand.nextInt(120) + 8);
            currentRand.nextInt(16);
        }
        for (int i = 0; i < 20; ++i) {
            currentRand.nextInt(16);
            currentRand.nextInt(currentRand.nextInt(120) + 8);
            currentRand.nextInt(16);
        }

        if (isSnowMode) {
            for (int x = startX + 8; x < startX + 8 + 16; ++x) {
                for (int z = startZ + 8; z < startZ + 8 + 16; ++z) {
                    int y = NostalgiaChunkCache.getHighestBlockY(x, z);
                    if (y > 0 && y < 128) {
                        byte blockId = NostalgiaChunkCache.getBlockSafely(x, y, z);
                        if (blockId == 0) {
                            byte belowBlock = NostalgiaChunkCache.getBlockSafely(x, y - 1, z);
                            if (belowBlock != 0 && belowBlock != 8 && belowBlock != 9 && belowBlock != 10 && belowBlock != 11 && belowBlock != 79 && belowBlock != 18 && belowBlock != 20 && belowBlock != 83) {
                                NostalgiaChunkCache.setBlockSafely(x, y, z, (byte) 78); // 78 is Snow Layer
                            }
                        }
                    }
                }
            }
        }
    }
}
