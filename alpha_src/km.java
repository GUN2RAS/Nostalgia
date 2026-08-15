/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.Random;

public class km
extends ly {
    private ly a;

    protected km(int n2, ly ly2) {
        super(n2, ly2.bb, ly2.bn);
        this.a = ly2;
        this.c(ly2.bd);
        this.b(ly2.be / 3.0f);
        this.a(ly2.bl);
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 10;
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        return super.c(nm2, n2, n3, n4, n5);
    }

    public void a(cn cn2, int n2, int n3, int n4, cf cf2, ArrayList arrayList) {
        int n5 = cn2.e(n2, n3, n4);
        if (n5 == 0) {
            this.a(0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 1.0f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
            this.a(0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
        } else if (n5 == 1) {
            this.a(0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
            this.a(0.5f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
        } else if (n5 == 2) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
            this.a(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
        } else if (n5 == 3) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
            this.a(0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f);
            super.a(cn2, n2, n3, n4, cf2, arrayList);
        }
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (cn2.y) {
            return;
        }
        if (cn2.f(n2, n3 + 1, n4).a()) {
            cn2.d(n2, n3, n4, this.a.bc);
        } else {
            this.h(cn2, n2, n3, n4);
            this.h(cn2, n2 + 1, n3 - 1, n4);
            this.h(cn2, n2 - 1, n3 - 1, n4);
            this.h(cn2, n2, n3 - 1, n4 - 1);
            this.h(cn2, n2, n3 - 1, n4 + 1);
            this.h(cn2, n2 + 1, n3 + 1, n4);
            this.h(cn2, n2 - 1, n3 + 1, n4);
            this.h(cn2, n2, n3 + 1, n4 - 1);
            this.h(cn2, n2, n3 + 1, n4 + 1);
        }
        this.a.a(cn2, n2, n3, n4, n5);
    }

    private void h(cn cn2, int n2, int n3, int n4) {
        if (!this.j(cn2, n2, n3, n4)) {
            return;
        }
        int n5 = -1;
        if (this.j(cn2, n2 + 1, n3 + 1, n4)) {
            n5 = 0;
        }
        if (this.j(cn2, n2 - 1, n3 + 1, n4)) {
            n5 = 1;
        }
        if (this.j(cn2, n2, n3 + 1, n4 + 1)) {
            n5 = 2;
        }
        if (this.j(cn2, n2, n3 + 1, n4 - 1)) {
            n5 = 3;
        }
        if (n5 < 0) {
            if (this.i(cn2, n2 + 1, n3, n4) && !this.i(cn2, n2 - 1, n3, n4)) {
                n5 = 0;
            }
            if (this.i(cn2, n2 - 1, n3, n4) && !this.i(cn2, n2 + 1, n3, n4)) {
                n5 = 1;
            }
            if (this.i(cn2, n2, n3, n4 + 1) && !this.i(cn2, n2, n3, n4 - 1)) {
                n5 = 2;
            }
            if (this.i(cn2, n2, n3, n4 - 1) && !this.i(cn2, n2, n3, n4 + 1)) {
                n5 = 3;
            }
        }
        if (n5 < 0) {
            if (this.j(cn2, n2 - 1, n3 - 1, n4)) {
                n5 = 0;
            }
            if (this.j(cn2, n2 + 1, n3 - 1, n4)) {
                n5 = 1;
            }
            if (this.j(cn2, n2, n3 - 1, n4 - 1)) {
                n5 = 2;
            }
            if (this.j(cn2, n2, n3 - 1, n4 + 1)) {
                n5 = 3;
            }
        }
        if (n5 >= 0) {
            cn2.b(n2, n3, n4, n5);
        }
    }

    private boolean i(cn cn2, int n2, int n3, int n4) {
        return cn2.f(n2, n3, n4).a();
    }

    private boolean j(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3, n4);
        if (n5 == 0) {
            return false;
        }
        return ly.n[n5].f() == 10;
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        this.a.b(cn2, n2, n3, n4, random);
    }

    public void b(cn cn2, int n2, int n3, int n4, dm dm2) {
        this.a.b(cn2, n2, n3, n4, dm2);
    }

    public void b(cn cn2, int n2, int n3, int n4, int n5) {
        this.a.b(cn2, n2, n3, n4, n5);
    }

    public float c(nm nm2, int n2, int n3, int n4) {
        return this.a.c(nm2, n2, n3, n4);
    }

    public float a(kh kh2) {
        return this.a.a(kh2);
    }

    public int g() {
        return this.a.g();
    }

    public int a(int n2, Random random) {
        return this.a.a(n2, random);
    }

    public int a(Random random) {
        return this.a.a(random);
    }

    public int a(int n2, int n3) {
        return this.a.a(n2, n3);
    }

    public int a(int n2) {
        return this.a.a(n2);
    }

    public int a(nm nm2, int n2, int n3, int n4, int n5) {
        return this.a.a(nm2, n2, n3, n4, n5);
    }

    public int a() {
        return this.a.a();
    }

    public cf f(cn cn2, int n2, int n3, int n4) {
        return this.a.f(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2, aj aj2) {
        this.a.a(cn2, n2, n3, n4, kh2, aj2);
    }

    public boolean h() {
        return this.a.h();
    }

    public boolean a(int n2, boolean bl2) {
        return this.a.a(n2, bl2);
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        return this.a.a(cn2, n2, n3, n4);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        this.a(cn2, n2, n3, n4, 0);
        this.a.e(cn2, n2, n3, n4);
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        this.a.b(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5, float f2) {
        this.a.a(cn2, n2, n3, n4, n5, f2);
    }

    public void b_(cn cn2, int n2, int n3, int n4, int n5) {
        this.a.b_(cn2, n2, n3, n4, n5);
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2) {
        this.a.a(cn2, n2, n3, n4, kh2);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        this.a.a(cn2, n2, n3, n4, random);
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        return this.a.a(cn2, n2, n3, n4, dm2);
    }

    public void c(cn cn2, int n2, int n3, int n4) {
        this.a.c(cn2, n2, n3, n4);
    }
}

