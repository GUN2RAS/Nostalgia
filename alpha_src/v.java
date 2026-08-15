/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class v
extends bk {
    private int[] d = new int[512];
    public double a;
    public double b;
    public double c;

    public v() {
        this(new Random());
    }

    public v(Random random) {
        int n2;
        this.a = random.nextDouble() * 256.0;
        this.b = random.nextDouble() * 256.0;
        this.c = random.nextDouble() * 256.0;
        for (n2 = 0; n2 < 256; ++n2) {
            this.d[n2] = n2;
        }
        for (n2 = 0; n2 < 256; ++n2) {
            int n3 = random.nextInt(256 - n2) + n2;
            int n4 = this.d[n2];
            this.d[n2] = this.d[n3];
            this.d[n3] = n4;
            this.d[n2 + 256] = this.d[n2];
        }
    }

    public double a(double d2, double d3, double d4) {
        double d5 = d2 + this.a;
        double d6 = d3 + this.b;
        double d7 = d4 + this.c;
        int n2 = (int)d5;
        int n3 = (int)d6;
        int n4 = (int)d7;
        if (d5 < (double)n2) {
            --n2;
        }
        if (d6 < (double)n3) {
            --n3;
        }
        if (d7 < (double)n4) {
            --n4;
        }
        int n5 = n2 & 0xFF;
        int n6 = n3 & 0xFF;
        int n7 = n4 & 0xFF;
        double d8 = (d5 -= (double)n2) * d5 * d5 * (d5 * (d5 * 6.0 - 15.0) + 10.0);
        double d9 = (d6 -= (double)n3) * d6 * d6 * (d6 * (d6 * 6.0 - 15.0) + 10.0);
        double d10 = (d7 -= (double)n4) * d7 * d7 * (d7 * (d7 * 6.0 - 15.0) + 10.0);
        int n8 = this.d[n5] + n6;
        int n9 = this.d[n8] + n7;
        int n10 = this.d[n8 + 1] + n7;
        int n11 = this.d[n5 + 1] + n6;
        int n12 = this.d[n11] + n7;
        int n13 = this.d[n11 + 1] + n7;
        return this.b(d10, this.b(d9, this.b(d8, this.a(this.d[n9], d5, d6, d7), this.a(this.d[n12], d5 - 1.0, d6, d7)), this.b(d8, this.a(this.d[n10], d5, d6 - 1.0, d7), this.a(this.d[n13], d5 - 1.0, d6 - 1.0, d7))), this.b(d9, this.b(d8, this.a(this.d[n9 + 1], d5, d6, d7 - 1.0), this.a(this.d[n12 + 1], d5 - 1.0, d6, d7 - 1.0)), this.b(d8, this.a(this.d[n10 + 1], d5, d6 - 1.0, d7 - 1.0), this.a(this.d[n13 + 1], d5 - 1.0, d6 - 1.0, d7 - 1.0))));
    }

    public double b(double d2, double d3, double d4) {
        return d3 + d2 * (d4 - d3);
    }

    public double a(int n2, double d2, double d3, double d4) {
        double d5;
        int n3 = n2 & 0xF;
        double d6 = d5 = n3 < 8 ? d2 : d3;
        double d7 = n3 < 4 ? d3 : (n3 == 12 || n3 == 14 ? d2 : d4);
        return ((n3 & 1) == 0 ? d5 : -d5) + ((n3 & 2) == 0 ? d7 : -d7);
    }

    public double a(double d2, double d3) {
        return this.a(d2, d3, 0.0);
    }

    public void a(double[] dArray, double d2, double d3, double d4, int n2, int n3, int n4, double d5, double d6, double d7, double d8) {
        int n5 = 0;
        double d9 = 1.0 / d8;
        int n6 = -1;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = 0;
        int n12 = 0;
        double d10 = 0.0;
        double d11 = 0.0;
        double d12 = 0.0;
        double d13 = 0.0;
        for (int i2 = 0; i2 < n2; ++i2) {
            double d14 = (d2 + (double)i2) * d5 + this.a;
            int n13 = (int)d14;
            if (d14 < (double)n13) {
                --n13;
            }
            int n14 = n13 & 0xFF;
            double d15 = (d14 -= (double)n13) * d14 * d14 * (d14 * (d14 * 6.0 - 15.0) + 10.0);
            for (int i3 = 0; i3 < n4; ++i3) {
                double d16 = (d4 + (double)i3) * d7 + this.c;
                int n15 = (int)d16;
                if (d16 < (double)n15) {
                    --n15;
                }
                int n16 = n15 & 0xFF;
                double d17 = (d16 -= (double)n15) * d16 * d16 * (d16 * (d16 * 6.0 - 15.0) + 10.0);
                for (int i4 = 0; i4 < n3; ++i4) {
                    double d18 = (d3 + (double)i4) * d6 + this.b;
                    int n17 = (int)d18;
                    if (d18 < (double)n17) {
                        --n17;
                    }
                    int n18 = n17 & 0xFF;
                    double d19 = (d18 -= (double)n17) * d18 * d18 * (d18 * (d18 * 6.0 - 15.0) + 10.0);
                    if (i4 == 0 || n18 != n6) {
                        n6 = n18;
                        n7 = this.d[n14] + n18;
                        n8 = this.d[n7] + n16;
                        n9 = this.d[n7 + 1] + n16;
                        n10 = this.d[n14 + 1] + n18;
                        n11 = this.d[n10] + n16;
                        n12 = this.d[n10 + 1] + n16;
                        d10 = this.b(d15, this.a(this.d[n8], d14, d18, d16), this.a(this.d[n11], d14 - 1.0, d18, d16));
                        d11 = this.b(d15, this.a(this.d[n9], d14, d18 - 1.0, d16), this.a(this.d[n12], d14 - 1.0, d18 - 1.0, d16));
                        d12 = this.b(d15, this.a(this.d[n8 + 1], d14, d18, d16 - 1.0), this.a(this.d[n11 + 1], d14 - 1.0, d18, d16 - 1.0));
                        d13 = this.b(d15, this.a(this.d[n9 + 1], d14, d18 - 1.0, d16 - 1.0), this.a(this.d[n12 + 1], d14 - 1.0, d18 - 1.0, d16 - 1.0));
                    }
                    double d20 = this.b(d19, d10, d11);
                    double d21 = this.b(d19, d12, d13);
                    double d22 = this.b(d17, d20, d21);
                    int n19 = n5++;
                    dArray[n19] = dArray[n19] + d22 * d9;
                }
            }
        }
    }
}

