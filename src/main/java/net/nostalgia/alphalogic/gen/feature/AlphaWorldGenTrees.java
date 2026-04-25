package net.nostalgia.alphalogic.gen.feature;

import java.util.Random;
import net.nostalgia.client.render.NostalgiaChunkCache;

public class AlphaWorldGenTrees {
    public boolean generate(Random rand, int x, int y, int z) {
        int height = rand.nextInt(3) + 4;
        boolean canGrow = true;

        if (y < 1 || y + height + 1 > 128) {
            return false;
        }

        for (int checkY = y; checkY <= y + 1 + height; ++checkY) {
            int radius = 1;
            if (checkY == y) {
                radius = 0;
            }
            if (checkY >= y + 1 + height - 2) {
                radius = 2;
            }
            for (int checkX = x - radius; checkX <= x + radius && canGrow; ++checkX) {
                for (int checkZ = z - radius; checkZ <= z + radius && canGrow; ++checkZ) {
                    if (checkY >= 0 && checkY < 128) {
                        byte blockId = NostalgiaChunkCache.getBlockSafely(checkX, checkY, checkZ);
                        if (blockId != 0 && blockId != 18) { 
                            canGrow = false;
                        }
                    } else {
                        canGrow = false;
                    }
                }
            }
        }

        if (!canGrow) {
            return false;
        }

        byte blockBelow = NostalgiaChunkCache.getBlockSafely(x, y - 1, z);
        if ((blockBelow == 2 || blockBelow == 3) && y < 128 - height - 1) { 
            NostalgiaChunkCache.setBlockSafely(x, y - 1, z, (byte) 3); 

            for (int leafY = y - 3 + height; leafY <= y + height; ++leafY) {
                int leafLayer = leafY - (y + height);
                int leafRadius = 1 - leafLayer / 2;
                for (int leafX = x - leafRadius; leafX <= x + leafRadius; ++leafX) {
                    int xOff = leafX - x;
                    for (int leafZ = z - leafRadius; leafZ <= z + leafRadius; ++leafZ) {
                        int zOff = leafZ - z;
                        if (Math.abs(xOff) != leafRadius || Math.abs(zOff) != leafRadius || (rand.nextInt(2) != 0 && leafLayer != 0)) {
                            byte currentBlock = NostalgiaChunkCache.getBlockSafely(leafX, leafY, leafZ);
                            if (currentBlock == 0 || currentBlock == 18) {
                                NostalgiaChunkCache.setBlockSafely(leafX, leafY, leafZ, (byte) 18); 
                            }
                        }
                    }
                }
            }

            for (int trunkY = 0; trunkY < height; ++trunkY) {
                byte currentBlock = NostalgiaChunkCache.getBlockSafely(x, y + trunkY, z);
                if (currentBlock == 0 || currentBlock == 18) {
                    NostalgiaChunkCache.setBlockSafely(x, y + trunkY, z, (byte) 17); 
                }
            }
            return true;
        }
        return false;
    }
}
