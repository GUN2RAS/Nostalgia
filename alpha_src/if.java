/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class if
extends ly {
    protected if(int n2, int n3) {
        super(n2, n3, gb.n);
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public boolean b() {
        return false;
    }

    public mf a(cn cn2, int n2, int n3, int n4, aj aj2, aj aj3) {
        this.a((nm)cn2, n2, n3, n4);
        return super.a(cn2, n2, n3, n4, aj2, aj3);
    }

    public void a(nm nm2, int n2, int n3, int n4) {
        int n5 = nm2.e(n2, n3, n4);
        if (n5 >= 2 && n5 <= 5) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f);
        } else {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        }
    }

    public int a(int n2, int n3) {
        if (n3 >= 6) {
            return this.bb - 16;
        }
        return this.bb;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 9;
    }

    public int a(Random random) {
        return 1;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        return cn2.g(n2, n3 - 1, n4);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        cn2.b(n2, n3, n4, 15);
        this.h(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        boolean bl2 = false;
        if (!cn2.g(n2, n3 - 1, n4)) {
            bl2 = true;
        }
        if (n6 == 2 && !cn2.g(n2 + 1, n3, n4)) {
            bl2 = true;
        }
        if (n6 == 3 && !cn2.g(n2 - 1, n3, n4)) {
            bl2 = true;
        }
        if (n6 == 4 && !cn2.g(n2, n3, n4 - 1)) {
            bl2 = true;
        }
        if (n6 == 5 && !cn2.g(n2, n3, n4 + 1)) {
            bl2 = true;
        }
        if (bl2) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        } else if (n5 > 0 && ly.n[n5].d() && mk.a(new mk(this, cn2, n2, n3, n4)) == 3) {
            this.h(cn2, n2, n3, n4);
        }
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        new mk(this, cn2, n2, n3, n4).a(cn2.o(n2, n3, n4));
    }
}

