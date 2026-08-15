/*
 * Decompiled with CFR 0.152.
 */
public class cq
extends nq {
    private float a;

    public cq(cn cn2, double d2, double d3, double d4) {
        super(cn2, d2, d3, d4, 0.0, 0.0, 0.0);
        this.an *= (double)0.8f;
        this.ao *= (double)0.8f;
        this.ap *= (double)0.8f;
        this.ao = this.aQ.nextFloat() * 0.4f + 0.05f;
        this.k = 1.0f;
        this.j = 1.0f;
        this.i = 1.0f;
        this.g *= this.aQ.nextFloat() * 2.0f + 0.2f;
        this.a = this.g;
        this.f = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.aN = false;
        this.b = 49;
    }

    public float a(float f2) {
        return 1.0f;
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = ((float)this.e + f2) / (float)this.f;
        this.g = this.a * (1.0f - f8 * f8);
        super.a(ho2, f2, f3, f4, f5, f6, f7);
    }

    public void e_() {
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        if (this.e++ >= this.f) {
            this.F();
        }
        float f2 = (float)this.e / (float)this.f;
        if (this.aQ.nextFloat() > f2) {
            this.ag.a("smoke", this.ak, this.al, this.am, this.an, this.ao, this.ap);
        }
        this.ao -= 0.03;
        this.c(this.an, this.ao, this.ap);
        this.an *= (double)0.999f;
        this.ao *= (double)0.999f;
        this.ap *= (double)0.999f;
        if (this.av) {
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
        }
    }
}

