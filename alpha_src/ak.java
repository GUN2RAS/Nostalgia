/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import org.lwjgl.opengl.GL11;

public abstract class ak {
    protected kx a;
    private fo d = new cr();
    private bc e = new bc();
    protected float b = 0.0f;
    protected float c = 1.0f;

    public abstract void a(kh var1, double var2, double var4, double var6, float var8, float var9);

    protected void a(String string) {
        ey ey2 = this.a.e;
        ey2.b(ey2.a(string));
    }

    protected void a(String string, String string2) {
        ey ey2 = this.a.e;
        ey2.b(ey2.a(string, string2));
    }

    private void a(kh kh2, double d2, double d3, double d4, float f2) {
        GL11.glDisable((int)2896);
        int n2 = ly.as.bb;
        int n3 = (n2 & 0xF) << 4;
        int n4 = n2 & 0xF0;
        float f3 = (float)n3 / 256.0f;
        float f4 = ((float)n3 + 15.99f) / 256.0f;
        float f5 = (float)n4 / 256.0f;
        float f6 = ((float)n4 + 15.99f) / 256.0f;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d2), (float)((float)d3), (float)((float)d4));
        float f7 = kh2.aC * 1.4f;
        GL11.glScalef((float)f7, (float)f7, (float)f7);
        this.a("/terrain.png");
        ho ho2 = ho.a;
        float f8 = 1.0f;
        float f9 = 0.5f;
        float f10 = 0.0f;
        float f11 = kh2.aD / kh2.aC;
        GL11.glRotatef((float)(-this.a.i), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)(0.4f + (float)((int)f11) * 0.02f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ho2.b();
        while (f11 > 0.0f) {
            ho2.a(f8 - f9, 0.0f - f10, 0.0, f4, f6);
            ho2.a(0.0f - f9, 0.0f - f10, 0.0, f3, f6);
            ho2.a(0.0f - f9, 1.4f - f10, 0.0, f3, f5);
            ho2.a(f8 - f9, 1.4f - f10, 0.0, f4, f5);
            f11 -= 1.0f;
            f10 -= 1.0f;
            f8 *= 0.9f;
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-0.04f);
        }
        ho2.a();
        GL11.glPopMatrix();
        GL11.glEnable((int)2896);
    }

    private void c(kh kh2, double d2, double d3, double d4, float f2, float f3) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        ey ey2 = this.a.e;
        ey2.b(ey2.a("%%/shadow.png"));
        cn cn2 = this.b();
        GL11.glDepthMask((boolean)false);
        float f4 = this.b;
        double d5 = kh2.aI + (kh2.ak - kh2.aI) * (double)f3;
        double d6 = kh2.aJ + (kh2.al - kh2.aJ) * (double)f3 + (double)kh2.h_();
        double d7 = kh2.aK + (kh2.am - kh2.aK) * (double)f3;
        int n2 = eo.b(d5 - (double)f4);
        int n3 = eo.b(d5 + (double)f4);
        int n4 = eo.b(d6 - (double)f4);
        int n5 = eo.b(d6);
        int n6 = eo.b(d7 - (double)f4);
        int n7 = eo.b(d7 + (double)f4);
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        double d10 = d4 - d7;
        ho ho2 = ho.a;
        ho2.b();
        for (int i2 = n2; i2 <= n3; ++i2) {
            for (int i3 = n4; i3 <= n5; ++i3) {
                for (int i4 = n6; i4 <= n7; ++i4) {
                    int n8 = cn2.a(i2, i3 - 1, i4);
                    if (n8 <= 0 || cn2.j(i2, i3, i4) <= 3) continue;
                    this.a(ly.n[n8], d2, d3 + (double)kh2.h_(), d4, i2, i3, i4, f2, f4, d8, d9 + (double)kh2.h_(), d10);
                }
            }
        }
        ho2.a();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
        GL11.glDepthMask((boolean)true);
    }

    private cn b() {
        return this.a.g;
    }

    private void a(ly ly2, double d2, double d3, double d4, int n2, int n3, int n4, float f2, float f3, double d5, double d6, double d7) {
        ho ho2 = ho.a;
        if (!ly2.c()) {
            return;
        }
        double d8 = ((double)f2 - (d3 - ((double)n3 + d6)) / 2.0) * 0.5 * (double)this.b().c(n2, n3, n4);
        if (d8 < 0.0) {
            return;
        }
        if (d8 > 1.0) {
            d8 = 1.0;
        }
        ho2.a(1.0f, 1.0f, 1.0f, (float)d8);
        double d9 = (double)n2 + ly2.bf + d5;
        double d10 = (double)n2 + ly2.bi + d5;
        double d11 = (double)n3 + ly2.bg + d6 + 0.015625;
        double d12 = (double)n4 + ly2.bh + d7;
        double d13 = (double)n4 + ly2.bk + d7;
        float f4 = (float)((d2 - d9) / 2.0 / (double)f3 + 0.5);
        float f5 = (float)((d2 - d10) / 2.0 / (double)f3 + 0.5);
        float f6 = (float)((d4 - d12) / 2.0 / (double)f3 + 0.5);
        float f7 = (float)((d4 - d13) / 2.0 / (double)f3 + 0.5);
        ho2.a(d9, d11, d12, f4, f6);
        ho2.a(d9, d11, d13, f4, f7);
        ho2.a(d10, d11, d13, f5, f7);
        ho2.a(d10, d11, d12, f5, f6);
    }

    public static void a(cf cf2, double d2, double d3, double d4) {
        GL11.glDisable((int)3553);
        ho ho2 = ho.a;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        ho2.b();
        ho2.b(d2, d3, d4);
        ho2.b(0.0f, 0.0f, -1.0f);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.b(0.0f, 0.0f, 1.0f);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.b(0.0f, -1.0f, 0.0f);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.b(0.0f, 1.0f, 0.0f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.b(-1.0f, 0.0f, 0.0f);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.b(1.0f, 0.0f, 0.0f);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.b(0.0, 0.0, 0.0);
        ho2.a();
        GL11.glEnable((int)3553);
    }

    public static void a(cf cf2) {
        ho ho2 = ho.a;
        ho2.b();
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a();
    }

    public void a(kx kx2) {
        this.a = kx2;
    }

    public void b(kh kh2, double d2, double d3, double d4, float f2, float f3) {
        double d5;
        float f4;
        if (this.a.k.i && this.b > 0.0f && (f4 = (float)((1.0 - (d5 = this.a.a(kh2.ak, kh2.al, kh2.am)) / 256.0) * (double)this.c)) > 0.0f) {
            this.c(kh2, d2, d3, d4, f4, f3);
        }
        if (kh2.aT > 0) {
            this.a(kh2, d2, d3, d4, f3);
        }
    }

    public kd a() {
        return this.a.a();
    }
}

