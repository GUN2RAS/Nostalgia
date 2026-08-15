/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class kf
extends ly {
    private boolean a = true;

    public kf(int n2, int n3) {
        super(n2, n3, gb.n);
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }

    public int a(int n2, int n3) {
        return this.bb + (n3 > 0 ? 16 : 0);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 5;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        return cn2.g(n2, n3 - 1, n4);
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8 = cn2.e(n2, n3, n4);
        int n9 = 0;
        this.a = false;
        boolean bl2 = cn2.o(n2, n3, n4);
        this.a = true;
        if (bl2) {
            n9 = 15;
        } else {
            for (n7 = 0; n7 < 4; ++n7) {
                n6 = n2;
                n5 = n4;
                if (n7 == 0) {
                    --n6;
                }
                if (n7 == 1) {
                    ++n6;
                }
                if (n7 == 2) {
                    --n5;
                }
                if (n7 == 3) {
                    ++n5;
                }
                n9 = this.g(cn2, n6, n3, n5, n9);
                if (cn2.g(n6, n3, n5) && !cn2.g(n2, n3 + 1, n4)) {
                    n9 = this.g(cn2, n6, n3 + 1, n5, n9);
                    continue;
                }
                if (cn2.g(n6, n3, n5)) continue;
                n9 = this.g(cn2, n6, n3 - 1, n5, n9);
            }
            n9 = n9 > 0 ? --n9 : 0;
        }
        if (n8 != n9) {
            cn2.b(n2, n3, n4, n9);
            cn2.b(n2, n3, n4, n2, n3, n4);
            if (n9 > 0) {
                --n9;
            }
            for (n7 = 0; n7 < 4; ++n7) {
                int n10;
                n6 = n2;
                n5 = n4;
                int n11 = n3 - 1;
                if (n7 == 0) {
                    --n6;
                }
                if (n7 == 1) {
                    ++n6;
                }
                if (n7 == 2) {
                    --n5;
                }
                if (n7 == 3) {
                    ++n5;
                }
                if (cn2.g(n6, n3, n5)) {
                    n11 += 2;
                }
                if ((n10 = this.g(cn2, n6, n3, n5, -1)) >= 0 && n10 != n9) {
                    this.h(cn2, n6, n3, n5);
                }
                if ((n10 = this.g(cn2, n6, n11, n5, -1)) < 0 || n10 == n9) continue;
                this.h(cn2, n6, n11, n5);
            }
            if (n8 == 0 || n9 == 0) {
                cn2.g(n2, n3, n4, this.bc);
                cn2.g(n2 - 1, n3, n4, this.bc);
                cn2.g(n2 + 1, n3, n4, this.bc);
                cn2.g(n2, n3, n4 - 1, this.bc);
                cn2.g(n2, n3, n4 + 1, this.bc);
                cn2.g(n2, n3 - 1, n4, this.bc);
                cn2.g(n2, n3 + 1, n4, this.bc);
            }
        }
    }

    private void i(cn cn2, int n2, int n3, int n4) {
        if (cn2.a(n2, n3, n4) != this.bc) {
            return;
        }
        cn2.g(n2, n3, n4, this.bc);
        cn2.g(n2 - 1, n3, n4, this.bc);
        cn2.g(n2 + 1, n3, n4, this.bc);
        cn2.g(n2, n3, n4 - 1, this.bc);
        cn2.g(n2, n3, n4 + 1, this.bc);
        cn2.g(n2, n3 - 1, n4, this.bc);
        cn2.g(n2, n3 + 1, n4, this.bc);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        super.e(cn2, n2, n3, n4);
        this.h(cn2, n2, n3, n4);
        cn2.g(n2, n3 + 1, n4, this.bc);
        cn2.g(n2, n3 - 1, n4, this.bc);
        this.i(cn2, n2 - 1, n3, n4);
        this.i(cn2, n2 + 1, n3, n4);
        this.i(cn2, n2, n3, n4 - 1);
        this.i(cn2, n2, n3, n4 + 1);
        if (cn2.g(n2 - 1, n3, n4)) {
            this.i(cn2, n2 - 1, n3 + 1, n4);
        } else {
            this.i(cn2, n2 - 1, n3 - 1, n4);
        }
        if (cn2.g(n2 + 1, n3, n4)) {
            this.i(cn2, n2 + 1, n3 + 1, n4);
        } else {
            this.i(cn2, n2 + 1, n3 - 1, n4);
        }
        if (cn2.g(n2, n3, n4 - 1)) {
            this.i(cn2, n2, n3 + 1, n4 - 1);
        } else {
            this.i(cn2, n2, n3 - 1, n4 - 1);
        }
        if (cn2.g(n2, n3, n4 + 1)) {
            this.i(cn2, n2, n3 + 1, n4 + 1);
        } else {
            this.i(cn2, n2, n3 - 1, n4 + 1);
        }
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        super.b(cn2, n2, n3, n4);
        cn2.g(n2, n3 + 1, n4, this.bc);
        cn2.g(n2, n3 - 1, n4, this.bc);
        this.h(cn2, n2, n3, n4);
        this.i(cn2, n2 - 1, n3, n4);
        this.i(cn2, n2 + 1, n3, n4);
        this.i(cn2, n2, n3, n4 - 1);
        this.i(cn2, n2, n3, n4 + 1);
        if (cn2.g(n2 - 1, n3, n4)) {
            this.i(cn2, n2 - 1, n3 + 1, n4);
        } else {
            this.i(cn2, n2 - 1, n3 - 1, n4);
        }
        if (cn2.g(n2 + 1, n3, n4)) {
            this.i(cn2, n2 + 1, n3 + 1, n4);
        } else {
            this.i(cn2, n2 + 1, n3 - 1, n4);
        }
        if (cn2.g(n2, n3, n4 - 1)) {
            this.i(cn2, n2, n3 + 1, n4 - 1);
        } else {
            this.i(cn2, n2, n3 - 1, n4 - 1);
        }
        if (cn2.g(n2, n3, n4 + 1)) {
            this.i(cn2, n2, n3 + 1, n4 + 1);
        } else {
            this.i(cn2, n2, n3 - 1, n4 + 1);
        }
    }

    private int g(cn cn2, int n2, int n3, int n4, int n5) {
        if (cn2.a(n2, n3, n4) != this.bc) {
            return n5;
        }
        int n6 = cn2.e(n2, n3, n4);
        if (n6 > n5) {
            return n6;
        }
        return n5;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        boolean bl2 = this.a(cn2, n2, n3, n4);
        if (!bl2) {
            this.b_(cn2, n2, n3, n4, n6);
            cn2.d(n2, n3, n4, 0);
        } else {
            this.h(cn2, n2, n3, n4);
        }
        super.a(cn2, n2, n3, n4, n5);
    }

    public int a(int n2, Random random) {
        return di.aA.aS;
    }

    public boolean c(cn cn2, int n2, int n3, int n4, int n5) {
        if (!this.a) {
            return false;
        }
        return this.b((nm)cn2, n2, n3, n4, n5);
    }

    public boolean b(nm nm2, int n2, int n3, int n4, int n5) {
        boolean bl2;
        if (!this.a) {
            return false;
        }
        if (nm2.e(n2, n3, n4) == 0) {
            return false;
        }
        if (n5 == 1) {
            return true;
        }
        boolean bl3 = kf.b(nm2, n2 - 1, n3, n4) || !nm2.g(n2 - 1, n3, n4) && kf.b(nm2, n2 - 1, n3 - 1, n4);
        boolean bl4 = kf.b(nm2, n2 + 1, n3, n4) || !nm2.g(n2 + 1, n3, n4) && kf.b(nm2, n2 + 1, n3 - 1, n4);
        boolean bl5 = kf.b(nm2, n2, n3, n4 - 1) || !nm2.g(n2, n3, n4 - 1) && kf.b(nm2, n2, n3 - 1, n4 - 1);
        boolean bl6 = bl2 = kf.b(nm2, n2, n3, n4 + 1) || !nm2.g(n2, n3, n4 + 1) && kf.b(nm2, n2, n3 - 1, n4 + 1);
        if (!nm2.g(n2, n3 + 1, n4)) {
            if (nm2.g(n2 - 1, n3, n4) && kf.b(nm2, n2 - 1, n3 + 1, n4)) {
                bl3 = true;
            }
            if (nm2.g(n2 + 1, n3, n4) && kf.b(nm2, n2 + 1, n3 + 1, n4)) {
                bl4 = true;
            }
            if (nm2.g(n2, n3, n4 - 1) && kf.b(nm2, n2, n3 + 1, n4 - 1)) {
                bl5 = true;
            }
            if (nm2.g(n2, n3, n4 + 1) && kf.b(nm2, n2, n3 + 1, n4 + 1)) {
                bl2 = true;
            }
        }
        if (!(bl5 || bl4 || bl3 || bl2 || n5 < 2 || n5 > 5)) {
            return true;
        }
        if (n5 == 2 && bl5 && !bl3 && !bl4) {
            return true;
        }
        if (n5 == 3 && bl2 && !bl3 && !bl4) {
            return true;
        }
        if (n5 == 4 && bl3 && !bl5 && !bl2) {
            return true;
        }
        return n5 == 5 && bl4 && !bl5 && !bl2;
    }

    public boolean d() {
        return this.a;
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        if (cn2.e(n2, n3, n4) > 0) {
            double d2 = (double)n2 + 0.5 + ((double)random.nextFloat() - 0.5) * 0.2;
            double d3 = (float)n3 + 0.0625f;
            double d4 = (double)n4 + 0.5 + ((double)random.nextFloat() - 0.5) * 0.2;
            cn2.a("reddust", d2, d3, d4, 0.0, 0.0, 0.0);
        }
    }

    public static boolean b(nm nm2, int n2, int n3, int n4) {
        int n5 = nm2.a(n2, n3, n4);
        if (n5 == ly.aw.bc) {
            return true;
        }
        if (n5 == 0) {
            return false;
        }
        return ly.n[n5].d();
    }
}

