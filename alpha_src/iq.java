/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.Display
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GLContext
 *  org.lwjgl.util.glu.GLU
 */
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class iq {
    private Minecraft h;
    private float i = 0.0f;
    public jh a;
    private int j;
    private kh k = null;
    private long l = System.currentTimeMillis();
    private Random m = new Random();
    volatile int b = 0;
    volatile int c = 0;
    FloatBuffer d = df.d(16);
    float e;
    float f;
    float g;
    private float n;
    private float o;

    public iq(Minecraft minecraft) {
        this.h = minecraft;
        this.a = new jh(minecraft);
    }

    public void a() {
        this.n = this.o;
        float f2 = this.h.e.c(eo.b(this.h.g.ak), eo.b(this.h.g.al), eo.b(this.h.g.am));
        float f3 = (float)(3 - this.h.y.e) / 3.0f;
        float f4 = f2 * (1.0f - f3) + f3;
        this.o += (f4 - this.o) * 0.1f;
        ++this.j;
        this.a.a();
        if (this.h.J) {
            this.c();
        }
    }

    public void a(float f2) {
        if (this.h.g == null) {
            return;
        }
        double d2 = this.h.b.b();
        this.h.x = this.h.g.a(d2, f2);
        double d3 = d2;
        aj aj2 = this.h.g.e(f2);
        if (this.h.x != null) {
            d3 = this.h.x.f.c(aj2);
        }
        if (this.h.b instanceof il) {
            d2 = 32.0;
            d3 = 32.0;
        } else {
            if (d3 > 3.0) {
                d3 = 3.0;
            }
            d2 = d3;
        }
        aj aj3 = this.h.g.f(f2);
        aj aj4 = aj2.c(aj3.a * d2, aj3.b * d2, aj3.c * d2);
        this.k = null;
        List list = this.h.e.b(this.h.g, this.h.g.au.a(aj3.a * d2, aj3.b * d2, aj3.c * d2));
        double d4 = 0.0;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            double d5;
            float f3;
            cf cf2;
            mf mf2;
            kh kh2 = (kh)list.get(i2);
            if (!kh2.c_() || (mf2 = (cf2 = kh2.au.b(f3 = 0.1f, f3, f3)).a(aj2, aj4)) == null || !((d5 = aj2.c(mf2.f)) < d4) && d4 != 0.0) continue;
            this.k = kh2;
            d4 = d5;
        }
        if (this.k != null && !(this.h.b instanceof il)) {
            this.h.x = new mf(this.k);
        }
    }

    private float d(float f2) {
        bi bi2 = this.h.g;
        float f3 = 70.0f;
        if (bi2.a(gb.f)) {
            f3 = 60.0f;
        }
        if (bi2.E <= 0) {
            float f4 = (float)bi2.J + f2;
            f3 /= (1.0f - 500.0f / (f4 + 500.0f)) * 2.0f + 1.0f;
        }
        return f3;
    }

    private void e(float f2) {
        float f3;
        bi bi2 = this.h.g;
        float f4 = (float)bi2.G - f2;
        if (bi2.E <= 0) {
            f3 = (float)bi2.J + f2;
            GL11.glRotatef((float)(40.0f - 8000.0f / (f3 + 200.0f)), (float)0.0f, (float)0.0f, (float)1.0f);
        }
        if (f4 < 0.0f) {
            return;
        }
        f4 /= (float)bi2.H;
        f4 = eo.a(f4 * f4 * f4 * f4 * (float)Math.PI);
        f3 = bi2.I;
        GL11.glRotatef((float)(-f3), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)(-f4 * 14.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glRotatef((float)f3, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void f(float f2) {
        if (this.h.y.x) {
            return;
        }
        bi bi2 = this.h.g;
        float f3 = bi2.aF - bi2.aE;
        float f4 = bi2.aF + f3 * f2;
        float f5 = bi2.e + (bi2.f - bi2.e) * f2;
        float f6 = bi2.L + (bi2.M - bi2.L) * f2;
        GL11.glTranslatef((float)(eo.a(f4 * (float)Math.PI) * f5 * 0.5f), (float)(-Math.abs(eo.b(f4 * (float)Math.PI) * f5)), (float)0.0f);
        GL11.glRotatef((float)(eo.a(f4 * (float)Math.PI) * f5 * 3.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glRotatef((float)(Math.abs(eo.b(f4 * (float)Math.PI + 0.2f) * f5) * 5.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)f6, (float)1.0f, (float)0.0f, (float)0.0f);
    }

    private void g(float f2) {
        bi bi2 = this.h.g;
        double d2 = bi2.ah + (bi2.ak - bi2.ah) * (double)f2;
        double d3 = bi2.ai + (bi2.al - bi2.ai) * (double)f2;
        double d4 = bi2.aj + (bi2.am - bi2.aj) * (double)f2;
        if (this.h.y.x) {
            double d5 = 4.0;
            float f3 = bi2.aq;
            float f4 = bi2.ar;
            double d6 = (double)(-eo.a(f3 / 180.0f * (float)Math.PI) * eo.b(f4 / 180.0f * (float)Math.PI)) * d5;
            double d7 = (double)(eo.b(f3 / 180.0f * (float)Math.PI) * eo.b(f4 / 180.0f * (float)Math.PI)) * d5;
            double d8 = (double)(-eo.a(f4 / 180.0f * (float)Math.PI)) * d5;
            for (int i2 = 0; i2 < 8; ++i2) {
                double d9;
                mf mf2;
                float f5 = (i2 & 1) * 2 - 1;
                float f6 = (i2 >> 1 & 1) * 2 - 1;
                float f7 = (i2 >> 2 & 1) * 2 - 1;
                if ((mf2 = this.h.e.a(aj.b(d2 + (double)(f5 *= 0.1f), d3 + (double)(f6 *= 0.1f), d4 + (double)(f7 *= 0.1f)), aj.b(d2 - d6 + (double)f5 + (double)f7, d3 - d8 + (double)f6, d4 - d7 + (double)f7))) == null || !((d9 = mf2.f.c(aj.b(d2, d3, d4))) < d5)) continue;
                d5 = d9;
            }
            GL11.glRotatef((float)(bi2.ar - f4), (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glRotatef((float)(bi2.aq - f3), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)((float)(-d5)));
            GL11.glRotatef((float)(f3 - bi2.aq), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(f4 - bi2.ar), (float)1.0f, (float)0.0f, (float)0.0f);
        } else {
            GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-0.1f);
        }
        GL11.glRotatef((float)(bi2.at + (bi2.ar - bi2.at) * f2), (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)(bi2.as + (bi2.aq - bi2.as) * f2 + 180.0f), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    private void a(float f2, int n2) {
        this.i = 256 >> this.h.y.e;
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        float f3 = 0.07f;
        if (this.h.y.g) {
            GL11.glTranslatef((float)((float)(-(n2 * 2 - 1)) * f3), (float)0.0f, (float)0.0f);
        }
        GLU.gluPerspective((float)this.d(f2), (float)((float)this.h.c / (float)this.h.d), (float)0.05f, (float)this.i);
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        if (this.h.y.g) {
            GL11.glTranslatef((float)((float)(n2 * 2 - 1) * 0.1f), (float)0.0f, (float)0.0f);
        }
        this.e(f2);
        if (this.h.y.f) {
            this.f(f2);
        }
        this.g(f2);
    }

    private void b(float f2, int n2) {
        GL11.glLoadIdentity();
        if (this.h.y.g) {
            GL11.glTranslatef((float)((float)(n2 * 2 - 1) * 0.1f), (float)0.0f, (float)0.0f);
        }
        GL11.glPushMatrix();
        this.e(f2);
        if (this.h.y.f) {
            this.f(f2);
        }
        if (!this.h.y.x) {
            this.a.a(f2);
        }
        GL11.glPopMatrix();
        if (!this.h.y.x) {
            this.a.b(f2);
            this.e(f2);
        }
        if (this.h.y.f) {
            this.f(f2);
        }
    }

    public void b(float f2) {
        int n2;
        if (!Display.isActive()) {
            if (System.currentTimeMillis() - this.l > 500L) {
                this.h.g();
            }
        } else {
            this.l = System.currentTimeMillis();
        }
        if (this.h.I) {
            this.h.B.c();
            float f3 = this.h.y.c * 0.6f + 0.2f;
            float f4 = f3 * f3 * f3 * 8.0f;
            float f5 = (float)this.h.B.a * f4;
            float f6 = (float)this.h.B.b * f4;
            n2 = 1;
            if (this.h.y.d) {
                n2 = -1;
            }
            this.h.g.d(f5, f6 * (float)n2);
        }
        if (this.h.v) {
            return;
        }
        iy iy2 = new iy(this.h.c, this.h.d);
        int n3 = iy2.a();
        int n4 = iy2.b();
        int n5 = Mouse.getX() * n3 / this.h.c;
        n2 = n4 - Mouse.getY() * n4 / this.h.d - 1;
        if (this.h.e != null) {
            this.c(f2);
            this.h.u.a(f2, this.h.p != null, n5, n2);
        } else {
            GL11.glViewport((int)0, (int)0, (int)this.h.c, (int)this.h.d);
            GL11.glClearColor((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
            GL11.glClear((int)16640);
            GL11.glMatrixMode((int)5889);
            GL11.glLoadIdentity();
            GL11.glMatrixMode((int)5888);
            GL11.glLoadIdentity();
            this.b();
        }
        if (this.h.p != null) {
            GL11.glClear((int)256);
            this.h.p.a(n5, n2, f2);
        }
    }

    public void c(float f2) {
        this.a(f2);
        bi bi2 = this.h.g;
        e e2 = this.h.f;
        bq bq2 = this.h.h;
        double d2 = bi2.aI + (bi2.ak - bi2.aI) * (double)f2;
        double d3 = bi2.aJ + (bi2.al - bi2.aJ) * (double)f2;
        double d4 = bi2.aK + (bi2.am - bi2.aK) * (double)f2;
        for (int i2 = 0; i2 < 2; ++i2) {
            if (this.h.y.g) {
                if (i2 == 0) {
                    GL11.glColorMask((boolean)false, (boolean)true, (boolean)true, (boolean)false);
                } else {
                    GL11.glColorMask((boolean)true, (boolean)false, (boolean)false, (boolean)false);
                }
            }
            GL11.glViewport((int)0, (int)0, (int)this.h.c, (int)this.h.d);
            this.i(f2);
            GL11.glClear((int)16640);
            GL11.glEnable((int)2884);
            this.a(f2, i2);
            j.a();
            if (this.h.y.e < 2) {
                this.a(-1);
                e2.a(f2);
            }
            GL11.glEnable((int)2912);
            this.a(1);
            kl kl2 = new kl();
            kl2.a(d2, d3, d4);
            this.h.f.a(kl2, f2);
            this.h.f.a(bi2, false);
            this.a(0);
            GL11.glEnable((int)2912);
            GL11.glBindTexture((int)3553, (int)this.h.n.a("/terrain.png"));
            i.a();
            e2.a(bi2, 0, (double)f2);
            i.b();
            e2.a(bi2.e(f2), kl2, f2);
            bq2.b(bi2, f2);
            i.a();
            this.a(0);
            bq2.a(bi2, f2);
            if (this.h.x != null && bi2.a(gb.f)) {
                GL11.glDisable((int)3008);
                e2.a(bi2, this.h.x, 0, bi2.b.a(), f2);
                e2.b(bi2, this.h.x, 0, bi2.b.a(), f2);
                GL11.glEnable((int)3008);
            }
            GL11.glBlendFunc((int)770, (int)771);
            this.a(0);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2884);
            GL11.glBindTexture((int)3553, (int)this.h.n.a("/terrain.png"));
            if (this.h.y.i) {
                GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
                int n2 = e2.a(bi2, 1, (double)f2);
                GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                if (this.h.y.g) {
                    if (i2 == 0) {
                        GL11.glColorMask((boolean)false, (boolean)true, (boolean)true, (boolean)false);
                    } else {
                        GL11.glColorMask((boolean)true, (boolean)false, (boolean)false, (boolean)false);
                    }
                }
                if (n2 > 0) {
                    e2.a(1, (double)f2);
                }
            } else {
                e2.a(bi2, 1, (double)f2);
            }
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2884);
            GL11.glDisable((int)3042);
            if (this.h.x != null && !bi2.a(gb.f)) {
                GL11.glDisable((int)3008);
                e2.a(bi2, this.h.x, 0, bi2.b.a(), f2);
                e2.b(bi2, this.h.x, 0, bi2.b.a(), f2);
                GL11.glEnable((int)3008);
            }
            GL11.glDisable((int)2912);
            if (this.h.e.d) {
                this.h(f2);
            }
            if (this.k != null) {
                // empty if block
            }
            this.a(0);
            GL11.glEnable((int)2912);
            e2.b(f2);
            GL11.glDisable((int)2912);
            this.a(1);
            GL11.glClear((int)256);
            this.b(f2, i2);
            if (this.h.y.g) continue;
            return;
        }
        GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)false);
    }

    private void c() {
        if (!this.h.y.i) {
            return;
        }
        bi bi2 = this.h.g;
        cn cn2 = this.h.e;
        int n2 = eo.b(bi2.ak);
        int n3 = eo.b(bi2.al);
        int n4 = eo.b(bi2.am);
        int n5 = 16;
        for (int i2 = 0; i2 < 150; ++i2) {
            int n6 = n2 + this.m.nextInt(n5) - this.m.nextInt(n5);
            int n7 = n4 + this.m.nextInt(n5) - this.m.nextInt(n5);
            int n8 = cn2.e(n6, n7);
            int n9 = cn2.a(n6, n8 - 1, n7);
            if (n8 > n3 + n5 || n8 < n3 - n5) continue;
            float f2 = this.m.nextFloat();
            float f3 = this.m.nextFloat();
            if (n9 <= 0) continue;
            this.h.h.a(new nf(cn2, (float)n6 + f2, (double)((float)n8 + 0.1f) - ly.n[n9].bg, (float)n7 + f3));
        }
    }

    private void h(float f2) {
        bi bi2 = this.h.g;
        cn cn2 = this.h.e;
        int n2 = eo.b(bi2.ak);
        int n3 = eo.b(bi2.al);
        int n4 = eo.b(bi2.am);
        ho ho2 = ho.a;
        GL11.glDisable((int)2884);
        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBindTexture((int)3553, (int)this.h.n.a("/snow.png"));
        double d2 = bi2.aI + (bi2.ak - bi2.aI) * (double)f2;
        double d3 = bi2.aJ + (bi2.al - bi2.aJ) * (double)f2;
        double d4 = bi2.aK + (bi2.am - bi2.aK) * (double)f2;
        int n5 = 5;
        if (this.h.y.i) {
            n5 = 10;
        }
        for (int i2 = n2 - n5; i2 <= n2 + n5; ++i2) {
            for (int i3 = n4 - n5; i3 <= n4 + n5; ++i3) {
                int n6 = cn2.d(i2, i3);
                if (n6 < 0) {
                    n6 = 0;
                }
                int n7 = n3 - n5;
                int n8 = n3 + n5;
                if (n7 < n6) {
                    n7 = n6;
                }
                if (n8 < n6) {
                    n8 = n6;
                }
                float f3 = 2.0f;
                if (n7 == n8) continue;
                this.m.setSeed(i2 * i2 * 3121 + i2 * 45238971 + i3 * i3 * 418711 + i3 * 13761);
                float f4 = (float)this.j + f2;
                float f5 = ((float)(this.j & 0x1FF) + f2) / 512.0f;
                float f6 = this.m.nextFloat() + f4 * 0.01f * (float)this.m.nextGaussian();
                float f7 = this.m.nextFloat() + f4 * (float)this.m.nextGaussian() * 0.001f;
                double d5 = (double)((float)i2 + 0.5f) - bi2.ak;
                double d6 = (double)((float)i3 + 0.5f) - bi2.am;
                float f8 = eo.a(d5 * d5 + d6 * d6) / (float)n5;
                ho2.b();
                float f9 = cn2.c(i2, 128, i3);
                GL11.glColor4f((float)f9, (float)f9, (float)f9, (float)((1.0f - f8 * f8) * 0.7f));
                ho2.b(-d2 * 1.0, -d3 * 1.0, -d4 * 1.0);
                ho2.a(i2 + 0, n7, i3 + 0, 0.0f * f3 + f6, (float)n7 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 1, n7, i3 + 1, 1.0f * f3 + f6, (float)n7 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 1, n8, i3 + 1, 1.0f * f3 + f6, (float)n8 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 0, n8, i3 + 0, 0.0f * f3 + f6, (float)n8 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 0, n7, i3 + 1, 0.0f * f3 + f6, (float)n7 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 1, n7, i3 + 0, 1.0f * f3 + f6, (float)n7 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 1, n8, i3 + 0, 1.0f * f3 + f6, (float)n8 * f3 / 8.0f + f5 * f3 + f7);
                ho2.a(i2 + 0, n8, i3 + 1, 0.0f * f3 + f6, (float)n8 * f3 / 8.0f + f5 * f3 + f7);
                ho2.b(0.0, 0.0, 0.0);
                ho2.a();
            }
        }
        GL11.glEnable((int)2884);
        GL11.glDisable((int)3042);
    }

    public void b() {
        iy iy2 = new iy(this.h.c, this.h.d);
        int n2 = iy2.a();
        int n3 = iy2.b();
        GL11.glClear((int)256);
        GL11.glMatrixMode((int)5889);
        GL11.glLoadIdentity();
        GL11.glOrtho((double)0.0, (double)n2, (double)n3, (double)0.0, (double)1000.0, (double)3000.0);
        GL11.glMatrixMode((int)5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-2000.0f);
    }

    private void i(float f2) {
        cn cn2 = this.h.e;
        bi bi2 = this.h.g;
        float f3 = 1.0f / (float)(4 - this.h.y.e);
        f3 = 1.0f - (float)Math.pow(f3, 0.25);
        aj aj2 = cn2.b(f2);
        float f4 = (float)aj2.a;
        float f5 = (float)aj2.b;
        float f6 = (float)aj2.c;
        aj aj3 = cn2.e(f2);
        this.e = (float)aj3.a;
        this.f = (float)aj3.b;
        this.g = (float)aj3.c;
        this.e += (f4 - this.e) * f3;
        this.f += (f5 - this.f) * f3;
        this.g += (f6 - this.g) * f3;
        if (bi2.a(gb.f)) {
            this.e = 0.02f;
            this.f = 0.02f;
            this.g = 0.2f;
        } else if (bi2.a(gb.g)) {
            this.e = 0.6f;
            this.f = 0.1f;
            this.g = 0.0f;
        }
        float f7 = this.n + (this.o - this.n) * f2;
        this.e *= f7;
        this.f *= f7;
        this.g *= f7;
        if (this.h.y.g) {
            float f8 = (this.e * 30.0f + this.f * 59.0f + this.g * 11.0f) / 100.0f;
            float f9 = (this.e * 30.0f + this.f * 70.0f) / 100.0f;
            float f10 = (this.e * 30.0f + this.g * 70.0f) / 100.0f;
            this.e = f8;
            this.f = f9;
            this.g = f10;
        }
        GL11.glClearColor((float)this.e, (float)this.f, (float)this.g, (float)0.0f);
    }

    private void a(int n2) {
        bi bi2 = this.h.g;
        GL11.glFog((int)2918, (FloatBuffer)this.a(this.e, this.f, this.g, 1.0f));
        GL11.glNormal3f((float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (bi2.a(gb.f)) {
            GL11.glFogi((int)2917, (int)2048);
            GL11.glFogf((int)2914, (float)0.1f);
            float f2 = 0.4f;
            float f3 = 0.4f;
            float f4 = 0.9f;
            if (this.h.y.g) {
                float f5 = (f2 * 30.0f + f3 * 59.0f + f4 * 11.0f) / 100.0f;
                float f6 = (f2 * 30.0f + f3 * 70.0f) / 100.0f;
                float f7 = (f2 * 30.0f + f4 * 70.0f) / 100.0f;
                f2 = f5;
                f3 = f6;
                f4 = f7;
            }
        } else if (bi2.a(gb.g)) {
            GL11.glFogi((int)2917, (int)2048);
            GL11.glFogf((int)2914, (float)2.0f);
            float f8 = 0.4f;
            float f9 = 0.3f;
            float f10 = 0.3f;
            if (this.h.y.g) {
                float f11 = (f8 * 30.0f + f9 * 59.0f + f10 * 11.0f) / 100.0f;
                float f12 = (f8 * 30.0f + f9 * 70.0f) / 100.0f;
                float f13 = (f8 * 30.0f + f10 * 70.0f) / 100.0f;
                f8 = f11;
                f9 = f12;
                f10 = f13;
            }
        } else {
            GL11.glFogi((int)2917, (int)9729);
            GL11.glFogf((int)2915, (float)(this.i * 0.25f));
            GL11.glFogf((int)2916, (float)this.i);
            if (n2 < 0) {
                GL11.glFogf((int)2915, (float)0.0f);
                GL11.glFogf((int)2916, (float)(this.i * 0.8f));
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi((int)34138, (int)34139);
            }
        }
        GL11.glEnable((int)2903);
        GL11.glColorMaterial((int)1028, (int)4608);
    }

    private FloatBuffer a(float f2, float f3, float f4, float f5) {
        this.d.clear();
        this.d.put(f2).put(f3).put(f4).put(f5);
        this.d.flip();
        return this.d;
    }
}

