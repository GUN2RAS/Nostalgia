/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;

public class dc
extends kh {
    public int a = 0;
    public int b = 0;
    public int c = 1;

    public dc(cn cn2) {
        super(cn2);
        this.ad = true;
        this.a(1.5f, 0.6f);
        this.aB = this.aD / 2.0f;
        this.aG = false;
    }

    public cf b_(kh kh2) {
        return kh2.au;
    }

    public cf f_() {
        return this.au;
    }

    public boolean d_() {
        return true;
    }

    public dc(cn cn2, double d2, double d3, double d4) {
        this(cn2);
        this.a(d2, d3 + (double)this.aB, d4);
        this.an = 0.0;
        this.ao = 0.0;
        this.ap = 0.0;
        this.ah = d2;
        this.ai = d3;
        this.aj = d4;
    }

    public double h() {
        return (double)this.aD * 0.0 - (double)0.3f;
    }

    public boolean a(kh kh2, int n2) {
        this.c = -this.c;
        this.b = 10;
        this.a += n2 * 10;
        if (this.a > 40) {
            int n3;
            for (n3 = 0; n3 < 3; ++n3) {
                this.a(ly.y.bc, 1, 0.0f);
            }
            for (n3 = 0; n3 < 2; ++n3) {
                this.a(di.B.aS, 1, 0.0f);
            }
            this.F();
        }
        return true;
    }

    public boolean c_() {
        return !this.aA;
    }

    public void e_() {
        double d2;
        double d3;
        double d4;
        super.e_();
        if (this.b > 0) {
            --this.b;
        }
        if (this.a > 0) {
            --this.a;
        }
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        int n2 = 5;
        double d5 = 0.0;
        for (int i2 = 0; i2 < n2; ++i2) {
            double d6 = this.au.b + (this.au.e - this.au.b) * (double)(i2 + 0) / (double)n2 - 0.125;
            double d7 = this.au.b + (this.au.e - this.au.b) * (double)(i2 + 1) / (double)n2 - 0.125;
            cf cf2 = cf.b(this.au.a, d6, this.au.c, this.au.d, d7, this.au.f);
            if (!this.ag.b(cf2, gb.f)) continue;
            d5 += 1.0 / (double)n2;
        }
        double d8 = d5 * 2.0 - 1.0;
        this.ao += (double)0.04f * d8;
        if (this.ae != null) {
            this.an += this.ae.an * 0.2;
            this.ap += this.ae.ap * 0.2;
        }
        if (this.an < -(d4 = 0.4)) {
            this.an = -d4;
        }
        if (this.an > d4) {
            this.an = d4;
        }
        if (this.ap < -d4) {
            this.ap = -d4;
        }
        if (this.ap > d4) {
            this.ap = d4;
        }
        if (this.av) {
            this.an *= 0.5;
            this.ao *= 0.5;
            this.ap *= 0.5;
        }
        this.c(this.an, this.ao, this.ap);
        double d9 = Math.sqrt(this.an * this.an + this.ap * this.ap);
        if (d9 > 0.15) {
            double d10 = Math.cos((double)this.aq * Math.PI / 180.0);
            d3 = Math.sin((double)this.aq * Math.PI / 180.0);
            int n3 = 0;
            while ((double)n3 < 1.0 + d9 * 60.0) {
                double d11;
                double d12;
                double d13 = this.aQ.nextFloat() * 2.0f - 1.0f;
                double d14 = (double)(this.aQ.nextInt(2) * 2 - 1) * 0.7;
                if (this.aQ.nextBoolean()) {
                    d12 = this.ak - d10 * d13 * 0.8 + d3 * d14;
                    d11 = this.am - d3 * d13 * 0.8 - d10 * d14;
                    this.ag.a("splash", d12, this.al - 0.125, d11, this.an, this.ao, this.ap);
                } else {
                    d12 = this.ak + d10 + d3 * d13 * 0.7;
                    d11 = this.am + d3 - d10 * d13 * 0.7;
                    this.ag.a("splash", d12, this.al - 0.125, d11, this.an, this.ao, this.ap);
                }
                ++n3;
            }
        }
        if (this.aw && d9 > 0.15) {
            int n4;
            this.F();
            for (n4 = 0; n4 < 3; ++n4) {
                this.a(ly.y.bc, 1, 0.0f);
            }
            for (n4 = 0; n4 < 2; ++n4) {
                this.a(di.B.aS, 1, 0.0f);
            }
        } else {
            this.an *= (double)0.99f;
            this.ao *= (double)0.95f;
            this.ap *= (double)0.99f;
        }
        this.ar = 0.0f;
        double d15 = this.aq;
        d3 = this.ah - this.ak;
        double d16 = this.aj - this.am;
        if (d3 * d3 + d16 * d16 > 0.001) {
            d15 = (float)(Math.atan2(d16, d3) * 180.0 / Math.PI);
        }
        for (d2 = d15 - (double)this.aq; d2 >= 180.0; d2 -= 360.0) {
        }
        while (d2 < -180.0) {
            d2 += 360.0;
        }
        if (d2 > 20.0) {
            d2 = 20.0;
        }
        if (d2 < -20.0) {
            d2 = -20.0;
        }
        this.aq = (float)((double)this.aq + d2);
        this.c(this.aq, this.ar);
        List list = this.ag.b(this, this.au.b(0.2f, 0.0, 0.2f));
        if (list != null && list.size() > 0) {
            for (int i3 = 0; i3 < list.size(); ++i3) {
                kh kh2 = (kh)list.get(i3);
                if (kh2 == this.ae || !kh2.d_() || !(kh2 instanceof dc)) continue;
                kh2.f(this);
            }
        }
        if (this.ae != null && this.ae.aA) {
            this.ae = null;
        }
    }

    protected void i_() {
        double d2 = Math.cos((double)this.aq * Math.PI / 180.0) * 0.4;
        double d3 = Math.sin((double)this.aq * Math.PI / 180.0) * 0.4;
        this.ae.a(this.ak + d2, this.al + this.h() + this.ae.v(), this.am + d3);
    }

    protected void a(hm hm2) {
    }

    protected void b(hm hm2) {
    }

    public float h_() {
        return 0.0f;
    }

    public boolean a(dm dm2) {
        dm2.g(this);
        return true;
    }
}

