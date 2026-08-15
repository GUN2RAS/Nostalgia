/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class dh
extends ly {
    public static boolean a = false;

    public dh(int n2, int n3) {
        super(n2, n3, gb.m);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        cn2.h(n2, n3, n4, this.bc);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        cn2.h(n2, n3, n4, this.bc);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        this.h(cn2, n2, n3, n4);
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        int n5 = n2;
        int n6 = n3;
        int n7 = n4;
        if (dh.a_(cn2, n5, n6 - 1, n7) && n6 >= 0) {
            ff ff2 = new ff(cn2, (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, this.bc);
            if (a) {
                while (!ff2.aA) {
                    ff2.e_();
                }
            } else {
                cn2.a(ff2);
            }
        }
    }

    public int a() {
        return 3;
    }

    public static boolean a_(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3, n4);
        if (n5 == 0) {
            return true;
        }
        if (n5 == ly.as.bc) {
            return true;
        }
        gb gb2 = ly.n[n5].bn;
        if (gb2 == gb.f) {
            return true;
        }
        return gb2 == gb.g;
    }
}

