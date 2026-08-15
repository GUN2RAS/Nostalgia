/*
 * Decompiled with CFR 0.152.
 */
public class ba
extends nq {
    public ba(cn cn2, double d2, double d3, double d4, double d5, double d6, double d7) {
        super(cn2, d2, d3, d4, d5, d6, d7);
        this.i = 1.0f;
        this.j = 1.0f;
        this.k = 1.0f;
        this.b = 32;
        this.a(0.02f, 0.02f);
        this.g *= this.aQ.nextFloat() * 0.6f + 0.2f;
        this.an = d5 * (double)0.2f + (double)((float)(Math.random() * 2.0 - 1.0) * 0.02f);
        this.ao = d6 * (double)0.2f + (double)((float)(Math.random() * 2.0 - 1.0) * 0.02f);
        this.ap = d7 * (double)0.2f + (double)((float)(Math.random() * 2.0 - 1.0) * 0.02f);
        this.f = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    public void e_() {
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        this.ao += 0.002;
        this.c(this.an, this.ao, this.ap);
        this.an *= (double)0.85f;
        this.ao *= (double)0.85f;
        this.ap *= (double)0.85f;
        if (this.ag.f(eo.b(this.ak), eo.b(this.al), eo.b(this.am)) != gb.f) {
            this.F();
        }
        if (this.f-- <= 0) {
            this.F();
        }
    }
}

