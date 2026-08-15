/*
 * Decompiled with CFR 0.152.
 */
public class nl
extends nq {
    float a;

    public nl(cn cn2, double d2, double d3, double d4) {
        this(cn2, d2, d3, d4, 1.0f);
    }

    public nl(cn cn2, double d2, double d3, double d4, float f2) {
        super(cn2, d2, d3, d4, 0.0, 0.0, 0.0);
        this.an *= (double)0.1f;
        this.ao *= (double)0.1f;
        this.ap *= (double)0.1f;
        this.j = this.k = (float)(Math.random() * (double)0.3f);
        this.i = this.k;
        this.g *= 0.75f;
        this.g *= f2;
        this.a = this.g;
        this.f = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.f = (int)((float)this.f * f2);
        this.aN = false;
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = ((float)this.e + f2) / (float)this.f * 32.0f;
        if (f8 < 0.0f) {
            f8 = 0.0f;
        }
        if (f8 > 1.0f) {
            f8 = 1.0f;
        }
        this.g = this.a * f8;
        super.a(ho2, f2, f3, f4, f5, f6, f7);
    }

    public void e_() {
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        if (this.e++ >= this.f) {
            this.F();
        }
        this.b = 7 - this.e * 8 / this.f;
        this.ao += 0.004;
        this.c(this.an, this.ao, this.ap);
        if (this.al == this.ai) {
            this.an *= 1.1;
            this.ap *= 1.1;
        }
        this.an *= (double)0.96f;
        this.ao *= (double)0.96f;
        this.ap *= (double)0.96f;
        if (this.av) {
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
        }
    }
}

