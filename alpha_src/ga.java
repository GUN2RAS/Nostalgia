/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ga {
    public static boolean a;
    public byte[] b;
    public boolean c;
    public cn d;
    public mu e;
    public mu f;
    public mu g;
    public byte[] h;
    public int i;
    public final int j;
    public final int k;
    public Map l = new HashMap();
    public List[] m = new List[8];
    public boolean n = false;
    public boolean o = false;
    public boolean p;
    public boolean q = false;
    public boolean r = false;
    public long s = 0L;

    public ga(cn cn2, int n2, int n3) {
        this.d = cn2;
        this.j = n2;
        this.k = n3;
        this.h = new byte[256];
        for (int i2 = 0; i2 < this.m.length; ++i2) {
            this.m[i2] = new ArrayList();
        }
    }

    public ga(cn cn2, byte[] byArray, int n2, int n3) {
        this(cn2, n2, n3);
        this.b = byArray;
        this.e = new mu(byArray.length);
        this.f = new mu(byArray.length);
        this.g = new mu(byArray.length);
    }

    public boolean a(int n2, int n3) {
        return n2 == this.j && n3 == this.k;
    }

    public int b(int n2, int n3) {
        return this.h[n3 << 4 | n2] & 0xFF;
    }

    public void a() {
    }

    public void b() {
        int n2 = 127;
        for (int i2 = 0; i2 < 16; ++i2) {
            for (int i3 = 0; i3 < 16; ++i3) {
                int n3;
                int n4 = i2 << 11 | i3 << 7;
                for (n3 = 127; n3 > 0 && ly.r[this.b[n4 + n3 - 1]] == 0; --n3) {
                }
                this.h[i3 << 4 | i2] = (byte)n3;
                if (n3 >= n2) continue;
                n2 = n3;
            }
        }
        this.i = n2;
        this.o = true;
    }

    public void c() {
        int n2;
        int n3;
        int n4 = 127;
        for (n3 = 0; n3 < 16; ++n3) {
            for (n2 = 0; n2 < 16; ++n2) {
                this.h[n2 << 4 | n3] = -128;
                this.g(n3, 127, n2);
                if ((this.h[n2 << 4 | n3] & 0xFF) >= n4) continue;
                n4 = this.h[n2 << 4 | n3] & 0xFF;
            }
        }
        this.i = n4;
        for (n3 = 0; n3 < 16; ++n3) {
            for (n2 = 0; n2 < 16; ++n2) {
                this.c(n3, n2);
            }
        }
        this.o = true;
    }

    private void c(int n2, int n3) {
        int n4 = this.b(n2, n3);
        int n5 = this.j * 16 + n2;
        int n6 = this.k * 16 + n3;
        this.f(n5 - 1, n6, n4);
        this.f(n5 + 1, n6, n4);
        this.f(n5, n6 - 1, n4);
        this.f(n5, n6 + 1, n4);
    }

    private void f(int n2, int n3, int n4) {
        int n5 = this.d.c(n2, n3);
        if (n5 > n4) {
            this.d.a(by.a, n2, n4, n3, n2, n5, n3);
        } else if (n5 < n4) {
            this.d.a(by.a, n2, n5, n3, n2, n4, n3);
        }
        this.o = true;
    }

    private void g(int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8;
        int n9 = n8 = this.h[n4 << 4 | n2] & 0xFF;
        if (n3 > n8) {
            n9 = n3;
        }
        int n10 = n2 << 11 | n4 << 7;
        while (n9 > 0 && ly.r[this.b[n10 + n9 - 1]] == 0) {
            --n9;
        }
        if (n9 == n8) {
            return;
        }
        this.d.f(n2, n4, n9, n8);
        this.h[n4 << 4 | n2] = (byte)n9;
        if (n9 < this.i) {
            this.i = n9;
        } else {
            n7 = 127;
            for (n6 = 0; n6 < 16; ++n6) {
                for (n5 = 0; n5 < 16; ++n5) {
                    if ((this.h[n5 << 4 | n6] & 0xFF) >= n7) continue;
                    n7 = this.h[n5 << 4 | n6] & 0xFF;
                }
            }
            this.i = n7;
        }
        n7 = this.j * 16 + n2;
        n6 = this.k * 16 + n4;
        if (n9 < n8) {
            for (n5 = n9; n5 < n8; ++n5) {
                this.f.a(n2, n5, n4, 15);
            }
        } else {
            this.d.a(by.a, n7, n8, n6, n7, n9, n6);
            for (n5 = n8; n5 < n9; ++n5) {
                this.f.a(n2, n5, n4, 0);
            }
        }
        n5 = 15;
        int n11 = n9;
        while (n9 > 0 && n5 > 0) {
            int n12;
            if ((n12 = ly.r[this.a(n2, --n9, n4)]) == 0) {
                n12 = 1;
            }
            if ((n5 -= n12) < 0) {
                n5 = 0;
            }
            this.f.a(n2, n9, n4, n5);
        }
        while (n9 > 0 && ly.r[this.a(n2, n9 - 1, n4)] == 0) {
            --n9;
        }
        if (n9 != n11) {
            this.d.a(by.a, n7 - 1, n9, n6 - 1, n7 + 1, n11, n6 + 1);
        }
        this.o = true;
    }

    public int a(int n2, int n3, int n4) {
        return this.b[n2 << 11 | n4 << 7 | n3];
    }

    public boolean a(int n2, int n3, int n4, int n5, int n6) {
        byte by2 = (byte)n5;
        int n7 = this.h[n4 << 4 | n2] & 0xFF;
        int n8 = this.b[n2 << 11 | n4 << 7 | n3] & 0xFF;
        if (n8 == n5 && this.e.a(n2, n3, n4) == n6) {
            return false;
        }
        int n9 = this.j * 16 + n2;
        int n10 = this.k * 16 + n4;
        this.b[n2 << 11 | n4 << 7 | n3] = by2;
        if (n8 != 0 && !this.d.y) {
            ly.n[n8].b(this.d, n9, n3, n10);
        }
        this.e.a(n2, n3, n4, n6);
        if (ly.r[by2] != 0) {
            if (n3 >= n7) {
                this.g(n2, n3 + 1, n4);
            }
        } else if (n3 == n7 - 1) {
            this.g(n2, n3, n4);
        }
        this.d.a(by.a, n9, n3, n10, n9, n3, n10);
        this.d.a(by.b, n9, n3, n10, n9, n3, n10);
        this.c(n2, n4);
        if (n5 != 0) {
            ly.n[n5].e(this.d, n9, n3, n10);
        }
        this.o = true;
        return true;
    }

    public boolean a(int n2, int n3, int n4, int n5) {
        byte by2 = (byte)n5;
        int n6 = this.h[n4 << 4 | n2] & 0xFF;
        int n7 = this.b[n2 << 11 | n4 << 7 | n3] & 0xFF;
        if (n7 == n5) {
            return false;
        }
        int n8 = this.j * 16 + n2;
        int n9 = this.k * 16 + n4;
        this.b[n2 << 11 | n4 << 7 | n3] = by2;
        if (n7 != 0) {
            ly.n[n7].b(this.d, n8, n3, n9);
        }
        this.e.a(n2, n3, n4, 0);
        if (ly.r[by2] != 0) {
            if (n3 >= n6) {
                this.g(n2, n3 + 1, n4);
            }
        } else if (n3 == n6 - 1) {
            this.g(n2, n3, n4);
        }
        this.d.a(by.a, n8, n3, n9, n8, n3, n9);
        this.d.a(by.b, n8, n3, n9, n8, n3, n9);
        this.c(n2, n4);
        if (n5 != 0 && !this.d.y) {
            ly.n[n5].e(this.d, n8, n3, n9);
        }
        this.o = true;
        return true;
    }

    public int b(int n2, int n3, int n4) {
        return this.e.a(n2, n3, n4);
    }

    public void b(int n2, int n3, int n4, int n5) {
        this.o = true;
        this.e.a(n2, n3, n4, n5);
    }

    public int a(by by2, int n2, int n3, int n4) {
        if (by2 == by.a) {
            return this.f.a(n2, n3, n4);
        }
        if (by2 == by.b) {
            return this.g.a(n2, n3, n4);
        }
        return 0;
    }

    public void a(by by2, int n2, int n3, int n4, int n5) {
        this.o = true;
        if (by2 == by.a) {
            this.f.a(n2, n3, n4, n5);
        } else if (by2 == by.b) {
            this.g.a(n2, n3, n4, n5);
        } else {
            return;
        }
    }

    public int c(int n2, int n3, int n4, int n5) {
        int n6;
        int n7 = this.f.a(n2, n3, n4);
        if (n7 > 0) {
            a = true;
        }
        if ((n6 = this.g.a(n2, n3, n4)) > (n7 -= n5)) {
            n7 = n6;
        }
        return n7;
    }

    public void a(kh kh2) {
        int n2;
        if (this.q) {
            return;
        }
        this.r = true;
        int n3 = eo.b(kh2.ak / 16.0);
        int n4 = eo.b(kh2.am / 16.0);
        if (n3 != this.j || n4 != this.k) {
            System.out.println("Wrong location! " + kh2);
        }
        if ((n2 = eo.b(kh2.al / 16.0)) < 0) {
            n2 = 0;
        }
        if (n2 >= this.m.length) {
            n2 = this.m.length - 1;
        }
        kh2.aZ = true;
        kh2.ba = this.j;
        kh2.bb = n2;
        kh2.bc = this.k;
        this.m[n2].add(kh2);
    }

    public void b(kh kh2) {
        this.a(kh2, kh2.bb);
    }

    public void a(kh kh2, int n2) {
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 >= this.m.length) {
            n2 = this.m.length - 1;
        }
        this.m[n2].remove(kh2);
    }

    public boolean c(int n2, int n3, int n4) {
        return n3 >= (this.h[n4 << 4 | n2] & 0xFF);
    }

    public ic d(int n2, int n3, int n4) {
        mt mt2 = new mt(n2, n3, n4);
        ic ic2 = (ic)this.l.get(mt2);
        if (ic2 == null) {
            int n5 = this.a(n2, n3, n4);
            if (!ly.q[n5]) {
                return null;
            }
            jt jt2 = (jt)ly.n[n5];
            jt2.e(this.d, this.j * 16 + n2, n3, this.k * 16 + n4);
            ic2 = (ic)this.l.get(mt2);
        }
        return ic2;
    }

    public void a(ic ic2) {
        int n2 = ic2.f - this.j * 16;
        int n3 = ic2.g;
        int n4 = ic2.h - this.k * 16;
        this.a(n2, n3, n4, ic2);
    }

    public void a(int n2, int n3, int n4, ic ic2) {
        mt mt2 = new mt(n2, n3, n4);
        ic2.e = this.d;
        ic2.f = this.j * 16 + n2;
        ic2.g = n3;
        ic2.h = this.k * 16 + n4;
        if (this.a(n2, n3, n4) == 0 || !(ly.n[this.a(n2, n3, n4)] instanceof jt)) {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
            return;
        }
        if (this.c) {
            if (this.l.get(mt2) != null) {
                this.d.b.remove(this.l.get(mt2));
            }
            this.d.b.add(ic2);
        }
        this.l.put(mt2, ic2);
    }

    public void e(int n2, int n3, int n4) {
        mt mt2 = new mt(n2, n3, n4);
        if (this.c) {
            this.d.b.remove(this.l.remove(mt2));
        }
    }

    public void d() {
        this.c = true;
        this.d.b.addAll(this.l.values());
        for (int i2 = 0; i2 < this.m.length; ++i2) {
            this.d.a(this.m[i2]);
        }
    }

    public void e() {
        this.c = false;
        this.d.b.removeAll(this.l.values());
        for (int i2 = 0; i2 < this.m.length; ++i2) {
            this.d.b(this.m[i2]);
        }
    }

    public void f() {
        this.o = true;
    }

    public void a(kh kh2, cf cf2, List list) {
        int n2 = eo.b((cf2.b - 2.0) / 16.0);
        int n3 = eo.b((cf2.e + 2.0) / 16.0);
        if (n2 < 0) {
            n2 = 0;
        }
        if (n3 >= this.m.length) {
            n3 = this.m.length - 1;
        }
        for (int i2 = n2; i2 <= n3; ++i2) {
            List list2 = this.m[i2];
            for (int i3 = 0; i3 < list2.size(); ++i3) {
                kh kh3 = (kh)list2.get(i3);
                if (kh3 == kh2 || !kh3.au.a(cf2)) continue;
                list.add(kh3);
            }
        }
    }

    public void a(Class clazz, cf cf2, List list) {
        int n2 = eo.b((cf2.b - 2.0) / 16.0);
        int n3 = eo.b((cf2.e + 2.0) / 16.0);
        if (n2 < 0) {
            n2 = 0;
        }
        if (n3 >= this.m.length) {
            n3 = this.m.length - 1;
        }
        for (int i2 = n2; i2 <= n3; ++i2) {
            List list2 = this.m[i2];
            for (int i3 = 0; i3 < list2.size(); ++i3) {
                kh kh2 = (kh)list2.get(i3);
                if (!clazz.isAssignableFrom(kh2.getClass()) || !kh2.au.a(cf2)) continue;
                list.add(kh2);
            }
        }
    }

    public boolean a(boolean bl2) {
        if (this.p) {
            return false;
        }
        if (this.r && this.d.c != this.s) {
            return true;
        }
        return this.o;
    }

    public int a(byte[] byArray, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int n9;
        int n10;
        int n11;
        int n12;
        for (n12 = n2; n12 < n5; ++n12) {
            for (n11 = n4; n11 < n7; ++n11) {
                n10 = n12 << 11 | n11 << 7 | n3;
                n9 = n6 - n3;
                System.arraycopy(byArray, n8, this.b, n10, n9);
                n8 += n9;
            }
        }
        this.b();
        for (n12 = n2; n12 < n5; ++n12) {
            for (n11 = n4; n11 < n7; ++n11) {
                n10 = (n12 << 11 | n11 << 7 | n3) >> 1;
                n9 = (n6 - n3) / 2;
                System.arraycopy(byArray, n8, this.e.a, n10, n9);
                n8 += n9;
            }
        }
        for (n12 = n2; n12 < n5; ++n12) {
            for (n11 = n4; n11 < n7; ++n11) {
                n10 = (n12 << 11 | n11 << 7 | n3) >> 1;
                n9 = (n6 - n3) / 2;
                System.arraycopy(byArray, n8, this.g.a, n10, n9);
                n8 += n9;
            }
        }
        for (n12 = n2; n12 < n5; ++n12) {
            for (n11 = n4; n11 < n7; ++n11) {
                n10 = (n12 << 11 | n11 << 7 | n3) >> 1;
                n9 = (n6 - n3) / 2;
                System.arraycopy(byArray, n8, this.f.a, n10, n9);
                n8 += n9;
            }
        }
        return n8;
    }

    public Random a(long l2) {
        return new Random(this.d.u + (long)(this.j * this.j * 4987142) + (long)(this.j * 5947611) + (long)(this.k * this.k) * 4392871L + (long)(this.k * 389711) ^ l2);
    }
}

