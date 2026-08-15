/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;

public class kg
extends kh {
    private int b = -1;
    private int c = -1;
    private int d = -1;
    private int e = 0;
    private boolean f = false;
    public int a = 0;
    private ge g;
    private int h;
    private int i = 0;

    public kg(cn cn2) {
        super(cn2);
        this.a(0.5f, 0.5f);
    }

    public kg(cn cn2, ge ge2) {
        super(cn2);
        this.g = ge2;
        this.a(0.5f, 0.5f);
        this.c(ge2.ak, ge2.al, ge2.am, ge2.aq, ge2.ar);
        this.ak -= (double)(eo.b(this.aq / 180.0f * (float)Math.PI) * 0.16f);
        this.al -= (double)0.1f;
        this.am -= (double)(eo.a(this.aq / 180.0f * (float)Math.PI) * 0.16f);
        this.a(this.ak, this.al, this.am);
        this.aB = 0.0f;
        this.an = -eo.a(this.aq / 180.0f * (float)Math.PI) * eo.b(this.ar / 180.0f * (float)Math.PI);
        this.ap = eo.b(this.aq / 180.0f * (float)Math.PI) * eo.b(this.ar / 180.0f * (float)Math.PI);
        this.ao = -eo.a(this.ar / 180.0f * (float)Math.PI);
        this.a(this.an, this.ao, this.ap, 1.5f, 1.0f);
    }

    public void a(double d2, double d3, double d4, float f2, float f3) {
        float f4 = eo.a(d2 * d2 + d3 * d3 + d4 * d4);
        d2 /= (double)f4;
        d3 /= (double)f4;
        d4 /= (double)f4;
        d2 += this.aQ.nextGaussian() * (double)0.0075f * (double)f3;
        d3 += this.aQ.nextGaussian() * (double)0.0075f * (double)f3;
        d4 += this.aQ.nextGaussian() * (double)0.0075f * (double)f3;
        this.an = d2 *= (double)f2;
        this.ao = d3 *= (double)f2;
        this.ap = d4 *= (double)f2;
        float f5 = eo.a(d2 * d2 + d4 * d4);
        this.as = this.aq = (float)(Math.atan2(d2, d4) * 180.0 / 3.1415927410125732);
        this.at = this.ar = (float)(Math.atan2(d3, f5) * 180.0 / 3.1415927410125732);
        this.h = 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void e_() {
        float f2;
        block19: {
            super.e_();
            if (this.a > 0) {
                --this.a;
            }
            if (this.f) {
                int n2 = this.ag.a(this.b, this.c, this.d);
                if (n2 != this.e) {
                    this.f = false;
                    this.an *= (double)(this.aQ.nextFloat() * 0.2f);
                    this.ao *= (double)(this.aQ.nextFloat() * 0.2f);
                    this.ap *= (double)(this.aQ.nextFloat() * 0.2f);
                    this.h = 0;
                    this.i = 0;
                    break block19;
                } else {
                    ++this.h;
                    if (this.h == 1200) {
                        this.F();
                    }
                    return;
                }
            }
            ++this.i;
        }
        aj aj2 = aj.b(this.ak, this.al, this.am);
        aj aj3 = aj.b(this.ak + this.an, this.al + this.ao, this.am + this.ap);
        mf mf2 = this.ag.a(aj2, aj3);
        aj2 = aj.b(this.ak, this.al, this.am);
        aj3 = aj.b(this.ak + this.an, this.al + this.ao, this.am + this.ap);
        if (mf2 != null) {
            aj3 = aj.b(mf2.f.a, mf2.f.b, mf2.f.c);
        }
        kh kh2 = null;
        List list = this.ag.b(this, this.au.a(this.an, this.ao, this.ap).b(1.0, 1.0, 1.0));
        double d2 = 0.0;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            double d3;
            cf cf2;
            mf mf3;
            kh kh3 = (kh)list.get(i2);
            if (!kh3.c_() || kh3 == this.g && this.i < 5 || (mf3 = (cf2 = kh3.au.b(f2 = 0.3f, f2, f2)).a(aj2, aj3)) == null || !((d3 = aj2.c(mf3.f)) < d2) && d2 != 0.0) continue;
            kh2 = kh3;
            d2 = d3;
        }
        if (kh2 != null) {
            mf2 = new mf(kh2);
        }
        if (mf2 != null) {
            if (mf2.g != null) {
                if (mf2.g.a(this.g, 4)) {
                    this.ag.a(this, "random.drr", 1.0f, 1.2f / (this.aQ.nextFloat() * 0.2f + 0.9f));
                    this.F();
                } else {
                    this.an *= (double)-0.1f;
                    this.ao *= (double)-0.1f;
                    this.ap *= (double)-0.1f;
                    this.aq += 180.0f;
                    this.as += 180.0f;
                    this.i = 0;
                }
            } else {
                this.b = mf2.b;
                this.c = mf2.c;
                this.d = mf2.d;
                this.e = this.ag.a(this.b, this.c, this.d);
                this.an = (float)(mf2.f.a - this.ak);
                this.ao = (float)(mf2.f.b - this.al);
                this.ap = (float)(mf2.f.c - this.am);
                float f3 = eo.a(this.an * this.an + this.ao * this.ao + this.ap * this.ap);
                this.ak -= this.an / (double)f3 * (double)0.05f;
                this.al -= this.ao / (double)f3 * (double)0.05f;
                this.am -= this.ap / (double)f3 * (double)0.05f;
                this.ag.a(this, "random.drr", 1.0f, 1.2f / (this.aQ.nextFloat() * 0.2f + 0.9f));
                this.f = true;
                this.a = 7;
            }
        }
        this.ak += this.an;
        this.al += this.ao;
        this.am += this.ap;
        float f4 = eo.a(this.an * this.an + this.ap * this.ap);
        this.aq = (float)(Math.atan2(this.an, this.ap) * 180.0 / 3.1415927410125732);
        this.ar = (float)(Math.atan2(this.ao, f4) * 180.0 / 3.1415927410125732);
        while (this.ar - this.at < -180.0f) {
            this.at -= 360.0f;
        }
        while (this.ar - this.at >= 180.0f) {
            this.at += 360.0f;
        }
        while (this.aq - this.as < -180.0f) {
            this.as -= 360.0f;
        }
        while (this.aq - this.as >= 180.0f) {
            this.as += 360.0f;
        }
        this.ar = this.at + (this.ar - this.at) * 0.2f;
        this.aq = this.as + (this.aq - this.as) * 0.2f;
        float f5 = 0.99f;
        f2 = 0.03f;
        if (this.g_()) {
            for (int i3 = 0; i3 < 4; ++i3) {
                float f6 = 0.25f;
                this.ag.a("bubble", this.ak - this.an * (double)f6, this.al - this.ao * (double)f6, this.am - this.ap * (double)f6, this.an, this.ao, this.ap);
            }
            f5 = 0.8f;
        }
        this.an *= (double)f5;
        this.ao *= (double)f5;
        this.ap *= (double)f5;
        this.ao -= (double)f2;
        this.a(this.ak, this.al, this.am);
    }

    public void a(hm hm2) {
        hm2.a("xTile", (short)this.b);
        hm2.a("yTile", (short)this.c);
        hm2.a("zTile", (short)this.d);
        hm2.a("inTile", (byte)this.e);
        hm2.a("shake", (byte)this.a);
        hm2.a("inGround", (byte)(this.f ? 1 : 0));
    }

    public void b(hm hm2) {
        this.b = hm2.d("xTile");
        this.c = hm2.d("yTile");
        this.d = hm2.d("zTile");
        this.e = hm2.c("inTile") & 0xFF;
        this.a = hm2.c("shake") & 0xFF;
        this.f = hm2.c("inGround") == 1;
    }

    public void b(dm dm2) {
        if (this.f && this.g == dm2 && this.a <= 0 && dm2.b.a(new ev(di.j.aS, 1))) {
            this.ag.a(this, "random.pop", 0.2f, ((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.7f + 1.0f) * 2.0f);
            dm2.a_(this, 1);
            this.F();
        }
    }

    public float h_() {
        return 0.0f;
    }
}

