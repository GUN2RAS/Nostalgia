package net.nostalgia.alphalogic.gen;

import java.util.Random;

public class AlphaNoiseGeneratorPerlin {
  private int[] permutations = new int[512];
  public double xCoord;
  public double yCoord;
  public double zCoord;

  public AlphaNoiseGeneratorPerlin() {
    this(new Random());
  }

  public AlphaNoiseGeneratorPerlin(Random random) {
    this.xCoord = random.nextDouble() * 256.0;
    this.yCoord = random.nextDouble() * 256.0;
    this.zCoord = random.nextDouble() * 256.0;
    int i = 0;

    while (i < 256) {
      this.permutations[i] = i++;
    }

    for (int var5 = 0; var5 < 256; var5++) {
      int j = random.nextInt(256 - var5) + var5;
      int k = this.permutations[var5];
      this.permutations[var5] = this.permutations[j];
      this.permutations[j] = k;
      this.permutations[var5 + 256] = this.permutations[var5];
    }
  }

  public double generateNoise(double x, double y, double z) {
    double d0 = x + this.xCoord;
    double d1 = y + this.yCoord;
    double d2 = z + this.zCoord;
    int i = (int)d0;
    int j = (int)d1;
    int k = (int)d2;
    if (d0 < i) {
      i--;
    }

    if (d1 < j) {
      j--;
    }

    if (d2 < k) {
      k--;
    }

    int l = i & 0xFF;
    int i1 = j & 0xFF;
    int j1 = k & 0xFF;
    d0 -= i;
    d1 -= j;
    d2 -= k;
    double d3 = d0 * d0 * d0 * (d0 * (d0 * 6.0 - 15.0) + 10.0);
    double d4 = d1 * d1 * d1 * (d1 * (d1 * 6.0 - 15.0) + 10.0);
    double d5 = d2 * d2 * d2 * (d2 * (d2 * 6.0 - 15.0) + 10.0);
    int k1 = this.permutations[l] + i1;
    int l1 = this.permutations[k1] + j1;
    int i2 = this.permutations[k1 + 1] + j1;
    int j2 = this.permutations[l + 1] + i1;
    int k2 = this.permutations[j2] + j1;
    int l2 = this.permutations[j2 + 1] + j1;
    return this.lerp(
      d5,
      this.lerp(
        d4,
        this.lerp(d3, this.grad(this.permutations[l1], d0, d1, d2), this.grad(this.permutations[k2], d0 - 1.0, d1, d2)),
        this.lerp(d3, this.grad(this.permutations[i2], d0, d1 - 1.0, d2), this.grad(this.permutations[l2], d0 - 1.0, d1 - 1.0, d2))
      ),
      this.lerp(
        d4,
        this.lerp(d3, this.grad(this.permutations[l1 + 1], d0, d1, d2 - 1.0), this.grad(this.permutations[k2 + 1], d0 - 1.0, d1, d2 - 1.0)),
        this.lerp(d3, this.grad(this.permutations[i2 + 1], d0, d1 - 1.0, d2 - 1.0), this.grad(this.permutations[l2 + 1], d0 - 1.0, d1 - 1.0, d2 - 1.0))
      )
    );
  }

  public double lerp(double d, double d1, double d2) {
    return d1 + d * (d2 - d1);
  }

  public double grad(int hash, double x, double y, double z) {
    int h = hash & 15;
    double u = h < 8 ? x : y;
    double v = h < 4 ? y : (h != 12 && h != 14 ? z : x);
    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }

  public double generateNoise(double x, double z) {
    return this.generateNoise(x, z, 0.0);
  }

  public void generateNoiseArray(
    double[] noiseArray,
    double xOffset,
    double yOffset,
    double zOffset,
    int xSize,
    int ySize,
    int zSize,
    double xScale,
    double yScale,
    double zScale,
    double noiseScale
  ) {
    int index = 0;
    double d = 1.0 / noiseScale;
    int i = -1;
    double d1 = 0.0;
    double d2 = 0.0;
    double d3 = 0.0;
    double d4 = 0.0;

    for (int i1 = 0; i1 < xSize; i1++) {
      double d5 = (xOffset + i1) * xScale + this.xCoord;
      int j1 = (int)d5;
      if (d5 < j1) {
        j1--;
      }

      int k1 = j1 & 0xFF;
      double var58;
      double d6 = (var58 = d5 - j1) * var58 * var58 * (var58 * (var58 * 6.0 - 15.0) + 10.0);

      for (int l1 = 0; l1 < zSize; l1++) {
        double d7 = (zOffset + l1) * zScale + this.zCoord;
        int i2 = (int)d7;
        if (d7 < i2) {
          i2--;
        }

        int j2 = i2 & 0xFF;
        double var59;
        double d8 = (var59 = d7 - i2) * var59 * var59 * (var59 * (var59 * 6.0 - 15.0) + 10.0);

        for (int k2 = 0; k2 < ySize; k2++) {
          double d9 = (yOffset + k2) * yScale + this.yCoord;
          int l2 = (int)d9;
          if (d9 < l2) {
            l2--;
          }

          int i3 = l2 & 0xFF;
          double var60;
          double d10 = (var60 = d9 - l2) * var60 * var60 * (var60 * (var60 * 6.0 - 15.0) + 10.0);
          if (k2 == 0 || i3 != i) {
            i = i3;
            int j3 = this.permutations[k1] + i3;
            int k3 = this.permutations[j3] + j2;
            int l3 = this.permutations[j3 + 1] + j2;
            int i4 = this.permutations[k1 + 1] + i3;
            int j4 = this.permutations[i4] + j2;
            int k4 = this.permutations[i4 + 1] + j2;
            d1 = this.lerp(d6, this.grad(this.permutations[k3], var58, var60, var59), this.grad(this.permutations[j4], var58 - 1.0, var60, var59));
            d2 = this.lerp(d6, this.grad(this.permutations[l3], var58, var60 - 1.0, var59), this.grad(this.permutations[k4], var58 - 1.0, var60 - 1.0, var59));
            d3 = this.lerp(
              d6, this.grad(this.permutations[k3 + 1], var58, var60, var59 - 1.0), this.grad(this.permutations[j4 + 1], var58 - 1.0, var60, var59 - 1.0)
            );
            d4 = this.lerp(
              d6,
              this.grad(this.permutations[l3 + 1], var58, var60 - 1.0, var59 - 1.0),
              this.grad(this.permutations[k4 + 1], var58 - 1.0, var60 - 1.0, var59 - 1.0)
            );
          }

          double d11 = this.lerp(d10, d1, d2);
          double d12 = this.lerp(d10, d3, d4);
          double d13 = this.lerp(d8, d11, d12);
          noiseArray[index] += d13 * d;
          index++;
        }
      }
    }
  }
}
