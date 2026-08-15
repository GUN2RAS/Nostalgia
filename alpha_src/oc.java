/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;

public class oc
extends kh
implements gh {
    private ev[] h = new ev[36];
    public int a = 0;
    public int b = 0;
    public int c = 1;
    private boolean i = false;
    public int d;
    public int e;
    public double f;
    public double g;
    private static final int[][][] j = new int[][][]{new int[][]{{0, 0, -1}, {0, 0, 1}}, new int[][]{{-1, 0, 0}, {1, 0, 0}}, new int[][]{{-1, -1, 0}, {1, 0, 0}}, new int[][]{{-1, 0, 0}, {1, -1, 0}}, new int[][]{{0, 0, -1}, {0, -1, 1}}, new int[][]{{0, -1, -1}, {0, 0, 1}}, new int[][]{{0, 0, 1}, {1, 0, 0}}, new int[][]{{0, 0, 1}, {-1, 0, 0}}, new int[][]{{0, 0, -1}, {-1, 0, 0}}, new int[][]{{0, 0, -1}, {1, 0, 0}}};
    private int k;
    private double l;
    private double m;
    private double n;
    private double o;
    private double p;

    public oc(cn cn2) {
        super(cn2);
        this.ad = true;
        this.a(0.98f, 0.7f);
        this.aB = this.aD / 2.0f;
        this.aG = false;
    }

    public cf b_(kh kh2) {
        return kh2.au;
    }

    public cf f_() {
        return this.au;
    }

    public boolean d_() {
        return true;
    }

    public oc(cn cn2, double d2, double d3, double d4, int n2) {
        this(cn2);
        this.a(d2, d3 + (double)this.aB, d4);
        this.an = 0.0;
        this.ao = 0.0;
        this.ap = 0.0;
        this.ah = d2;
        this.ai = d3;
        this.aj = d4;
        this.d = n2;
    }

    public double h() {
        return (double)this.aD * 0.0 - (double)0.3f;
    }

    public boolean a(kh kh2, int n2) {
        this.c = -this.c;
        this.b = 10;
        this.a += n2 * 10;
        if (this.a > 40) {
            this.a(di.ax.aS, 1, 0.0f);
            if (this.d == 1) {
                this.a(ly.av.bc, 1, 0.0f);
            } else if (this.d == 2) {
                this.a(ly.aC.bc, 1, 0.0f);
            }
            this.F();
        }
        return true;
    }

    public boolean c_() {
        return !this.aA;
    }

    public void F() {
        for (int i2 = 0; i2 < this.c(); ++i2) {
            ev ev2 = this.c(i2);
            if (ev2 == null) continue;
            float f2 = this.aQ.nextFloat() * 0.8f + 0.1f;
            float f3 = this.aQ.nextFloat() * 0.8f + 0.1f;
            float f4 = this.aQ.nextFloat() * 0.8f + 0.1f;
            while (ev2.a > 0) {
                int n2 = this.aQ.nextInt(21) + 10;
                if (n2 > ev2.a) {
                    n2 = ev2.a;
                }
                ev2.a -= n2;
                dx dx2 = new dx(this.ag, this.ak + (double)f2, this.al + (double)f3, this.am + (double)f4, new ev(ev2.c, n2, ev2.d));
                float f5 = 0.05f;
                dx2.an = (float)this.aQ.nextGaussian() * f5;
                dx2.ao = (float)this.aQ.nextGaussian() * f5 + 0.2f;
                dx2.ap = (float)this.aQ.nextGaussian() * f5;
                this.ag.a(dx2);
            }
        }
        super.F();
    }

    public void e_() {
        double d2;
        int n2;
        int n3;
        if (this.ag.y) {
            if (this.k > 0) {
                double d3;
                double d4 = this.ak + (this.l - this.ak) / (double)this.k;
                double d5 = this.al + (this.m - this.al) / (double)this.k;
                double d6 = this.am + (this.n - this.am) / (double)this.k;
                for (d3 = this.o - (double)this.aq; d3 < -180.0; d3 += 360.0) {
                }
                while (d3 >= 180.0) {
                    d3 -= 360.0;
                }
                this.aq = (float)((double)this.aq + d3 / (double)this.k);
                this.ar = (float)((double)this.ar + (this.p - (double)this.ar) / (double)this.k);
                --this.k;
                this.a(d4, d5, d6);
                this.c(this.aq, this.ar);
            } else {
                this.a(this.ak, this.al, this.am);
                this.c(this.aq, this.ar);
            }
            return;
        }
        if (this.b > 0) {
            --this.b;
        }
        if (this.a > 0) {
            --this.a;
        }
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        this.ao -= (double)0.04f;
        int n4 = eo.b(this.ak);
        if (this.ag.a(n4, (n3 = eo.b(this.al)) - 1, n2 = eo.b(this.am)) == ly.aH.bc) {
            --n3;
        }
        double d7 = 0.4;
        boolean bl2 = false;
        double d8 = 0.0078125;
        if (this.ag.a(n4, n3, n2) == ly.aH.bc) {
            double d9;
            double d10;
            double d11;
            double d12;
            aj aj2 = this.g(this.ak, this.al, this.am);
            int n5 = this.ag.e(n4, n3, n2);
            this.al = n3;
            if (n5 >= 2 && n5 <= 5) {
                this.al = n3 + 1;
            }
            if (n5 == 2) {
                this.an -= d8;
            }
            if (n5 == 3) {
                this.an += d8;
            }
            if (n5 == 4) {
                this.ap += d8;
            }
            if (n5 == 5) {
                this.ap -= d8;
            }
            int[][] nArray = j[n5];
            double d13 = nArray[1][0] - nArray[0][0];
            double d14 = nArray[1][2] - nArray[0][2];
            double d15 = Math.sqrt(d13 * d13 + d14 * d14);
            double d16 = this.an * d13 + this.ap * d14;
            if (d16 < 0.0) {
                d13 = -d13;
                d14 = -d14;
            }
            double d17 = Math.sqrt(this.an * this.an + this.ap * this.ap);
            this.an = d17 * d13 / d15;
            this.ap = d17 * d14 / d15;
            double d18 = 0.0;
            double d19 = (double)n4 + 0.5 + (double)nArray[0][0] * 0.5;
            double d20 = (double)n2 + 0.5 + (double)nArray[0][2] * 0.5;
            double d21 = (double)n4 + 0.5 + (double)nArray[1][0] * 0.5;
            double d22 = (double)n2 + 0.5 + (double)nArray[1][2] * 0.5;
            d13 = d21 - d19;
            d14 = d22 - d20;
            if (d13 == 0.0) {
                this.ak = (double)n4 + 0.5;
                d18 = this.am - (double)n2;
            } else if (d14 == 0.0) {
                this.am = (double)n2 + 0.5;
                d18 = this.ak - (double)n4;
            } else {
                d12 = this.ak - d19;
                d11 = this.am - d20;
                d18 = d10 = (d12 * d13 + d11 * d14) * 2.0;
            }
            this.ak = d19 + d13 * d18;
            this.am = d20 + d14 * d18;
            this.a(this.ak, this.al + (double)this.aB, this.am);
            d12 = this.an;
            d11 = this.ap;
            if (this.ae != null) {
                d12 *= 0.75;
                d11 *= 0.75;
            }
            if (d12 < -d7) {
                d12 = -d7;
            }
            if (d12 > d7) {
                d12 = d7;
            }
            if (d11 < -d7) {
                d11 = -d7;
            }
            if (d11 > d7) {
                d11 = d7;
            }
            this.c(d12, 0.0, d11);
            if (nArray[0][1] != 0 && eo.b(this.ak) - n4 == nArray[0][0] && eo.b(this.am) - n2 == nArray[0][2]) {
                this.a(this.ak, this.al + (double)nArray[0][1], this.am);
            } else if (nArray[1][1] != 0 && eo.b(this.ak) - n4 == nArray[1][0] && eo.b(this.am) - n2 == nArray[1][2]) {
                this.a(this.ak, this.al + (double)nArray[1][1], this.am);
            }
            if (this.ae != null) {
                this.an *= (double)0.997f;
                this.ao *= 0.0;
                this.ap *= (double)0.997f;
            } else {
                if (this.d == 2) {
                    d10 = eo.a(this.f * this.f + this.g * this.g);
                    if (d10 > 0.01) {
                        bl2 = true;
                        this.f /= d10;
                        this.g /= d10;
                        double d23 = 0.04;
                        this.an *= (double)0.8f;
                        this.ao *= 0.0;
                        this.ap *= (double)0.8f;
                        this.an += this.f * d23;
                        this.ap += this.g * d23;
                    } else {
                        this.an *= (double)0.9f;
                        this.ao *= 0.0;
                        this.ap *= (double)0.9f;
                    }
                }
                this.an *= (double)0.96f;
                this.ao *= 0.0;
                this.ap *= (double)0.96f;
            }
            aj aj3 = this.g(this.ak, this.al, this.am);
            if (aj3 != null && aj2 != null) {
                double d24 = (aj2.b - aj3.b) * 0.05;
                d17 = Math.sqrt(this.an * this.an + this.ap * this.ap);
                if (d17 > 0.0) {
                    this.an = this.an / d17 * (d17 + d24);
                    this.ap = this.ap / d17 * (d17 + d24);
                }
                this.a(this.ak, aj3.b, this.am);
            }
            int n6 = eo.b(this.ak);
            int n7 = eo.b(this.am);
            if (n6 != n4 || n7 != n2) {
                d17 = Math.sqrt(this.an * this.an + this.ap * this.ap);
                this.an = d17 * (double)(n6 - n4);
                this.ap = d17 * (double)(n7 - n2);
            }
            if (this.d == 2 && (d9 = (double)eo.a(this.f * this.f + this.g * this.g)) > 0.01 && this.an * this.an + this.ap * this.ap > 0.001) {
                this.f /= d9;
                this.g /= d9;
                if (this.f * this.an + this.g * this.ap < 0.0) {
                    this.f = 0.0;
                    this.g = 0.0;
                } else {
                    this.f = this.an;
                    this.g = this.ap;
                }
            }
        } else {
            if (this.an < -d7) {
                this.an = -d7;
            }
            if (this.an > d7) {
                this.an = d7;
            }
            if (this.ap < -d7) {
                this.ap = -d7;
            }
            if (this.ap > d7) {
                this.ap = d7;
            }
            if (this.av) {
                this.an *= 0.5;
                this.ao *= 0.5;
                this.ap *= 0.5;
            }
            this.c(this.an, this.ao, this.ap);
            if (!this.av) {
                this.an *= (double)0.95f;
                this.ao *= (double)0.95f;
                this.ap *= (double)0.95f;
            }
        }
        this.ar = 0.0f;
        double d25 = this.ah - this.ak;
        double d26 = this.aj - this.am;
        if (d25 * d25 + d26 * d26 > 0.001) {
            this.aq = (float)(Math.atan2(d26, d25) * 180.0 / Math.PI);
            if (this.i) {
                this.aq += 180.0f;
            }
        }
        for (d2 = (double)(this.aq - this.as); d2 >= 180.0; d2 -= 360.0) {
        }
        while (d2 < -180.0) {
            d2 += 360.0;
        }
        if (d2 < -170.0 || d2 >= 170.0) {
            this.aq += 180.0f;
            this.i = !this.i;
        }
        this.c(this.aq, this.ar);
        List list = this.ag.b(this, this.au.b(0.2f, 0.0, 0.2f));
        if (list != null && list.size() > 0) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                kh kh2 = (kh)list.get(i2);
                if (kh2 == this.ae || !kh2.d_() || !(kh2 instanceof oc)) continue;
                kh2.f(this);
            }
        }
        if (this.ae != null && this.ae.aA) {
            this.ae = null;
        }
        if (bl2 && this.aQ.nextInt(4) == 0) {
            --this.e;
            if (this.e < 0) {
                this.g = 0.0;
                this.f = 0.0;
            }
            this.ag.a("largesmoke", this.ak, this.al + 0.8, this.am, 0.0, 0.0, 0.0);
        }
    }

    public aj a(double d2, double d3, double d4, double d5) {
        int n2;
        int n3;
        int n4 = eo.b(d2);
        if (this.ag.a(n4, (n3 = eo.b(d3)) - 1, n2 = eo.b(d4)) == ly.aH.bc) {
            --n3;
        }
        if (this.ag.a(n4, n3, n2) == ly.aH.bc) {
            int n5 = this.ag.e(n4, n3, n2);
            d3 = n3;
            if (n5 >= 2 && n5 <= 5) {
                d3 = n3 + 1;
            }
            int[][] nArray = j[n5];
            double d6 = nArray[1][0] - nArray[0][0];
            double d7 = nArray[1][2] - nArray[0][2];
            double d8 = Math.sqrt(d6 * d6 + d7 * d7);
            if (nArray[0][1] != 0 && eo.b(d2 += (d6 /= d8) * d5) - n4 == nArray[0][0] && eo.b(d4 += (d7 /= d8) * d5) - n2 == nArray[0][2]) {
                d3 += (double)nArray[0][1];
            } else if (nArray[1][1] != 0 && eo.b(d2) - n4 == nArray[1][0] && eo.b(d4) - n2 == nArray[1][2]) {
                d3 += (double)nArray[1][1];
            }
            return this.g(d2, d3, d4);
        }
        return null;
    }

    public aj g(double d2, double d3, double d4) {
        int n2;
        int n3;
        int n4 = eo.b(d2);
        if (this.ag.a(n4, (n3 = eo.b(d3)) - 1, n2 = eo.b(d4)) == ly.aH.bc) {
            --n3;
        }
        if (this.ag.a(n4, n3, n2) == ly.aH.bc) {
            int n5 = this.ag.e(n4, n3, n2);
            d3 = n3;
            if (n5 >= 2 && n5 <= 5) {
                d3 = n3 + 1;
            }
            int[][] nArray = j[n5];
            double d5 = 0.0;
            double d6 = (double)n4 + 0.5 + (double)nArray[0][0] * 0.5;
            double d7 = (double)n3 + 0.5 + (double)nArray[0][1] * 0.5;
            double d8 = (double)n2 + 0.5 + (double)nArray[0][2] * 0.5;
            double d9 = (double)n4 + 0.5 + (double)nArray[1][0] * 0.5;
            double d10 = (double)n3 + 0.5 + (double)nArray[1][1] * 0.5;
            double d11 = (double)n2 + 0.5 + (double)nArray[1][2] * 0.5;
            double d12 = d9 - d6;
            double d13 = (d10 - d7) * 2.0;
            double d14 = d11 - d8;
            if (d12 == 0.0) {
                d2 = (double)n4 + 0.5;
                d5 = d4 - (double)n2;
            } else if (d14 == 0.0) {
                d4 = (double)n2 + 0.5;
                d5 = d2 - (double)n4;
            } else {
                double d15;
                double d16 = d2 - d6;
                double d17 = d4 - d8;
                d5 = d15 = (d16 * d12 + d17 * d14) * 2.0;
            }
            d2 = d6 + d12 * d5;
            d3 = d7 + d13 * d5;
            d4 = d8 + d14 * d5;
            if (d13 < 0.0) {
                d3 += 1.0;
            }
            if (d13 > 0.0) {
                d3 += 0.5;
            }
            return aj.b(d2, d3, d4);
        }
        return null;
    }

    protected void a(hm hm2) {
        hm2.a("Type", this.d);
        if (this.d == 2) {
            hm2.a("PushX", this.f);
            hm2.a("PushZ", this.g);
            hm2.a("Fuel", (short)this.e);
        } else if (this.d == 1) {
            ki ki2 = new ki();
            for (int i2 = 0; i2 < this.h.length; ++i2) {
                if (this.h[i2] == null) continue;
                hm hm3 = new hm();
                hm3.a("Slot", (byte)i2);
                this.h[i2].a(hm3);
                ki2.a(hm3);
            }
            hm2.a("Items", ki2);
        }
    }

    protected void b(hm hm2) {
        this.d = hm2.e("Type");
        if (this.d == 2) {
            this.f = hm2.h("PushX");
            this.g = hm2.h("PushZ");
            this.e = hm2.d("Fuel");
        } else if (this.d == 1) {
            ki ki2 = hm2.l("Items");
            this.h = new ev[this.c()];
            for (int i2 = 0; i2 < ki2.c(); ++i2) {
                hm hm3 = (hm)ki2.a(i2);
                int n2 = hm3.c("Slot") & 0xFF;
                if (n2 < 0 || n2 >= this.h.length) continue;
                this.h[n2] = new ev(hm3);
            }
        }
    }

    public float h_() {
        return 0.0f;
    }

    public void f(kh kh2) {
        double d2;
        double d3;
        double d4;
        if (kh2 == this.ae) {
            return;
        }
        if (kh2 instanceof ge && !(kh2 instanceof dm) && this.d == 0 && this.an * this.an + this.ap * this.ap > 0.01 && this.ae == null && kh2.af == null) {
            kh2.g(this);
        }
        if ((d4 = (d3 = kh2.ak - this.ak) * d3 + (d2 = kh2.am - this.am) * d2) >= (double)1.0E-4f) {
            d4 = eo.a(d4);
            d3 /= d4;
            d2 /= d4;
            double d5 = 1.0 / d4;
            if (d5 > 1.0) {
                d5 = 1.0;
            }
            d3 *= d5;
            d2 *= d5;
            d3 *= (double)0.1f;
            d2 *= (double)0.1f;
            d3 *= (double)(1.0f - this.aO);
            d2 *= (double)(1.0f - this.aO);
            d3 *= 0.5;
            d2 *= 0.5;
            if (kh2 instanceof oc) {
                double d6 = kh2.an + this.an;
                double d7 = kh2.ap + this.ap;
                if (((oc)kh2).d == 2 && this.d != 2) {
                    this.an *= (double)0.2f;
                    this.ap *= (double)0.2f;
                    this.f(kh2.an - d3, 0.0, kh2.ap - d2);
                    kh2.an *= (double)0.7f;
                    kh2.ap *= (double)0.7f;
                } else if (((oc)kh2).d != 2 && this.d == 2) {
                    kh2.an *= (double)0.2f;
                    kh2.ap *= (double)0.2f;
                    kh2.f(this.an + d3, 0.0, this.ap + d2);
                    this.an *= (double)0.7f;
                    this.ap *= (double)0.7f;
                } else {
                    this.an *= (double)0.2f;
                    this.ap *= (double)0.2f;
                    this.f((d6 /= 2.0) - d3, 0.0, (d7 /= 2.0) - d2);
                    kh2.an *= (double)0.2f;
                    kh2.ap *= (double)0.2f;
                    kh2.f(d6 + d3, 0.0, d7 + d2);
                }
            } else {
                this.f(-d3, 0.0, -d2);
                kh2.f(d3 / 4.0, 0.0, d2 / 4.0);
            }
        }
    }

    public int c() {
        return 27;
    }

    public ev c(int n2) {
        return this.h[n2];
    }

    public ev a(int n2, int n3) {
        if (this.h[n2] != null) {
            if (this.h[n2].a <= n3) {
                ev ev2 = this.h[n2];
                this.h[n2] = null;
                return ev2;
            }
            ev ev3 = this.h[n2].a(n3);
            if (this.h[n2].a == 0) {
                this.h[n2] = null;
            }
            return ev3;
        }
        return null;
    }

    public void a(int n2, ev ev2) {
        this.h[n2] = ev2;
        if (ev2 != null && ev2.a > this.e()) {
            ev2.a = this.e();
        }
    }

    public String d() {
        return "Minecart";
    }

    public int e() {
        return 64;
    }

    public void j_() {
    }

    public boolean a(dm dm2) {
        if (this.d == 0) {
            dm2.g(this);
        } else if (this.d == 1) {
            dm2.a(this);
        } else if (this.d == 2) {
            ev ev2 = dm2.b.a();
            if (ev2 != null && ev2.c == di.k.aS) {
                if (--ev2.a == 0) {
                    dm2.b.a(dm2.b.d, null);
                }
                this.e += 1200;
            }
            this.f = this.ak - dm2.ak;
            this.g = this.am - dm2.am;
        }
        return true;
    }

    public void a(double d2, double d3, double d4, float f2, float f3, int n2) {
        this.l = d2;
        this.m = d3;
        this.n = d4;
        this.o = f2;
        this.p = f3;
        this.k = n2;
    }
}

