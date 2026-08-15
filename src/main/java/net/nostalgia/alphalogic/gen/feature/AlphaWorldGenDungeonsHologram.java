package net.nostalgia.alphalogic.gen.feature;

import java.util.Random;
import net.nostalgia.client.events.caches.impl.AlphaByteCache;

public class AlphaWorldGenDungeonsHologram {
  public AlphaWorldGenDungeonsHologram() {
  }

  public boolean generate(Random random, int x, int y, int z) {
    int radiusX = random.nextInt(2) + 2;
    int radiusZ = random.nextInt(2) + 2;
    int height = 3;
    int solidCount = 0;

    for (int curX = x - radiusX - 1; curX <= x + radiusX + 1; curX++) {
      for (int curY = y - 1; curY <= y + height + 1; curY++) {
        for (int curZ = z - radiusZ - 1; curZ <= z + radiusZ + 1; curZ++) {
          byte blockId = AlphaByteCache.getBlockSafely(curX, curY, curZ);
          boolean isSolid = this.isSolid(blockId);
          if (curY == y - 1 && !isSolid) {
            return false;
          }

          if (curY == y + height + 1 && !isSolid) {
            return false;
          }

          byte blockAbove = AlphaByteCache.getBlockSafely(curX, curY + 1, curZ);
          if ((curX == x - radiusX - 1 || curX == x + radiusX + 1 || curZ == z - radiusZ - 1 || curZ == z + radiusZ + 1)
            && curY == y
            && blockId == 0
            && blockAbove == 0) {
            solidCount++;
          }
        }
      }
    }

    if (solidCount >= 1 && solidCount <= 5) {
      for (int curX = x - radiusX - 1; curX <= x + radiusX + 1; curX++) {
        for (int curY = y + height; curY >= y - 1; curY--) {
          for (int curZ = z - radiusZ - 1; curZ <= z + radiusZ + 1; curZ++) {
            if (curX != x - radiusX - 1
              && curY != y - 1
              && curZ != z - radiusZ - 1
              && curX != x + radiusX + 1
              && curY != y + height + 1
              && curZ != z + radiusZ + 1) {
              AlphaByteCache.setBlockSafely(curX, curY, curZ, (byte)0);
            } else if (curY >= 0 && !this.isSolid(AlphaByteCache.getBlockSafely(curX, curY - 1, curZ))) {
              AlphaByteCache.setBlockSafely(curX, curY, curZ, (byte)0);
            } else if (this.isSolid(AlphaByteCache.getBlockSafely(curX, curY, curZ))) {
              if (curY == y - 1 && random.nextInt(4) != 0) {
                AlphaByteCache.setBlockSafely(curX, curY, curZ, (byte)48);
              } else {
                AlphaByteCache.setBlockSafely(curX, curY, curZ, (byte)4);
              }
            }
          }
        }
      }

      for (int chests = 0; chests < 2; chests++) {
        for (int attempts = 0; attempts < 3; attempts++) {
          int cx = x + random.nextInt(radiusX * 2 + 1) - radiusX;
          int cz = z + random.nextInt(radiusZ * 2 + 1) - radiusZ;
          if (AlphaByteCache.getBlockSafely(cx, y, cz) == 0) {
            int adjacentSolid = 0;
            if (this.isSolid(AlphaByteCache.getBlockSafely(cx - 1, y, cz))) {
              adjacentSolid++;
            }

            if (this.isSolid(AlphaByteCache.getBlockSafely(cx + 1, y, cz))) {
              adjacentSolid++;
            }

            if (this.isSolid(AlphaByteCache.getBlockSafely(cx, y, cz - 1))) {
              adjacentSolid++;
            }

            if (this.isSolid(AlphaByteCache.getBlockSafely(cx, y, cz + 1))) {
              adjacentSolid++;
            }

            if (adjacentSolid == 1) {
              for (int i = 0; i < 8; i++) {
                boolean hasLoot = this.generateFakeLoot(random);
                if (hasLoot) {
                  random.nextInt(27);
                }
              }
              break;
            }
          }
        }
      }

      this.getFakeSpawnerEntity(random);
      return true;
    } else {
      return false;
    }
  }

  private boolean generateFakeLoot(Random random) {
    int i = random.nextInt(11);
    if (i == 0) {
      return true;
    } else if (i == 1) {
      random.nextInt(4);
      return true;
    } else if (i == 2) {
      return true;
    } else if (i == 3) {
      random.nextInt(4);
      return true;
    } else if (i == 4) {
      random.nextInt(4);
      return true;
    } else if (i == 5) {
      random.nextInt(4);
      return true;
    } else if (i == 6) {
      return true;
    } else if (i == 7 && random.nextInt(100) == 0) {
      return true;
    } else if (i == 8 && random.nextInt(2) == 0) {
      random.nextInt(4);
      return true;
    } else if (i == 9 && random.nextInt(10) == 0) {
      random.nextInt(2);
      return true;
    } else {
      return false;
    }
  }

  private void getFakeSpawnerEntity(Random random) {
    random.nextInt(4);
  }

  private boolean isSolid(byte id) {
    return id != 0
      && id != 8
      && id != 9
      && id != 10
      && id != 11
      && id != 18
      && id != 20
      && id != 37
      && id != 38
      && id != 39
      && id != 40
      && id != 50
      && id != 51
      && id != 55
      && id != 59
      && id != 69
      && id != 75
      && id != 76
      && id != 77
      && id != 83
      && id != 90
      && id != 115
      && id != 119;
  }
}
