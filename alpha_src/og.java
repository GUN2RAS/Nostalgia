/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class og
extends ly {
    private int[] a = new int[256];
    private int[] b = new int[256];

    protected og(int n2, int n3) {
        super(n2, n3, gb.l);
        this.a(ly.y.bc, 5, 20);
        this.a(ly.K.bc, 5, 5);
        this.a(ly.L.bc, 30, 60);
        this.a(ly.ao.bc, 30, 20);
        this.a(ly.an.bc, 15, 100);
        this.a(ly.ac.bc, 30, 60);
        this.b(true);
    }

    private void a(int n2, int n3, int n4) {
        this.a[n2] = n3;
        this.b[n2] = n4;
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 3;
    }

    public int a(Random random) {
        return 0;
    }

    public int a() {
        return 10;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        int n5 = cn2.e(n2, n3, n4);
        if (n5 < 15) {
            cn2.b(n2, n3, n4, n5 + 1);
            cn2.h(n2, n3, n4, this.bc);
        }
        if (!this.h(cn2, n2, n3, n4)) {
            if (!cn2.g(n2, n3 - 1, n4) || n5 > 3) {
                cn2.d(n2, n3, n4, 0);
            }
            return;
        }
        if (!this.b((nm)cn2, n2, n3 - 1, n4) && n5 == 15 && random.nextInt(4) == 0) {
            cn2.d(n2, n3, n4, 0);
            return;
        }
        if (n5 % 2 == 0 && n5 > 2) {
            this.a(cn2, n2 + 1, n3, n4, 300, random);
            this.a(cn2, n2 - 1, n3, n4, 300, random);
            this.a(cn2, n2, n3 - 1, n4, 200, random);
            this.a(cn2, n2, n3 + 1, n4, 250, random);
            this.a(cn2, n2, n3, n4 - 1, 300, random);
            this.a(cn2, n2, n3, n4 + 1, 300, random);
            for (int i2 = n2 - 1; i2 <= n2 + 1; ++i2) {
                for (int i3 = n4 - 1; i3 <= n4 + 1; ++i3) {
                    for (int i4 = n3 - 1; i4 <= n3 + 4; ++i4) {
                        int n6;
                        if (i2 == n2 && i4 == n3 && i3 == n4) continue;
                        int n7 = 100;
                        if (i4 > n3 + 1) {
                            n7 += (i4 - (n3 + 1)) * 100;
                        }
                        if ((n6 = this.i(cn2, i2, i4, i3)) <= 0 || random.nextInt(n7) > n6) continue;
                        cn2.d(i2, i4, i3, this.bc);
                    }
                }
            }
        }
    }

    private void a(cn cn2, int n2, int n3, int n4, int n5, Random random) {
        int n6 = this.b[cn2.a(n2, n3, n4)];
        if (random.nextInt(n5) < n6) {
            boolean bl2;
            boolean bl3 = bl2 = cn2.a(n2, n3, n4) == ly.an.bc;
            if (random.nextInt(2) == 0) {
                cn2.d(n2, n3, n4, this.bc);
            } else {
                cn2.d(n2, n3, n4, 0);
            }
            if (bl2) {
                ly.an.b(cn2, n2, n3, n4, 0);
            }
        }
    }

    private boolean h(cn cn2, int n2, int n3, int n4) {
        if (this.b((nm)cn2, n2 + 1, n3, n4)) {
            return true;
        }
        if (this.b((nm)cn2, n2 - 1, n3, n4)) {
            return true;
        }
        if (this.b((nm)cn2, n2, n3 - 1, n4)) {
            return true;
        }
        if (this.b((nm)cn2, n2, n3 + 1, n4)) {
            return true;
        }
        if (this.b((nm)cn2, n2, n3, n4 - 1)) {
            return true;
        }
        return this.b((nm)cn2, n2, n3, n4 + 1);
    }

    private int i(cn cn2, int n2, int n3, int n4) {
        int n5 = 0;
        if (cn2.a(n2, n3, n4) != 0) {
            return 0;
        }
        n5 = this.g(cn2, n2 + 1, n3, n4, n5);
        n5 = this.g(cn2, n2 - 1, n3, n4, n5);
        n5 = this.g(cn2, n2, n3 - 1, n4, n5);
        n5 = this.g(cn2, n2, n3 + 1, n4, n5);
        n5 = this.g(cn2, n2, n3, n4 - 1, n5);
        n5 = this.g(cn2, n2, n3, n4 + 1, n5);
        return n5;
    }

    public boolean h() {
        return false;
    }

    public boolean b(nm nm2, int n2, int n3, int n4) {
        return this.a[nm2.a(n2, n3, n4)] > 0;
    }

    public int g(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = this.a[cn2.a(n2, n3, n4)];
        if (n6 > n5) {
            return n6;
        }
        return n5;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        return cn2.g(n2, n3 - 1, n4) || this.h(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (!cn2.g(n2, n3 - 1, n4) && !this.h(cn2, n2, n3, n4)) {
            cn2.d(n2, n3, n4, 0);
            return;
        }
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        if (!cn2.g(n2, n3 - 1, n4) && !this.h(cn2, n2, n3, n4)) {
            cn2.d(n2, n3, n4, 0);
            return;
        }
        cn2.h(n2, n3, n4, this.bc);
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        block12: {
            float f2;
            float f3;
            float f4;
            int n5;
            block11: {
                if (random.nextInt(24) == 0) {
                    cn2.a((float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, "fire.fire", 1.0f + random.nextFloat(), random.nextFloat() * 0.7f + 0.3f);
                }
                if (!cn2.g(n2, n3 - 1, n4) && !ly.as.b((nm)cn2, n2, n3 - 1, n4)) break block11;
                for (int i2 = 0; i2 < 3; ++i2) {
                    float f5 = (float)n2 + random.nextFloat();
                    float f6 = (float)n3 + random.nextFloat() * 0.5f + 0.5f;
                    float f7 = (float)n4 + random.nextFloat();
                    cn2.a("largesmoke", f5, f6, f7, 0.0, 0.0, 0.0);
                }
                break block12;
            }
            if (ly.as.b((nm)cn2, n2 - 1, n3, n4)) {
                for (n5 = 0; n5 < 2; ++n5) {
                    f4 = (float)n2 + random.nextFloat() * 0.1f;
                    f3 = (float)n3 + random.nextFloat();
                    f2 = (float)n4 + random.nextFloat();
                    cn2.a("largesmoke", f4, f3, f2, 0.0, 0.0, 0.0);
                }
            }
            if (ly.as.b((nm)cn2, n2 + 1, n3, n4)) {
                for (n5 = 0; n5 < 2; ++n5) {
                    f4 = (float)(n2 + 1) - random.nextFloat() * 0.1f;
                    f3 = (float)n3 + random.nextFloat();
                    f2 = (float)n4 + random.nextFloat();
                    cn2.a("largesmoke", f4, f3, f2, 0.0, 0.0, 0.0);
                }
            }
            if (ly.as.b((nm)cn2, n2, n3, n4 - 1)) {
                for (n5 = 0; n5 < 2; ++n5) {
                    f4 = (float)n2 + random.nextFloat();
                    f3 = (float)n3 + random.nextFloat();
                    f2 = (float)n4 + random.nextFloat() * 0.1f;
                    cn2.a("largesmoke", f4, f3, f2, 0.0, 0.0, 0.0);
                }
            }
            if (ly.as.b((nm)cn2, n2, n3, n4 + 1)) {
                for (n5 = 0; n5 < 2; ++n5) {
                    f4 = (float)n2 + random.nextFloat();
                    f3 = (float)n3 + random.nextFloat();
                    f2 = (float)(n4 + 1) - random.nextFloat() * 0.1f;
                    cn2.a("largesmoke", f4, f3, f2, 0.0, 0.0, 0.0);
                }
            }
            if (!ly.as.b((nm)cn2, n2, n3 + 1, n4)) break block12;
            for (n5 = 0; n5 < 2; ++n5) {
                f4 = (float)n2 + random.nextFloat();
                f3 = (float)(n3 + 1) - random.nextFloat() * 0.1f;
                f2 = (float)n4 + random.nextFloat();
                cn2.a("largesmoke", f4, f3, f2, 0.0, 0.0, 0.0);
            }
        }
    }
}

