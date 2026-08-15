/*
 * Decompiled with CFR 0.152.
 */
public final class ev {
    public int a = 0;
    public int b;
    public int c;
    public int d;

    public ev(ly ly2) {
        this(ly2, 1);
    }

    public ev(ly ly2, int n2) {
        this(ly2.bc, n2);
    }

    public ev(di di2) {
        this(di2, 1);
    }

    public ev(di di2, int n2) {
        this(di2.aS, n2);
    }

    public ev(int n2) {
        this(n2, 1);
    }

    public ev(int n2, int n3) {
        this.c = n2;
        this.a = n3;
    }

    public ev(int n2, int n3, int n4) {
        this.c = n2;
        this.a = n3;
        this.d = n4;
    }

    public ev(hm hm2) {
        this.b(hm2);
    }

    public ev a(int n2) {
        this.a -= n2;
        return new ev(this.c, n2, this.d);
    }

    public di a() {
        return di.c[this.c];
    }

    public int b() {
        return this.a().a(this);
    }

    public boolean a(dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        return this.a().a(this, dm2, cn2, n2, n3, n4, n5);
    }

    public float a(ly ly2) {
        return this.a().a(this, ly2);
    }

    public ev a(cn cn2, dm dm2) {
        return this.a().a(this, cn2, dm2);
    }

    public hm a(hm hm2) {
        hm2.a("id", (short)this.c);
        hm2.a("Count", (byte)this.a);
        hm2.a("Damage", (short)this.d);
        return hm2;
    }

    public void b(hm hm2) {
        this.c = hm2.d("id");
        this.a = hm2.c("Count");
        this.d = hm2.d("Damage");
    }

    public int c() {
        return this.a().b();
    }

    public int d() {
        return di.c[this.c].c();
    }

    public void b(int n2) {
        this.d += n2;
        if (this.d > this.d()) {
            --this.a;
            if (this.a < 0) {
                this.a = 0;
            }
            this.d = 0;
        }
    }

    public void a(ge ge2) {
        di.c[this.c].a(this, ge2);
    }

    public void a(int n2, int n3, int n4, int n5) {
        di.c[this.c].a(this, n2, n3, n4, n5);
    }

    public int a(kh kh2) {
        return di.c[this.c].a(kh2);
    }

    public boolean b(ly ly2) {
        return di.c[this.c].a(ly2);
    }

    public void a(dm dm2) {
    }

    public void b(ge ge2) {
        di.c[this.c].b(this, ge2);
    }

    public ev e() {
        return new ev(this.c, this.a, this.d);
    }
}

