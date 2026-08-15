/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class mq
extends ly {
    protected mq(int n2, int n3) {
        super(n2, gb.i);
        this.bb = n3;
        this.b(true);
        float f2 = 0.2f;
        this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, f2 * 3.0f, 0.5f + f2);
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        return this.b(cn2.a(n2, n3 - 1, n4));
    }

    protected boolean b(int n2) {
        return n2 == ly.v.bc || n2 == ly.w.bc || n2 == ly.aB.bc;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        super.a(cn2, n2, n3, n4, n5);
        this.h(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        this.h(cn2, n2, n3, n4);
    }

    protected final void h(cn cn2, int n2, int n3, int n4) {
        if (!this.g(cn2, n2, n3, n4)) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        }
    }

    public boolean g(cn cn2, int n2, int n3, int n4) {
        return (cn2.j(n2, n3, n4) >= 8 || cn2.i(n2, n3, n4)) && this.b(cn2.a(n2, n3 - 1, n4));
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
        return 1;
    }
}

