/*
 * Decompiled with CFR 0.152.
 */
public class dp
extends nq {
    public dp(cn cn2, double d2, double d3, double d4, double d5, double d6, double d7) {
        super(cn2, d2, d3, d4, d5, d6, d7);
        this.an = d5 + (double)((float)(Math.random() * 2.0 - 1.0) * 0.05f);
        this.ao = d6 + (double)((float)(Math.random() * 2.0 - 1.0) * 0.05f);
        this.ap = d7 + (double)((float)(Math.random() * 2.0 - 1.0) * 0.05f);
        this.j = this.k = this.aQ.nextFloat() * 0.3f + 0.7f;
        this.i = this.k;
        this.g = this.aQ.nextFloat() * this.aQ.nextFloat() * 6.0f + 1.0f;
        this.f = (int)(16.0 / ((double)this.aQ.nextFloat() * 0.8 + 0.2)) + 2;
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
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
        this.an *= (double)0.9f;
        this.ao *= (double)0.9f;
        this.ap *= (double)0.9f;
        if (this.av) {
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
        }
    }
}

