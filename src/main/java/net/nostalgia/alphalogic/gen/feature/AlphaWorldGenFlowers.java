package net.nostalgia.alphalogic.gen.feature;

import java.util.Random;
import net.nostalgia.client.render.NostalgiaChunkCache;

public class AlphaWorldGenFlowers {
    private byte flowerId;

    public AlphaWorldGenFlowers(byte id) {
        this.flowerId = id;
    }

    public boolean generate(Random rand, int x, int y, int z) {
        for (int i = 0; i < 64; ++i) {
            int cx = x + rand.nextInt(8) - rand.nextInt(8);
            int cy = y + rand.nextInt(4) - rand.nextInt(4);
            int cz = z + rand.nextInt(8) - rand.nextInt(8);
            
            if (cy >= 0 && cy < 128 && NostalgiaChunkCache.getBlockSafely(cx, cy, cz) == 0 && NostalgiaChunkCache.getBlockSafely(cx, cy - 1, cz) == 2) {
                NostalgiaChunkCache.setBlockSafely(cx, cy, cz, this.flowerId);
            }
        }
        return true;
    }
}
