/*
 * Decompiled with CFR 0.152.
 */
public class iw
extends nq {
    public iw(cn cn2, double d2, double d3, double d4, double d5, double d6, double d7, ly ly2) {
        super(cn2, d2, d3, d4, d5, d6, d7);
        this.b = ly2.bb;
        this.h = ly2.bm;
        this.k = 0.6f;
        this.j = 0.6f;
        this.i = 0.6f;
        this.g /= 2.0f;
    }

    public int c() {
        return 1;
    }

    public void a(ho ho2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = ((float)(this.b % 16) + this.c / 4.0f) / 16.0f;
        float f9 = f8 + 0.015609375f;
        float f10 = ((float)(this.b / 16) + this.d / 4.0f) / 16.0f;
        float f11 = f10 + 0.015609375f;
        float f12 = 0.1f * this.g;
        float f13 = (float)(this.ah + (this.ak - this.ah) * (double)f2 - l);
        float f14 = (float)(this.ai + (this.al - this.ai) * (double)f2 - m);
        float f15 = (float)(this.aj + (this.am - this.aj) * (double)f2 - n);
        float f16 = this.a(f2);
        ho2.a(f16 * this.i, f16 * this.j, f16 * this.k);
        ho2.a(f13 - f3 * f12 - f6 * f12, f14 - f4 * f12, f15 - f5 * f12 - f7 * f12, f8, f11);
        ho2.a(f13 - f3 * f12 + f6 * f12, f14 + f4 * f12, f15 - f5 * f12 + f7 * f12, f8, f10);
        ho2.a(f13 + f3 * f12 + f6 * f12, f14 + f4 * f12, f15 + f5 * f12 + f7 * f12, f9, f10);
        ho2.a(f13 + f3 * f12 - f6 * f12, f14 - f4 * f12, f15 + f5 * f12 - f7 * f12, f9, f11);
    }
}

