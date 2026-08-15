/*
 * Decompiled with CFR 0.152.
 */
public class ir {
    public float a;
    private double f;
    public int b;
    public float c;
    public float d = 1.0f;
    public float e = 0.0f;
    private long g;
    private long h;
    private double i = 1.0;

    public ir(float f2) {
        this.a = f2;
        this.g = System.currentTimeMillis();
        this.h = System.nanoTime() / 1000000L;
    }

    public void a() {
        double d2;
        long l2 = System.currentTimeMillis();
        long l3 = l2 - this.g;
        long l4 = System.nanoTime() / 1000000L;
        if (l3 > 1000L) {
            long l5 = l4 - this.h;
            d2 = (double)l3 / (double)l5;
            this.i += (d2 - this.i) * (double)0.2f;
            this.g = l2;
            this.h = l4;
        }
        if (l3 < 0L) {
            this.g = l2;
            this.h = l4;
        }
        double d3 = (double)l4 / 1000.0;
        d2 = (d3 - this.f) * this.i;
        this.f = d3;
        if (d2 < 0.0) {
            d2 = 0.0;
        }
        if (d2 > 1.0) {
            d2 = 1.0;
        }
        this.e = (float)((double)this.e + d2 * (double)this.d * (double)this.a);
        this.b = (int)this.e;
        this.e -= (float)this.b;
        if (this.b > 10) {
            this.b = 10;
        }
        this.c = this.e;
    }
}

