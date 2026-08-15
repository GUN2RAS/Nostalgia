/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public abstract class jp
extends ly {
    protected int d = 1;

    protected jp(int n2, gb gb2) {
        super(n2, (gb2 == gb.g ? 14 : 12) * 16 + 13, gb2);
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (gb2 == gb.g) {
            this.d = 2;
        }
        this.a(0.0f + f3, 0.0f + f2, 0.0f + f3, 1.0f + f3, 1.0f + f2, 1.0f + f3);
        this.b(true);
    }

    public static float b(int n2) {
        if (n2 >= 8) {
            n2 = 0;
        }
        float f2 = (float)(n2 + 1) / 9.0f;
        return f2;
    }

    public int a(int n2) {
        if (n2 == 0 || n2 == 1) {
            return this.bb;
        }
        return this.bb + 1;
    }

    protected int h(cn cn2, int n2, int n3, int n4) {
        if (cn2.f(n2, n3, n4) != this.bn) {
            return -1;
        }
        return cn2.e(n2, n3, n4);
    }

    protected int b(nm nm2, int n2, int n3, int n4) {
        if (nm2.f(n2, n3, n4) != this.bn) {
            return -1;
        }
        int n5 = nm2.e(n2, n3, n4);
        if (n5 >= 8) {
            n5 = 0;
        }
        return n5;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean a(int n2, boolean bl2) {
        return bl2 && n2 == 0;
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        gb gb2 = nm2.f(n2, n3, n4);
        if (gb2 == this.bn) {
            return false;
        }
        if (gb2 == gb.r) {
            return false;
        }
        if (n5 == 1) {
            return true;
        }
        return super.c(nm2, n2, n3, n4, n5);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public int f() {
        return 4;
    }

    public int a(int n2, Random random) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }

    private aj e(nm nm2, int n2, int n3, int n4) {
        int n5;
        aj aj2 = aj.b(0.0, 0.0, 0.0);
        int n6 = this.b(nm2, n2, n3, n4);
        for (n5 = 0; n5 < 4; ++n5) {
            int n7;
            int n8;
            int n9 = n2;
            int n10 = n3;
            int n11 = n4;
            if (n5 == 0) {
                --n9;
            }
            if (n5 == 1) {
                --n11;
            }
            if (n5 == 2) {
                ++n9;
            }
            if (n5 == 3) {
                ++n11;
            }
            if ((n8 = this.b(nm2, n9, n10, n11)) < 0) {
                if (nm2.f(n9, n10, n11).c() || (n8 = this.b(nm2, n9, n10 - 1, n11)) < 0) continue;
                n7 = n8 - (n6 - 8);
                aj2 = aj2.c((n9 - n2) * n7, (n10 - n3) * n7, (n11 - n4) * n7);
                continue;
            }
            if (n8 < 0) continue;
            n7 = n8 - n6;
            aj2 = aj2.c((n9 - n2) * n7, (n10 - n3) * n7, (n11 - n4) * n7);
        }
        if (nm2.e(n2, n3, n4) >= 8) {
            n5 = 0;
            if (n5 != 0 || this.c(nm2, n2, n3, n4 - 1, 2)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2, n3, n4 + 1, 3)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2 - 1, n3, n4, 4)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2 + 1, n3, n4, 5)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2, n3 + 1, n4 - 1, 2)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2, n3 + 1, n4 + 1, 3)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2 - 1, n3 + 1, n4, 4)) {
                n5 = 1;
            }
            if (n5 != 0 || this.c(nm2, n2 + 1, n3 + 1, n4, 5)) {
                n5 = 1;
            }
            if (n5 != 0) {
                aj2 = aj2.b().c(0.0, -6.0, 0.0);
            }
        }
        aj2 = aj2.b();
        return aj2;
    }

    public void a(cn cn2, int n2, int n3, int n4, kh kh2, aj aj2) {
        aj aj3 = this.e((nm)cn2, n2, n3, n4);
        aj2.a += aj3.a;
        aj2.b += aj3.b;
        aj2.c += aj3.c;
    }

    public int a() {
        if (this.bn == gb.f) {
            return 5;
        }
        if (this.bn == gb.g) {
            return 30;
        }
        return 0;
    }

    public float c(nm nm2, int n2, int n3, int n4) {
        float f2;
        float f3 = nm2.c(n2, n3, n4);
        return f3 > (f2 = nm2.c(n2, n3 + 1, n4)) ? f3 : f2;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        super.a(cn2, n2, n3, n4, random);
    }

    public int g() {
        return this.bn == gb.f ? 1 : 0;
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        int n5;
        if (this.bn == gb.f && random.nextInt(64) == 0 && (n5 = cn2.e(n2, n3, n4)) > 0 && n5 < 8) {
            cn2.a((float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, "liquid.water", random.nextFloat() * 0.25f + 0.75f, random.nextFloat() * 1.0f + 0.5f);
        }
        if (this.bn == gb.g && cn2.f(n2, n3 + 1, n4) == gb.a && !cn2.g(n2, n3 + 1, n4) && random.nextInt(100) == 0) {
            double d2 = (float)n2 + random.nextFloat();
            double d3 = (double)n3 + this.bj;
            double d4 = (float)n4 + random.nextFloat();
            cn2.a("lava", d2, d3, d4, 0.0, 0.0, 0.0);
        }
    }

    public static double a(nm nm2, int n2, int n3, int n4, gb gb2) {
        aj aj2 = null;
        if (gb2 == gb.f) {
            aj2 = ((jp)ly.B).e(nm2, n2, n3, n4);
        }
        if (gb2 == gb.g) {
            aj2 = ((jp)ly.D).e(nm2, n2, n3, n4);
        }
        if (aj2.a == 0.0 && aj2.c == 0.0) {
            return -1000.0;
        }
        return Math.atan2(aj2.c, aj2.a) - 1.5707963267948966;
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        this.j(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        this.j(cn2, n2, n3, n4);
    }

    private void j(cn cn2, int n2, int n3, int n4) {
        if (cn2.a(n2, n3, n4) != this.bc) {
            return;
        }
        if (this.bn == gb.g) {
            boolean bl2 = false;
            if (bl2 || cn2.f(n2, n3, n4 - 1) == gb.f) {
                bl2 = true;
            }
            if (bl2 || cn2.f(n2, n3, n4 + 1) == gb.f) {
                bl2 = true;
            }
            if (bl2 || cn2.f(n2 - 1, n3, n4) == gb.f) {
                bl2 = true;
            }
            if (bl2 || cn2.f(n2 + 1, n3, n4) == gb.f) {
                bl2 = true;
            }
            if (bl2 || cn2.f(n2, n3 + 1, n4) == gb.f) {
                bl2 = true;
            }
            if (bl2) {
                int n5 = cn2.e(n2, n3, n4);
                if (n5 == 0) {
                    cn2.d(n2, n3, n4, ly.aq.bc);
                } else if (n5 <= 4) {
                    cn2.d(n2, n3, n4, ly.x.bc);
                }
                this.i(cn2, n2, n3, n4);
            }
        }
    }

    protected void i(cn cn2, int n2, int n3, int n4) {
        cn2.a((float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, "random.fizz", 0.5f, 2.6f + (cn2.n.nextFloat() - cn2.n.nextFloat()) * 0.8f);
        for (int i2 = 0; i2 < 8; ++i2) {
            cn2.a("largesmoke", (double)n2 + Math.random(), (double)n3 + 1.2, (double)n4 + Math.random(), 0.0, 0.0, 0.0);
        }
    }
}

