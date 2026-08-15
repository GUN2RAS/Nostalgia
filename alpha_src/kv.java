/*
 * Decompiled with CFR 0.152.
 */
public class kv
extends fo {
    public ip a;
    public ip b;
    public ip c;
    public ip d;
    public ip e;
    public ip f;
    public ip g;
    public ip h;

    public kv() {
        int n2 = 16;
        this.a = new ip(0, 0);
        this.a.a(-2.0f, -6.0f, -2.0f, 4, 6, 3, 0.0f);
        this.a.a(0.0f, -1 + n2, -4.0f);
        this.g = new ip(14, 0);
        this.g.a(-2.0f, -4.0f, -4.0f, 4, 2, 2, 0.0f);
        this.g.a(0.0f, -1 + n2, -4.0f);
        this.h = new ip(14, 4);
        this.h.a(-1.0f, -2.0f, -3.0f, 2, 2, 2, 0.0f);
        this.h.a(0.0f, -1 + n2, -4.0f);
        this.b = new ip(0, 9);
        this.b.a(-3.0f, -4.0f, -3.0f, 6, 8, 6, 0.0f);
        this.b.a(0.0f, 0 + n2, 0.0f);
        this.c = new ip(26, 0);
        this.c.a(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.c.a(-2.0f, 3 + n2, 1.0f);
        this.d = new ip(26, 0);
        this.d.a(-1.0f, 0.0f, -3.0f, 3, 5, 3);
        this.d.a(1.0f, 3 + n2, 1.0f);
        this.e = new ip(24, 13);
        this.e.a(0.0f, 0.0f, -3.0f, 1, 4, 6);
        this.e.a(-4.0f, -3 + n2, 0.0f);
        this.f = new ip(24, 13);
        this.f.a(-1.0f, 0.0f, -3.0f, 1, 4, 6);
        this.f.a(4.0f, -3 + n2, 0.0f);
    }

    public void b(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.a(f2, f3, f4, f5, f6, f7);
        this.a.a(f7);
        this.g.a(f7);
        this.h.a(f7);
        this.b.a(f7);
        this.c.a(f7);
        this.d.a(f7);
        this.e.a(f7);
        this.f.a(f7);
    }

    public void a(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.a.d = -(f6 / 57.295776f);
        this.a.e = f5 / 57.295776f;
        this.g.d = this.a.d;
        this.g.e = this.a.e;
        this.h.d = this.a.d;
        this.h.e = this.a.e;
        this.b.d = 1.5707964f;
        this.c.d = eo.b(f2 * 0.6662f) * 1.4f * f3;
        this.d.d = eo.b(f2 * 0.6662f + (float)Math.PI) * 1.4f * f3;
        this.e.f = f4;
        this.f.f = -f4;
    }
}

