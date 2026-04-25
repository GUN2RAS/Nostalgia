package net.nostalgia.alphalogic;

import net.minecraft.util.Mth;

public class ExperimentalMathBridge {

    private static final int TABLE_SIZE = 256;
    private static final float INV_TABLE_SIZE = 1.0F / (float) TABLE_SIZE;

    private static final float[] SINE_TABLE = new float[TABLE_SIZE];
    private static final float[] COSINE_TABLE = new float[TABLE_SIZE];

    private static boolean tablesInitialized = false;

    public static void ensureTablesReady() {
        if (tablesInitialized) {
            return;
        }
        for (int i = 0; i < TABLE_SIZE; i++) {
            float angle = (float) i * INV_TABLE_SIZE * (float) (Math.PI * 2.0);
            SINE_TABLE[i] = Mth.sin(angle);
            COSINE_TABLE[i] = Mth.cos(angle);
        }
        tablesInitialized = true;
    }

    public static float lookupSin(float radians) {
        ensureTablesReady();
        int index = (int) (radians / (Math.PI * 2.0) * TABLE_SIZE) & (TABLE_SIZE - 1);
        return SINE_TABLE[index];
    }

    public static float lookupCos(float radians) {
        ensureTablesReady();
        int index = (int) (radians / (Math.PI * 2.0) * TABLE_SIZE) & (TABLE_SIZE - 1);
        return COSINE_TABLE[index];
    }

    public static float calculateVintageFogDensity(int altitude) {
        float density = 0.05F;
        if (altitude < 64) {
            density += (64 - altitude) * 0.001F;
        }
        return Math.min(density, 0.12F);
    }

    public static double smoothLerp(double a, double b, double t) {
        double clamped = Math.max(0.0, Math.min(1.0, t));
        double smoothed = clamped * clamped * (3.0 - 2.0 * clamped);
        return a + (b - a) * smoothed;
    }

    public static float wrapAngleDegrees(float angle) {
        float result = angle % 360.0F;
        if (result >= 180.0F) {
            result -= 360.0F;
        }
        if (result < -180.0F) {
            result += 360.0F;
        }
        return result;
    }
}
