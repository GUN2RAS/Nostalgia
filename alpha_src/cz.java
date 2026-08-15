/*
 * Decompiled with CFR 0.152.
 */
public class cz {
    private nm a;
    private fx b = new fx();
    private fi c = new fi();
    private a[] d = new a[32];

    public cz(nm nm2) {
        this.a = nm2;
    }

    public bl a(kh kh2, kh kh3, float f2) {
        return this.a(kh2, kh3.ak, kh3.au.b, kh3.am, f2);
    }

    public bl a(kh kh2, int n2, int n3, int n4, float f2) {
        return this.a(kh2, (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, f2);
    }

    private bl a(kh kh2, double d2, double d3, double d4, float f2) {
        this.b.a();
        this.c.a();
        a a2 = this.a(eo.b(kh2.au.a), eo.b(kh2.au.b), eo.b(kh2.au.c));
        a a3 = this.a(eo.b(d2 - (double)(kh2.aC / 2.0f)), eo.b(d3), eo.b(d4 - (double)(kh2.aC / 2.0f)));
        a a4 = new a(eo.d(kh2.aC + 1.0f), eo.d(kh2.aD + 1.0f), eo.d(kh2.aC + 1.0f));
        bl bl2 = this.a(kh2, a2, a3, a4, f2);
        return bl2;
    }

    private bl a(kh kh2, a a2, a a3, a a4, float f2) {
        a2.f = 0.0f;
        a2.h = a2.g = a2.a(a3);
        this.b.a();
        this.b.a(a2);
        a a5 = a2;
        while (!this.b.c()) {
            a a6 = this.b.b();
            if (a6.d == a3.d) {
                return this.a(a2, a3);
            }
            if (a6.a(a3) < a5.a(a3)) {
                a5 = a6;
            }
            a6.j = true;
            int n2 = this.b(kh2, a6, a4, a3, f2);
            for (int i2 = 0; i2 < n2; ++i2) {
                a a7 = this.d[i2];
                float f3 = a6.f + a6.a(a7);
                if (a7.a() && !(f3 < a7.f)) continue;
                a7.i = a6;
                a7.f = f3;
                a7.g = a7.a(a3);
                if (a7.a()) {
                    this.b.a(a7, a7.f + a7.g);
                    continue;
                }
                a7.h = a7.f + a7.g;
                this.b.a(a7);
            }
        }
        if (a5 == a2) {
            return null;
        }
        return this.a(a2, a5);
    }

    private int b(kh kh2, a a2, a a3, a a4, float f2) {
        int n2 = 0;
        int n3 = 0;
        if (this.a(kh2, a2.a, a2.b + 1, a2.c, a3) > 0) {
            n3 = 1;
        }
        a a5 = this.a(kh2, a2.a, a2.b, a2.c + 1, a3, n3);
        a a6 = this.a(kh2, a2.a - 1, a2.b, a2.c, a3, n3);
        a a7 = this.a(kh2, a2.a + 1, a2.b, a2.c, a3, n3);
        a a8 = this.a(kh2, a2.a, a2.b, a2.c - 1, a3, n3);
        if (a5 != null && !a5.j && a5.a(a4) < f2) {
            this.d[n2++] = a5;
        }
        if (a6 != null && !a6.j && a6.a(a4) < f2) {
            this.d[n2++] = a6;
        }
        if (a7 != null && !a7.j && a7.a(a4) < f2) {
            this.d[n2++] = a7;
        }
        if (a8 != null && !a8.j && a8.a(a4) < f2) {
            this.d[n2++] = a8;
        }
        return n2;
    }

    private a a(kh kh2, int n2, int n3, int n4, a a2, int n5) {
        a a3 = null;
        if (this.a(kh2, n2, n3, n4, a2) > 0) {
            a3 = this.a(n2, n3, n4);
        }
        if (a3 == null && this.a(kh2, n2, n3 + n5, n4, a2) > 0) {
            a3 = this.a(n2, n3 + n5, n4);
            n3 += n5;
        }
        if (a3 != null) {
            int n6 = 0;
            int n7 = 0;
            while (n3 > 0 && (n7 = this.a(kh2, n2, n3 - 1, n4, a2)) > 0) {
                if (n7 < 0) {
                    return null;
                }
                if (++n6 >= 4) {
                    return null;
                }
                --n3;
            }
            if (n3 > 0) {
                a3 = this.a(n2, n3, n4);
            }
        }
        return a3;
    }

    private final a a(int n2, int n3, int n4) {
        int n5 = n2 | n3 << 10 | n4 << 20;
        a a2 = (a)this.c.a(n5);
        if (a2 == null) {
            a2 = new a(n2, n3, n4);
            this.c.a(n5, a2);
        }
        return a2;
    }

    private int a(kh kh2, int n2, int n3, int n4, a a2) {
        for (int i2 = n2; i2 < n2 + a2.a; ++i2) {
            for (int i3 = n3; i3 < n3 + a2.b; ++i3) {
                for (int i4 = n4; i4 < n4 + a2.c; ++i4) {
                    gb gb2 = this.a.f(n2, n3, n4);
                    if (gb2.c()) {
                        return 0;
                    }
                    if (gb2 != gb.f && gb2 != gb.g) continue;
                    return -1;
                }
            }
        }
        return 1;
    }

    private bl a(a a2, a a3) {
        int n2 = 1;
        a a4 = a3;
        while (a4.i != null) {
            ++n2;
            a4 = a4.i;
        }
        a[] aArray = new a[n2];
        a4 = a3;
        aArray[--n2] = a4;
        while (a4.i != null) {
            a4 = a4.i;
            aArray[--n2] = a4;
        }
        return new bl(aArray);
    }
}

