/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class he
extends fc {
    public he(int n2, int n3) {
        super(n2, n3, gb.r, false);
        this.bo = 0.98f;
        this.b(true);
    }

    public int g() {
        return 1;
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        return super.c(nm2, n2, n3, n4, 1 - n5);
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        gb gb2 = cn2.f(n2, n3 - 1, n4);
        if (gb2.c() || gb2.d()) {
            cn2.d(n2, n3, n4, ly.B.bc);
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (cn2.a(by.b, n2, n3, n4) > 11 - ly.r[this.bc]) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, ly.C.bc);
        }
    }
}

