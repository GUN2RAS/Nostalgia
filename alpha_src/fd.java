/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class fd
extends ly {
    protected fd(int n2, int n3) {
        super(n2, n3, gb.s);
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        this.b(true);
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

    public boolean a(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3 - 1, n4);
        if (n5 == 0 || !ly.n[n5].b()) {
            return false;
        }
        return cn2.f(n2, n3 - 1, n4).c();
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        this.h(cn2, n2, n3, n4);
    }

    private boolean h(cn cn2, int n2, int n3, int n4) {
        if (!this.a(cn2, n2, n3, n4)) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
            return false;
        }
        return true;
    }

    public void a_(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = di.aB.aS;
        float f2 = 0.7f;
        double d2 = (double)(cn2.n.nextFloat() * f2) + (double)(1.0f - f2) * 0.5;
        double d3 = (double)(cn2.n.nextFloat() * f2) + (double)(1.0f - f2) * 0.5;
        double d4 = (double)(cn2.n.nextFloat() * f2) + (double)(1.0f - f2) * 0.5;
        dx dx2 = new dx(cn2, (double)n2 + d2, (double)n3 + d3, (double)n4 + d4, new ev(n6));
        dx2.c = 10;
        cn2.a(dx2);
        cn2.d(n2, n3, n4, 0);
    }

    public int a(int n2, Random random) {
        return di.aB.aS;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (cn2.a(by.b, n2, n3, n4) > 11) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        }
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        gb gb2 = nm2.f(n2, n3, n4);
        if (n5 == 1) {
            return true;
        }
        if (gb2 == this.bn) {
            return false;
        }
        return super.c(nm2, n2, n3, n4, n5);
    }
}

