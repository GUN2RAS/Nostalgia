/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;

public class ge
extends kh {
    public int j = 20;
    public float k;
    public float l;
    public float m;
    public float n = 0.0f;
    public float o = 0.0f;
    protected float p;
    protected float q;
    protected float r;
    protected float s;
    protected boolean t = true;
    protected String u = "/char.png";
    protected boolean v = true;
    protected float w = 0.0f;
    protected String x = null;
    protected float y = 1.0f;
    protected int z = 0;
    protected float A = 0.0f;
    public boolean B = false;
    public float C;
    public float D;
    public int E = 10;
    public int F;
    private int a;
    public int G;
    public int H;
    public float I = 0.0f;
    public int J = 0;
    public int K = 0;
    public float L;
    public float M;
    protected boolean N = false;
    public int O = -1;
    public float P = (float)(Math.random() * (double)0.9f + (double)0.1f);
    public float Q;
    public float R;
    public float S;
    private int b;
    private double c;
    private double d;
    private double e;
    private double f;
    private double g;
    float T = 0.0f;
    protected int U = 0;
    protected float V;
    protected float W;
    protected float X;
    protected boolean Y = false;
    protected float Z = 0.0f;
    protected float aa = 0.7f;
    private kh h;
    private int i = 0;

    public ge(cn cn2) {
        super(cn2);
        this.ad = true;
        this.m = (float)(Math.random() + 1.0) * 0.01f;
        this.a(this.ak, this.al, this.am);
        this.k = (float)Math.random() * 12398.0f;
        this.aq = (float)(Math.random() * 3.1415927410125732 * 2.0);
        this.l = 1.0f;
        this.aM = 0.5f;
    }

    protected boolean c(kh kh2) {
        return this.ag.a(aj.b(this.ak, this.al + (double)this.s(), this.am), aj.b(kh2.ak, kh2.al + (double)kh2.s(), kh2.am)) == null;
    }

    public String x() {
        return this.u;
    }

    public boolean c_() {
        return !this.aA;
    }

    public boolean d_() {
        return !this.aA;
    }

    protected float s() {
        return this.aD * 0.85f;
    }

    public int b() {
        return 80;
    }

    public void y() {
        this.C = this.D;
        super.y();
        if (this.aQ.nextInt(1000) < this.a++) {
            this.a = -this.b();
            String string = this.c();
            if (string != null) {
                this.ag.a(this, string, this.f(), (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f);
            }
        }
        if (this.B() && this.I()) {
            this.a(null, 1);
        }
        if (this.B() && this.a(gb.f)) {
            --this.aX;
            if (this.aX == -20) {
                this.aX = 0;
                for (int i2 = 0; i2 < 8; ++i2) {
                    float f2 = this.aQ.nextFloat() - this.aQ.nextFloat();
                    float f3 = this.aQ.nextFloat() - this.aQ.nextFloat();
                    float f4 = this.aQ.nextFloat() - this.aQ.nextFloat();
                    this.ag.a("bubble", this.ak + (double)f2, this.al + (double)f3, this.am + (double)f4, this.an, this.ao, this.ap);
                }
                this.a(null, 2);
            }
            this.aT = 0;
        } else {
            this.aX = this.aU;
        }
        this.L = this.M;
        if (this.K > 0) {
            --this.K;
        }
        if (this.G > 0) {
            --this.G;
        }
        if (this.aW > 0) {
            --this.aW;
        }
        if (this.E <= 0) {
            ++this.J;
            if (this.J > 20) {
                this.D();
                this.F();
                for (int i3 = 0; i3 < 20; ++i3) {
                    double d2 = this.aQ.nextGaussian() * 0.02;
                    double d3 = this.aQ.nextGaussian() * 0.02;
                    double d4 = this.aQ.nextGaussian() * 0.02;
                    this.ag.a("explode", this.ak + (double)(this.aQ.nextFloat() * this.aC * 2.0f) - (double)this.aC, this.al + (double)(this.aQ.nextFloat() * this.aD), this.am + (double)(this.aQ.nextFloat() * this.aC * 2.0f) - (double)this.aC, d2, d3, d4);
                }
            }
        }
        this.s = this.r;
        this.o = this.n;
        this.as = this.aq;
        this.at = this.ar;
    }

    public void z() {
        for (int i2 = 0; i2 < 20; ++i2) {
            double d2 = this.aQ.nextGaussian() * 0.02;
            double d3 = this.aQ.nextGaussian() * 0.02;
            double d4 = this.aQ.nextGaussian() * 0.02;
            double d5 = 10.0;
            this.ag.a("explode", this.ak + (double)(this.aQ.nextFloat() * this.aC * 2.0f) - (double)this.aC - d2 * d5, this.al + (double)(this.aQ.nextFloat() * this.aD) - d3 * d5, this.am + (double)(this.aQ.nextFloat() * this.aC * 2.0f) - (double)this.aC - d4 * d5, d2, d3, d4);
        }
    }

    public void p() {
        super.p();
        this.p = this.q;
        this.q = 0.0f;
    }

    public void a(double d2, double d3, double d4, float f2, float f3, int n2) {
        this.aB = 0.0f;
        this.c = d2;
        this.d = d3;
        this.e = d4;
        this.f = f2;
        this.g = f3;
        this.b = n2;
    }

    public void e_() {
        boolean bl2;
        float f2;
        float f3;
        super.e_();
        this.j();
        double d2 = this.ak - this.ah;
        double d3 = this.am - this.aj;
        float f4 = eo.a(d2 * d2 + d3 * d3);
        float f5 = this.n;
        float f6 = 0.0f;
        this.p = this.q;
        float f7 = 0.0f;
        if (!(f4 <= 0.05f)) {
            f7 = 1.0f;
            f6 = f4 * 3.0f;
            f5 = (float)Math.atan2(d3, d2) * 180.0f / (float)Math.PI - 90.0f;
        }
        if (this.D > 0.0f) {
            f5 = this.aq;
        }
        if (!this.av) {
            f7 = 0.0f;
        }
        this.q += (f7 - this.q) * 0.3f;
        for (f3 = f5 - this.n; f3 < -180.0f; f3 += 360.0f) {
        }
        while (f3 >= 180.0f) {
            f3 -= 360.0f;
        }
        this.n += f3 * 0.3f;
        for (f2 = this.aq - this.n; f2 < -180.0f; f2 += 360.0f) {
        }
        while (f2 >= 180.0f) {
            f2 -= 360.0f;
        }
        boolean bl3 = bl2 = f2 < -90.0f || f2 >= 90.0f;
        if (f2 < -75.0f) {
            f2 = -75.0f;
        }
        if (f2 >= 75.0f) {
            f2 = 75.0f;
        }
        this.n = this.aq - f2;
        if (f2 * f2 > 2500.0f) {
            this.n += f2 * 0.2f;
        }
        if (bl2) {
            f6 *= -1.0f;
        }
        while (this.aq - this.as < -180.0f) {
            this.as -= 360.0f;
        }
        while (this.aq - this.as >= 180.0f) {
            this.as += 360.0f;
        }
        while (this.n - this.o < -180.0f) {
            this.o -= 360.0f;
        }
        while (this.n - this.o >= 180.0f) {
            this.o += 360.0f;
        }
        while (this.ar - this.at < -180.0f) {
            this.at -= 360.0f;
        }
        while (this.ar - this.at >= 180.0f) {
            this.at += 360.0f;
        }
        this.r += f6;
    }

    protected void a(float f2, float f3) {
        super.a(f2, f3);
    }

    public void b(int n2) {
        if (this.E <= 0) {
            return;
        }
        this.E += n2;
        if (this.E > 20) {
            this.E = 20;
        }
        this.aW = this.j / 2;
    }

    public boolean a(kh kh2, int n2) {
        if (this.ag.y) {
            n2 = 0;
        }
        this.U = 0;
        if (this.E <= 0) {
            return false;
        }
        this.R = 1.5f;
        if ((float)this.aW > (float)this.j / 2.0f) {
            if (this.F - n2 >= this.E) {
                return false;
            }
            this.E = this.F - n2;
        } else {
            this.F = this.E;
            this.aW = this.j;
            this.E -= n2;
            this.H = 10;
            this.G = 10;
        }
        this.I = 0.0f;
        if (kh2 != null) {
            double d2 = kh2.ak - this.ak;
            double d3 = kh2.am - this.am;
            while (d2 * d2 + d3 * d3 < 1.0E-4) {
                d2 = (Math.random() - Math.random()) * 0.01;
                d3 = (Math.random() - Math.random()) * 0.01;
            }
            this.I = (float)(Math.atan2(d3, d2) * 180.0 / 3.1415927410125732) - this.aq;
            this.a(kh2, n2, d2, d3);
        } else {
            this.I = (int)(Math.random() * 2.0) * 180;
        }
        if (this.E <= 0) {
            this.ag.a(this, this.e(), this.f(), (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f);
            this.b(kh2);
        } else {
            this.ag.a(this, this.d(), this.f(), (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f);
        }
        return true;
    }

    protected float f() {
        return 1.0f;
    }

    protected String c() {
        return null;
    }

    protected String d() {
        return "random.hurt";
    }

    protected String e() {
        return "random.hurt";
    }

    public void a(kh kh2, int n2, double d2, double d3) {
        float f2 = eo.a(d2 * d2 + d3 * d3);
        float f3 = 0.4f;
        this.an /= 2.0;
        this.ao /= 2.0;
        this.ap /= 2.0;
        this.an -= d2 / (double)f2 * (double)f3;
        this.ao += (double)0.4f;
        this.ap -= d3 / (double)f2 * (double)f3;
        if (this.ao > (double)0.4f) {
            this.ao = 0.4f;
        }
    }

    public void b(kh kh2) {
        if (this.z > 0 && kh2 != null) {
            kh2.b(this, this.z);
        }
        this.N = true;
        int n2 = this.g();
        if (n2 > 0) {
            int n3 = this.aQ.nextInt(3);
            for (int i2 = 0; i2 < n3; ++i2) {
                this.b(n2, 1);
            }
        }
    }

    protected int g() {
        return 0;
    }

    protected void c(float f2) {
        int n2 = (int)Math.ceil(f2 - 3.0f);
        if (n2 > 0) {
            this.a(null, n2);
            int n3 = this.ag.a(eo.b(this.ak), eo.b(this.al - (double)0.2f - (double)this.aB), eo.b(this.am));
            if (n3 > 0) {
                bb bb2 = ly.n[n3].bl;
                this.ag.a(this, bb2.d(), bb2.b() * 0.5f, bb2.c() * 0.75f);
            }
        }
    }

    public void b(float f2, float f3) {
        if (this.g_()) {
            double d2 = this.al;
            this.a(f2, f3, 0.02f);
            this.c(this.an, this.ao, this.ap);
            this.an *= (double)0.8f;
            this.ao *= (double)0.8f;
            this.ap *= (double)0.8f;
            this.ao -= 0.02;
            if (this.aw && this.b(this.an, this.ao + (double)0.6f - this.al + d2, this.ap)) {
                this.ao = 0.3f;
            }
        } else if (this.G()) {
            double d3 = this.al;
            this.a(f2, f3, 0.02f);
            this.c(this.an, this.ao, this.ap);
            this.an *= 0.5;
            this.ao *= 0.5;
            this.ap *= 0.5;
            this.ao -= 0.02;
            if (this.aw && this.b(this.an, this.ao + (double)0.6f - this.al + d3, this.ap)) {
                this.ao = 0.3f;
            }
        } else {
            float f4 = 0.91f;
            if (this.av) {
                f4 = 0.54600006f;
                int n2 = this.ag.a(eo.b(this.ak), eo.b(this.au.b) - 1, eo.b(this.am));
                if (n2 > 0) {
                    f4 = ly.n[n2].bo * 0.91f;
                }
            }
            float f5 = 0.16277136f / (f4 * f4 * f4);
            this.a(f2, f3, this.av ? 0.1f * f5 : 0.02f);
            f4 = 0.91f;
            if (this.av) {
                f4 = 0.54600006f;
                int n3 = this.ag.a(eo.b(this.ak), eo.b(this.au.b) - 1, eo.b(this.am));
                if (n3 > 0) {
                    f4 = ly.n[n3].bo * 0.91f;
                }
            }
            if (this.A()) {
                this.aH = 0.0f;
                if (this.ao < -0.15) {
                    this.ao = -0.15;
                }
            }
            this.c(this.an, this.ao, this.ap);
            if (this.aw && this.A()) {
                this.ao = 0.2;
            }
            this.ao -= 0.08;
            this.ao *= (double)0.98f;
            this.an *= (double)f4;
            this.ap *= (double)f4;
        }
        this.Q = this.R;
        double d4 = this.ak - this.ah;
        double d5 = this.am - this.aj;
        float f6 = eo.a(d4 * d4 + d5 * d5) * 4.0f;
        if (f6 > 1.0f) {
            f6 = 1.0f;
        }
        this.R += (f6 - this.R) * 0.4f;
        this.S += this.R;
    }

    public boolean A() {
        int n2;
        int n3;
        int n4 = eo.b(this.ak);
        return this.ag.a(n4, n3 = eo.b(this.au.b), n2 = eo.b(this.am)) == ly.aG.bc || this.ag.a(n4, n3 + 1, n2) == ly.aG.bc;
    }

    public void a(hm hm2) {
        hm2.a("Health", (short)this.E);
        hm2.a("HurtTime", (short)this.G);
        hm2.a("DeathTime", (short)this.J);
        hm2.a("AttackTime", (short)this.K);
    }

    public void b(hm hm2) {
        this.E = hm2.d("Health");
        if (!hm2.b("Health")) {
            this.E = 10;
        }
        this.G = hm2.d("HurtTime");
        this.J = hm2.d("DeathTime");
        this.K = hm2.d("AttackTime");
    }

    public boolean B() {
        return !this.aA && this.E > 0;
    }

    public void j() {
        if (this.b > 0) {
            double d2;
            double d3 = this.ak + (this.c - this.ak) / (double)this.b;
            double d4 = this.al + (this.d - this.al) / (double)this.b;
            double d5 = this.am + (this.e - this.am) / (double)this.b;
            for (d2 = this.f - (double)this.aq; d2 < -180.0; d2 += 360.0) {
            }
            while (d2 >= 180.0) {
                d2 -= 360.0;
            }
            this.aq = (float)((double)this.aq + d2 / (double)this.b);
            this.ar = (float)((double)this.ar + (this.g - (double)this.ar) / (double)this.b);
            --this.b;
            this.a(d3, d4, d5);
            this.c(this.aq, this.ar);
        }
        if (this.E <= 0) {
            this.Y = false;
            this.V = 0.0f;
            this.W = 0.0f;
            this.X = 0.0f;
        } else if (!this.B) {
            this.b_();
        }
        boolean bl2 = this.g_();
        boolean bl3 = this.G();
        if (this.Y) {
            if (bl2) {
                this.ao += (double)0.04f;
            } else if (bl3) {
                this.ao += (double)0.04f;
            } else if (this.av) {
                this.C();
            }
        }
        this.V *= 0.98f;
        this.W *= 0.98f;
        this.X *= 0.9f;
        this.b(this.V, this.W);
        List list = this.ag.b(this, this.au.b(0.2f, 0.0, 0.2f));
        if (list != null && list.size() > 0) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                kh kh2 = (kh)list.get(i2);
                if (!kh2.d_()) continue;
                kh2.f(this);
            }
        }
    }

    protected void C() {
        this.ao = 0.42f;
    }

    protected void b_() {
        ++this.U;
        dm dm2 = this.ag.a((kh)this, -1.0);
        if (dm2 != null) {
            double d2 = dm2.ak - this.ak;
            double d3 = dm2.al - this.al;
            double d4 = dm2.am - this.am;
            double d5 = d2 * d2 + d3 * d3 + d4 * d4;
            if (d5 > 16384.0) {
                this.F();
            }
            if (this.U > 600 && this.aQ.nextInt(800) == 0) {
                if (d5 < 1024.0) {
                    this.U = 0;
                } else {
                    this.F();
                }
            }
        }
        this.V = 0.0f;
        this.W = 0.0f;
        float f2 = 8.0f;
        if (this.aQ.nextFloat() < 0.02f) {
            dm2 = this.ag.a((kh)this, f2);
            if (dm2 != null) {
                this.h = dm2;
                this.i = 10 + this.aQ.nextInt(20);
            } else {
                this.X = (this.aQ.nextFloat() - 0.5f) * 20.0f;
            }
        }
        if (this.h != null) {
            this.b(this.h, 10.0f);
            if (this.i-- <= 0 || this.h.aA || this.h.e(this) > (double)(f2 * f2)) {
                this.h = null;
            }
        } else {
            if (this.aQ.nextFloat() < 0.05f) {
                this.X = (this.aQ.nextFloat() - 0.5f) * 20.0f;
            }
            this.aq += this.X;
            this.ar = this.Z;
        }
        boolean bl2 = this.g_();
        boolean bl3 = this.G();
        if (bl2 || bl3) {
            this.Y = this.aQ.nextFloat() < 0.8f;
        }
    }

    public void b(kh kh2, float f2) {
        double d2;
        double d3 = kh2.ak - this.ak;
        double d4 = kh2.am - this.am;
        if (kh2 instanceof ge) {
            ge ge2 = (ge)kh2;
            d2 = ge2.al + (double)ge2.s() - (this.al + (double)this.s());
        } else {
            d2 = (kh2.au.b + kh2.au.e) / 2.0 - (this.al + (double)this.s());
        }
        double d5 = eo.a(d3 * d3 + d4 * d4);
        float f3 = (float)(Math.atan2(d4, d3) * 180.0 / 3.1415927410125732) - 90.0f;
        float f4 = (float)(Math.atan2(d2, d5) * 180.0 / 3.1415927410125732);
        this.ar = this.b(this.ar, f4, f2);
        this.aq = this.b(this.aq, f3, f2);
    }

    private float b(float f2, float f3, float f4) {
        float f5;
        for (f5 = f3 - f2; f5 < -180.0f; f5 += 360.0f) {
        }
        while (f5 >= 180.0f) {
            f5 -= 360.0f;
        }
        if (f5 > f4) {
            f5 = f4;
        }
        if (f5 < -f4) {
            f5 = -f4;
        }
        return f2 + f5;
    }

    public void D() {
    }

    public boolean a() {
        return this.ag.a(this.au) && this.ag.a((kh)this, this.au).size() == 0 && !this.ag.b(this.au);
    }

    protected void E() {
        this.a(null, 4);
    }

    public float d(float f2) {
        float f3 = this.D - this.C;
        if (f3 < 0.0f) {
            f3 += 1.0f;
        }
        return this.C + f3 * f2;
    }

    public aj e(float f2) {
        if (f2 == 1.0f) {
            return aj.b(this.ak, this.al, this.am);
        }
        double d2 = this.ah + (this.ak - this.ah) * (double)f2;
        double d3 = this.ai + (this.al - this.ai) * (double)f2;
        double d4 = this.aj + (this.am - this.aj) * (double)f2;
        return aj.b(d2, d3, d4);
    }

    public aj f(float f2) {
        if (f2 == 1.0f) {
            float f3 = eo.b(-this.aq * ((float)Math.PI / 180) - (float)Math.PI);
            float f4 = eo.a(-this.aq * ((float)Math.PI / 180) - (float)Math.PI);
            float f5 = -eo.b(-this.ar * ((float)Math.PI / 180));
            float f6 = eo.a(-this.ar * ((float)Math.PI / 180));
            return aj.b(f4 * f5, f6, f3 * f5);
        }
        float f7 = this.at + (this.ar - this.at) * f2;
        float f8 = this.as + (this.aq - this.as) * f2;
        float f9 = eo.b(-f8 * ((float)Math.PI / 180) - (float)Math.PI);
        float f10 = eo.a(-f8 * ((float)Math.PI / 180) - (float)Math.PI);
        float f11 = -eo.b(-f7 * ((float)Math.PI / 180));
        float f12 = eo.a(-f7 * ((float)Math.PI / 180));
        return aj.b(f10 * f11, f12, f9 * f11);
    }

    public mf a(double d2, float f2) {
        aj aj2 = this.e(f2);
        aj aj3 = this.f(f2);
        aj aj4 = aj2.c(aj3.a * d2, aj3.b * d2, aj3.c * d2);
        return this.ag.a(aj2, aj4);
    }
}

