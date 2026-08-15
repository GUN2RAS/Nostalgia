/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class b
extends jt {
    private Random a = new Random();

    protected b(int n2) {
        super(n2, gb.c);
        this.bb = 26;
    }

    public int a(nm nm2, int n2, int n3, int n4, int n5) {
        if (n5 == 1) {
            return this.bb - 1;
        }
        if (n5 == 0) {
            return this.bb - 1;
        }
        int n6 = nm2.a(n2, n3, n4 - 1);
        int n7 = nm2.a(n2, n3, n4 + 1);
        int n8 = nm2.a(n2 - 1, n3, n4);
        int n9 = nm2.a(n2 + 1, n3, n4);
        if (n6 == this.bc || n7 == this.bc) {
            if (n5 == 2 || n5 == 3) {
                return this.bb;
            }
            int n10 = 0;
            if (n6 == this.bc) {
                n10 = -1;
            }
            int n11 = nm2.a(n2 - 1, n3, n6 == this.bc ? n4 - 1 : n4 + 1);
            int n12 = nm2.a(n2 + 1, n3, n6 == this.bc ? n4 - 1 : n4 + 1);
            if (n5 == 4) {
                n10 = -1 - n10;
            }
            int n13 = 5;
            if ((ly.p[n8] || ly.p[n11]) && !ly.p[n9] && !ly.p[n12]) {
                n13 = 5;
            }
            if ((ly.p[n9] || ly.p[n12]) && !ly.p[n8] && !ly.p[n11]) {
                n13 = 4;
            }
            return (n5 == n13 ? this.bb + 16 : this.bb + 32) + n10;
        }
        if (n8 == this.bc || n9 == this.bc) {
            if (n5 == 4 || n5 == 5) {
                return this.bb;
            }
            int n14 = 0;
            if (n8 == this.bc) {
                n14 = -1;
            }
            int n15 = nm2.a(n8 == this.bc ? n2 - 1 : n2 + 1, n3, n4 - 1);
            int n16 = nm2.a(n8 == this.bc ? n2 - 1 : n2 + 1, n3, n4 + 1);
            if (n5 == 3) {
                n14 = -1 - n14;
            }
            int n17 = 3;
            if ((ly.p[n6] || ly.p[n15]) && !ly.p[n7] && !ly.p[n16]) {
                n17 = 3;
            }
            if ((ly.p[n7] || ly.p[n16]) && !ly.p[n6] && !ly.p[n15]) {
                n17 = 2;
            }
            return (n5 == n17 ? this.bb + 16 : this.bb + 32) + n14;
        }
        int n18 = 3;
        if (ly.p[n6] && !ly.p[n7]) {
            n18 = 3;
        }
        if (ly.p[n7] && !ly.p[n6]) {
            n18 = 2;
        }
        if (ly.p[n8] && !ly.p[n9]) {
            n18 = 5;
        }
        if (ly.p[n9] && !ly.p[n8]) {
            n18 = 4;
        }
        return n5 == n18 ? this.bb + 1 : this.bb;
    }

    public int a(int n2) {
        if (n2 == 1) {
            return this.bb - 1;
        }
        if (n2 == 0) {
            return this.bb - 1;
        }
        if (n2 == 3) {
            return this.bb + 1;
        }
        return this.bb;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        int n5 = 0;
        if (cn2.a(n2 - 1, n3, n4) == this.bc) {
            ++n5;
        }
        if (cn2.a(n2 + 1, n3, n4) == this.bc) {
            ++n5;
        }
        if (cn2.a(n2, n3, n4 - 1) == this.bc) {
            ++n5;
        }
        if (cn2.a(n2, n3, n4 + 1) == this.bc) {
            ++n5;
        }
        if (n5 > 1) {
            return false;
        }
        if (this.h(cn2, n2 - 1, n3, n4)) {
            return false;
        }
        if (this.h(cn2, n2 + 1, n3, n4)) {
            return false;
        }
        if (this.h(cn2, n2, n3, n4 - 1)) {
            return false;
        }
        return !this.h(cn2, n2, n3, n4 + 1);
    }

    private boolean h(cn cn2, int n2, int n3, int n4) {
        if (cn2.a(n2, n3, n4) != this.bc) {
            return false;
        }
        if (cn2.a(n2 - 1, n3, n4) == this.bc) {
            return true;
        }
        if (cn2.a(n2 + 1, n3, n4) == this.bc) {
            return true;
        }
        if (cn2.a(n2, n3, n4 - 1) == this.bc) {
            return true;
        }
        return cn2.a(n2, n3, n4 + 1) == this.bc;
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        fe fe2 = (fe)cn2.b(n2, n3, n4);
        for (int i2 = 0; i2 < fe2.c(); ++i2) {
            ev ev2 = fe2.c(i2);
            if (ev2 == null) continue;
            float f2 = this.a.nextFloat() * 0.8f + 0.1f;
            float f3 = this.a.nextFloat() * 0.8f + 0.1f;
            float f4 = this.a.nextFloat() * 0.8f + 0.1f;
            while (ev2.a > 0) {
                int n5 = this.a.nextInt(21) + 10;
                if (n5 > ev2.a) {
                    n5 = ev2.a;
                }
                ev2.a -= n5;
                dx dx2 = new dx(cn2, (float)n2 + f2, (float)n3 + f3, (float)n4 + f4, new ev(ev2.c, n5, ev2.d));
                float f5 = 0.05f;
                dx2.an = (float)this.a.nextGaussian() * f5;
                dx2.ao = (float)this.a.nextGaussian() * f5 + 0.2f;
                dx2.ap = (float)this.a.nextGaussian() * f5;
                cn2.a(dx2);
            }
        }
        super.b(cn2, n2, n3, n4);
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        gh gh2 = (fe)cn2.b(n2, n3, n4);
        if (cn2.g(n2, n3 + 1, n4)) {
            return true;
        }
        if (cn2.a(n2 - 1, n3, n4) == this.bc && cn2.g(n2 - 1, n3 + 1, n4)) {
            return true;
        }
        if (cn2.a(n2 + 1, n3, n4) == this.bc && cn2.g(n2 + 1, n3 + 1, n4)) {
            return true;
        }
        if (cn2.a(n2, n3, n4 - 1) == this.bc && cn2.g(n2, n3 + 1, n4 - 1)) {
            return true;
        }
        if (cn2.a(n2, n3, n4 + 1) == this.bc && cn2.g(n2, n3 + 1, n4 + 1)) {
            return true;
        }
        if (cn2.a(n2 - 1, n3, n4) == this.bc) {
            gh2 = new hs("Large chest", (fe)cn2.b(n2 - 1, n3, n4), gh2);
        }
        if (cn2.a(n2 + 1, n3, n4) == this.bc) {
            gh2 = new hs("Large chest", gh2, (fe)cn2.b(n2 + 1, n3, n4));
        }
        if (cn2.a(n2, n3, n4 - 1) == this.bc) {
            gh2 = new hs("Large chest", (fe)cn2.b(n2, n3, n4 - 1), gh2);
        }
        if (cn2.a(n2, n3, n4 + 1) == this.bc) {
            gh2 = new hs("Large chest", gh2, (fe)cn2.b(n2, n3, n4 + 1));
        }
        dm2.a(gh2);
        return true;
    }

    protected ic a_() {
        return new fe();
    }
}

