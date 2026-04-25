package net.nostalgia.alphalogic.core;

public class AlphaMathHelper {
    private static float[] SIN_TABLE = new float[65536];

    public static final float sin(float value) {
        return SIN_TABLE[(int)(value * 10430.378f) & 0xFFFF];
    }

    public static final float cos(float value) {
        return SIN_TABLE[(int)(value * 10430.378f + 16384.0f) & 0xFFFF];
    }

    public static final float sqrt(float value) {
        return (float)Math.sqrt(value);
    }

    public static final float sqrt(double value) {
        return (float)Math.sqrt(value);
    }

    public static int floor(float value) {
        int i = (int)value;
        return value < (float)i ? i - 1 : i;
    }

    public static int floor(double value) {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }

    public static float abs(float value) {
        return value >= 0.0f ? value : -value;
    }

    public static double absMax(double a, double b) {
        if (a < 0.0) a = -a;
        if (b < 0.0) b = -b;
        return a > b ? a : b;
    }

    public static int intFloorDiv(int x, int y) {
        if (x < 0) {
            return -((-x - 1) / y) - 1;
        }
        return x / y;
    }

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0 / 65536.0);
        }
    }
}
