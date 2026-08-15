/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class iz
extends hi {
    private int b;
    private int c = 0;

    protected iz(int n2, int n3) {
        super(n2, n3, gb.h, false);
        this.b = n3;
        this.b(true);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        this.c = 0;
        this.h(cn2, n2, n3, n4);
        super.a(cn2, n2, n3, n4, n5);
    }

    public void f(cn cn2, int n2, int n3, int n4, int n5) {
        if (cn2.a(n2, n3, n4) != this.bc) {
            return;
        }
        int n6 = cn2.e(n2, n3, n4);
        if (n6 == 0 || n6 != n5 - 1) {
            return;
        }
        this.h(cn2, n2, n3, n4);
    }

    public void h(cn cn2, int n2, int n3, int n4) {
        if (this.c++ >= 100) {
            return;
        }
        int n5 = cn2.f(n2, n3 - 1, n4).a() ? 16 : 0;
        int n6 = cn2.e(n2, n3, n4);
        if (n6 == 0) {
            n6 = 1;
            cn2.b(n2, n3, n4, 1);
        }
        n5 = this.g(cn2, n2, n3 - 1, n4, n5);
        n5 = this.g(cn2, n2, n3, n4 - 1, n5);
        n5 = this.g(cn2, n2, n3, n4 + 1, n5);
        n5 = this.g(cn2, n2 - 1, n3, n4, n5);
        int n7 = (n5 = this.g(cn2, n2 + 1, n3, n4, n5)) - 1;
        if (n7 < 10) {
            n7 = 1;
        }
        if (n7 != n6) {
            cn2.b(n2, n3, n4, n7);
            this.f(cn2, n2, n3 - 1, n4, n6);
            this.f(cn2, n2, n3 + 1, n4, n6);
            this.f(cn2, n2, n3, n4 - 1, n6);
            this.f(cn2, n2, n3, n4 + 1, n6);
            this.f(cn2, n2 - 1, n3, n4, n6);
            this.f(cn2, n2 + 1, n3, n4, n6);
        }
    }

    private int g(cn cn2, int n2, int n3, int n4, int n5) {
        int n6;
        int n7 = cn2.a(n2, n3, n4);
        if (n7 == ly.K.bc) {
            return 16;
        }
        if (n7 == this.bc && (n6 = cn2.e(n2, n3, n4)) != 0 && n6 > n5) {
            return n6;
        }
        return n5;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        int n5 = cn2.e(n2, n3, n4);
        if (n5 == 0) {
            this.c = 0;
            this.h(cn2, n2, n3, n4);
        } else if (n5 == 1) {
            this.i(cn2, n2, n3, n4);
        } else if (random.nextInt(10) == 0) {
            this.h(cn2, n2, n3, n4);
        }
    }

    private void i(cn cn2, int n2, int n3, int n4) {
        this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
        cn2.d(n2, n3, n4, 0);
    }

    public int a(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public int a(int n2, Random random) {
        return ly.z.bc;
    }

    public boolean b() {
        return !this.a;
    }

    public void a(boolean bl2) {
        this.a = bl2;
        this.bb = this.b + (bl2 ? 0 : 1);
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2) {
        super.a(cn2, n2, n3, n4, kh2);
    }
}

