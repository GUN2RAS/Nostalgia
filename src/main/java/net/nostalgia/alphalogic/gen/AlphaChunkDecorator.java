package net.nostalgia.alphalogic.gen;

import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenTrees;
import net.nostalgia.alphalogic.gen.feature.AlphaWorldGenFlowers;
import net.nostalgia.client.render.NostalgiaChunkCache;

public class AlphaChunkDecorator {
    private static final AlphaWorldGenTrees treeGen = new AlphaWorldGenTrees();
    private static final AlphaWorldGenFlowers dandelionGen = new AlphaWorldGenFlowers((byte) 37);
    private static final AlphaWorldGenFlowers poppyGen = new AlphaWorldGenFlowers((byte) 38);

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
        int chunkX = cp.x();
        int chunkZ = cp.z();
        Random currentRand = new Random(seed);
        long xSeed = currentRand.nextLong() / 2L * 2L + 1L;
        long zSeed = currentRand.nextLong() / 2L * 2L + 1L;
        currentRand.setSeed((long)chunkX * xSeed + (long)chunkZ * zSeed ^ seed);

        for (int i = 0; i < 20; ++i) { skipOre(currentRand, 128, 32); } 
        for (int i = 0; i < 10; ++i) { skipOre(currentRand, 128, 32); } 
        for (int i = 0; i < 20; ++i) { skipOre(currentRand, 128, 16); } 
        for (int i = 0; i < 20; ++i) { skipOre(currentRand, 64, 8); }  
        for (int i = 0; i < 2; ++i) { skipOre(currentRand, 32, 8); }   
        for (int i = 0; i < 8; ++i) { skipOre(currentRand, 16, 7); }   
        for (int i = 0; i < 1; ++i) { skipOre(currentRand, 16, 7); }   

        int numTrees = (int)(currentRand.nextDouble() * currentRand.nextDouble() * 10.0 + 1.0);
        
        for (int i = 0; i < numTrees; ++i) {
            int x = chunkX * 16 + currentRand.nextInt(16) + 8;
            int z = chunkZ * 16 + currentRand.nextInt(16) + 8;
            int y = NostalgiaChunkCache.getHighestBlockY(x, z);
            if (y > 0) treeGen.generate(currentRand, x, y, z);
        }

        for (int i = 0; i < 2; ++i) {
            int x = chunkX * 16 + currentRand.nextInt(16) + 8;
            int y = currentRand.nextInt(128);
            int z = chunkZ * 16 + currentRand.nextInt(16) + 8;
            dandelionGen.generate(currentRand, x, y, z);
        }

        if (currentRand.nextInt(2) == 0) {
            int x = chunkX * 16 + currentRand.nextInt(16) + 8;
            int y = currentRand.nextInt(128);
            int z = chunkZ * 16 + currentRand.nextInt(16) + 8;
            poppyGen.generate(currentRand, x, y, z);
        }
    }
}
