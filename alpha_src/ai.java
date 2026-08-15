/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class ai
extends ly {
    private boolean a;

    public ai(int n2, int n3, boolean bl2) {
        super(n2, n3, gb.d);
        if (bl2) {
            this.b(true);
        }
        this.a = bl2;
    }

    public int a() {
        return 30;
    }

    public void b(cn cn2, int n2, int n3, int n4, dm dm2) {
        this.h(cn2, n2, n3, n4);
        super.b(cn2, n2, n3, n4, dm2);
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2) {
        this.h(cn2, n2, n3, n4);
        super.a(cn2, n2, n3, n4, kh2);
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        this.h(cn2, n2, n3, n4);
        return super.a(cn2, n2, n3, n4, dm2);
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        this.i(cn2, n2, n3, n4);
        if (this.bc == ly.aO.bc) {
            cn2.d(n2, n3, n4, ly.aP.bc);
        }
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (this.bc == ly.aP.bc) {
            cn2.d(n2, n3, n4, ly.aO.bc);
        }
    }

    public int a(int n2, Random random) {
        return di.aA.aS;
    }

    public int a(Random random) {
        return 4 + random.nextInt(2);
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        if (this.a) {
            this.i(cn2, n2, n3, n4);
        }
    }

    private void i(cn cn2, int n2, int n3, int n4) {
        Random random = cn2.n;
        double d2 = 0.0625;
        for (int i2 = 0; i2 < 6; ++i2) {
            double d3 = (float)n2 + random.nextFloat();
            double d4 = (float)n3 + random.nextFloat();
            double d5 = (float)n4 + random.nextFloat();
            if (i2 == 0 && !cn2.g(n2, n3 + 1, n4)) {
                d4 = (double)(n3 + 1) + d2;
            }
            if (i2 == 1 && !cn2.g(n2, n3 - 1, n4)) {
                d4 = (double)(n3 + 0) - d2;
            }
            if (i2 == 2 && !cn2.g(n2, n3, n4 + 1)) {
                d5 = (double)(n4 + 1) + d2;
            }
            if (i2 == 3 && !cn2.g(n2, n3, n4 - 1)) {
                d5 = (double)(n4 + 0) - d2;
            }
            if (i2 == 4 && !cn2.g(n2 + 1, n3, n4)) {
                d3 = (double)(n2 + 1) + d2;
            }
            if (i2 == 5 && !cn2.g(n2 - 1, n3, n4)) {
                d3 = (double)(n2 + 0) - d2;
            }
            if (!(d3 < (double)n2 || d3 > (double)(n2 + 1) || d4 < 0.0 || d4 > (double)(n3 + 1) || d5 < (double)n4) && !(d5 > (double)(n4 + 1))) continue;
            cn2.a("reddust", d3, d4, d5, 0.0, 0.0, 0.0);
        }
    }
}

