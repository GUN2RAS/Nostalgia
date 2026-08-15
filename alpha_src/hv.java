/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class hv
extends jp {
    int a = 0;
    boolean[] b = new boolean[4];
    int[] c = new int[4];

    protected hv(int n2, gb gb2) {
        super(n2, gb2);
    }

    private void j(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        cn2.a(n2, n3, n4, this.bc + 1, n5);
        cn2.b(n2, n3, n4, n2, n3, n4);
        cn2.h(n2, n3, n4);
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        int n5;
        int n6 = this.h(cn2, n2, n3, n4);
        boolean bl2 = true;
        if (n6 > 0) {
            int n7 = -100;
            this.a = 0;
            n7 = this.f(cn2, n2 - 1, n3, n4, n7);
            n7 = this.f(cn2, n2 + 1, n3, n4, n7);
            n7 = this.f(cn2, n2, n3, n4 - 1, n7);
            n5 = (n7 = this.f(cn2, n2, n3, n4 + 1, n7)) + this.d;
            if (n5 >= 8 || n7 < 0) {
                n5 = -1;
            }
            if (this.h(cn2, n2, n3 + 1, n4) >= 0) {
                int n8 = this.h(cn2, n2, n3 + 1, n4);
                n5 = n8 >= 8 ? n8 : n8 + 8;
            }
            if (this.a >= 2 && this.bn == gb.f) {
                if (cn2.g(n2, n3 - 1, n4)) {
                    n5 = 0;
                } else if (cn2.f(n2, n3 - 1, n4) == this.bn && cn2.e(n2, n3, n4) == 0) {
                    n5 = 0;
                }
            }
            if (this.bn == gb.g && n6 < 8 && n5 < 8 && n5 > n6 && random.nextInt(4) != 0) {
                n5 = n6;
                bl2 = false;
            }
            if (n5 != n6) {
                n6 = n5;
                if (n6 < 0) {
                    cn2.d(n2, n3, n4, 0);
                } else {
                    cn2.b(n2, n3, n4, n6);
                    cn2.h(n2, n3, n4, this.bc);
                    cn2.g(n2, n3, n4, this.bc);
                }
            } else if (bl2) {
                this.j(cn2, n2, n3, n4);
            }
        } else {
            this.j(cn2, n2, n3, n4);
        }
        if (this.m(cn2, n2, n3 - 1, n4)) {
            if (n6 >= 8) {
                cn2.b(n2, n3 - 1, n4, this.bc, n6);
            } else {
                cn2.b(n2, n3 - 1, n4, this.bc, n6 + 8);
            }
        } else if (n6 >= 0 && (n6 == 0 || this.l(cn2, n2, n3 - 1, n4))) {
            boolean[] blArray = this.k(cn2, n2, n3, n4);
            n5 = n6 + this.d;
            if (n6 >= 8) {
                n5 = 1;
            }
            if (n5 >= 8) {
                return;
            }
            if (blArray[0]) {
                this.g(cn2, n2 - 1, n3, n4, n5);
            }
            if (blArray[1]) {
                this.g(cn2, n2 + 1, n3, n4, n5);
            }
            if (blArray[2]) {
                this.g(cn2, n2, n3, n4 - 1, n5);
            }
            if (blArray[3]) {
                this.g(cn2, n2, n3, n4 + 1, n5);
            }
        }
    }

    private void g(cn cn2, int n2, int n3, int n4, int n5) {
        if (this.m(cn2, n2, n3, n4)) {
            int n6 = cn2.a(n2, n3, n4);
            if (n6 > 0) {
                if (this.bn == gb.g) {
                    this.i(cn2, n2, n3, n4);
                } else {
                    ly.n[n6].b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
                }
            }
            cn2.b(n2, n3, n4, this.bc, n5);
        }
    }

    private int a(cn cn2, int n2, int n3, int n4, int n5, int n6) {
        int n7 = 1000;
        for (int i2 = 0; i2 < 4; ++i2) {
            int n8;
            if (i2 == 0 && n6 == 1 || i2 == 1 && n6 == 0 || i2 == 2 && n6 == 3 || i2 == 3 && n6 == 2) continue;
            int n9 = n2;
            int n10 = n3;
            int n11 = n4;
            if (i2 == 0) {
                --n9;
            }
            if (i2 == 1) {
                ++n9;
            }
            if (i2 == 2) {
                --n11;
            }
            if (i2 == 3) {
                ++n11;
            }
            if (this.l(cn2, n9, n10, n11) || cn2.f(n9, n10, n11) == this.bn && cn2.e(n9, n10, n11) == 0) continue;
            if (!this.l(cn2, n9, n10 - 1, n11)) {
                return n5;
            }
            if (n5 >= 4 || (n8 = this.a(cn2, n9, n10, n11, n5 + 1, i2)) >= n7) continue;
            n7 = n8;
        }
        return n7;
    }

    private boolean[] k(cn cn2, int n2, int n3, int n4) {
        int n5;
        int n6;
        for (n6 = 0; n6 < 4; ++n6) {
            this.c[n6] = 1000;
            n5 = n2;
            int n7 = n3;
            int n8 = n4;
            if (n6 == 0) {
                --n5;
            }
            if (n6 == 1) {
                ++n5;
            }
            if (n6 == 2) {
                --n8;
            }
            if (n6 == 3) {
                ++n8;
            }
            if (this.l(cn2, n5, n7, n8) || cn2.f(n5, n7, n8) == this.bn && cn2.e(n5, n7, n8) == 0) continue;
            this.c[n6] = !this.l(cn2, n5, n7 - 1, n8) ? 0 : this.a(cn2, n5, n7, n8, 1, n6);
        }
        n6 = this.c[0];
        for (n5 = 1; n5 < 4; ++n5) {
            if (this.c[n5] >= n6) continue;
            n6 = this.c[n5];
        }
        for (n5 = 0; n5 < 4; ++n5) {
            this.b[n5] = this.c[n5] == n6;
        }
        return this.b;
    }

    private boolean l(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.a(n2, n3, n4);
        if (n5 == ly.aF.bc || n5 == ly.aM.bc || n5 == ly.aE.bc || n5 == ly.aG.bc || n5 == ly.aY.bc) {
            return true;
        }
        if (n5 == 0) {
            return false;
        }
        gb gb2 = ly.n[n5].bn;
        return gb2.a();
    }

    protected int f(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = this.h(cn2, n2, n3, n4);
        if (n6 < 0) {
            return n5;
        }
        if (n6 == 0) {
            ++this.a;
        }
        if (n6 >= 8) {
            n6 = 0;
        }
        return n5 < 0 || n6 < n5 ? n6 : n5;
    }

    private boolean m(cn cn2, int n2, int n3, int n4) {
        gb gb2 = cn2.f(n2, n3, n4);
        if (gb2 == this.bn) {
            return false;
        }
        if (gb2 == gb.g) {
            return false;
        }
        return !this.l(cn2, n2, n3, n4);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        super.e(cn2, n2, n3, n4);
        if (cn2.a(n2, n3, n4) == this.bc) {
            cn2.h(n2, n3, n4, this.bc);
        }
    }
}

