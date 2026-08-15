/*
 * Decompiled with CFR 0.152.
 */
public class nq
extends kh {
    protected int b;
    protected float c;
    protected float d;
    protected int e = 0;
    protected int f = 0;
    protected float g;
    protected float h;
    protected float i;
    protected float j;
    protected float k;
    public static double l;
    public static double m;
    public static double n;

    public nq(cn cn2, double d2, double d3, double d4, double d5, double d6, double d7) {
        super(cn2);
        this.a(0.2f, 0.2f);
        this.aB = this.aD / 2.0f;
        this.a(d2, d3, d4);
        this.k = 1.0f;
        this.j = 1.0f;
        this.i = 1.0f;
        this.an = d5 + (double)((float)(Math.random() * 2.0 - 1.0) * 0.4f);
        this.ao = d6 + (double)((float)(Math.random() * 2.0 - 1.0) * 0.4f);
        this.ap = d7 + (double)((float)(Math.random() * 2.0 - 1.0) * 0.4f);
        float f2 = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        float f3 = eo.a(this.an * this.an + this.ao * this.ao + this.ap * this.ap);
        this.an = this.an / (double)f3 * (double)f2 * (double)0.4f;
        this.ao = this.ao / (double)f3 * (double)f2 * (double)0.4f + (double)0.1f;
        this.ap = this.ap / (double)f3 * (double)f2 * (double)0.4f;
        this.c = this.aQ.nextFloat() * 3.0f;
        this.d = this.aQ.nextFloat() * 3.0f;
        this.g = (this.aQ.nextFloat() * 0.5f + 0.5f) * 2.0f;
        this.f = (int)(4.0f / (this.aQ.nextFloat() * 0.9f + 0.1f));
        this.e = 0;
        this.aG = false;
    }

    public nq b(float f2) {
        this.an *= (double)f2;
        this.ao = (this.ao - (double)0.1f) * (double)f2 + (double)0.1f;
        this.ap *= (double)f2;
        return this;
    }

    public nq d(float f2) {
        this.a(0.2f * f2, 0.2f * f2);
        this.g *= f2;
        return this;
    }

    public void e_() {
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        if (this.e++ >= this.f) {
            this.F();
        }
        this.ao -= 0.04 * (double)this.h;
        this.c(this.an, this.ao, this.ap);
        this.an *= (double)0.98f;
        this.ao *= (double)0.98f;
        this.ap *= (double)0.98f;
        if (this.av) {
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
        }
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = (float)(this.b % 16) / 16.0f;
        float f9 = f8 + 0.0624375f;
        float f10 = (float)(this.b / 16) / 16.0f;
        float f11 = f10 + 0.0624375f;
        float f12 = 0.1f * this.g;
        float f13 = (float)(this.ah + (this.ak - this.ah) * (double)f2 - l);
        float f14 = (float)(this.ai + (this.al - this.ai) * (double)f2 - m);
        float f15 = (float)(this.aj + (this.am - this.aj) * (double)f2 - n);
        float f16 = this.a(f2);
        ho2.a(this.i * f16, this.j * f16, this.k * f16);
        ho2.a(f13 - f3 * f12 - f6 * f12, f14 - f4 * f12, f15 - f5 * f12 - f7 * f12, f8, f11);
        ho2.a(f13 - f3 * f12 + f6 * f12, f14 + f4 * f12, f15 - f5 * f12 + f7 * f12, f8, f10);
        ho2.a(f13 + f3 * f12 + f6 * f12, f14 + f4 * f12, f15 + f5 * f12 + f7 * f12, f9, f10);
        ho2.a(f13 + f3 * f12 - f6 * f12, f14 - f4 * f12, f15 + f5 * f12 - f7 * f12, f9, f11);
    }

    public int c() {
        return 0;
    }

    public void a(hm hm2) {
    }

    public void b(hm hm2) {
    }
}

