/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class jm
extends ly {
    protected jm(int n2, int n3) {
        super(n2, gb.i);
        this.bb = n3;
        float f2 = 0.375f;
        this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, 1.0f, 0.5f + f2);
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

    public boolean a(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3 - 1, n4);
        if (n5 == this.bc) {
            return true;
        }
        if (n5 != ly.v.bc && n5 != ly.w.bc) {
            return false;
        }
        if (cn2.f(n2 - 1, n3 - 1, n4) == gb.f) {
            return true;
        }
        if (cn2.f(n2 + 1, n3 - 1, n4) == gb.f) {
            return true;
        }
        if (cn2.f(n2, n3 - 1, n4 - 1) == gb.f) {
            return true;
        }
        return cn2.f(n2, n3 - 1, n4 + 1) == gb.f;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        this.h(cn2, n2, n3, n4);
    }

    protected final void h(cn cn2, int n2, int n3, int n4) {
        if (!this.g(cn2, n2, n3, n4)) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        }
    }

    public boolean g(cn cn2, int n2, int n3, int n4) {
        return this.a(cn2, n2, n3, n4);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public int a(int n2, Random random) {
        return di.aH.aS;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 1;
    }
}

