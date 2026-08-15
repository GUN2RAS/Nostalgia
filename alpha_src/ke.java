/*
 * Decompiled with CFR 0.152.
 */
public class ke
extends ic
implements gh {
    private ev[] a = new ev[3];
    private int b = 0;
    private int c = 0;
    private int d = 0;

    public int c() {
        return this.a.length;
    }

    public ev c(int n2) {
        return this.a[n2];
    }

    public ev a(int n2, int n3) {
        if (this.a[n2] != null) {
            if (this.a[n2].a <= n3) {
                ev ev2 = this.a[n2];
                this.a[n2] = null;
                return ev2;
            }
            ev ev3 = this.a[n2].a(n3);
            if (this.a[n2].a == 0) {
                this.a[n2] = null;
            }
            return ev3;
        }
        return null;
    }

    public void a(int n2, ev ev2) {
        this.a[n2] = ev2;
        if (ev2 != null && ev2.a > this.e()) {
            ev2.a = this.e();
        }
    }

    public String d() {
        return "Chest";
    }

    public void a(hm hm2) {
        super.a(hm2);
        ki ki2 = hm2.l("Items");
        this.a = new ev[this.c()];
        for (int i2 = 0; i2 < ki2.c(); ++i2) {
            hm hm3 = (hm)ki2.a(i2);
            byte by2 = hm3.c("Slot");
            if (by2 < 0 || by2 >= this.a.length) continue;
            this.a[by2] = new ev(hm3);
        }
        this.b = hm2.d("BurnTime");
        this.d = hm2.d("CookTime");
        this.c = this.a(this.a[1]);
    }

    public void b(hm hm2) {
        super.b(hm2);
        hm2.a("BurnTime", (short)this.b);
        hm2.a("CookTime", (short)this.d);
        ki ki2 = new ki();
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            if (this.a[i2] == null) continue;
            hm hm3 = new hm();
            hm3.a("Slot", (byte)i2);
            this.a[i2].a(hm3);
            ki2.a(hm3);
        }
        hm2.a("Items", ki2);
    }

    public int e() {
        return 64;
    }

    public int a(int n2) {
        return this.d * n2 / 200;
    }

    public int b(int n2) {
        if (this.c == 0) {
            this.c = 200;
        }
        return this.b * n2 / this.c;
    }

    public boolean a() {
        return this.b > 0;
    }

    public void b() {
        boolean bl2 = this.b > 0;
        boolean bl3 = false;
        if (this.b > 0) {
            --this.b;
        }
        if (!this.e.y) {
            if (this.b == 0 && this.j()) {
                this.c = this.b = this.a(this.a[1]);
                if (this.b > 0) {
                    bl3 = true;
                    if (this.a[1] != null) {
                        --this.a[1].a;
                        if (this.a[1].a == 0) {
                            this.a[1] = null;
                        }
                    }
                }
            }
            if (this.a() && this.j()) {
                ++this.d;
                if (this.d == 200) {
                    this.d = 0;
                    this.i();
                    bl3 = true;
                }
            } else {
                this.d = 0;
            }
            if (bl2 != this.b > 0) {
                bl3 = true;
                ku.a(this.b > 0, this.e, this.f, this.g, this.h);
            }
        }
        if (bl3) {
            this.j_();
        }
    }

    private boolean j() {
        if (this.a[0] == null) {
            return false;
        }
        int n2 = this.d(this.a[0].a().aS);
        if (n2 < 0) {
            return false;
        }
        if (this.a[2] == null) {
            return true;
        }
        if (this.a[2].c != n2) {
            return false;
        }
        if (this.a[2].a < this.e() && this.a[2].a < this.a[2].c()) {
            return true;
        }
        return this.a[2].a < di.c[n2].b();
    }

    public void i() {
        if (!this.j()) {
            return;
        }
        int n2 = this.d(this.a[0].a().aS);
        if (this.a[2] == null) {
            this.a[2] = new ev(n2, 1);
        } else if (this.a[2].c == n2) {
            ++this.a[2].a;
        }
        --this.a[0].a;
        if (this.a[0].a <= 0) {
            this.a[0] = null;
        }
    }

    private int d(int n2) {
        if (n2 == ly.I.bc) {
            return di.m.aS;
        }
        if (n2 == ly.H.bc) {
            return di.n.aS;
        }
        if (n2 == ly.ax.bc) {
            return di.l.aS;
        }
        if (n2 == ly.F.bc) {
            return ly.N.bc;
        }
        if (n2 == di.ao.aS) {
            return di.ap.aS;
        }
        if (n2 == ly.x.bc) {
            return ly.u.bc;
        }
        if (n2 == di.aG.aS) {
            return di.aF.aS;
        }
        return -1;
    }

    private int a(ev ev2) {
        if (ev2 == null) {
            return 0;
        }
        int n2 = ev2.a().aS;
        if (n2 < 256 && ly.n[n2].bn == gb.c) {
            return 300;
        }
        if (n2 == di.B.aS) {
            return 100;
        }
        if (n2 == di.k.aS) {
            return 1600;
        }
        if (n2 == di.aw.aS) {
            return 20000;
        }
        return 0;
    }
}

