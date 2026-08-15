/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class nw
implements aw {
    private Random j;
    private lp k;
    private lp l;
    private lp m;
    private lp n;
    private lp o;
    public lp a;
    public lp b;
    public lp c;
    private cn p;
    private double[] q;
    private double[] r = new double[256];
    private double[] s = new double[256];
    private double[] t = new double[256];
    private cy u = new kk();
    double[] d;
    double[] e;
    double[] f;
    double[] g;
    double[] h;
    int[][] i = new int[32][32];

    public nw(cn cn2, long l2) {
        this.p = cn2;
        this.j = new Random(l2);
        this.k = new lp(this.j, 16);
        this.l = new lp(this.j, 16);
        this.m = new lp(this.j, 8);
        this.n = new lp(this.j, 4);
        this.o = new lp(this.j, 4);
        this.a = new lp(this.j, 10);
        this.b = new lp(this.j, 16);
        this.c = new lp(this.j, 8);
    }

    public void a(int n2, int n3, byte[] byArray) {
        int n4 = 4;
        int n5 = 64;
        int n6 = n4 + 1;
        int n7 = 17;
        int n8 = n4 + 1;
        this.q = this.a(this.q, n2 * n4, 0, n3 * n4, n6, n7, n8);
        for (int i2 = 0; i2 < n4; ++i2) {
            for (int i3 = 0; i3 < n4; ++i3) {
                for (int i4 = 0; i4 < 16; ++i4) {
                    double d2 = 0.125;
                    double d3 = this.q[((i2 + 0) * n8 + (i3 + 0)) * n7 + (i4 + 0)];
                    double d4 = this.q[((i2 + 0) * n8 + (i3 + 1)) * n7 + (i4 + 0)];
                    double d5 = this.q[((i2 + 1) * n8 + (i3 + 0)) * n7 + (i4 + 0)];
                    double d6 = this.q[((i2 + 1) * n8 + (i3 + 1)) * n7 + (i4 + 0)];
                    double d7 = (this.q[((i2 + 0) * n8 + (i3 + 0)) * n7 + (i4 + 1)] - d3) * d2;
                    double d8 = (this.q[((i2 + 0) * n8 + (i3 + 1)) * n7 + (i4 + 1)] - d4) * d2;
                    double d9 = (this.q[((i2 + 1) * n8 + (i3 + 0)) * n7 + (i4 + 1)] - d5) * d2;
                    double d10 = (this.q[((i2 + 1) * n8 + (i3 + 1)) * n7 + (i4 + 1)] - d6) * d2;
                    for (int i5 = 0; i5 < 8; ++i5) {
                        double d11 = 0.25;
                        double d12 = d3;
                        double d13 = d4;
                        double d14 = (d5 - d3) * d11;
                        double d15 = (d6 - d4) * d11;
                        for (int i6 = 0; i6 < 4; ++i6) {
                            int n9 = i6 + i2 * 4 << 11 | 0 + i3 * 4 << 7 | i4 * 8 + i5;
                            int n10 = 128;
                            double d16 = 0.25;
                            double d17 = d12;
                            double d18 = (d13 - d12) * d16;
                            for (int i7 = 0; i7 < 4; ++i7) {
                                int n11 = 0;
                                if (i4 * 8 + i5 < n5) {
                                    n11 = this.p.d && i4 * 8 + i5 >= n5 - 1 ? ly.aU.bc : ly.C.bc;
                                }
                                if (d17 > 0.0) {
                                    n11 = ly.u.bc;
                                }
                                byArray[n9] = (byte)n11;
                                n9 += n10;
                                d17 += d18;
                            }
                            d12 += d14;
                            d13 += d15;
                        }
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                        d6 += d10;
                    }
                }
            }
        }
    }

    public void b(int n2, int n3, byte[] byArray) {
        int n4 = 64;
        double d2 = 0.03125;
        this.r = this.n.a(this.r, n2 * 16, n3 * 16, 0.0, 16, 16, 1, d2, d2, 1.0);
        this.s = this.n.a(this.s, n3 * 16, 109.0134, n2 * 16, 16, 1, 16, d2, 1.0, d2);
        this.t = this.o.a(this.t, n2 * 16, n3 * 16, 0.0, 16, 16, 1, d2 * 2.0, d2 * 2.0, d2 * 2.0);
        for (int i2 = 0; i2 < 16; ++i2) {
            for (int i3 = 0; i3 < 16; ++i3) {
                boolean bl2 = this.r[i2 + i3 * 16] + this.j.nextDouble() * 0.2 > 0.0;
                boolean bl3 = this.s[i2 + i3 * 16] + this.j.nextDouble() * 0.2 > 3.0;
                int n5 = (int)(this.t[i2 + i3 * 16] / 3.0 + 3.0 + this.j.nextDouble() * 0.25);
                int n6 = -1;
                byte by2 = (byte)ly.v.bc;
                byte by3 = (byte)ly.w.bc;
                for (int i4 = 127; i4 >= 0; --i4) {
                    int n7 = (i2 * 16 + i3) * 128 + i4;
                    if (i4 <= 0 + this.j.nextInt(6) - 1) {
                        byArray[n7] = (byte)ly.A.bc;
                        continue;
                    }
                    byte by4 = byArray[n7];
                    if (by4 == 0) {
                        n6 = -1;
                        continue;
                    }
                    if (by4 != ly.u.bc) continue;
                    if (n6 == -1) {
                        if (n5 <= 0) {
                            by2 = 0;
                            by3 = (byte)ly.u.bc;
                        } else if (i4 >= n4 - 4 && i4 <= n4 + 1) {
                            by2 = (byte)ly.v.bc;
                            by3 = (byte)ly.w.bc;
                            if (bl3) {
                                by2 = 0;
                            }
                            if (bl3) {
                                by3 = (byte)ly.G.bc;
                            }
                            if (bl2) {
                                by2 = (byte)ly.F.bc;
                            }
                            if (bl2) {
                                by3 = (byte)ly.F.bc;
                            }
                        }
                        if (i4 < n4 && by2 == 0) {
                            by2 = (byte)ly.C.bc;
                        }
                        n6 = n5;
                        if (i4 >= n4 - 1) {
                            byArray[n7] = by2;
                            continue;
                        }
                        byArray[n7] = by3;
                        continue;
                    }
                    if (n6 <= 0) continue;
                    --n6;
                    byArray[n7] = by3;
                }
            }
        }
    }

    public ga b(int n2, int n3) {
        this.j.setSeed((long)n2 * 341873128712L + (long)n3 * 132897987541L);
        byte[] byArray = new byte[32768];
        ga ga2 = new ga(this.p, byArray, n2, n3);
        this.a(n2, n3, byArray);
        this.b(n2, n3, byArray);
        this.u.a(this, this.p, n2, n3, byArray);
        ga2.c();
        return ga2;
    }

    private double[] a(double[] dArray, int n2, int n3, int n4, int n5, int n6, int n7) {
        if (dArray == null) {
            dArray = new double[n5 * n6 * n7];
        }
        double d2 = 684.412;
        double d3 = 684.412;
        this.g = this.a.a(this.g, n2, n3, n4, n5, 1, n7, 1.0, 0.0, 1.0);
        this.h = this.b.a(this.h, n2, n3, n4, n5, 1, n7, 100.0, 0.0, 100.0);
        this.d = this.m.a(this.d, n2, n3, n4, n5, n6, n7, d2 / 80.0, d3 / 160.0, d2 / 80.0);
        this.e = this.k.a(this.e, n2, n3, n4, n5, n6, n7, d2, d3, d2);
        this.f = this.l.a(this.f, n2, n3, n4, n5, n6, n7, d2, d3, d2);
        int n8 = 0;
        int n9 = 0;
        for (int i2 = 0; i2 < n5; ++i2) {
            for (int i3 = 0; i3 < n7; ++i3) {
                double d4 = (this.g[n9] + 256.0) / 512.0;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                double d5 = 0.0;
                double d6 = this.h[n9] / 8000.0;
                if (d6 < 0.0) {
                    d6 = -d6;
                }
                if ((d6 = d6 * 3.0 - 3.0) < 0.0) {
                    if ((d6 /= 2.0) < -1.0) {
                        d6 = -1.0;
                    }
                    d6 /= 1.4;
                    d6 /= 2.0;
                    d4 = 0.0;
                } else {
                    if (d6 > 1.0) {
                        d6 = 1.0;
                    }
                    d6 /= 6.0;
                }
                d4 += 0.5;
                d6 = d6 * (double)n6 / 16.0;
                double d7 = (double)n6 / 2.0 + d6 * 4.0;
                ++n9;
                for (int i4 = 0; i4 < n6; ++i4) {
                    double d8;
                    double d9 = 0.0;
                    double d10 = ((double)i4 - d7) * 12.0 / d4;
                    if (d10 < 0.0) {
                        d10 *= 4.0;
                    }
                    double d11 = this.e[n8] / 512.0;
                    double d12 = this.f[n8] / 512.0;
                    double d13 = (this.d[n8] / 10.0 + 1.0) / 2.0;
                    d9 = d13 < 0.0 ? d11 : (d13 > 1.0 ? d12 : d11 + (d12 - d11) * d13);
                    d9 -= d10;
                    if (i4 > n6 - 4) {
                        d8 = (float)(i4 - (n6 - 4)) / 3.0f;
                        d9 = d9 * (1.0 - d8) + -10.0 * d8;
                    }
                    if ((double)i4 < d5) {
                        d8 = (d5 - (double)i4) / 4.0;
                        if (d8 < 0.0) {
                            d8 = 0.0;
                        }
                        if (d8 > 1.0) {
                            d8 = 1.0;
                        }
                        d9 = d9 * (1.0 - d8) + -10.0 * d8;
                    }
                    dArray[n8] = d9;
                    ++n8;
                }
            }
        }
        return dArray;
    }

    public boolean a(int n2, int n3) {
        return true;
    }

    public void a(aw aw2, int n2, int n3) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        dh.a = true;
        int n10 = n2 * 16;
        int n11 = n3 * 16;
        this.j.setSeed(this.p.u);
        long l2 = this.j.nextLong() / 2L * 2L + 1L;
        long l3 = this.j.nextLong() / 2L * 2L + 1L;
        this.j.setSeed((long)n2 * l2 + (long)n3 * l3 ^ this.p.u);
        double d2 = 0.25;
        for (n9 = 0; n9 < 8; ++n9) {
            n8 = n10 + this.j.nextInt(16) + 8;
            n7 = this.j.nextInt(128);
            n6 = n11 + this.j.nextInt(16) + 8;
            new cg().a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 10; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(128);
            n6 = n11 + this.j.nextInt(16);
            new gv(32).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 20; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(128);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.w.bc, 32).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 10; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(128);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.G.bc, 32).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 20; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(128);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.J.bc, 16).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 20; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(64);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.I.bc, 8).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 2; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(32);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.H.bc, 8).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 8; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(16);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.aO.bc, 7).a(this.p, this.j, n8, n7, n6);
        }
        for (n9 = 0; n9 < 1; ++n9) {
            n8 = n10 + this.j.nextInt(16);
            n7 = this.j.nextInt(16);
            n6 = n11 + this.j.nextInt(16);
            new cu(ly.ax.bc, 7).a(this.p, this.j, n8, n7, n6);
        }
        d2 = 0.5;
        n9 = (int)((this.c.a((double)n10 * d2, (double)n11 * d2) / 8.0 + this.j.nextDouble() * 4.0 + 4.0) / 3.0);
        if (n9 < 0) {
            n9 = 0;
        }
        if (this.j.nextInt(10) == 0) {
            ++n9;
        }
        ik ik2 = new oa();
        if (this.j.nextInt(10) == 0) {
            ik2 = new ej();
        }
        for (n7 = 0; n7 < n9; ++n7) {
            n6 = n10 + this.j.nextInt(16) + 8;
            n5 = n11 + this.j.nextInt(16) + 8;
            ik2.a(1.0, 1.0, 1.0);
            ik2.a(this.p, this.j, n6, this.p.c(n6, n5), n5);
        }
        for (n7 = 0; n7 < 2; ++n7) {
            n6 = n10 + this.j.nextInt(16) + 8;
            n5 = this.j.nextInt(128);
            n4 = n11 + this.j.nextInt(16) + 8;
            new ae(ly.ae.bc).a(this.p, this.j, n6, n5, n4);
        }
        if (this.j.nextInt(2) == 0) {
            n7 = n10 + this.j.nextInt(16) + 8;
            n6 = this.j.nextInt(128);
            n5 = n11 + this.j.nextInt(16) + 8;
            new ae(ly.af.bc).a(this.p, this.j, n7, n6, n5);
        }
        if (this.j.nextInt(4) == 0) {
            n7 = n10 + this.j.nextInt(16) + 8;
            n6 = this.j.nextInt(128);
            n5 = n11 + this.j.nextInt(16) + 8;
            new ae(ly.ag.bc).a(this.p, this.j, n7, n6, n5);
        }
        if (this.j.nextInt(8) == 0) {
            n7 = n10 + this.j.nextInt(16) + 8;
            n6 = this.j.nextInt(128);
            n5 = n11 + this.j.nextInt(16) + 8;
            new ae(ly.ah.bc).a(this.p, this.j, n7, n6, n5);
        }
        for (n7 = 0; n7 < 10; ++n7) {
            n6 = n10 + this.j.nextInt(16) + 8;
            n5 = this.j.nextInt(128);
            n4 = n11 + this.j.nextInt(16) + 8;
            new es().a(this.p, this.j, n6, n5, n4);
        }
        for (n7 = 0; n7 < 1; ++n7) {
            n6 = n10 + this.j.nextInt(16) + 8;
            n5 = this.j.nextInt(128);
            n4 = n11 + this.j.nextInt(16) + 8;
            new da().a(this.p, this.j, n6, n5, n4);
        }
        for (n7 = 0; n7 < 50; ++n7) {
            n6 = n10 + this.j.nextInt(16) + 8;
            n5 = this.j.nextInt(this.j.nextInt(120) + 8);
            n4 = n11 + this.j.nextInt(16) + 8;
            new nn(ly.B.bc).a(this.p, this.j, n6, n5, n4);
        }
        for (n7 = 0; n7 < 20; ++n7) {
            n6 = n10 + this.j.nextInt(16) + 8;
            n5 = this.j.nextInt(this.j.nextInt(this.j.nextInt(112) + 8) + 8);
            n4 = n11 + this.j.nextInt(16) + 8;
            new nn(ly.D.bc).a(this.p, this.j, n6, n5, n4);
        }
        for (n7 = n10 + 8 + 0; n7 < n10 + 8 + 16; ++n7) {
            for (n6 = n11 + 8 + 0; n6 < n11 + 8 + 16; ++n6) {
                n5 = this.p.d(n7, n6);
                if (!this.p.d || n5 <= 0 || n5 >= 128 || this.p.a(n7, n5, n6) != 0 || !this.p.f(n7, n5 - 1, n6).c() || this.p.f(n7, n5 - 1, n6) == gb.r) continue;
                this.p.d(n7, n5, n6, ly.aT.bc);
            }
        }
        dh.a = false;
    }

    public boolean a(boolean bl2, nu nu2) {
        return true;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return true;
    }
}

