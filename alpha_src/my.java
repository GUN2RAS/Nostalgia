/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class my
extends ly {
    protected my(int n2) {
        super(n2, gb.b);
        this.bb = 3;
        this.b(true);
    }

    public int a(nm nm2, int n2, int n3, int n4, int n5) {
        if (n5 == 1) {
            return 0;
        }
        if (n5 == 0) {
            return 2;
        }
        gb gb2 = nm2.f(n2, n3 + 1, n4);
        if (gb2 == gb.s || gb2 == gb.t) {
            return 68;
        }
        return 3;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        int n5;
        int n6;
        int n7;
        if (cn2.j(n2, n3 + 1, n4) < 4 && cn2.f(n2, n3 + 1, n4).b()) {
            if (random.nextInt(4) != 0) {
                return;
            }
            cn2.d(n2, n3, n4, ly.w.bc);
        } else if (cn2.j(n2, n3 + 1, n4) >= 9 && cn2.a(n7 = n2 + random.nextInt(3) - 1, n6 = n3 + random.nextInt(5) - 3, n5 = n4 + random.nextInt(3) - 1) == ly.w.bc && cn2.j(n7, n6 + 1, n5) >= 4 && !cn2.f(n7, n6 + 1, n5).b()) {
            cn2.d(n7, n6, n5, ly.v.bc);
        }
    }

    public int a(int n2, Random random) {
        return ly.w.a(0, random);
    }
}

