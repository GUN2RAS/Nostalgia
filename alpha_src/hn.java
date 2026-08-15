/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class hn
extends jp {
    protected hn(int n2, gb gb2) {
        super(n2, gb2);
        this.b(false);
        if (gb2 == gb.g) {
            this.b(true);
        }
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        super.a(cn2, n2, n3, n4, n5);
        if (cn2.a(n2, n3, n4) == this.bc) {
            this.j(cn2, n2, n3, n4);
        }
    }

    private void j(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        cn2.h = true;
        cn2.a(n2, n3, n4, this.bc - 1, n5);
        cn2.b(n2, n3, n4, n2, n3, n4);
        cn2.h(n2, n3, n4, this.bc - 1);
        cn2.h = false;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (this.bn == gb.g) {
            int n5 = random.nextInt(3);
            for (int i2 = 0; i2 < n5; ++i2) {
                int n6 = cn2.a(n2 += random.nextInt(3) - 1, ++n3, n4 += random.nextInt(3) - 1);
                if (n6 == 0) {
                    if (!this.k(cn2, n2 - 1, n3, n4) && !this.k(cn2, n2 + 1, n3, n4) && !this.k(cn2, n2, n3, n4 - 1) && !this.k(cn2, n2, n3, n4 + 1) && !this.k(cn2, n2, n3 - 1, n4) && !this.k(cn2, n2, n3 + 1, n4)) continue;
                    cn2.d(n2, n3, n4, ly.as.bc);
                    return;
                }
                if (!ly.n[n6].bn.c()) continue;
                return;
            }
        }
    }

    private boolean k(cn cn2, int n2, int n3, int n4) {
        return cn2.f(n2, n3, n4).e();
    }
}

