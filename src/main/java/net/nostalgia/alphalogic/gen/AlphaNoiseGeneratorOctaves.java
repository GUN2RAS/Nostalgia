package net.nostalgia.alphalogic.gen;

import java.util.Random;

public class AlphaNoiseGeneratorOctaves {
    private AlphaNoiseGeneratorPerlin[] generatorCollection;
    private int octaves;

    public AlphaNoiseGeneratorOctaves(Random random, int octaves) {
        this.octaves = octaves;
        this.generatorCollection = new AlphaNoiseGeneratorPerlin[octaves];
        for (int i = 0; i < octaves; ++i) {
            this.generatorCollection[i] = new AlphaNoiseGeneratorPerlin(random);
        }
    }

    public double generateNoise(double x, double z) {
        double d = 0.0;
        double d1 = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            d += this.generatorCollection[i].generateNoise(x * d1, z * d1) / d1;
            d1 /= 2.0;
        }
        return d;
    }

    public double[] generateNoiseOctaves(double[] noiseArray, double xOffset, double yOffset, double zOffset, int xSize, int ySize, int zSize, double xScale, double yScale, double zScale) {
        if (noiseArray == null) {
            noiseArray = new double[xSize * ySize * zSize];
        } else {
            for (int i = 0; i < noiseArray.length; ++i) {
                noiseArray[i] = 0.0;
            }
        }
        double d = 1.0;
        for (int i = 0; i < this.octaves; ++i) {
            this.generatorCollection[i].generateNoiseArray(noiseArray, xOffset, yOffset, zOffset, xSize, ySize, zSize, xScale * d, yScale * d, zScale * d, d);
            d /= 2.0;
        }
        return noiseArray;
    }
}
