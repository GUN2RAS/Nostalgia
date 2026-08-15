/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class mi
extends ly {
    protected mi(int n2) {
        super(n2, gb.b);
        this.bb = 87;
        this.b(true);
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
        this.d(255);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return cf.b(n2 + 0, n3 + 0, n4 + 0, n2 + 1, n3 + 1, n4 + 1);
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int a(int n2, int n3) {
        if (n2 == 1 && n3 > 0) {
            return this.bb - 1;
        }
        if (n2 == 1) {
            return this.bb;
        }
        return 2;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (random.nextInt(5) == 0) {
            if (this.i(cn2, n2, n3, n4)) {
                cn2.b(n2, n3, n4, 7);
            } else {
                int n5 = cn2.e(n2, n3, n4);
                if (n5 > 0) {
                    cn2.b(n2, n3, n4, n5 - 1);
                } else if (!this.h(cn2, n2, n3, n4)) {
                    cn2.d(n2, n3, n4, ly.w.bc);
                }
            }
        }
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2) {
        if (cn2.n.nextInt(4) == 0) {
            cn2.d(n2, n3, n4, ly.w.bc);
        }
    }

    private boolean h(cn cn2, int n2, int n3, int n4) {
        int n5 = 0;
        for (int i2 = n2 - n5; i2 <= n2 + n5; ++i2) {
            for (int i3 = n4 - n5; i3 <= n4 + n5; ++i3) {
                if (cn2.a(i2, n3 + 1, i3) != ly.aA.bc) continue;
                return true;
            }
        }
        return false;
    }

    private boolean i(cn cn2, int n2, int n3, int n4) {
        for (int i2 = n2 - 4; i2 <= n2 + 4; ++i2) {
            for (int i3 = n3; i3 <= n3 + 1; ++i3) {
                for (int i4 = n4 - 4; i4 <= n4 + 4; ++i4) {
                    if (cn2.f(i2, i3, i4) != gb.f) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        super.a(cn2, n2, n3, n4, n5);
        gb gb2 = cn2.f(n2, n3 + 1, n4);
        if (gb2.a()) {
            cn2.d(n2, n3, n4, ly.w.bc);
        }
    }

    public int a(int n2, Random random) {
        return ly.w.a(0, random);
    }
}

