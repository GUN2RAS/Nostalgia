/*
 * Decompiled with CFR 0.152.
 */
public class dx
extends kh {
    public ev a;
    private int e;
    public int b = 0;
    public int c;
    private int f = 5;
    public float d = (float)(Math.random() * Math.PI * 2.0);

    public dx(cn cn2, double d2, double d3, double d4, ev ev2) {
        super(cn2);
        this.a(0.25f, 0.25f);
        this.aB = this.aD / 2.0f;
        this.a(d2, d3, d4);
        this.a = ev2;
        this.aq = (float)(Math.random() * 360.0);
        this.an = (float)(Math.random() * (double)0.2f - (double)0.1f);
        this.ao = 0.2f;
        this.ap = (float)(Math.random() * (double)0.2f - (double)0.1f);
        this.aG = false;
    }

    public dx(cn cn2) {
        super(cn2);
        this.a(0.25f, 0.25f);
        this.aB = this.aD / 2.0f;
    }

    public void e_() {
        super.e_();
        if (this.c > 0) {
            --this.c;
        }
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        this.ao -= (double)0.04f;
        if (this.ag.f(eo.b(this.ak), eo.b(this.al), eo.b(this.am)) == gb.g) {
            this.ao = 0.2f;
            this.an = (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f;
            this.ap = (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f;
            this.ag.a(this, "random.fizz", 0.4f, 2.0f + this.aQ.nextFloat() * 0.4f);
        }
        this.g(this.ak, this.al, this.am);
        this.g_();
        this.c(this.an, this.ao, this.ap);
        float f2 = 0.98f;
        if (this.av) {
            f2 = 0.58800006f;
            int n2 = this.ag.a(eo.b(this.ak), eo.b(this.au.b) - 1, eo.b(this.am));
            if (n2 > 0) {
                f2 = ly.n[n2].bo * 0.98f;
            }
        }
        this.an *= (double)f2;
        this.ao *= (double)0.98f;
        this.ap *= (double)f2;
        if (this.av) {
            this.ao *= -0.5;
        }
        ++this.e;
        ++this.b;
        if (this.b >= 6000) {
            this.F();
        }
    }

    public boolean g_() {
        return this.ag.a(this.au, gb.f, this);
    }

    private boolean g(double d2, double d3, double d4) {
        int n2 = eo.b(d2);
        int n3 = eo.b(d3);
        int n4 = eo.b(d4);
        double d5 = d2 - (double)n2;
        double d6 = d3 - (double)n3;
        double d7 = d4 - (double)n4;
        if (ly.p[this.ag.a(n2, n3, n4)]) {
            boolean bl2 = !ly.p[this.ag.a(n2 - 1, n3, n4)];
            boolean bl3 = !ly.p[this.ag.a(n2 + 1, n3, n4)];
            boolean bl4 = !ly.p[this.ag.a(n2, n3 - 1, n4)];
            boolean bl5 = !ly.p[this.ag.a(n2, n3 + 1, n4)];
            boolean bl6 = !ly.p[this.ag.a(n2, n3, n4 - 1)];
            boolean bl7 = !ly.p[this.ag.a(n2, n3, n4 + 1)];
            int n5 = -1;
            double d8 = 9999.0;
            if (bl2 && d5 < d8) {
                d8 = d5;
                n5 = 0;
            }
            if (bl3 && 1.0 - d5 < d8) {
                d8 = 1.0 - d5;
                n5 = 1;
            }
            if (bl4 && d6 < d8) {
                d8 = d6;
                n5 = 2;
            }
            if (bl5 && 1.0 - d6 < d8) {
                d8 = 1.0 - d6;
                n5 = 3;
            }
            if (bl6 && d7 < d8) {
                d8 = d7;
                n5 = 4;
            }
            if (bl7 && 1.0 - d7 < d8) {
                d8 = 1.0 - d7;
                n5 = 5;
            }
            float f2 = this.aQ.nextFloat() * 0.2f + 0.1f;
            if (n5 == 0) {
                this.an = -f2;
            }
            if (n5 == 1) {
                this.an = f2;
            }
            if (n5 == 2) {
                this.ao = -f2;
            }
            if (n5 == 3) {
                this.ao = f2;
            }
            if (n5 == 4) {
                this.ap = -f2;
            }
            if (n5 == 5) {
                this.ap = f2;
            }
        }
        return false;
    }

    protected void a(int n2) {
        this.a(null, n2);
    }

    public boolean a(kh kh2, int n2) {
        this.f -= n2;
        if (this.f <= 0) {
            this.F();
        }
        return false;
    }

    public void a(hm hm2) {
        hm2.a("Health", (short)((byte)this.f));
        hm2.a("Age", (short)this.b);
        hm2.a("Item", this.a.a(new hm()));
    }

    public void b(hm hm2) {
        this.f = hm2.d("Health") & 0xFF;
        this.b = hm2.d("Age");
        hm hm3 = hm2.k("Item");
        this.a = new ev(hm3);
    }

    public void b(dm dm2) {
        if (this.ag.y) {
            return;
        }
        int n2 = this.a.a;
        if (this.c == 0 && dm2.b.a(this.a)) {
            this.ag.a(this, "random.pop", 0.2f, ((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            dm2.a_(this, n2);
            this.F();
        }
    }
}

