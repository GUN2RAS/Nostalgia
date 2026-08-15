/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class q
extends ly {
    public q(int n2, int n3) {
        super(n2, n3, gb.p);
    }

    public int a(int n2) {
        if (n2 == 0) {
            return this.bb + 2;
        }
        if (n2 == 1) {
            return this.bb + 1;
        }
        return this.bb;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (n5 > 0 && ly.n[n5].d() && cn2.o(n2, n3, n4)) {
            this.b(cn2, n2, n3, n4, 0);
            cn2.d(n2, n3, n4, 0);
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void c(cn cn2, int n2, int n3, int n4) {
        jd jd2 = new jd(cn2, (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f);
        jd2.a = cn2.n.nextInt(jd2.a / 4) + jd2.a / 8;
        cn2.a(jd2);
    }

    public void b(cn cn2, int n2, int n3, int n4, int n5) {
        jd jd2 = new jd(cn2, (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f);
        cn2.a(jd2);
        cn2.a(jd2, "random.fuse", 1.0f, 1.0f);
    }
}

