/*
 * Decompiled with CFR 0.152.
 */
public class kn {
    public final by a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;

    public kn(by by2, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.a = by2;
        this.b = n2;
        this.c = n3;
        this.d = n4;
        this.e = n5;
        this.f = n6;
        this.g = n7;
    }

    public void a(cn cn2) {
        int n2 = this.e - this.b;
        int n3 = this.f - this.c;
        int n4 = this.g - this.d;
        int n5 = n2 * n3 * n4;
        if (n5 > 32768) {
            return;
        }
        for (int i2 = this.b; i2 <= this.e; ++i2) {
            for (int i3 = this.d; i3 <= this.g; ++i3) {
                if (!cn2.d(i2, 0, i3)) continue;
                for (int i4 = this.c; i4 <= this.f; ++i4) {
                    int n6;
                    if (i4 < 0 || i4 >= 128) continue;
                    int n7 = cn2.a(this.a, i2, i4, i3);
                    int n8 = 0;
                    int n9 = cn2.a(i2, i4, i3);
                    int n10 = ly.r[n9];
                    if (n10 == 0) {
                        n10 = 1;
                    }
                    int n11 = 0;
                    if (this.a == by.a) {
                        if (cn2.k(i2, i4, i3)) {
                            n11 = 15;
                        }
                    } else if (this.a == by.b) {
                        n11 = ly.t[n9];
                    }
                    if (n10 >= 15 && n11 == 0) {
                        n8 = 0;
                    } else {
                        n6 = cn2.a(this.a, i2 - 1, i4, i3);
                        int n12 = cn2.a(this.a, i2 + 1, i4, i3);
                        int n13 = cn2.a(this.a, i2, i4 - 1, i3);
                        int n14 = cn2.a(this.a, i2, i4 + 1, i3);
                        int n15 = cn2.a(this.a, i2, i4, i3 - 1);
                        int n16 = cn2.a(this.a, i2, i4, i3 + 1);
                        n8 = n6;
                        if (n12 > n8) {
                            n8 = n12;
                        }
                        if (n13 > n8) {
                            n8 = n13;
                        }
                        if (n14 > n8) {
                            n8 = n14;
                        }
                        if (n15 > n8) {
                            n8 = n15;
                        }
                        if (n16 > n8) {
                            n8 = n16;
                        }
                        if ((n8 -= n10) < 0) {
                            n8 = 0;
                        }
                        if (n11 > n8) {
                            n8 = n11;
                        }
                    }
                    if (n7 == n8) continue;
                    cn2.b(this.a, i2, i4, i3, n8);
                    n6 = n8 - 1;
                    if (n6 < 0) {
                        n6 = 0;
                    }
                    cn2.a(this.a, i2 - 1, i4, i3, n6);
                    cn2.a(this.a, i2, i4 - 1, i3, n6);
                    cn2.a(this.a, i2, i4, i3 - 1, n6);
                    if (i2 + 1 >= this.e) {
                        cn2.a(this.a, i2 + 1, i4, i3, n6);
                    }
                    if (i4 + 1 >= this.f) {
                        cn2.a(this.a, i2, i4 + 1, i3, n6);
                    }
                    if (i3 + 1 < this.g) continue;
                    cn2.a(this.a, i2, i4, i3 + 1, n6);
                }
            }
        }
    }

    public boolean a(int n2, int n3, int n4, int n5, int n6, int n7) {
        if (n2 >= this.b && n3 >= this.c && n4 >= this.d && n5 <= this.e && n6 <= this.f && n7 <= this.g) {
            return true;
        }
        int n8 = 1;
        if (n2 >= this.b - n8 && n3 >= this.c - n8 && n4 >= this.d - n8 && n5 <= this.e + n8 && n6 <= this.f + n8 && n7 <= this.g + n8) {
            int n9;
            int n10;
            int n11;
            int n12;
            int n13;
            int n14 = this.e - this.b;
            int n15 = this.f - this.c;
            int n16 = this.g - this.d;
            if (n2 > this.b) {
                n2 = this.b;
            }
            if (n3 > this.c) {
                n3 = this.c;
            }
            if (n4 > this.d) {
                n4 = this.d;
            }
            if (n5 < this.e) {
                n5 = this.e;
            }
            if (n6 < this.f) {
                n6 = this.f;
            }
            if (n7 < this.g) {
                n7 = this.g;
            }
            if ((n13 = (n12 = n5 - n2) * (n11 = n6 - n3) * (n10 = n7 - n4)) - (n9 = n14 * n15 * n16) <= 2) {
                this.b = n2;
                this.c = n3;
                this.d = n4;
                this.e = n5;
                this.f = n6;
                this.g = n7;
                return true;
            }
        }
        return false;
    }
}

