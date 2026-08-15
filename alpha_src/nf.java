/*
 * Decompiled with CFR 0.152.
 */
public class nf
extends nq {
    public nf(cn cn2, double d2, double d3, double d4) {
        super(cn2, d2, d3, d4, 0.0, 0.0, 0.0);
        this.an *= (double)0.3f;
        this.ao = (float)Math.random() * 0.2f + 0.1f;
        this.ap *= (double)0.3f;
        this.i = 1.0f;
        this.j = 1.0f;
        this.k = 1.0f;
        this.b = 19 + this.aQ.nextInt(4);
        this.a(0.01f, 0.01f);
        this.h = 0.06f;
        this.f = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
        super.a(ho2, f2, f3, f4, f5, f6, f7);
    }

    public void e_() {
        double d2;
        gb gb2;
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        this.ao -= (double)this.h;
        this.c(this.an, this.ao, this.ap);
        this.an *= (double)0.98f;
        this.ao *= (double)0.98f;
        this.ap *= (double)0.98f;
        if (this.f-- <= 0) {
            this.F();
        }
        if (this.av) {
            if (Math.random() < 0.5) {
                this.F();
            }
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
        }
        if (((gb2 = this.ag.f(eo.b(this.ak), eo.b(this.al), eo.b(this.am))).d() || gb2.a()) && this.al < (d2 = (double)((float)(eo.b(this.al) + 1) - jp.b(this.ag.e(eo.b(this.ak), eo.b(this.al), eo.b(this.am)))))) {
            this.F();
        }
    }
}

