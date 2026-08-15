/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class hy
extends ly {
    protected hy(int n2, int n3) {
        super(n2, n3, gb.u);
        this.b(true);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (cn2.a(n2, n3 + 1, n4) == 0) {
            int n5 = 1;
            while (cn2.a(n2, n3 - n5, n4) == this.bc) {
                ++n5;
            }
            if (n5 < 3) {
                int n6 = cn2.e(n2, n3, n4);
                if (n6 == 15) {
                    cn2.d(n2, n3 + 1, n4, this.bc);
                    cn2.b(n2, n3, n4, 0);
                } else {
                    cn2.b(n2, n3, n4, n6 + 1);
                }
            }
        }
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        float f2 = 0.0625f;
        return cf.b((float)n2 + f2, n3, (float)n4 + f2, (float)(n2 + 1) - f2, (float)(n3 + 1) - f2, (float)(n4 + 1) - f2);
    }

    public cf f(cn cn2, int n2, int n3, int n4) {
        float f2 = 0.0625f;
        return cf.b((float)n2 + f2, n3, (float)n4 + f2, (float)(n2 + 1) - f2, n3 + 1, (float)(n4 + 1) - f2);
    }

    public int a(int n2) {
        if (n2 == 1) {
            return this.bb - 1;
        }
        if (n2 == 0) {
            return this.bb + 1;
        }
        return this.bb;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int f() {
        return 13;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        if (!super.a(cn2, n2, n3, n4)) {
            return false;
        }
        return this.g(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (!this.g(cn2, n2, n3, n4)) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        }
    }

    public boolean g(cn cn2, int n2, int n3, int n4) {
        if (cn2.f(n2 - 1, n3, n4).a()) {
            return false;
        }
        if (cn2.f(n2 + 1, n3, n4).a()) {
            return false;
        }
        if (cn2.f(n2, n3, n4 - 1).a()) {
            return false;
        }
        if (cn2.f(n2, n3, n4 + 1).a()) {
            return false;
        }
        int n5 = cn2.a(n2, n3 - 1, n4);
        return n5 == ly.aW.bc || n5 == ly.F.bc;
    }

    public void b(cn cn2, int n2, int n3, int n4, kh kh2) {
        kh2.a(null, 1);
    }
}

