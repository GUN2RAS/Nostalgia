package net.nostalgia.alphalogic.gen;

import java.util.Random;

public class AlphaMapGenBase {
  protected int range = 8;
  protected Random rand = new Random();

  public AlphaMapGenBase() {
  }

  public void generate(AlphaLevelSource source, int chunkX, int chunkZ, byte[] blocks) {
    int i = this.range;
    this.rand.setSeed(chunkX * 341873128712L + chunkZ * 132897987541L);

    for (int k = chunkX - i; k <= chunkX + i; k++) {
      for (int l = chunkZ - i; l <= chunkZ + i; l++) {
        long l1 = k * 341873128712L + l * 132897987541L;
        this.rand.setSeed(l1);
        this.recursiveGenerate(source, k, l, chunkX, chunkZ, blocks);
      }
    }
  }

  protected void recursiveGenerate(AlphaLevelSource source, int chunkX, int chunkZ, int originX, int originZ, byte[] blocks) {
  }
}
