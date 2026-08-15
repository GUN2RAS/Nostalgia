/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class fw
extends ly {
    protected fw(int n2, gb gb2) {
        super(n2, gb2);
        this.bb = 97;
        if (gb2 == gb.e) {
            ++this.bb;
        }
        float f2 = 0.5f;
        float f3 = 1.0f;
        this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, f3, 0.5f + f2);
    }

    public int a(int n2, int n3) {
        if (n2 == 0 || n2 == 1) {
            return this.bb;
        }
        int n4 = this.c(n3);
        if ((n4 == 0 || n4 == 2) ^ n2 <= 3) {
            return this.bb;
        }
        int n5 = n4 / 2 + (n2 & 1 ^ n4);
        int n6 = this.bb - (n3 & 8) * 2;
        if (((n5 += (n3 & 4) / 4) & 1) != 0) {
            n6 = -n6;
        }
        return n6;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 7;
    }

    public cf f(cn cn2, int n2, int n3, int n4) {
        this.a((nm)cn2, n2, n3, n4);
        return super.f(cn2, n2, n3, n4);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        this.a((nm)cn2, n2, n3, n4);
        return super.d(cn2, n2, n3, n4);
    }

    public void a(nm nm2, int n2, int n3, int n4) {
        this.b(this.c(nm2.e(n2, n3, n4)));
    }

    public void b(int n2) {
        float f2 = 0.1875f;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
        if (n2 == 0) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f2);
        }
        if (n2 == 1) {
            this.a(1.0f - f2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        if (n2 == 2) {
            this.a(0.0f, 0.0f, 1.0f - f2, 1.0f, 1.0f, 1.0f);
        }
        if (n2 == 3) {
            this.a(0.0f, 0.0f, 0.0f, f2, 1.0f, 1.0f);
        }
    }

    public void b(cn cn2, int n2, int n3, int n4, dm dm2) {
        this.a(cn2, n2, n3, n4, dm2);
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        if (this.bn == gb.e) {
            return true;
        }
        int n5 = cn2.e(n2, n3, n4);
        if ((n5 & 8) != 0) {
            if (cn2.a(n2, n3 - 1, n4) == this.bc) {
                this.a(cn2, n2, n3 - 1, n4, dm2);
            }
            return true;
        }
        if (cn2.a(n2, n3 + 1, n4) == this.bc) {
            cn2.b(n2, n3 + 1, n4, (n5 ^ 4) + 8);
        }
        cn2.b(n2, n3, n4, n5 ^ 4);
        cn2.b(n2, n3 - 1, n4, n2, n3, n4);
        if (Math.random() < 0.5) {
            cn2.a((double)n2 + 0.5, (double)n3 + 0.5, (double)n4 + 0.5, "random.door_open", 1.0f, cn2.n.nextFloat() * 0.1f + 0.9f);
        } else {
            cn2.a((double)n2 + 0.5, (double)n3 + 0.5, (double)n4 + 0.5, "random.door_close", 1.0f, cn2.n.nextFloat() * 0.1f + 0.9f);
        }
        return true;
    }

    public void a(cn cn2, int n2, int n3, int n4, boolean bl2) {
        boolean bl3;
        int n5 = cn2.e(n2, n3, n4);
        if ((n5 & 8) != 0) {
            if (cn2.a(n2, n3 - 1, n4) == this.bc) {
                this.a(cn2, n2, n3 - 1, n4, bl2);
            }
            return;
        }
        boolean bl4 = bl3 = (cn2.e(n2, n3, n4) & 4) > 0;
        if (bl3 == bl2) {
            return;
        }
        if (cn2.a(n2, n3 + 1, n4) == this.bc) {
            cn2.b(n2, n3 + 1, n4, (n5 ^ 4) + 8);
        }
        cn2.b(n2, n3, n4, n5 ^ 4);
        cn2.b(n2, n3 - 1, n4, n2, n3, n4);
        if (Math.random() < 0.5) {
            cn2.a((double)n2 + 0.5, (double)n3 + 0.5, (double)n4 + 0.5, "random.door_open", 1.0f, cn2.n.nextFloat() * 0.1f + 0.9f);
        } else {
            cn2.a((double)n2 + 0.5, (double)n3 + 0.5, (double)n4 + 0.5, "random.door_close", 1.0f, cn2.n.nextFloat() * 0.1f + 0.9f);
        }
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        if ((n6 & 8) != 0) {
            if (cn2.a(n2, n3 - 1, n4) != this.bc) {
                cn2.d(n2, n3, n4, 0);
            }
            if (n5 > 0 && ly.n[n5].d()) {
                this.a(cn2, n2, n3 - 1, n4, n5);
            }
        } else {
            boolean bl2 = false;
            if (cn2.a(n2, n3 + 1, n4) != this.bc) {
                cn2.d(n2, n3, n4, 0);
                bl2 = true;
            }
            if (!cn2.g(n2, n3 - 1, n4)) {
                cn2.d(n2, n3, n4, 0);
                bl2 = true;
                if (cn2.a(n2, n3 + 1, n4) == this.bc) {
                    cn2.d(n2, n3 + 1, n4, 0);
                }
            }
            if (bl2) {
                this.b_(cn2, n2, n3, n4, n6);
            } else if (n5 > 0 && ly.n[n5].d()) {
                boolean bl3 = cn2.o(n2, n3, n4) || cn2.o(n2, n3 + 1, n4);
                this.a(cn2, n2, n3, n4, bl3);
            }
        }
    }

    public int a(int n2, Random random) {
        if ((n2 & 8) != 0) {
            return 0;
        }
        if (this.bn == gb.e) {
            return di.az.aS;
        }
        return di.at.aS;
    }

    public mf a(cn cn2, int n2, int n3, int n4, aj aj2, aj aj3) {
        this.a((nm)cn2, n2, n3, n4);
        return super.a(cn2, n2, n3, n4, aj2, aj3);
    }

    public int c(int n2) {
        if ((n2 & 4) == 0) {
            return n2 - 1 & 3;
        }
        return n2 & 3;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        if (n3 >= 127) {
            return false;
        }
        return cn2.g(n2, n3 - 1, n4) && super.a(cn2, n2, n3, n4) && super.a(cn2, n2, n3 + 1, n4);
    }
}

