package net.nostalgia.alphalogic.gen;

import java.util.Random;

public class AlphaLevelSource {
    private Random rand;
    private AlphaNoiseGeneratorOctaves minLimitPerlinNoise;
    private AlphaNoiseGeneratorOctaves maxLimitPerlinNoise;
    private AlphaNoiseGeneratorOctaves mainPerlinNoise;
    private AlphaNoiseGeneratorOctaves scaleNoise;
    private AlphaNoiseGeneratorOctaves depthNoise;
    public AlphaNoiseGeneratorOctaves treeNoise; 
    private AlphaNoiseGeneratorOctaves surfaceNoise;
    private AlphaNoiseGeneratorOctaves scaleNoiseVolumetric;
    private AlphaNoiseGeneratorOctaves depthNoise2;
    private AlphaMapGenBase caveGenerator;
    public boolean isSnowMode;
    
    private double[] noiseArray;
    private double[] stoneNoise = new double[256];
    private double[] sandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] depthNoiseArr = new double[256];
    
    double[] noise3;
    double[] noise1;
    double[] noise2;
    double[] noise6;
    double[] noise5;
    
    public AlphaLevelSource(long seed) {
        this.rand = new Random(seed);
        this.isSnowMode = new Random(seed).nextInt(4) == 0;
        this.caveGenerator = new AlphaMapGenCaves();
        this.minLimitPerlinNoise = new AlphaNoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new AlphaNoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new AlphaNoiseGeneratorOctaves(this.rand, 8);
        this.scaleNoise = new AlphaNoiseGeneratorOctaves(this.rand, 4);
        this.depthNoise = new AlphaNoiseGeneratorOctaves(this.rand, 4);
        
        this.surfaceNoise = new AlphaNoiseGeneratorOctaves(this.rand, 10);
        this.scaleNoiseVolumetric = new AlphaNoiseGeneratorOctaves(this.rand, 16);
        this.depthNoise2 = new AlphaNoiseGeneratorOctaves(this.rand, 8);
        this.treeNoise = new AlphaNoiseGeneratorOctaves(this.rand, 8);
    }

    public void generateTerrain(int chunkX, int chunkZ, byte[] blocks) {
        int xSize = 4;
        int seaLevel = 64;
        int ySize = xSize + 1;
        int zHeight = 17;
        int zSize = xSize + 1;
        this.noiseArray = this.initializeNoiseField(this.noiseArray, chunkX * xSize, 0, chunkZ * xSize, ySize, zHeight, zSize);
        
        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < xSize; ++j) {
                for (int k = 0; k < 16; ++k) {
                    double d0 = 0.125;
                    double d1 = this.noiseArray[((i + 0) * zSize + (j + 0)) * zHeight + (k + 0)];
                    double d2 = this.noiseArray[((i + 0) * zSize + (j + 1)) * zHeight + (k + 0)];
                    double d3 = this.noiseArray[((i + 1) * zSize + (j + 0)) * zHeight + (k + 0)];
                    double d4 = this.noiseArray[((i + 1) * zSize + (j + 1)) * zHeight + (k + 0)];
                    double d5 = (this.noiseArray[((i + 0) * zSize + (j + 0)) * zHeight + (k + 1)] - d1) * d0;
                    double d6 = (this.noiseArray[((i + 0) * zSize + (j + 1)) * zHeight + (k + 1)] - d2) * d0;
                    double d7 = (this.noiseArray[((i + 1) * zSize + (j + 0)) * zHeight + (k + 1)] - d3) * d0;
                    double d8 = (this.noiseArray[((i + 1) * zSize + (j + 1)) * zHeight + (k + 1)] - d4) * d0;
                    
                    for (int l = 0; l < 8; ++l) {
                        double d9 = 0.25;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        
                        for (int i1 = 0; i1 < 4; ++i1) {
                            int index = i1 + i * 4 << 11 | 0 + j * 4 << 7 | k * 8 + l;
                            int j1 = 128;
                            double d14 = 0.25;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            
                            for (int k1 = 0; k1 < 4; ++k1) {
                                int blockId = 0;
                                if (k * 8 + l < seaLevel) {
                                    blockId = (this.isSnowMode && k * 8 + l >= seaLevel - 1) ? 79 : 9; 
                                }
                                if (d15 > 0.0) {
                                    blockId = 1; 
                                }
                                blocks[index] = (byte)blockId;
                                index += j1;
                                d15 += d16;
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBlocksForBiome(int chunkX, int chunkZ, byte[] blocks) {
        int seaLevel = 64;
        double d0 = 0.03125;
        this.sandNoise = this.scaleNoise.generateNoiseOctaves(this.sandNoise, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, d0, d0, 1.0);
        this.gravelNoise = this.scaleNoise.generateNoiseOctaves(this.gravelNoise, chunkZ * 16, 109.0134, chunkX * 16, 16, 1, 16, d0, 1.0, d0);
        this.depthNoiseArr = this.depthNoise.generateNoiseOctaves(this.depthNoiseArr, chunkX * 16, chunkZ * 16, 0.0, 16, 16, 1, d0 * 2.0, d0 * 2.0, d0 * 2.0);
        
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                boolean isSand = this.sandNoise[i + j * 16] + this.rand.nextDouble() * 0.2 > 0.0;
                boolean isGravel = this.gravelNoise[i + j * 16] + this.rand.nextDouble() * 0.2 > 3.0; 
                int depth = (int)(this.depthNoiseArr[i + j * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
                int run = -1;
                byte topBlock = 2; 
                byte fillerBlock = 3; 
                
                for (int k = 127; k >= 0; --k) {
                    int index = (i * 16 + j) * 128 + k;
                    if (k <= 0 + this.rand.nextInt(6) - 1) {
                        blocks[index] = 7; 
                        continue;
                    }
                    byte currentBlock = blocks[index];
                    if (currentBlock == 0) {
                        run = -1;
                        continue;
                    }
                    if (currentBlock != 1) continue; 

                    if (run == -1) {
                        if (depth <= 0) {
                            topBlock = 0;
                            fillerBlock = 1; 
                        } else if (k >= seaLevel - 4 && k <= seaLevel + 1) {
                            topBlock = 2;
                            fillerBlock = 3;
                            if (isGravel) {
                                topBlock = 0;
                                fillerBlock = 13; 
                            }
                            if (isSand) {
                                topBlock = 12;
                                fillerBlock = 12; 
                            }
                        }
                        if (k < seaLevel && topBlock == 0) {
                            topBlock = 9; 
                        }
                        run = depth;
                        if (k >= seaLevel - 1) {
                            blocks[index] = topBlock;
                            continue;
                        }
                        blocks[index] = fillerBlock;
                        continue;
                    }
                    if (run <= 0) continue;
                    --run;
                    blocks[index] = fillerBlock;
                }
            }
        }
    }

    private double[] initializeNoiseField(double[] outArray, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize) {
        if (outArray == null) {
            outArray = new double[xSize * ySize * zSize];
        }
        double d0 = 684.412;
        double d1 = 684.412;

        this.noise5 = this.surfaceNoise.generateNoiseOctaves(this.noise5, xOffset, yOffset, zOffset, xSize, 1, zSize, 1.0, 0.0, 1.0);
        this.noise6 = this.scaleNoiseVolumetric.generateNoiseOctaves(this.noise6, xOffset, yOffset, zOffset, xSize, 1, zSize, 100.0, 0.0, 100.0);
        this.noise3 = this.mainPerlinNoise.generateNoiseOctaves(this.noise3, xOffset, yOffset, zOffset, xSize, ySize, zSize, d0 / 80.0, d1 / 160.0, d0 / 80.0);
        this.noise1 = this.minLimitPerlinNoise.generateNoiseOctaves(this.noise1, xOffset, yOffset, zOffset, xSize, ySize, zSize, d0, d1, d0);
        this.noise2 = this.maxLimitPerlinNoise.generateNoiseOctaves(this.noise2, xOffset, yOffset, zOffset, xSize, ySize, zSize, d0, d1, d0);
        
        int index1 = 0;
        int index2 = 0;
        for (int i = 0; i < xSize; ++i) {
            for (int j = 0; j < zSize; ++j) {
                double d2 = (this.noise5[index2] + 256.0) / 512.0;
                if (d2 > 1.0) d2 = 1.0;
                double d3 = 0.0;
                double d4 = this.noise6[index2] / 8000.0;
                if (d4 < 0.0) d4 = -d4;
                d4 = d4 * 3.0 - 3.0;
                if (d4 < 0.0) {
                    if ((d4 /= 2.0) < -1.0) d4 = -1.0;
                    d4 /= 1.4;
                    d4 /= 2.0;
                    d2 = 0.0;
                } else {
                    if (d4 > 1.0) d4 = 1.0;
                    d4 /= 6.0;
                }
                d2 += 0.5;
                d4 = d4 * (double)ySize / 16.0;
                double d5 = (double)ySize / 2.0 + d4 * 4.0;
                ++index2;
                for (int k = 0; k < ySize; ++k) {
                    double d6 = 0.0;
                    double d7 = ((double)k - d5) * 12.0 / d2;
                    if (d7 < 0.0) d7 *= 4.0;
                    double d8 = this.noise1[index1] / 512.0;
                    double d9 = this.noise2[index1] / 512.0;
                    double d10 = (this.noise3[index1] / 10.0 + 1.0) / 2.0;
                    if (d10 < 0.0) d6 = d8;
                    else if (d10 > 1.0) d6 = d9;
                    else d6 = d8 + (d9 - d8) * d10;
                    d6 -= d7;
                    if (k > ySize - 4) {
                        double d11 = (float)(k - (ySize - 4)) / 3.0f;
                        d6 = d6 * (1.0 - d11) + -10.0 * d11;
                    }
                    if ((double)k < d3) {
                        double d11 = (d3 - (double)k) / 4.0;
                        if (d11 < 0.0) d11 = 0.0;
                        if (d11 > 1.0) d11 = 1.0;
                        d6 = d6 * (1.0 - d11) + -10.0 * d11;
                    }
                    outArray[index1] = d6;
                    ++index1;
                }
            }
        }
        return outArray;
    }

    public void provideChunk(int chunkX, int chunkZ, byte[] blocks) {
        this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        this.generateTerrain(chunkX, chunkZ, blocks);
        this.replaceBlocksForBiome(chunkX, chunkZ, blocks);
        this.caveGenerator.generate(this, chunkX, chunkZ, blocks);
    }
}
