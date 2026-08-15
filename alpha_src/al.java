/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;
import java.util.Random;

public class al
extends ly {
    private js a;

    protected al(int n2, int n3, js js2) {
        super(n2, n3, gb.d);
        this.a = js2;
        this.b(true);
        float f2 = 0.0625f;
        this.a(f2, 0.0f, f2, 1.0f - f2, 0.03125f, 1.0f - f2);
    }

    public int a() {
        return 20;
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
        return cn2.g(n2, n3 - 1, n4);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        boolean bl2 = false;
        if (!cn2.g(n2, n3 - 1, n4)) {
            bl2 = true;
        }
        if (bl2) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        }
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        if (cn2.e(n2, n3, n4) == 0) {
            return;
        }
        this.h(cn2, n2, n3, n4);
    }

    public void b(cn cn2, int n2, int n3, int n4, kh kh2) {
        if (cn2.e(n2, n3, n4) == 1) {
            return;
        }
        this.h(cn2, n2, n3, n4);
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        boolean bl2 = cn2.e(n2, n3, n4) == 1;
        boolean bl3 = false;
        float f2 = 0.125f;
        List list = null;
        if (this.a == js.a) {
            list = cn2.b(null, cf.b((float)n2 + f2, n3, (float)n4 + f2, (float)(n2 + 1) - f2, (double)n3 + 0.25, (float)(n4 + 1) - f2));
        }
        if (this.a == js.b) {
            list = cn2.a(ge.class, cf.b((float)n2 + f2, n3, (float)n4 + f2, (float)(n2 + 1) - f2, (double)n3 + 0.25, (float)(n4 + 1) - f2));
        }
        if (this.a == js.c) {
            list = cn2.a(dm.class, cf.b((float)n2 + f2, n3, (float)n4 + f2, (float)(n2 + 1) - f2, (double)n3 + 0.25, (float)(n4 + 1) - f2));
        }
        if (list.size() > 0) {
            bl3 = true;
        }
        if (bl3 && !bl2) {
            cn2.b(n2, n3, n4, 1);
            cn2.g(n2, n3, n4, this.bc);
            cn2.g(n2, n3 - 1, n4, this.bc);
            cn2.b(n2, n3, n4, n2, n3, n4);
            cn2.a((double)n2 + 0.5, (double)n3 + 0.1, (double)n4 + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!bl3 && bl2) {
            cn2.b(n2, n3, n4, 0);
            cn2.g(n2, n3, n4, this.bc);
            cn2.g(n2, n3 - 1, n4, this.bc);
            cn2.b(n2, n3, n4, n2, n3, n4);
            cn2.a((double)n2 + 0.5, (double)n3 + 0.1, (double)n4 + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (bl3) {
            cn2.h(n2, n3, n4, this.bc);
        }
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        if (n5 > 0) {
            cn2.g(n2, n3, n4, this.bc);
            cn2.g(n2, n3 - 1, n4, this.bc);
        }
        super.b(cn2, n2, n3, n4);
    }

    public void a(nm nm2, int n2, int n3, int n4) {
        boolean bl2 = nm2.e(n2, n3, n4) == 1;
        float f2 = 0.0625f;
        if (bl2) {
            this.a(f2, 0.0f, f2, 1.0f - f2, 0.03125f, 1.0f - f2);
        } else {
            this.a(f2, 0.0f, f2, 1.0f - f2, 0.0625f, 1.0f - f2);
        }
    }

    public boolean b(nm nm2, int n2, int n3, int n4, int n5) {
        return nm2.e(n2, n3, n4) > 0;
    }

    public boolean c(cn cn2, int n2, int n3, int n4, int n5) {
        if (cn2.e(n2, n3, n4) == 0) {
            return false;
        }
        return n5 == 1;
    }

    public boolean d() {
        return true;
    }

    public void e() {
        float f2 = 0.5f;
        float f3 = 0.125f;
        float f4 = 0.5f;
        this.a(0.5f - f2, 0.5f - f3, 0.5f - f4, 0.5f + f2, 0.5f + f3, 0.5f + f4);
    }
}

