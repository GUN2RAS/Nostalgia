/*
 * Decompiled with CFR 0.152.
 */
public class eu
implements gh {
    public ev[] a = new ev[37];
    public ev[] b = new ev[4];
    public ev[] c = new ev[4];
    public int d = 0;
    private dm g;
    public ev e;
    public boolean f = false;

    public eu(dm dm2) {
        this.g = dm2;
    }

    public ev a() {
        return this.a[this.d];
    }

    private int f(int n2) {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            if (this.a[i2] == null || this.a[i2].c != n2) continue;
            return i2;
        }
        return -1;
    }

    private int g(int n2) {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            if (this.a[i2] == null || this.a[i2].c != n2 || this.a[i2].a >= this.a[i2].c() || this.a[i2].a >= this.e()) continue;
            return i2;
        }
        return -1;
    }

    private int j() {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            if (this.a[i2] != null) continue;
            return i2;
        }
        return -1;
    }

    public void a(int n2, boolean bl2) {
        int n3 = this.f(n2);
        if (n3 >= 0 && n3 < 9) {
            this.d = n3;
            return;
        }
    }

    public void a(int n2) {
        if (n2 > 0) {
            n2 = 1;
        }
        if (n2 < 0) {
            n2 = -1;
        }
        this.d -= n2;
        while (this.d < 0) {
            this.d += 9;
        }
        while (this.d >= 9) {
            this.d -= 9;
        }
    }

    private int b(int n2, int n3) {
        int n4;
        int n5 = this.g(n2);
        if (n5 < 0) {
            n5 = this.j();
        }
        if (n5 < 0) {
            return n3;
        }
        if (this.a[n5] == null) {
            this.a[n5] = new ev(n2, 0);
        }
        if ((n4 = n3) > this.a[n5].c() - this.a[n5].a) {
            n4 = this.a[n5].c() - this.a[n5].a;
        }
        if (n4 > this.e() - this.a[n5].a) {
            n4 = this.e() - this.a[n5].a;
        }
        if (n4 == 0) {
            return n3;
        }
        this.a[n5].a += n4;
        this.a[n5].b = 5;
        return n3 -= n4;
    }

    public void b() {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            if (this.a[i2] == null || this.a[i2].b <= 0) continue;
            --this.a[i2].b;
        }
    }

    public boolean b(int n2) {
        int n3 = this.f(n2);
        if (n3 < 0) {
            return false;
        }
        if (--this.a[n3].a <= 0) {
            this.a[n3] = null;
        }
        return true;
    }

    public boolean a(ev ev2) {
        int n2;
        if (ev2.d == 0) {
            ev2.a = this.b(ev2.c, ev2.a);
            if (ev2.a == 0) {
                return true;
            }
        }
        if ((n2 = this.j()) >= 0) {
            this.a[n2] = ev2;
            this.a[n2].b = 5;
            return true;
        }
        return false;
    }

    public ev a(int n2, int n3) {
        ev[] evArray = this.a;
        if (n2 >= this.a.length) {
            evArray = this.b;
            n2 -= this.a.length;
        }
        if (evArray[n2] != null) {
            if (evArray[n2].a <= n3) {
                ev ev2 = evArray[n2];
                evArray[n2] = null;
                return ev2;
            }
            ev ev3 = evArray[n2].a(n3);
            if (evArray[n2].a == 0) {
                evArray[n2] = null;
            }
            return ev3;
        }
        return null;
    }

    public void a(int n2, ev ev2) {
        ev[] evArray = this.a;
        if (n2 >= evArray.length) {
            n2 -= evArray.length;
            evArray = this.b;
        }
        if (n2 >= evArray.length) {
            n2 -= evArray.length;
            evArray = this.c;
        }
        evArray[n2] = ev2;
    }

    public float a(ly ly2) {
        float f2 = 1.0f;
        if (this.a[this.d] != null) {
            f2 *= this.a[this.d].a(ly2);
        }
        return f2;
    }

    public ki a(ki ki2) {
        hm hm2;
        int n2;
        for (n2 = 0; n2 < this.a.length; ++n2) {
            if (this.a[n2] == null) continue;
            hm2 = new hm();
            hm2.a("Slot", (byte)n2);
            this.a[n2].a(hm2);
            ki2.a(hm2);
        }
        for (n2 = 0; n2 < this.b.length; ++n2) {
            if (this.b[n2] == null) continue;
            hm2 = new hm();
            hm2.a("Slot", (byte)(n2 + 100));
            this.b[n2].a(hm2);
            ki2.a(hm2);
        }
        for (n2 = 0; n2 < this.c.length; ++n2) {
            if (this.c[n2] == null) continue;
            hm2 = new hm();
            hm2.a("Slot", (byte)(n2 + 80));
            this.c[n2].a(hm2);
            ki2.a(hm2);
        }
        return ki2;
    }

    public void b(ki ki2) {
        this.a = new ev[36];
        this.b = new ev[4];
        this.c = new ev[4];
        for (int i2 = 0; i2 < ki2.c(); ++i2) {
            hm hm2 = (hm)ki2.a(i2);
            int n2 = hm2.c("Slot") & 0xFF;
            if (n2 >= 0 && n2 < this.a.length) {
                this.a[n2] = new ev(hm2);
            }
            if (n2 >= 80 && n2 < this.c.length + 80) {
                this.c[n2 - 80] = new ev(hm2);
            }
            if (n2 < 100 || n2 >= this.b.length + 100) continue;
            this.b[n2 - 100] = new ev(hm2);
        }
    }

    public int c() {
        return this.a.length + 4;
    }

    public ev c(int n2) {
        ev[] evArray = this.a;
        if (n2 >= evArray.length) {
            n2 -= evArray.length;
            evArray = this.b;
        }
        if (n2 >= evArray.length) {
            n2 -= evArray.length;
            evArray = this.c;
        }
        return evArray[n2];
    }

    public String d() {
        return "Inventory";
    }

    public int e() {
        return 64;
    }

    public int a(kh kh2) {
        ev ev2 = this.c(this.d);
        if (ev2 != null) {
            return ev2.a(kh2);
        }
        return 1;
    }

    public boolean b(ly ly2) {
        if (ly2.bn != gb.d && ly2.bn != gb.e && ly2.bn != gb.t && ly2.bn != gb.s) {
            return true;
        }
        ev ev2 = this.c(this.d);
        if (ev2 != null) {
            return ev2.b(ly2);
        }
        return false;
    }

    public ev d(int n2) {
        return this.b[n2];
    }

    public int f() {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        for (int i2 = 0; i2 < this.b.length; ++i2) {
            if (this.b[i2] == null || !(this.b[i2].a() instanceof mr)) continue;
            int n5 = this.b[i2].d();
            int n6 = this.b[i2].d;
            int n7 = n5 - n6;
            n3 += n7;
            n4 += n5;
            int n8 = ((mr)this.b[i2].a()).aY;
            n2 += n8;
        }
        if (n4 == 0) {
            return 0;
        }
        return (n2 - 1) * n3 / n4 + 1;
    }

    public void e(int n2) {
        for (int i2 = 0; i2 < this.b.length; ++i2) {
            if (this.b[i2] == null || !(this.b[i2].a() instanceof mr)) continue;
            this.b[i2].b(n2);
            if (this.b[i2].a != 0) continue;
            this.b[i2].a(this.g);
            this.b[i2] = null;
        }
    }

    public void g() {
        int n2;
        for (n2 = 0; n2 < this.a.length; ++n2) {
            if (this.a[n2] == null) continue;
            this.g.a(this.a[n2], true);
            this.a[n2] = null;
        }
        for (n2 = 0; n2 < this.b.length; ++n2) {
            if (this.b[n2] == null) continue;
            this.g.a(this.b[n2], true);
            this.b[n2] = null;
        }
    }

    public void j_() {
        this.f = true;
    }

    public boolean a(eu eu2) {
        int n2;
        for (n2 = 0; n2 < this.a.length; ++n2) {
            if (this.a(eu2.a[n2], this.a[n2])) continue;
            return false;
        }
        for (n2 = 0; n2 < this.b.length; ++n2) {
            if (this.a(eu2.b[n2], this.b[n2])) continue;
            return false;
        }
        for (n2 = 0; n2 < this.c.length; ++n2) {
            if (this.a(eu2.c[n2], this.c[n2])) continue;
            return false;
        }
        return true;
    }

    private boolean a(ev ev2, ev ev3) {
        if (ev2 == null && ev3 == null) {
            return true;
        }
        if (ev2 == null || ev3 == null) {
            return false;
        }
        return ev2.c == ev3.c && ev2.a == ev3.a && ev2.d == ev3.d;
    }

    public eu i() {
        int n2;
        eu eu2 = new eu(null);
        for (n2 = 0; n2 < this.a.length; ++n2) {
            eu2.a[n2] = this.a[n2] != null ? this.a[n2].e() : null;
        }
        for (n2 = 0; n2 < this.b.length; ++n2) {
            eu2.b[n2] = this.b[n2] != null ? this.b[n2].e() : null;
        }
        for (n2 = 0; n2 < this.c.length; ++n2) {
            eu2.c[n2] = this.c[n2] != null ? this.c[n2].e() : null;
        }
        return eu2;
    }
}

