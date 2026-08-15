/*
 * Decompiled with CFR 0.152.
 */
public class jy
extends fo {
    public ip a;
    public ip b;
    public ip c;
    public ip d;
    public ip e;
    public ip f;
    public ip g;
    public ip h;
    public ip i;
    public ip j;
    public ip m;

    public jy() {
        float f2 = 0.0f;
        int n2 = 15;
        this.a = new ip(32, 4);
        this.a.a(-4.0f, -4.0f, -8.0f, 8, 8, 8, f2);
        this.a.a(0.0f, 0 + n2, -3.0f);
        this.b = new ip(0, 0);
        this.b.a(-3.0f, -3.0f, -3.0f, 6, 6, 6, f2);
        this.b.a(0.0f, n2, 0.0f);
        this.c = new ip(0, 12);
        this.c.a(-5.0f, -4.0f, -6.0f, 10, 8, 12, f2);
        this.c.a(0.0f, 0 + n2, 9.0f);
        this.d = new ip(18, 0);
        this.d.a(-15.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.d.a(-4.0f, 0 + n2, 2.0f);
        this.e = new ip(18, 0);
        this.e.a(-1.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.e.a(4.0f, 0 + n2, 2.0f);
        this.f = new ip(18, 0);
        this.f.a(-15.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.f.a(-4.0f, 0 + n2, 1.0f);
        this.g = new ip(18, 0);
        this.g.a(-1.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.g.a(4.0f, 0 + n2, 1.0f);
        this.h = new ip(18, 0);
        this.h.a(-15.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.h.a(-4.0f, 0 + n2, 0.0f);
        this.i = new ip(18, 0);
        this.i.a(-1.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.i.a(4.0f, 0 + n2, 0.0f);
        this.j = new ip(18, 0);
        this.j.a(-15.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.j.a(-4.0f, 0 + n2, -1.0f);
        this.m = new ip(18, 0);
        this.m.a(-1.0f, -1.0f, -1.0f, 16, 2, 2, f2);
        this.m.a(4.0f, 0 + n2, -1.0f);
    }

    public void b(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.a(f2, f3, f4, f5, f6, f7);
        this.a.a(f7);
        this.b.a(f7);
        this.c.a(f7);
        this.d.a(f7);
        this.e.a(f7);
        this.f.a(f7);
        this.g.a(f7);
        this.h.a(f7);
        this.i.a(f7);
        this.j.a(f7);
        this.m.a(f7);
    }

    public void a(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.a.e = f5 / 57.295776f;
        this.a.d = f6 / 57.295776f;
        float f8 = 0.7853982f;
        this.d.f = -f8;
        this.e.f = f8;
        this.f.f = -f8 * 0.74f;
        this.g.f = f8 * 0.74f;
        this.h.f = -f8 * 0.74f;
        this.i.f = f8 * 0.74f;
        this.j.f = -f8;
        this.m.f = f8;
        float f9 = -0.0f;
        float f10 = 0.3926991f;
        this.d.e = f10 * 2.0f + f9;
        this.e.e = -f10 * 2.0f - f9;
        this.f.e = f10 * 1.0f + f9;
        this.g.e = -f10 * 1.0f - f9;
        this.h.e = -f10 * 1.0f + f9;
        this.i.e = f10 * 1.0f - f9;
        this.j.e = -f10 * 2.0f + f9;
        this.m.e = f10 * 2.0f - f9;
        float f11 = -(eo.b(f2 * 0.6662f * 2.0f + 0.0f) * 0.4f) * f3;
        float f12 = -(eo.b(f2 * 0.6662f * 2.0f + (float)Math.PI) * 0.4f) * f3;
        float f13 = -(eo.b(f2 * 0.6662f * 2.0f + 1.5707964f) * 0.4f) * f3;
        float f14 = -(eo.b(f2 * 0.6662f * 2.0f + 4.712389f) * 0.4f) * f3;
        float f15 = Math.abs(eo.a(f2 * 0.6662f + 0.0f) * 0.4f) * f3;
        float f16 = Math.abs(eo.a(f2 * 0.6662f + (float)Math.PI) * 0.4f) * f3;
        float f17 = Math.abs(eo.a(f2 * 0.6662f + 1.5707964f) * 0.4f) * f3;
        float f18 = Math.abs(eo.a(f2 * 0.6662f + 4.712389f) * 0.4f) * f3;
        this.d.e += f11;
        this.e.e += -f11;
        this.f.e += f12;
        this.g.e += -f12;
        this.h.e += f13;
        this.i.e += -f13;
        this.j.e += f14;
        this.m.e += -f14;
        this.d.f += f15;
        this.e.f += -f15;
        this.f.f += f16;
        this.g.f += -f16;
        this.h.f += f17;
        this.i.f += -f17;
        this.j.f += f18;
        this.m.f += -f18;
    }
}

