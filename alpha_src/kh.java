/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;
import java.util.Random;

public abstract class kh {
    private static int a = 0;
    public int ab = a++;
    public double ac = 1.0;
    public boolean ad = false;
    public kh ae;
    public kh af;
    protected cn ag;
    public double ah;
    public double ai;
    public double aj;
    public double ak;
    public double al;
    public double am;
    public double an;
    public double ao;
    public double ap;
    public float aq;
    public float ar;
    public float as;
    public float at;
    public final cf au = cf.a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    public boolean av = false;
    public boolean aw;
    public boolean ax;
    public boolean ay = false;
    public boolean az = true;
    public boolean aA = false;
    public float aB = 0.0f;
    public float aC = 0.6f;
    public float aD = 1.8f;
    public float aE = 0.0f;
    public float aF = 0.0f;
    protected boolean aG = true;
    protected float aH = 0.0f;
    private int b = 1;
    public double aI;
    public double aJ;
    public double aK;
    public float aL = 0.0f;
    public float aM = 0.0f;
    public boolean aN = false;
    public float aO = 0.0f;
    public boolean aP = false;
    protected Random aQ = new Random();
    public int aR = 0;
    public int aS = 1;
    public int aT = 0;
    protected int aU = 300;
    protected boolean aV = false;
    public int aW = 0;
    public int aX = 300;
    private boolean c = true;
    public String aY;
    private double d;
    private double e;
    public boolean aZ = false;
    public int ba;
    public int bb;
    public int bc;
    public int bd;
    public int be;
    public int bf;

    public kh(cn cn2) {
        this.ag = cn2;
        this.a(0.0, 0.0, 0.0);
    }

    public boolean equals(Object object) {
        if (object instanceof kh) {
            return ((kh)object).ab == this.ab;
        }
        return false;
    }

    public int hashCode() {
        return this.ab;
    }

    protected void q() {
        if (this.ag == null) {
            return;
        }
        while (this.al > 0.0) {
            this.a(this.ak, this.al, this.am);
            if (this.ag.a(this, this.au).size() == 0) break;
            this.al += 1.0;
        }
        this.ap = 0.0;
        this.ao = 0.0;
        this.an = 0.0;
        this.ar = 0.0f;
    }

    public void F() {
        this.aA = true;
    }

    protected void a(float f2, float f3) {
        this.aC = f2;
        this.aD = f3;
    }

    protected void c(float f2, float f3) {
        this.aq = f2;
        this.ar = f3;
    }

    public void a(double d2, double d3, double d4) {
        this.ak = d2;
        this.al = d3;
        this.am = d4;
        float f2 = this.aC / 2.0f;
        float f3 = this.aD;
        this.au.c(d2 - (double)f2, d3 - (double)this.aB + (double)this.aL, d4 - (double)f2, d2 + (double)f2, d3 - (double)this.aB + (double)this.aL + (double)f3, d4 + (double)f2);
    }

    public void d(float f2, float f3) {
        float f4 = this.ar;
        float f5 = this.aq;
        this.aq = (float)((double)this.aq + (double)f2 * 0.15);
        this.ar = (float)((double)this.ar - (double)f3 * 0.15);
        if (this.ar < -90.0f) {
            this.ar = -90.0f;
        }
        if (this.ar > 90.0f) {
            this.ar = 90.0f;
        }
        this.at += this.ar - f4;
        this.as += this.aq - f5;
    }

    public void e_() {
        this.y();
    }

    public void y() {
        if (this.af != null && this.af.aA) {
            this.af = null;
        }
        ++this.aR;
        this.aE = this.aF;
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        this.at = this.ar;
        this.as = this.aq;
        if (this.g_()) {
            if (!this.aV && !this.c) {
                float f2;
                float f3;
                float f4 = eo.a(this.an * this.an * (double)0.2f + this.ao * this.ao + this.ap * this.ap * (double)0.2f) * 0.2f;
                if (f4 > 1.0f) {
                    f4 = 1.0f;
                }
                this.ag.a(this, "random.splash", f4, 1.0f + (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.4f);
                float f5 = eo.b(this.au.b);
                int n2 = 0;
                while ((float)n2 < 1.0f + this.aC * 20.0f) {
                    f3 = (this.aQ.nextFloat() * 2.0f - 1.0f) * this.aC;
                    f2 = (this.aQ.nextFloat() * 2.0f - 1.0f) * this.aC;
                    this.ag.a("bubble", this.ak + (double)f3, f5 + 1.0f, this.am + (double)f2, this.an, this.ao - (double)(this.aQ.nextFloat() * 0.2f), this.ap);
                    ++n2;
                }
                n2 = 0;
                while ((float)n2 < 1.0f + this.aC * 20.0f) {
                    f3 = (this.aQ.nextFloat() * 2.0f - 1.0f) * this.aC;
                    f2 = (this.aQ.nextFloat() * 2.0f - 1.0f) * this.aC;
                    this.ag.a("splash", this.ak + (double)f3, f5 + 1.0f, this.am + (double)f2, this.an, this.ao, this.ap);
                    ++n2;
                }
            }
            this.aH = 0.0f;
            this.aV = true;
            this.aT = 0;
        } else {
            this.aV = false;
        }
        if (this.aT > 0) {
            if (this.aT % 20 == 0) {
                this.a(null, 1);
            }
            --this.aT;
        }
        if (this.G()) {
            this.a(null, 10);
            this.aT = 600;
        }
        if (this.al < -64.0) {
            this.E();
        }
        this.c = false;
    }

    protected void E() {
        this.F();
    }

    public boolean b(double d2, double d3, double d4) {
        cf cf2 = this.au.c(d2, d3, d4);
        List list = this.ag.a(this, cf2);
        if (list.size() > 0) {
            return false;
        }
        return !this.ag.b(cf2);
    }

    public void c(double d2, double d3, double d4) {
        int n2;
        int n3;
        double d5;
        int n4;
        int n5;
        boolean bl2;
        if (this.aN) {
            this.au.d(d2, d3, d4);
            this.ak = (this.au.a + this.au.d) / 2.0;
            this.al = this.au.b + (double)this.aB - (double)this.aL;
            this.am = (this.au.c + this.au.f) / 2.0;
            return;
        }
        double d6 = this.ak;
        double d7 = this.am;
        double d8 = d2;
        double d9 = d3;
        double d10 = d4;
        cf cf2 = this.au.c();
        boolean bl3 = bl2 = this.av && this.o();
        if (bl2) {
            double d11 = 0.05;
            while (d2 != 0.0 && this.ag.a(this, this.au.c(d2, -1.0, 0.0)).size() == 0) {
                d2 = d2 < d11 && d2 >= -d11 ? 0.0 : (d2 > 0.0 ? (d2 -= d11) : (d2 += d11));
                d8 = d2;
            }
            while (d4 != 0.0 && this.ag.a(this, this.au.c(0.0, -1.0, d4)).size() == 0) {
                d4 = d4 < d11 && d4 >= -d11 ? 0.0 : (d4 > 0.0 ? (d4 -= d11) : (d4 += d11));
                d10 = d4;
            }
        }
        List list = this.ag.a(this, this.au.a(d2, d3, d4));
        for (n5 = 0; n5 < list.size(); ++n5) {
            d3 = ((cf)list.get(n5)).b(this.au, d3);
        }
        this.au.d(0.0, d3, 0.0);
        if (!this.az && d9 != d3) {
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
        }
        n5 = this.av || d9 != d3 && d9 < 0.0 ? 1 : 0;
        for (n4 = 0; n4 < list.size(); ++n4) {
            d2 = ((cf)list.get(n4)).a(this.au, d2);
        }
        this.au.d(d2, 0.0, 0.0);
        if (!this.az && d8 != d2) {
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
        }
        for (n4 = 0; n4 < list.size(); ++n4) {
            d4 = ((cf)list.get(n4)).c(this.au, d4);
        }
        this.au.d(0.0, 0.0, d4);
        if (!this.az && d10 != d4) {
            d4 = 0.0;
            d3 = 0.0;
            d2 = 0.0;
        }
        if (this.aM > 0.0f && n5 != 0 && this.aL < 0.05f && (d8 != d2 || d10 != d4)) {
            double d12 = d2;
            d5 = d3;
            double d13 = d4;
            d2 = d8;
            d3 = this.aM;
            d4 = d10;
            cf cf3 = this.au.c();
            this.au.b(cf2);
            list = this.ag.a(this, this.au.a(d2, d3, d4));
            for (n3 = 0; n3 < list.size(); ++n3) {
                d3 = ((cf)list.get(n3)).b(this.au, d3);
            }
            this.au.d(0.0, d3, 0.0);
            if (!this.az && d9 != d3) {
                d4 = 0.0;
                d3 = 0.0;
                d2 = 0.0;
            }
            for (n3 = 0; n3 < list.size(); ++n3) {
                d2 = ((cf)list.get(n3)).a(this.au, d2);
            }
            this.au.d(d2, 0.0, 0.0);
            if (!this.az && d8 != d2) {
                d4 = 0.0;
                d3 = 0.0;
                d2 = 0.0;
            }
            for (n3 = 0; n3 < list.size(); ++n3) {
                d4 = ((cf)list.get(n3)).c(this.au, d4);
            }
            this.au.d(0.0, 0.0, d4);
            if (!this.az && d10 != d4) {
                d4 = 0.0;
                d3 = 0.0;
                d2 = 0.0;
            }
            if (d12 * d12 + d13 * d13 >= d2 * d2 + d4 * d4) {
                d2 = d12;
                d3 = d5;
                d4 = d13;
                this.au.b(cf3);
            } else {
                this.aL = (float)((double)this.aL + 0.5);
            }
        }
        this.ak = (this.au.a + this.au.d) / 2.0;
        this.al = this.au.b + (double)this.aB - (double)this.aL;
        this.am = (this.au.c + this.au.f) / 2.0;
        this.aw = d8 != d2 || d10 != d4;
        this.ax = d9 != d3;
        this.av = d9 != d3 && d9 < 0.0;
        boolean bl4 = this.ay = this.aw || this.ax;
        if (this.av) {
            if (this.aH > 0.0f) {
                this.c(this.aH);
                this.aH = 0.0f;
            }
        } else if (d3 < 0.0) {
            this.aH = (float)((double)this.aH - d3);
        }
        if (d8 != d2) {
            this.an = 0.0;
        }
        if (d9 != d3) {
            this.ao = 0.0;
        }
        if (d10 != d4) {
            this.ap = 0.0;
        }
        double d14 = this.ak - d6;
        d5 = this.am - d7;
        this.aF = (float)((double)this.aF + (double)eo.a(d14 * d14 + d5 * d5) * 0.6);
        if (this.aG && !bl2) {
            int n6 = eo.b(this.ak);
            n2 = eo.b(this.al - (double)0.2f - (double)this.aB);
            int n7 = eo.b(this.am);
            n3 = this.ag.a(n6, n2, n7);
            if (this.aF > (float)this.b && n3 > 0) {
                ++this.b;
                bb bb2 = ly.n[n3].bl;
                if (this.ag.a(n6, n2 + 1, n7) == ly.aT.bc) {
                    bb2 = ly.aT.bl;
                    this.ag.a(this, bb2.d(), bb2.b() * 0.15f, bb2.c());
                } else if (!ly.n[n3].bn.d()) {
                    this.ag.a(this, bb2.d(), bb2.b() * 0.15f, bb2.c());
                }
                ly.n[n3].a(this.ag, n6, n2, n7, this);
            }
        }
        int n8 = eo.b(this.au.a);
        n2 = eo.b(this.au.b);
        int n9 = eo.b(this.au.c);
        n3 = eo.b(this.au.d);
        int n10 = eo.b(this.au.e);
        int n11 = eo.b(this.au.f);
        for (int i2 = n8; i2 <= n3; ++i2) {
            for (int i3 = n2; i3 <= n10; ++i3) {
                for (int i4 = n9; i4 <= n11; ++i4) {
                    int n12 = this.ag.a(i2, i3, i4);
                    if (n12 <= 0) continue;
                    ly.n[n12].b(this.ag, i2, i3, i4, this);
                }
            }
        }
        this.aL *= 0.4f;
        n8 = this.g_() ? 1 : 0;
        if (this.ag.c(this.au)) {
            this.a(1);
            if (n8 == 0) {
                ++this.aT;
                if (this.aT == 0) {
                    this.aT = 300;
                }
            }
        } else if (this.aT <= 0) {
            this.aT = -this.aS;
        }
        if (n8 != 0 && this.aT > 0) {
            this.ag.a(this, "random.fizz", 0.7f, 1.6f + (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.4f);
            this.aT = -this.aS;
        }
    }

    public boolean o() {
        return false;
    }

    public cf f_() {
        return null;
    }

    protected void a(int n2) {
        this.a(null, n2);
    }

    protected void c(float f2) {
    }

    public boolean g_() {
        return this.ag.a(this.au.b(0.0, -0.4f, 0.0), gb.f, this);
    }

    public boolean a(gb gb2) {
        int n2;
        int n3;
        double d2 = this.al + (double)this.s();
        int n4 = eo.b(this.ak);
        int n5 = this.ag.a(n4, n3 = eo.d(eo.b(d2)), n2 = eo.b(this.am));
        if (n5 != 0 && ly.n[n5].bn == gb2) {
            float f2 = jp.b(this.ag.e(n4, n3, n2)) - 0.11111111f;
            float f3 = (float)(n3 + 1) - f2;
            return d2 < (double)f3;
        }
        return false;
    }

    protected float s() {
        return 0.0f;
    }

    public boolean G() {
        return this.ag.a(this.au.b(0.0, -0.4f, 0.0), gb.g);
    }

    public void a(float f2, float f3, float f4) {
        float f5 = eo.c(f2 * f2 + f3 * f3);
        if (f5 < 0.01f) {
            return;
        }
        if (f5 < 1.0f) {
            f5 = 1.0f;
        }
        f5 = f4 / f5;
        float f6 = eo.a(this.aq * (float)Math.PI / 180.0f);
        float f7 = eo.b(this.aq * (float)Math.PI / 180.0f);
        this.an += (double)((f2 *= f5) * f7 - (f3 *= f5) * f6);
        this.ap += (double)(f3 * f7 + f2 * f6);
    }

    public float a(float f2) {
        int n2 = eo.b(this.ak);
        double d2 = (this.au.e - this.au.b) * 0.66;
        int n3 = eo.b(this.al - (double)this.aB + d2);
        int n4 = eo.b(this.am);
        return this.ag.c(n2, n3, n4);
    }

    public void a(cn cn2) {
        this.ag = cn2;
    }

    public void b(double d2, double d3, double d4, float f2, float f3) {
        this.ah = this.ak = d2;
        this.ai = this.al = d3;
        this.aj = this.am = d4;
        this.aq = f2;
        this.ar = f3;
        this.aL = 0.0f;
        double d5 = this.as - f2;
        if (d5 < -180.0) {
            this.as += 360.0f;
        }
        if (d5 >= 180.0) {
            this.as -= 360.0f;
        }
        this.a(this.ak, this.al, this.am);
    }

    public void c(double d2, double d3, double d4, float f2, float f3) {
        this.ah = this.ak = d2;
        this.ai = this.al = d3 + (double)this.aB;
        this.aj = this.am = d4;
        this.aq = f2;
        this.ar = f3;
        this.a(this.ak, this.al, this.am);
    }

    public float d(kh kh2) {
        float f2 = (float)(this.ak - kh2.ak);
        float f3 = (float)(this.al - kh2.al);
        float f4 = (float)(this.am - kh2.am);
        return eo.c(f2 * f2 + f3 * f3 + f4 * f4);
    }

    public double d(double d2, double d3, double d4) {
        double d5 = this.ak - d2;
        double d6 = this.al - d3;
        double d7 = this.am - d4;
        return d5 * d5 + d6 * d6 + d7 * d7;
    }

    public double e(double d2, double d3, double d4) {
        double d5 = this.ak - d2;
        double d6 = this.al - d3;
        double d7 = this.am - d4;
        return eo.a(d5 * d5 + d6 * d6 + d7 * d7);
    }

    public double e(kh kh2) {
        double d2 = this.ak - kh2.ak;
        double d3 = this.al - kh2.al;
        double d4 = this.am - kh2.am;
        return d2 * d2 + d3 * d3 + d4 * d4;
    }

    public void b(dm dm2) {
    }

    public void f(kh kh2) {
        if (kh2.ae == this || kh2.af == this) {
            return;
        }
        double d2 = kh2.ak - this.ak;
        double d3 = kh2.am - this.am;
        double d4 = eo.a(d2, d3);
        if (d4 >= (double)0.01f) {
            d4 = eo.a(d4);
            d2 /= d4;
            d3 /= d4;
            double d5 = 1.0 / d4;
            if (d5 > 1.0) {
                d5 = 1.0;
            }
            d2 *= d5;
            d3 *= d5;
            d2 *= (double)0.05f;
            d3 *= (double)0.05f;
            this.f(-(d2 *= (double)(1.0f - this.aO)), 0.0, -(d3 *= (double)(1.0f - this.aO)));
            kh2.f(d2, 0.0, d3);
        }
    }

    public void f(double d2, double d3, double d4) {
        this.an += d2;
        this.ao += d3;
        this.ap += d4;
    }

    public boolean a(kh kh2, int n2) {
        return false;
    }

    public boolean c_() {
        return false;
    }

    public boolean d_() {
        return false;
    }

    public void b(kh kh2, int n2) {
    }

    public boolean a(aj aj2) {
        double d2 = this.ak - aj2.a;
        double d3 = this.al - aj2.b;
        double d4 = this.am - aj2.c;
        double d5 = d2 * d2 + d3 * d3 + d4 * d4;
        return this.a(d5);
    }

    public boolean a(double d2) {
        double d3 = this.au.b();
        return d2 < (d3 *= 64.0 * this.ac) * d3;
    }

    public String x() {
        return null;
    }

    public boolean c(hm hm2) {
        String string = this.H();
        if (this.aA || string == null) {
            return false;
        }
        hm2.a("id", string);
        this.d(hm2);
        return true;
    }

    public void d(hm hm2) {
        hm2.a("Pos", this.a(new double[]{this.ak, this.al, this.am}));
        hm2.a("Motion", this.a(new double[]{this.an, this.ao, this.ap}));
        hm2.a("Rotation", this.a(new float[]{this.aq, this.ar}));
        hm2.a("FallDistance", this.aH);
        hm2.a("Fire", (short)this.aT);
        hm2.a("Air", (short)this.aX);
        hm2.a("OnGround", this.av);
        this.a(hm2);
    }

    public void e(hm hm2) {
        ki ki2 = hm2.l("Pos");
        ki ki3 = hm2.l("Motion");
        ki ki4 = hm2.l("Rotation");
        this.a(0.0, 0.0, 0.0);
        this.an = ((kr)ki3.a((int)0)).a;
        this.ao = ((kr)ki3.a((int)1)).a;
        this.ap = ((kr)ki3.a((int)2)).a;
        this.aI = this.ak = ((kr)ki2.a((int)0)).a;
        this.ah = this.ak;
        this.aJ = this.al = ((kr)ki2.a((int)1)).a;
        this.ai = this.al;
        this.aK = this.am = ((kr)ki2.a((int)2)).a;
        this.aj = this.am;
        this.as = this.aq = ((f)ki4.a((int)0)).a;
        this.at = this.ar = ((f)ki4.a((int)1)).a;
        this.aH = hm2.g("FallDistance");
        this.aT = hm2.d("Fire");
        this.aX = hm2.d("Air");
        this.av = hm2.m("OnGround");
        this.a(this.ak, this.al, this.am);
        this.b(hm2);
    }

    protected final String H() {
        return ew.b(this);
    }

    protected abstract void b(hm var1);

    protected abstract void a(hm var1);

    protected ki a(double ... dArray) {
        ki ki2 = new ki();
        for (double d2 : dArray) {
            ki2.a(new kr(d2));
        }
        return ki2;
    }

    protected ki a(float ... fArray) {
        ki ki2 = new ki();
        for (float f2 : fArray) {
            ki2.a(new f(f2));
        }
        return ki2;
    }

    public float h_() {
        return this.aD / 2.0f;
    }

    public dx b(int n2, int n3) {
        return this.a(n2, n3, 0.0f);
    }

    public dx a(int n2, int n3, float f2) {
        dx dx2 = new dx(this.ag, this.ak, this.al + (double)f2, this.am, new ev(n2, n3));
        dx2.c = 10;
        this.ag.a(dx2);
        return dx2;
    }

    public boolean B() {
        return !this.aA;
    }

    public boolean I() {
        int n2 = eo.b(this.ak);
        int n3 = eo.b(this.al + (double)this.s());
        int n4 = eo.b(this.am);
        return this.ag.g(n2, n3, n4);
    }

    public boolean a(dm dm2) {
        return false;
    }

    public cf b_(kh kh2) {
        return null;
    }

    public void p() {
        if (this.af.aA) {
            this.af = null;
            return;
        }
        this.an = 0.0;
        this.ao = 0.0;
        this.ap = 0.0;
        this.e_();
        this.af.i_();
        this.e += (double)(this.af.aq - this.af.as);
        this.d += (double)(this.af.ar - this.af.at);
        while (this.e >= 180.0) {
            this.e -= 360.0;
        }
        while (this.e < -180.0) {
            this.e += 360.0;
        }
        while (this.d >= 180.0) {
            this.d -= 360.0;
        }
        while (this.d < -180.0) {
            this.d += 360.0;
        }
        double d2 = this.e * 0.5;
        double d3 = this.d * 0.5;
        float f2 = 10.0f;
        if (d2 > (double)f2) {
            d2 = f2;
        }
        if (d2 < (double)(-f2)) {
            d2 = -f2;
        }
        if (d3 > (double)f2) {
            d3 = f2;
        }
        if (d3 < (double)(-f2)) {
            d3 = -f2;
        }
        this.e -= d2;
        this.d -= d3;
        this.aq = (float)((double)this.aq + d2);
        this.ar = (float)((double)this.ar + d3);
    }

    protected void i_() {
        this.ae.a(this.ak, this.al + this.h() + this.ae.v(), this.am);
    }

    public double v() {
        return this.aB;
    }

    public double h() {
        return (double)this.aD * 0.75;
    }

    public void g(kh kh2) {
        this.d = 0.0;
        this.e = 0.0;
        if (this.af == kh2) {
            this.af.ae = null;
            this.af = null;
            this.c(kh2.ak, kh2.au.b + (double)kh2.aD, kh2.am, this.aq, this.ar);
            return;
        }
        if (this.af != null) {
            this.af.ae = null;
        }
        if (kh2.ae != null) {
            kh2.ae.af = null;
        }
        this.af = kh2;
        kh2.ae = this;
    }

    public void a(double d2, double d3, double d4, float f2, float f3, int n2) {
        this.a(d2, d3, d4);
        this.c(f2, f3);
    }
}

