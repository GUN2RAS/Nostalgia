package net.nostalgia.alphalogic.gen.feature;

import java.util.Random;
import net.nostalgia.client.render.NostalgiaChunkCache;

public class AlphaWorldGenDungeonsHologram {
    public boolean generate(Random random, int x, int y, int z) {
        byte materialId = NostalgiaChunkCache.getBlockSafely(x, y, z);
        byte materialBelow = NostalgiaChunkCache.getBlockSafely(x, y - 1, z);
        
        byte mat = materialBelow;
        if (mat == 0 || !isSolid(mat)) {
            return false;
        }

        int xSize = random.nextInt(2) + 2;
        int ySize = 3;
        int zSize = random.nextInt(2) + 2;
        int solidCount = 0;

        for (int i = x - xSize - 1; i <= x + xSize + 1; ++i) {
            for (int j = y - 1; j <= y + ySize + 1; ++j) {
                for (int k = z - zSize - 1; k <= z + zSize + 1; ++k) {
                    byte blockId = NostalgiaChunkCache.getBlockSafely(i, j, k);
                    if (j == y - 1 && !isSolid(blockId)) {
                        return false;
                    }
                    if (j == y + ySize + 1 && !isSolid(blockId)) {
                        return false;
                    }
                    if ((i == x - xSize - 1 || i == x + xSize + 1 || k == z - zSize - 1 || k == z + zSize + 1) && j == y && blockId == 0) {
                        ++solidCount;
                    }
                }
            }
        }

        if (solidCount >= 1 && solidCount <= 5) {
            for (int i = x - xSize - 1; i <= x + xSize + 1; ++i) {
                for (int j = y + ySize; j >= y - 1; --j) {
                    for (int k = z - zSize - 1; k <= z + zSize + 1; ++k) {
                        if (i != x - xSize - 1 && j != y - 1 && k != z - zSize - 1 && i != x + xSize + 1 && j != y + ySize + 1 && k != z + zSize + 1) {
                            NostalgiaChunkCache.setBlockSafely(i, j, k, (byte) 0); // AIR
                        } else if (j >= 0 && !isSolid(NostalgiaChunkCache.getBlockSafely(i, j - 1, k))) {
                            NostalgiaChunkCache.setBlockSafely(i, j, k, (byte) 0); // AIR
                        } else {
                            byte currentBlock = NostalgiaChunkCache.getBlockSafely(i, j, k);
                            if (isSolid(currentBlock)) {
                                if (j == y - 1 && random.nextInt(4) != 0) {
                                    NostalgiaChunkCache.setBlockSafely(i, j, k, (byte) 48); // MOSSY COBBLE
                                } else {
                                    NostalgiaChunkCache.setBlockSafely(i, j, k, (byte) 4); // COBBLE
                                }
                            }
                        }
                    }
                }
            }

            // We do not place a spawner block (ID 52) or chests for the hologram since we can't render TileEntities
            return true;
        }

        return false;
    }

    private boolean isSolid(byte id) {
        if (id == 0 || id == 8 || id == 9 || id == 10 || id == 11 || id == 18 || id == 20) return false;
        return true;
    }
}
