/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.ARBOcclusionQuery
 *  org.lwjgl.opengl.GL11
 */
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;

public class e
implements im {
    public List a = new ArrayList();
    private cn k;
    private ey l;
    private List m = new ArrayList();
    private bn[] n;
    private bn[] o;
    private int p;
    private int q;
    private int r;
    private int s;
    private Minecraft t;
    private bc u;
    private IntBuffer v;
    private boolean w = false;
    private int x = 0;
    private int y;
    private int z;
    private int A;
    private int B;
    private int C;
    private int D;
    private int E;
    private int F;
    private int G;
    private int H = -1;
    private int I = 2;
    private int J;
    private int K;
    private int L;
    int[] b = new int[50000];
    IntBuffer c = df.c(64);
    private int M;
    private int N;
    private int O;
    private int P;
    private int Q;
    private List R = new ArrayList();
    private kw[] S = new kw[]{new kw(), new kw(), new kw(), new kw()};
    int d = 0;
    int e = df.a(1);
    double f = -9999.0;
    double g = -9999.0;
    double h = -9999.0;
    public float i;
    int j = 0;

    public e(Minecraft minecraft, ey ey2) {
        int n2;
        int n3;
        this.t = minecraft;
        this.l = ey2;
        int n4 = 64;
        this.s = df.a(n4 * n4 * n4 * 3);
        this.w = minecraft.k().a();
        if (this.w) {
            this.c.clear();
            this.v = df.c(n4 * n4 * n4);
            this.v.clear();
            this.v.position(0);
            this.v.limit(n4 * n4 * n4);
            ARBOcclusionQuery.glGenQueriesARB((IntBuffer)this.v);
        }
        this.y = df.a(3);
        GL11.glPushMatrix();
        GL11.glNewList((int)this.y, (int)4864);
        this.f();
        GL11.glEndList();
        GL11.glPopMatrix();
        ho ho2 = ho.a;
        this.z = this.y + 1;
        GL11.glNewList((int)this.z, (int)4864);
        int n5 = 64;
        int n6 = 256 / n5 + 2;
        float f2 = 16.0f;
        for (n3 = -n5 * n6; n3 <= n5 * n6; n3 += n5) {
            for (n2 = -n5 * n6; n2 <= n5 * n6; n2 += n5) {
                ho2.b();
                ho2.a((double)(n3 + 0), (double)f2, (double)(n2 + 0));
                ho2.a((double)(n3 + n5), (double)f2, (double)(n2 + 0));
                ho2.a((double)(n3 + n5), (double)f2, (double)(n2 + n5));
                ho2.a((double)(n3 + 0), (double)f2, (double)(n2 + n5));
                ho2.a();
            }
        }
        GL11.glEndList();
        this.A = this.y + 2;
        GL11.glNewList((int)this.A, (int)4864);
        f2 = -16.0f;
        ho2.b();
        for (n3 = -n5 * n6; n3 <= n5 * n6; n3 += n5) {
            for (n2 = -n5 * n6; n2 <= n5 * n6; n2 += n5) {
                ho2.a((double)(n3 + n5), (double)f2, (double)(n2 + 0));
                ho2.a((double)(n3 + 0), (double)f2, (double)(n2 + 0));
                ho2.a((double)(n3 + 0), (double)f2, (double)(n2 + n5));
                ho2.a((double)(n3 + n5), (double)f2, (double)(n2 + n5));
            }
        }
        ho2.a();
        GL11.glEndList();
    }

    private void f() {
        Random random = new Random(10842L);
        ho ho2 = ho.a;
        ho2.b();
        for (int i2 = 0; i2 < 1500; ++i2) {
            double d2 = random.nextFloat() * 2.0f - 1.0f;
            double d3 = random.nextFloat() * 2.0f - 1.0f;
            double d4 = random.nextFloat() * 2.0f - 1.0f;
            double d5 = 0.25f + random.nextFloat() * 0.25f;
            double d6 = d2 * d2 + d3 * d3 + d4 * d4;
            if (!(d6 < 1.0) || !(d6 > 0.01)) continue;
            d6 = 1.0 / Math.sqrt(d6);
            double d7 = (d2 *= d6) * 100.0;
            double d8 = (d3 *= d6) * 100.0;
            double d9 = (d4 *= d6) * 100.0;
            double d10 = Math.atan2(d2, d4);
            double d11 = Math.sin(d10);
            double d12 = Math.cos(d10);
            double d13 = Math.atan2(Math.sqrt(d2 * d2 + d4 * d4), d3);
            double d14 = Math.sin(d13);
            double d15 = Math.cos(d13);
            double d16 = random.nextDouble() * Math.PI * 2.0;
            double d17 = Math.sin(d16);
            double d18 = Math.cos(d16);
            for (int i3 = 0; i3 < 4; ++i3) {
                double d19;
                double d20 = 0.0;
                double d21 = (double)((i3 & 2) - 1) * d5;
                double d22 = (double)((i3 + 1 & 2) - 1) * d5;
                double d23 = d20;
                double d24 = d21 * d18 - d22 * d17;
                double d25 = d19 = d22 * d18 + d21 * d17;
                double d26 = d24 * d14 + d23 * d15;
                double d27 = d23 * d14 - d24 * d15;
                double d28 = d27 * d11 - d25 * d12;
                double d29 = d26;
                double d30 = d25 * d11 + d27 * d12;
                ho2.a(d7 + d28, d8 + d29, d9 + d30);
            }
        }
        ho2.a();
    }

    public void a(cn cn2) {
        if (this.k != null) {
            this.k.b(this);
        }
        this.f = -9999.0;
        this.g = -9999.0;
        this.h = -9999.0;
        kx.a.a(cn2);
        this.k = cn2;
        this.u = new bc(cn2);
        if (cn2 != null) {
            cn2.a(this);
            this.a();
        }
    }

    public void a() {
        int n2;
        int n3;
        ly.L.a(this.t.y.i);
        this.H = this.t.y.e;
        if (this.o != null) {
            for (n3 = 0; n3 < this.o.length; ++n3) {
                this.o[n3].c();
            }
        }
        if ((n3 = 64 << 3 - this.H) > 400) {
            n3 = 400;
        }
        this.p = n3 / 16 + 1;
        this.q = 8;
        this.r = n3 / 16 + 1;
        this.o = new bn[this.p * this.q * this.r];
        this.n = new bn[this.p * this.q * this.r];
        int n4 = 0;
        int n5 = 0;
        this.B = 0;
        this.C = 0;
        this.D = 0;
        this.E = this.p;
        this.F = this.q;
        this.G = this.r;
        for (n2 = 0; n2 < this.m.size(); ++n2) {
            ((bn)this.m.get((int)n2)).u = false;
        }
        this.m.clear();
        this.a.clear();
        for (n2 = 0; n2 < this.p; ++n2) {
            for (int i2 = 0; i2 < this.q; ++i2) {
                for (int i3 = 0; i3 < this.r; ++i3) {
                    this.o[(i3 * this.q + i2) * this.p + n2] = new bn(this.k, this.a, n2 * 16, i2 * 16, i3 * 16, 16, this.s + n4);
                    if (this.w) {
                        this.o[(i3 * this.q + i2) * this.p + n2].z = this.v.get(n5);
                    }
                    this.o[(i3 * this.q + i2) * this.p + n2].y = false;
                    this.o[(i3 * this.q + i2) * this.p + n2].x = true;
                    this.o[(i3 * this.q + i2) * this.p + n2].o = true;
                    this.o[(i3 * this.q + i2) * this.p + n2].w = n5++;
                    this.o[(i3 * this.q + i2) * this.p + n2].f();
                    this.n[(i3 * this.q + i2) * this.p + n2] = this.o[(i3 * this.q + i2) * this.p + n2];
                    this.m.add(this.o[(i3 * this.q + i2) * this.p + n2]);
                    n4 += 3;
                }
            }
        }
        if (this.k != null) {
            bi bi2 = this.t.g;
            this.b(eo.b(bi2.ak), eo.b(bi2.al), eo.b(bi2.am));
            Arrays.sort(this.n, new fb(bi2));
        }
        this.I = 2;
    }

    public void a(aj aj2, oe oe2, float f2) {
        int n2;
        if (this.I > 0) {
            --this.I;
            return;
        }
        fz.a.a(this.k, this.l, this.t.o, this.t.g, f2);
        kx.a.a(this.k, this.l, this.t.o, this.t.g, this.t.y, f2);
        this.J = 0;
        this.K = 0;
        this.L = 0;
        bi bi2 = this.t.g;
        kx.b = bi2.aI + (bi2.ak - bi2.aI) * (double)f2;
        kx.c = bi2.aJ + (bi2.al - bi2.aJ) * (double)f2;
        kx.d = bi2.aK + (bi2.am - bi2.aK) * (double)f2;
        fz.b = bi2.aI + (bi2.ak - bi2.aI) * (double)f2;
        fz.c = bi2.aJ + (bi2.al - bi2.aJ) * (double)f2;
        fz.d = bi2.aK + (bi2.am - bi2.aK) * (double)f2;
        List list = this.k.i();
        this.J = list.size();
        for (n2 = 0; n2 < list.size(); ++n2) {
            kh kh2 = (kh)list.get(n2);
            if (!kh2.a(aj2) || !oe2.a(kh2.au) || kh2 == this.t.g && !this.t.y.x) continue;
            ++this.K;
            kx.a.a(kh2, f2);
        }
        for (n2 = 0; n2 < this.a.size(); ++n2) {
            fz.a.a((ic)this.a.get(n2), f2);
        }
    }

    public String b() {
        return "C: " + this.P + "/" + this.M + ". F: " + this.N + ", O: " + this.O + ", E: " + this.Q;
    }

    public String c() {
        return "E: " + this.K + "/" + this.J + ". B: " + this.L + ", I: " + (this.J - this.L - this.K);
    }

    private void b(int n2, int n3, int n4) {
        n2 -= 8;
        n3 -= 8;
        n4 -= 8;
        this.B = Integer.MAX_VALUE;
        this.C = Integer.MAX_VALUE;
        this.D = Integer.MAX_VALUE;
        this.E = Integer.MIN_VALUE;
        this.F = Integer.MIN_VALUE;
        this.G = Integer.MIN_VALUE;
        int n5 = this.p * 16;
        int n6 = n5 / 2;
        for (int i2 = 0; i2 < this.p; ++i2) {
            int n7 = i2 * 16;
            int n8 = n7 + n6 - n2;
            if (n8 < 0) {
                n8 -= n5 - 1;
            }
            if ((n7 -= (n8 /= n5) * n5) < this.B) {
                this.B = n7;
            }
            if (n7 > this.E) {
                this.E = n7;
            }
            for (int i3 = 0; i3 < this.r; ++i3) {
                int n9 = i3 * 16;
                int n10 = n9 + n6 - n4;
                if (n10 < 0) {
                    n10 -= n5 - 1;
                }
                if ((n9 -= (n10 /= n5) * n5) < this.D) {
                    this.D = n9;
                }
                if (n9 > this.G) {
                    this.G = n9;
                }
                for (int i4 = 0; i4 < this.q; ++i4) {
                    int n11 = i4 * 16;
                    if (n11 < this.C) {
                        this.C = n11;
                    }
                    if (n11 > this.F) {
                        this.F = n11;
                    }
                    bn bn2 = this.o[(i3 * this.q + i4) * this.p + i2];
                    boolean bl2 = bn2.u;
                    bn2.a(n7, n11, n9);
                    if (bl2 || !bn2.u) continue;
                    this.m.add(bn2);
                }
            }
        }
    }

    public int a(dm dm2, int n2, double d2) {
        if (this.t.y.e != this.H) {
            this.a();
        }
        if (n2 == 0) {
            this.M = 0;
            this.N = 0;
            this.O = 0;
            this.P = 0;
            this.Q = 0;
        }
        double d3 = dm2.aI + (dm2.ak - dm2.aI) * d2;
        double d4 = dm2.aJ + (dm2.al - dm2.aJ) * d2;
        double d5 = dm2.aK + (dm2.am - dm2.aK) * d2;
        double d6 = dm2.ak - this.f;
        double d7 = dm2.al - this.g;
        double d8 = dm2.am - this.h;
        if (d6 * d6 + d7 * d7 + d8 * d8 > 16.0) {
            this.f = dm2.ak;
            this.g = dm2.al;
            this.h = dm2.am;
            this.b(eo.b(dm2.ak), eo.b(dm2.al), eo.b(dm2.am));
            Arrays.sort(this.n, new fb(dm2));
        }
        int n3 = 0;
        if (this.w && !this.t.y.g && n2 == 0) {
            int n4 = 0;
            int n5 = 16;
            this.a(n4, n5);
            for (int i2 = n4; i2 < n5; ++i2) {
                this.n[i2].x = true;
            }
            n3 += this.a(n4, n5, n2, d2);
            do {
                n4 = n5;
                if ((n5 *= 2) > this.n.length) {
                    n5 = this.n.length;
                }
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2896);
                GL11.glDisable((int)3008);
                GL11.glDisable((int)2912);
                GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
                GL11.glDepthMask((boolean)false);
                this.a(n4, n5);
                GL11.glPushMatrix();
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                for (int i3 = n4; i3 < n5; ++i3) {
                    float f5;
                    int n6;
                    if (this.n[i3].e()) {
                        this.n[i3].o = false;
                        continue;
                    }
                    if (!this.n[i3].o) {
                        this.n[i3].x = true;
                    }
                    if (!this.n[i3].o || this.n[i3].y || this.x % (n6 = (int)(1.0f + (f5 = eo.c(this.n[i3].a(dm2))) / 128.0f)) != i3 % n6) continue;
                    bn bn2 = this.n[i3];
                    float f6 = (float)((double)bn2.i - d3);
                    float f7 = (float)((double)bn2.j - d4);
                    float f8 = (float)((double)bn2.k - d5);
                    float f9 = f6 - f2;
                    float f10 = f7 - f3;
                    float f11 = f8 - f4;
                    if (f9 != 0.0f || f10 != 0.0f || f11 != 0.0f) {
                        GL11.glTranslatef((float)f9, (float)f10, (float)f11);
                        f2 += f9;
                        f3 += f10;
                        f4 += f11;
                    }
                    ARBOcclusionQuery.glBeginQueryARB((int)35092, (int)this.n[i3].z);
                    this.n[i3].d();
                    ARBOcclusionQuery.glEndQueryARB((int)35092);
                    this.n[i3].y = true;
                }
                GL11.glPopMatrix();
                GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
                GL11.glDepthMask((boolean)true);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)3008);
                GL11.glEnable((int)2912);
                n3 += this.a(n4, n5, n2, d2);
            } while (n5 < this.n.length);
        } else {
            n3 += this.a(0, this.n.length, n2, d2);
        }
        return n3;
    }

    private void a(int n2, int n3) {
        for (int i2 = n2; i2 < n3; ++i2) {
            if (!this.n[i2].y) continue;
            this.c.clear();
            ARBOcclusionQuery.glGetQueryObjectuARB((int)this.n[i2].z, (int)34919, (IntBuffer)this.c);
            if (this.c.get(0) == 0) continue;
            this.n[i2].y = false;
            this.c.clear();
            ARBOcclusionQuery.glGetQueryObjectuARB((int)this.n[i2].z, (int)34918, (IntBuffer)this.c);
            this.n[i2].x = this.c.get(0) != 0;
        }
    }

    private int a(int n2, int n3, int n4, double d2) {
        int n5;
        this.R.clear();
        int n6 = 0;
        for (int i2 = n2; i2 < n3; ++i2) {
            int n7;
            if (n4 == 0) {
                ++this.M;
                if (this.n[i2].p[n4]) {
                    ++this.Q;
                } else if (!this.n[i2].o) {
                    ++this.N;
                } else if (this.w && !this.n[i2].x) {
                    ++this.O;
                } else {
                    ++this.P;
                }
            }
            if (this.n[i2].p[n4] || !this.n[i2].o || !this.n[i2].x || (n7 = this.n[i2].a(n4)) < 0) continue;
            this.R.add(this.n[i2]);
            ++n6;
        }
        bi bi2 = this.t.g;
        double d3 = bi2.aI + (bi2.ak - bi2.aI) * d2;
        double d4 = bi2.aJ + (bi2.al - bi2.aJ) * d2;
        double d5 = bi2.aK + (bi2.am - bi2.aK) * d2;
        int n8 = 0;
        for (n5 = 0; n5 < this.S.length; ++n5) {
            this.S[n5].b();
        }
        for (n5 = 0; n5 < this.R.size(); ++n5) {
            bn bn2 = (bn)this.R.get(n5);
            int n9 = -1;
            for (int i3 = 0; i3 < n8; ++i3) {
                if (!this.S[i3].a(bn2.i, bn2.j, bn2.k)) continue;
                n9 = i3;
            }
            if (n9 < 0) {
                n9 = n8++;
                this.S[n9].a(bn2.i, bn2.j, bn2.k, d3, d4, d5);
            }
            this.S[n9].a(bn2.a(n4));
        }
        this.a(n4, d2);
        return n6;
    }

    public void a(int n2, double d2) {
        for (int i2 = 0; i2 < this.S.length; ++i2) {
            this.S[i2].a();
        }
    }

    public void d() {
        ++this.x;
    }

    public void a(float f2) {
        float f3;
        float f4;
        GL11.glDisable((int)3553);
        aj aj2 = this.k.b(f2);
        float f5 = (float)aj2.a;
        float f6 = (float)aj2.b;
        float f7 = (float)aj2.c;
        if (this.t.y.g) {
            float f8 = (f5 * 30.0f + f6 * 59.0f + f7 * 11.0f) / 100.0f;
            f4 = (f5 * 30.0f + f6 * 70.0f) / 100.0f;
            f3 = (f5 * 30.0f + f7 * 70.0f) / 100.0f;
            f5 = f8;
            f6 = f4;
            f7 = f3;
        }
        GL11.glColor3f((float)f5, (float)f6, (float)f7);
        ho ho2 = ho.a;
        GL11.glDepthMask((boolean)false);
        GL11.glEnable((int)2912);
        GL11.glColor3f((float)f5, (float)f6, (float)f7);
        GL11.glCallList((int)this.z);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2912);
        GL11.glDisable((int)3008);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)1, (int)1);
        GL11.glPushMatrix();
        f4 = 0.0f;
        f3 = 0.0f;
        float f9 = 0.0f;
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glTranslatef((float)f4, (float)f3, (float)f9);
        GL11.glRotatef((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glRotatef((float)(this.k.c(f2) * 360.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        float f10 = 30.0f;
        GL11.glBindTexture((int)3553, (int)this.l.a("/terrain/sun.png"));
        ho2.b();
        ho2.a(-f10, 100.0, -f10, 0.0, 0.0);
        ho2.a(f10, 100.0, -f10, 1.0, 0.0);
        ho2.a(f10, 100.0, f10, 1.0, 1.0);
        ho2.a(-f10, 100.0, f10, 0.0, 1.0);
        ho2.a();
        f10 = 20.0f;
        GL11.glBindTexture((int)3553, (int)this.l.a("/terrain/moon.png"));
        ho2.b();
        ho2.a(-f10, -100.0, f10, 1.0, 1.0);
        ho2.a(f10, -100.0, f10, 0.0, 1.0);
        ho2.a(f10, -100.0, -f10, 0.0, 0.0);
        ho2.a(-f10, -100.0, -f10, 1.0, 0.0);
        ho2.a();
        GL11.glDisable((int)3553);
        float f11 = this.k.f(f2);
        if (f11 > 0.0f) {
            GL11.glColor4f((float)f11, (float)f11, (float)f11, (float)f11);
            GL11.glCallList((int)this.y);
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glEnable((int)2912);
        GL11.glPopMatrix();
        GL11.glColor3f((float)(f5 * 0.2f + 0.04f), (float)(f6 * 0.2f + 0.04f), (float)(f7 * 0.6f + 0.1f));
        GL11.glDisable((int)3553);
        GL11.glCallList((int)this.A);
        GL11.glEnable((int)3553);
        GL11.glDepthMask((boolean)true);
    }

    public void b(float f2) {
        float f3;
        if (this.t.y.i) {
            this.c(f2);
            return;
        }
        GL11.glDisable((int)2884);
        float f4 = (float)(this.t.g.aJ + (this.t.g.al - this.t.g.aJ) * (double)f2);
        int n2 = 32;
        int n3 = 256 / n2;
        ho ho2 = ho.a;
        GL11.glBindTexture((int)3553, (int)this.l.a("/clouds.png"));
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        aj aj2 = this.k.d(f2);
        float f5 = (float)aj2.a;
        float f6 = (float)aj2.b;
        float f7 = (float)aj2.c;
        if (this.t.y.g) {
            f3 = (f5 * 30.0f + f6 * 59.0f + f7 * 11.0f) / 100.0f;
            float f8 = (f5 * 30.0f + f6 * 70.0f) / 100.0f;
            float f9 = (f5 * 30.0f + f7 * 70.0f) / 100.0f;
            f5 = f3;
            f6 = f8;
            f7 = f9;
        }
        f3 = 4.8828125E-4f;
        double d2 = this.t.g.ah + (this.t.g.ak - this.t.g.ah) * (double)f2 + (double)(((float)this.x + f2) * 0.03f);
        double d3 = this.t.g.aj + (this.t.g.am - this.t.g.aj) * (double)f2;
        int n4 = eo.b(d2 / 2048.0);
        int n5 = eo.b(d3 / 2048.0);
        float f10 = 120.0f - f4 + 0.33f;
        float f11 = (float)((d2 -= (double)(n4 * 2048)) * (double)f3);
        float f12 = (float)((d3 -= (double)(n5 * 2048)) * (double)f3);
        ho2.b();
        ho2.a(f5, f6, f7, 0.8f);
        for (int i2 = -n2 * n3; i2 < n2 * n3; i2 += n2) {
            for (int i3 = -n2 * n3; i3 < n2 * n3; i3 += n2) {
                ho2.a(i2 + 0, f10, i3 + n2, (float)(i2 + 0) * f3 + f11, (float)(i3 + n2) * f3 + f12);
                ho2.a(i2 + n2, f10, i3 + n2, (float)(i2 + n2) * f3 + f11, (float)(i3 + n2) * f3 + f12);
                ho2.a(i2 + n2, f10, i3 + 0, (float)(i2 + n2) * f3 + f11, (float)(i3 + 0) * f3 + f12);
                ho2.a(i2 + 0, f10, i3 + 0, (float)(i2 + 0) * f3 + f11, (float)(i3 + 0) * f3 + f12);
            }
        }
        ho2.a();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
    }

    public void c(float f2) {
        float f3;
        float f4;
        float f5;
        GL11.glDisable((int)2884);
        float f6 = (float)(this.t.g.aJ + (this.t.g.al - this.t.g.aJ) * (double)f2);
        ho ho2 = ho.a;
        float f7 = 12.0f;
        float f8 = 4.0f;
        double d2 = (this.t.g.ah + (this.t.g.ak - this.t.g.ah) * (double)f2 + (double)(((float)this.x + f2) * 0.03f)) / (double)f7;
        double d3 = (this.t.g.aj + (this.t.g.am - this.t.g.aj) * (double)f2) / (double)f7 + (double)0.33f;
        float f9 = 108.0f - f6 + 0.33f;
        int n2 = eo.b(d2 / 2048.0);
        int n3 = eo.b(d3 / 2048.0);
        d2 -= (double)(n2 * 2048);
        d3 -= (double)(n3 * 2048);
        GL11.glBindTexture((int)3553, (int)this.l.a("/clouds.png"));
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        aj aj2 = this.k.d(f2);
        float f10 = (float)aj2.a;
        float f11 = (float)aj2.b;
        float f12 = (float)aj2.c;
        if (this.t.y.g) {
            f5 = (f10 * 30.0f + f11 * 59.0f + f12 * 11.0f) / 100.0f;
            f4 = (f10 * 30.0f + f11 * 70.0f) / 100.0f;
            f3 = (f10 * 30.0f + f12 * 70.0f) / 100.0f;
            f10 = f5;
            f11 = f4;
            f12 = f3;
        }
        f5 = (float)(d2 * 0.0);
        f4 = (float)(d3 * 0.0);
        f3 = 0.00390625f;
        f5 = (float)eo.b(d2) * f3;
        f4 = (float)eo.b(d3) * f3;
        float f13 = (float)(d2 - (double)eo.b(d2));
        float f14 = (float)(d3 - (double)eo.b(d3));
        int n4 = 8;
        int n5 = 3;
        float f15 = 9.765625E-4f;
        GL11.glScalef((float)f7, (float)1.0f, (float)f7);
        for (int i2 = 0; i2 < 2; ++i2) {
            if (i2 == 0) {
                GL11.glColorMask((boolean)false, (boolean)false, (boolean)false, (boolean)false);
            } else {
                GL11.glColorMask((boolean)true, (boolean)true, (boolean)true, (boolean)true);
            }
            for (int i3 = -n5 + 1; i3 <= n5; ++i3) {
                for (int i4 = -n5 + 1; i4 <= n5; ++i4) {
                    int n6;
                    ho2.b();
                    float f16 = i3 * n4;
                    float f17 = i4 * n4;
                    float f18 = f16 - f13;
                    float f19 = f17 - f14;
                    if (f9 > -f8 - 1.0f) {
                        ho2.a(f10 * 0.7f, f11 * 0.7f, f12 * 0.7f, 0.8f);
                        ho2.b(0.0f, -1.0f, 0.0f);
                        ho2.a(f18 + 0.0f, f9 + 0.0f, f19 + (float)n4, (f16 + 0.0f) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                        ho2.a(f18 + (float)n4, f9 + 0.0f, f19 + (float)n4, (f16 + (float)n4) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                        ho2.a(f18 + (float)n4, f9 + 0.0f, f19 + 0.0f, (f16 + (float)n4) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                        ho2.a(f18 + 0.0f, f9 + 0.0f, f19 + 0.0f, (f16 + 0.0f) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                    }
                    if (f9 <= f8 + 1.0f) {
                        ho2.a(f10, f11, f12, 0.8f);
                        ho2.b(0.0f, 1.0f, 0.0f);
                        ho2.a(f18 + 0.0f, f9 + f8 - f15, f19 + (float)n4, (f16 + 0.0f) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                        ho2.a(f18 + (float)n4, f9 + f8 - f15, f19 + (float)n4, (f16 + (float)n4) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                        ho2.a(f18 + (float)n4, f9 + f8 - f15, f19 + 0.0f, (f16 + (float)n4) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                        ho2.a(f18 + 0.0f, f9 + f8 - f15, f19 + 0.0f, (f16 + 0.0f) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                    }
                    ho2.a(f10 * 0.9f, f11 * 0.9f, f12 * 0.9f, 0.8f);
                    if (i3 > -1) {
                        ho2.b(-1.0f, 0.0f, 0.0f);
                        for (n6 = 0; n6 < n4; ++n6) {
                            ho2.a(f18 + (float)n6 + 0.0f, f9 + 0.0f, f19 + (float)n4, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                            ho2.a(f18 + (float)n6 + 0.0f, f9 + f8, f19 + (float)n4, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                            ho2.a(f18 + (float)n6 + 0.0f, f9 + f8, f19 + 0.0f, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                            ho2.a(f18 + (float)n6 + 0.0f, f9 + 0.0f, f19 + 0.0f, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                        }
                    }
                    if (i3 <= 1) {
                        ho2.b(1.0f, 0.0f, 0.0f);
                        for (n6 = 0; n6 < n4; ++n6) {
                            ho2.a(f18 + (float)n6 + 1.0f - f15, f9 + 0.0f, f19 + (float)n4, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                            ho2.a(f18 + (float)n6 + 1.0f - f15, f9 + f8, f19 + (float)n4, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + (float)n4) * f3 + f4);
                            ho2.a(f18 + (float)n6 + 1.0f - f15, f9 + f8, f19 + 0.0f, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                            ho2.a(f18 + (float)n6 + 1.0f - f15, f9 + 0.0f, f19 + 0.0f, (f16 + (float)n6 + 0.5f) * f3 + f5, (f17 + 0.0f) * f3 + f4);
                        }
                    }
                    ho2.a(f10 * 0.8f, f11 * 0.8f, f12 * 0.8f, 0.8f);
                    if (i4 > -1) {
                        ho2.b(0.0f, 0.0f, -1.0f);
                        for (n6 = 0; n6 < n4; ++n6) {
                            ho2.a(f18 + 0.0f, f9 + f8, f19 + (float)n6 + 0.0f, (f16 + 0.0f) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                            ho2.a(f18 + (float)n4, f9 + f8, f19 + (float)n6 + 0.0f, (f16 + (float)n4) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                            ho2.a(f18 + (float)n4, f9 + 0.0f, f19 + (float)n6 + 0.0f, (f16 + (float)n4) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                            ho2.a(f18 + 0.0f, f9 + 0.0f, f19 + (float)n6 + 0.0f, (f16 + 0.0f) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                        }
                    }
                    if (i4 <= 1) {
                        ho2.b(0.0f, 0.0f, 1.0f);
                        for (n6 = 0; n6 < n4; ++n6) {
                            ho2.a(f18 + 0.0f, f9 + f8, f19 + (float)n6 + 1.0f - f15, (f16 + 0.0f) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                            ho2.a(f18 + (float)n4, f9 + f8, f19 + (float)n6 + 1.0f - f15, (f16 + (float)n4) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                            ho2.a(f18 + (float)n4, f9 + 0.0f, f19 + (float)n6 + 1.0f - f15, (f16 + (float)n4) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                            ho2.a(f18 + 0.0f, f9 + 0.0f, f19 + (float)n6 + 1.0f - f15, (f16 + 0.0f) * f3 + f5, (f17 + (float)n6 + 0.5f) * f3 + f4);
                        }
                    }
                    ho2.a();
                }
            }
        }
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
    }

    public boolean a(dm dm2, boolean bl2) {
        Collections.sort(this.m, new gl(dm2));
        int n2 = this.m.size() - 1;
        int n3 = this.m.size();
        for (int i2 = 0; i2 < n3; ++i2) {
            bn bn2 = (bn)this.m.get(n2 - i2);
            if (!bl2) {
                if (bn2.a(dm2) > 1024.0f && (bn2.o ? i2 >= 3 : i2 >= 1)) {
                    return false;
                }
            } else if (!bn2.o) continue;
            bn2.a();
            this.m.remove(bn2);
            bn2.u = false;
        }
        return this.m.size() == 0;
    }

    public void a(dm dm2, mf mf2, int n2, ev ev2, float f2) {
        ho ho2 = ho.a;
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3008);
        GL11.glBlendFunc((int)770, (int)1);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)((eo.a((float)System.currentTimeMillis() / 100.0f) * 0.2f + 0.4f) * 0.5f));
        if (n2 == 0) {
            if (this.i > 0.0f) {
                GL11.glBlendFunc((int)774, (int)768);
                int n3 = this.l.a("/terrain.png");
                GL11.glBindTexture((int)3553, (int)n3);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)0.5f);
                GL11.glPushMatrix();
                int n4 = this.k.a(mf2.b, mf2.c, mf2.d);
                ly ly2 = n4 > 0 ? ly.n[n4] : null;
                GL11.glDisable((int)3008);
                GL11.glPolygonOffset((float)-3.0f, (float)-3.0f);
                GL11.glEnable((int)32823);
                ho2.b();
                double d2 = dm2.aI + (dm2.ak - dm2.aI) * (double)f2;
                double d3 = dm2.aJ + (dm2.al - dm2.aJ) * (double)f2;
                double d4 = dm2.aK + (dm2.am - dm2.aK) * (double)f2;
                ho2.b(-d2, -d3, -d4);
                ho2.c();
                if (ly2 == null) {
                    ly2 = ly.u;
                }
                this.u.a(ly2, mf2.b, mf2.c, mf2.d, 240 + (int)(this.i * 10.0f));
                ho2.a();
                ho2.b(0.0, 0.0, 0.0);
                GL11.glPolygonOffset((float)0.0f, (float)0.0f);
                GL11.glDisable((int)32823);
                GL11.glEnable((int)3008);
                GL11.glDepthMask((boolean)true);
                GL11.glPopMatrix();
            }
        } else if (ev2 != null) {
            GL11.glBlendFunc((int)770, (int)771);
            float f3 = eo.a((float)System.currentTimeMillis() / 100.0f) * 0.2f + 0.8f;
            GL11.glColor4f((float)f3, (float)f3, (float)f3, (float)(eo.a((float)System.currentTimeMillis() / 200.0f) * 0.2f + 0.5f));
            int n5 = this.l.a("/terrain.png");
            GL11.glBindTexture((int)3553, (int)n5);
            int n6 = mf2.b;
            int n7 = mf2.c;
            int n8 = mf2.d;
            if (mf2.e == 0) {
                --n7;
            }
            if (mf2.e == 1) {
                ++n7;
            }
            if (mf2.e == 2) {
                --n8;
            }
            if (mf2.e == 3) {
                ++n8;
            }
            if (mf2.e == 4) {
                --n6;
            }
            if (mf2.e == 5) {
                ++n6;
            }
        }
        GL11.glDisable((int)3042);
        GL11.glDisable((int)3008);
    }

    public void b(dm dm2, mf mf2, int n2, ev ev2, float f2) {
        if (n2 == 0 && mf2.a == 0) {
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.4f);
            GL11.glLineWidth((float)2.0f);
            GL11.glDisable((int)3553);
            GL11.glDepthMask((boolean)false);
            float f3 = 0.002f;
            int n3 = this.k.a(mf2.b, mf2.c, mf2.d);
            if (n3 > 0) {
                ly.n[n3].a((nm)this.k, mf2.b, mf2.c, mf2.d);
                double d2 = dm2.aI + (dm2.ak - dm2.aI) * (double)f2;
                double d3 = dm2.aJ + (dm2.al - dm2.aJ) * (double)f2;
                double d4 = dm2.aK + (dm2.am - dm2.aK) * (double)f2;
                this.a(ly.n[n3].f(this.k, mf2.b, mf2.c, mf2.d).b(f3, f3, f3).c(-d2, -d3, -d4));
            }
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)3553);
            GL11.glDisable((int)3042);
        }
    }

    private void a(cf cf2) {
        ho ho2 = ho.a;
        ho2.a(3);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a();
        ho2.a(3);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a();
        ho2.a(1);
        ho2.a(cf2.a, cf2.b, cf2.c);
        ho2.a(cf2.a, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.c);
        ho2.a(cf2.d, cf2.e, cf2.c);
        ho2.a(cf2.d, cf2.b, cf2.f);
        ho2.a(cf2.d, cf2.e, cf2.f);
        ho2.a(cf2.a, cf2.b, cf2.f);
        ho2.a(cf2.a, cf2.e, cf2.f);
        ho2.a();
    }

    public void a(int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = eo.a(n2, 16);
        int n9 = eo.a(n3, 16);
        int n10 = eo.a(n4, 16);
        int n11 = eo.a(n5, 16);
        int n12 = eo.a(n6, 16);
        int n13 = eo.a(n7, 16);
        for (int i2 = n8; i2 <= n11; ++i2) {
            int n14 = i2 % this.p;
            if (n14 < 0) {
                n14 += this.p;
            }
            for (int i3 = n9; i3 <= n12; ++i3) {
                int n15 = i3 % this.q;
                if (n15 < 0) {
                    n15 += this.q;
                }
                for (int i4 = n10; i4 <= n13; ++i4) {
                    int n16 = i4 % this.r;
                    if (n16 < 0) {
                        n16 += this.r;
                    }
                    int n17 = (n16 * this.q + n15) * this.p + n14;
                    bn bn2 = this.o[n17];
                    if (!bn2.u) {
                        this.m.add(bn2);
                    }
                    bn2.f();
                }
            }
        }
    }

    public void a(int n2, int n3, int n4) {
        this.a(n2 - 1, n3 - 1, n4 - 1, n2 + 1, n3 + 1, n4 + 1);
    }

    public void b(int n2, int n3, int n4, int n5, int n6, int n7) {
        this.a(n2 - 1, n3 - 1, n4 - 1, n5 + 1, n6 + 1, n7 + 1);
    }

    public void a(oe oe2, float f2) {
        for (int i2 = 0; i2 < this.o.length; ++i2) {
            if (this.o[i2].e() || this.o[i2].o && (i2 + this.j & 0xF) != 0) continue;
            this.o[i2].a(oe2);
        }
        ++this.j;
    }

    public void a(String string, int n2, int n3, int n4) {
        if (string != null) {
            this.t.u.b("C418 - " + string);
        }
        this.t.A.a(string, n2, n3, n4, 1.0f, 1.0f);
    }

    public void a(String string, double d2, double d3, double d4, float f2, float f3) {
        float f4 = 16.0f;
        if (f2 > 1.0f) {
            f4 *= f2;
        }
        if (this.t.g.d(d2, d3, d4) < (double)(f4 * f4)) {
            this.t.A.b(string, (float)d2, (float)d3, (float)d4, f2, f3);
        }
    }

    public void a(String string, double d2, double d3, double d4, double d5, double d6, double d7) {
        double d8 = this.t.g.ak - d2;
        double d9 = this.t.g.al - d3;
        double d10 = this.t.g.am - d4;
        if (d8 * d8 + d9 * d9 + d10 * d10 > 256.0) {
            return;
        }
        if (string == "bubble") {
            this.t.h.a(new ba(this.k, d2, d3, d4, d5, d6, d7));
        } else if (string == "smoke") {
            this.t.h.a(new nl(this.k, d2, d3, d4));
        } else if (string == "explode") {
            this.t.h.a(new dp(this.k, d2, d3, d4, d5, d6, d7));
        } else if (string == "flame") {
            this.t.h.a(new jb(this.k, d2, d3, d4, d5, d6, d7));
        } else if (string == "lava") {
            this.t.h.a(new cq(this.k, d2, d3, d4));
        } else if (string == "splash") {
            this.t.h.a(new kq(this.k, d2, d3, d4, d5, d6, d7));
        } else if (string == "largesmoke") {
            this.t.h.a(new nl(this.k, d2, d3, d4, 2.5f));
        } else if (string == "reddust") {
            this.t.h.a(new en(this.k, d2, d3, d4));
        } else if (string == "snowballpoof") {
            this.t.h.a(new ig(this.k, d2, d3, d4, di.aB));
        } else if (string == "slime") {
            this.t.h.a(new ig(this.k, d2, d3, d4, di.aK));
        }
    }

    public void a(kh kh2) {
        if (kh2.aY != null) {
            this.l.a(kh2.aY, new jr());
        }
    }

    public void b(kh kh2) {
        if (kh2.aY != null) {
            this.l.b(kh2.aY);
        }
    }

    public void e() {
        for (int i2 = 0; i2 < this.o.length; ++i2) {
            if (!this.o[i2].A) continue;
            if (!this.o[i2].u) {
                this.m.add(this.o[i2]);
            }
            this.o[i2].f();
        }
    }

    public void a(int n2, int n3, int n4, ic ic2) {
    }
}

