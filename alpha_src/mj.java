/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class mj
extends ly {
    protected mj(int n2, int n3) {
        super(n2, n3, gb.n);
        this.b(true);
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
        return 2;
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
        if (cn2.g(n2, n3, n4 + 1)) {
            return true;
        }
        return cn2.g(n2, n3 - 1, n4);
    }

    public void d(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        if (n5 == 1 && cn2.g(n2, n3 - 1, n4)) {
            n6 = 5;
        }
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
        cn2.b(n2, n3, n4, n6);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        super.a(cn2, n2, n3, n4, random);
        if (cn2.e(n2, n3, n4) == 0) {
            this.e(cn2, n2, n3, n4);
        }
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
        } else if (cn2.g(n2, n3 - 1, n4)) {
            cn2.b(n2, n3, n4, 5);
        }
        this.h(cn2, n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (this.h(cn2, n2, n3, n4)) {
            int n6 = cn2.e(n2, n3, n4);
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
            if (!cn2.g(n2, n3 - 1, n4) && n6 == 5) {
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

    public mf a(cn cn2, int n2, int n3, int n4, aj aj2, aj aj3) {
        int n5 = cn2.e(n2, n3, n4) & 7;
        float f2 = 0.15f;
        if (n5 == 1) {
            this.a(0.0f, 0.2f, 0.5f - f2, f2 * 2.0f, 0.8f, 0.5f + f2);
        } else if (n5 == 2) {
            this.a(1.0f - f2 * 2.0f, 0.2f, 0.5f - f2, 1.0f, 0.8f, 0.5f + f2);
        } else if (n5 == 3) {
            this.a(0.5f - f2, 0.2f, 0.0f, 0.5f + f2, 0.8f, f2 * 2.0f);
        } else if (n5 == 4) {
            this.a(0.5f - f2, 0.2f, 1.0f - f2 * 2.0f, 0.5f + f2, 0.8f, 1.0f);
        } else {
            f2 = 0.1f;
            this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, 0.6f, 0.5f + f2);
        }
        return super.a(cn2, n2, n3, n4, aj2, aj3);
    }

    public void b(cn cn2, int n2, int n3, int n4, Random random) {
        int n5 = cn2.e(n2, n3, n4);
        double d2 = (float)n2 + 0.5f;
        double d3 = (float)n3 + 0.7f;
        double d4 = (float)n4 + 0.5f;
        double d5 = 0.22f;
        double d6 = 0.27f;
        if (n5 == 1) {
            cn2.a("smoke", d2 - d6, d3 + d5, d4, 0.0, 0.0, 0.0);
            cn2.a("flame", d2 - d6, d3 + d5, d4, 0.0, 0.0, 0.0);
        } else if (n5 == 2) {
            cn2.a("smoke", d2 + d6, d3 + d5, d4, 0.0, 0.0, 0.0);
            cn2.a("flame", d2 + d6, d3 + d5, d4, 0.0, 0.0, 0.0);
        } else if (n5 == 3) {
            cn2.a("smoke", d2, d3 + d5, d4 - d6, 0.0, 0.0, 0.0);
            cn2.a("flame", d2, d3 + d5, d4 - d6, 0.0, 0.0, 0.0);
        } else if (n5 == 4) {
            cn2.a("smoke", d2, d3 + d5, d4 + d6, 0.0, 0.0, 0.0);
            cn2.a("flame", d2, d3 + d5, d4 + d6, 0.0, 0.0, 0.0);
        } else {
            cn2.a("smoke", d2, d3, d4, 0.0, 0.0, 0.0);
            cn2.a("flame", d2, d3, d4, 0.0, 0.0, 0.0);
        }
    }
}

