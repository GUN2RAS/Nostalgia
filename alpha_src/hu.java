/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class hu
extends ly {
    protected hu(int n2, int n3) {
        super(n2, n3, gb.n);
        this.b(true);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public int a() {
        return 20;
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        if (cn2.g(n2 - 1, n3, n4)) {
            return true;
        }
        if (cn2.g(n2 + 1, n3, n4)) {
            return true;
        }
        if (cn2.g(n2, n3, n4 - 1)) {
            return true;
        }
        return cn2.g(n2, n3, n4 + 1);
    }

    public void d(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        int n7 = n6 & 8;
        n6 &= 7;
        if (n5 == 2 && cn2.g(n2, n3, n4 + 1)) {
            n6 = 4;
        }
        if (n5 == 3 && cn2.g(n2, n3, n4 - 1)) {
            n6 = 3;
        }
        if (n5 == 4 && cn2.g(n2 + 1, n3, n4)) {
            n6 = 2;
        }
        if (n5 == 5 && cn2.g(n2 - 1, n3, n4)) {
            n6 = 1;
        }
        cn2.b(n2, n3, n4, n6 + n7);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        if (cn2.g(n2 - 1, n3, n4)) {
            cn2.b(n2, n3, n4, 1);
        } else if (cn2.g(n2 + 1, n3, n4)) {
            cn2.b(n2, n3, n4, 2);
        } else if (cn2.g(n2, n3, n4 - 1)) {
            cn2.b(n2, n3, n4, 3);
        } else if (cn2.g(n2, n3, n4 + 1)) {
            cn2.b(n2, n3, n4, 4);
        }
        this.h(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (this.h(cn2, n2, n3, n4)) {
            int n6 = cn2.e(n2, n3, n4) & 7;
            boolean bl2 = false;
            if (!cn2.g(n2 - 1, n3, n4) && n6 == 1) {
                bl2 = true;
            }
            if (!cn2.g(n2 + 1, n3, n4) && n6 == 2) {
                bl2 = true;
            }
            if (!cn2.g(n2, n3, n4 - 1) && n6 == 3) {
                bl2 = true;
            }
            if (!cn2.g(n2, n3, n4 + 1) && n6 == 4) {
                bl2 = true;
            }
            if (bl2) {
                this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
                cn2.d(n2, n3, n4, 0);
            }
        }
    }

    private boolean h(cn cn2, int n2, int n3, int n4) {
        if (!this.a(cn2, n2, n3, n4)) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
            return false;
        }
        return true;
    }

    public void a(nm nm2, int n2, int n3, int n4) {
        int n5 = nm2.e(n2, n3, n4);
        int n6 = n5 & 7;
        boolean bl2 = (n5 & 8) > 0;
        float f2 = 0.375f;
        float f3 = 0.625f;
        float f4 = 0.1875f;
        float f5 = 0.125f;
        if (bl2) {
            f5 = 0.0625f;
        }
        if (n6 == 1) {
            this.a(0.0f, f2, 0.5f - f4, f5, f3, 0.5f + f4);
        } else if (n6 == 2) {
            this.a(1.0f - f5, f2, 0.5f - f4, 1.0f, f3, 0.5f + f4);
        } else if (n6 == 3) {
            this.a(0.5f - f4, f2, 0.0f, 0.5f + f4, f3, f5);
        } else if (n6 == 4) {
            this.a(0.5f - f4, f2, 1.0f - f5, 0.5f + f4, f3, 1.0f);
        }
    }

    public void b(cn cn2, int n2, int n3, int n4, dm dm2) {
        this.a(cn2, n2, n3, n4, dm2);
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        int n5 = cn2.e(n2, n3, n4);
        int n6 = n5 & 7;
        int n7 = 8 - (n5 & 8);
        if (n7 == 0) {
            return true;
        }
        cn2.b(n2, n3, n4, n6 + n7);
        cn2.b(n2, n3, n4, n2, n3, n4);
        cn2.a((double)n2 + 0.5, (double)n3 + 0.5, (double)n4 + 0.5, "random.click", 0.3f, 0.6f);
        cn2.g(n2, n3, n4, this.bc);
        if (n6 == 1) {
            cn2.g(n2 - 1, n3, n4, this.bc);
        } else if (n6 == 2) {
            cn2.g(n2 + 1, n3, n4, this.bc);
        } else if (n6 == 3) {
            cn2.g(n2, n3, n4 - 1, this.bc);
        } else if (n6 == 4) {
            cn2.g(n2, n3, n4 + 1, this.bc);
        } else {
            cn2.g(n2, n3 - 1, n4, this.bc);
        }
        cn2.h(n2, n3, n4, this.bc);
        return true;
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        if ((n5 & 8) > 0) {
            cn2.g(n2, n3, n4, this.bc);
            int n6 = n5 & 7;
            if (n6 == 1) {
                cn2.g(n2 - 1, n3, n4, this.bc);
            } else if (n6 == 2) {
                cn2.g(n2 + 1, n3, n4, this.bc);
            } else if (n6 == 3) {
                cn2.g(n2, n3, n4 - 1, this.bc);
            } else if (n6 == 4) {
                cn2.g(n2, n3, n4 + 1, this.bc);
            } else {
                cn2.g(n2, n3 - 1, n4, this.bc);
            }
        }
        super.b(cn2, n2, n3, n4);
    }

    public boolean b(nm nm2, int n2, int n3, int n4, int n5) {
        return (nm2.e(n2, n3, n4) & 8) > 0;
    }

    public boolean c(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        if ((n6 & 8) == 0) {
            return false;
        }
        int n7 = n6 & 7;
        if (n7 == 5 && n5 == 1) {
            return true;
        }
        if (n7 == 4 && n5 == 2) {
            return true;
        }
        if (n7 == 3 && n5 == 3) {
            return true;
        }
        if (n7 == 2 && n5 == 4) {
            return true;
        }
        return n7 == 1 && n5 == 5;
    }

    public boolean d() {
        return true;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        int n5 = cn2.e(n2, n3, n4);
        if ((n5 & 8) == 0) {
            return;
        }
        cn2.b(n2, n3, n4, n5 & 7);
        cn2.g(n2, n3, n4, this.bc);
        int n6 = n5 & 7;
        if (n6 == 1) {
            cn2.g(n2 - 1, n3, n4, this.bc);
        } else if (n6 == 2) {
            cn2.g(n2 + 1, n3, n4, this.bc);
        } else if (n6 == 3) {
            cn2.g(n2, n3, n4 - 1, this.bc);
        } else if (n6 == 4) {
            cn2.g(n2, n3, n4 + 1, this.bc);
        } else {
            cn2.g(n2, n3 - 1, n4, this.bc);
        }
        cn2.a((double)n2 + 0.5, (double)n3 + 0.5, (double)n4 + 0.5, "random.click", 0.3f, 0.5f);
        cn2.b(n2, n3, n4, n2, n3, n4);
    }

    public void e() {
        float f2 = 0.1875f;
        float f3 = 0.125f;
        float f4 = 0.125f;
        this.a(0.5f - f2, 0.5f - f3, 0.5f - f4, 0.5f + f2, 0.5f + f3, 0.5f + f4);
    }
}

