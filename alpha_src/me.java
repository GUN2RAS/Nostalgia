/*
 * Decompiled with CFR 0.152.
 */
public class me
extends di {
    public me(int n2) {
        super(n2);
        this.aT = 1;
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
        mf mf2 = cn2.a(aj2, aj3, true);
        if (mf2 == null) {
            return ev2;
        }
        if (mf2.a == 0) {
            int n2 = mf2.b;
            int n3 = mf2.c;
            int n4 = mf2.d;
            cn2.a(new dc(cn2, (float)n2 + 0.5f, (float)n3 + 1.5f, (float)n4 + 0.5f));
            --ev2.a;
        }
        return ev2;
    }
}

