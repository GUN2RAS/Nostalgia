/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class ku
extends jt {
    private final boolean a;

    protected ku(int n2, boolean bl2) {
        super(n2, gb.d);
        this.a = bl2;
        this.bb = 45;
    }

    public int a(int n2, Random random) {
        return ly.aC.bc;
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        super.e(cn2, n2, n3, n4);
        this.h(cn2, n2, n3, n4);
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3, n4 - 1);
        int n6 = cn2.a(n2, n3, n4 + 1);
        int n7 = cn2.a(n2 - 1, n3, n4);
        int n8 = cn2.a(n2 + 1, n3, n4);
        int n9 = 3;
        if (ly.p[n5] && !ly.p[n6]) {
            n9 = 3;
        }
        if (ly.p[n6] && !ly.p[n5]) {
            n9 = 2;
        }
        if (ly.p[n7] && !ly.p[n8]) {
            n9 = 5;
        }
        if (ly.p[n8] && !ly.p[n7]) {
            n9 = 4;
        }
        cn2.b(n2, n3, n4, n9);
    }

    public int a(nm nm2, int n2, int n3, int n4, int n5) {
        if (n5 == 1) {
            return ly.u.bb;
        }
        if (n5 == 0) {
            return ly.u.bb;
        }
        int n6 = nm2.e(n2, n3, n4);
        if (n5 != n6) {
            return this.bb;
        }
        if (this.a) {
            return this.bb + 16;
        }
        return this.bb - 1;
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        if (!this.a) {
            return;
        }
        int n5 = cn2.e(n2, n3, n4);
        float f2 = (float)n2 + 0.5f;
        float f3 = (float)n3 + 0.0f + random.nextFloat() * 6.0f / 16.0f;
        float f4 = (float)n4 + 0.5f;
        float f5 = 0.52f;
        float f6 = random.nextFloat() * 0.6f - 0.3f;
        if (n5 == 4) {
            cn2.a("smoke", f2 - f5, f3, f4 + f6, 0.0, 0.0, 0.0);
            cn2.a("flame", f2 - f5, f3, f4 + f6, 0.0, 0.0, 0.0);
        } else if (n5 == 5) {
            cn2.a("smoke", f2 + f5, f3, f4 + f6, 0.0, 0.0, 0.0);
            cn2.a("flame", f2 + f5, f3, f4 + f6, 0.0, 0.0, 0.0);
        } else if (n5 == 2) {
            cn2.a("smoke", f2 + f6, f3, f4 - f5, 0.0, 0.0, 0.0);
            cn2.a("flame", f2 + f6, f3, f4 - f5, 0.0, 0.0, 0.0);
        } else if (n5 == 3) {
            cn2.a("smoke", f2 + f6, f3, f4 + f5, 0.0, 0.0, 0.0);
            cn2.a("flame", f2 + f6, f3, f4 + f5, 0.0, 0.0, 0.0);
        }
    }

    public int a(int n2) {
        if (n2 == 1) {
            return ly.u.bc;
        }
        if (n2 == 0) {
            return ly.u.bc;
        }
        if (n2 == 3) {
            return this.bb - 1;
        }
        return this.bb;
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        ke ke2 = (ke)cn2.b(n2, n3, n4);
        dm2.a(ke2);
        return true;
    }

    public static void a(boolean bl2, cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        ic ic2 = cn2.b(n2, n3, n4);
        if (bl2) {
            cn2.d(n2, n3, n4, ly.aD.bc);
        } else {
            cn2.d(n2, n3, n4, ly.aC.bc);
        }
        cn2.b(n2, n3, n4, n5);
        cn2.a(n2, n3, n4, ic2);
    }

    protected ic a_() {
        return new ke();
    }
}

