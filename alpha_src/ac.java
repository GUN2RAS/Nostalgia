/*
 * Decompiled with CFR 0.152.
 */
public class ac
extends di {
    private int a;

    public ac(int n2, int n3) {
        super(n2);
        this.aT = 1;
        this.aU = 64;
        this.a = n3;
    }

    public ev a(ev ev2, cn cn2, dm dm2) {
        float f2;
        float f3;
        float f4;
        double d2;
        float f5;
        float f6 = 1.0f;
        float f7 = dm2.at + (dm2.ar - dm2.at) * f6;
        float f8 = dm2.as + (dm2.aq - dm2.as) * f6;
        double d3 = dm2.ah + (dm2.ak - dm2.ah) * (double)f6;
        double d4 = dm2.ai + (dm2.al - dm2.ai) * (double)f6;
        double d5 = dm2.aj + (dm2.am - dm2.aj) * (double)f6;
        aj aj2 = aj.b(d3, d4, d5);
        float f9 = eo.b(-f8 * ((float)Math.PI / 180) - (float)Math.PI);
        float f10 = eo.a(-f8 * ((float)Math.PI / 180) - (float)Math.PI);
        float f11 = f10 * (f5 = -eo.b(-f7 * ((float)Math.PI / 180)));
        aj aj3 = aj2.c((double)f11 * (d2 = 5.0), (double)(f4 = (f3 = eo.a(-f7 * ((float)Math.PI / 180)))) * d2, (double)(f2 = f9 * f5) * d2);
        mf mf2 = cn2.a(aj2, aj3, this.a == 0);
        if (mf2 == null) {
            return ev2;
        }
        if (mf2.a == 0) {
            int n2 = mf2.b;
            int n3 = mf2.c;
            int n4 = mf2.d;
            if (this.a == 0) {
                if (cn2.f(n2, n3, n4) == gb.f && cn2.e(n2, n3, n4) == 0) {
                    cn2.d(n2, n3, n4, 0);
                    return new ev(di.av);
                }
                if (cn2.f(n2, n3, n4) == gb.g && cn2.e(n2, n3, n4) == 0) {
                    cn2.d(n2, n3, n4, 0);
                    return new ev(di.aw);
                }
            } else {
                if (this.a < 0) {
                    return new ev(di.au);
                }
                if (mf2.e == 0) {
                    --n3;
                }
                if (mf2.e == 1) {
                    ++n3;
                }
                if (mf2.e == 2) {
                    --n4;
                }
                if (mf2.e == 3) {
                    ++n4;
                }
                if (mf2.e == 4) {
                    --n2;
                }
                if (mf2.e == 5) {
                    ++n2;
                }
                if (cn2.a(n2, n3, n4) == 0 || !cn2.f(n2, n3, n4).a()) {
                    cn2.b(n2, n3, n4, this.a, 0);
                    return new ev(di.au);
                }
            }
        } else if (this.a == 0 && mf2.g instanceof am) {
            return new ev(di.aE);
        }
        return ev2;
    }
}

