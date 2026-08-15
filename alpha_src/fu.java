/*
 * Decompiled with CFR 0.152.
 */
public class fu
extends di {
    public fu(int n2, int n3) {
        super(n2);
        this.aT = 1;
        this.aU = 32 << n3;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.a(n2, n3, n4);
        gb gb2 = cn2.f(n2, n3 + 1, n4);
        if (!gb2.a() && n6 == ly.v.bc || n6 == ly.w.bc) {
            ly ly2 = ly.aB;
            cn2.a((float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, ly2.bl.d(), (ly2.bl.b() + 1.0f) / 2.0f, ly2.bl.c() * 0.8f);
            cn2.d(n2, n3, n4, ly2.bc);
            ev2.b(1);
            if (cn2.n.nextInt(8) == 0 && n6 == ly.v.bc) {
                int n7 = 1;
                for (int i2 = 0; i2 < n7; ++i2) {
                    float f2 = 0.7f;
                    float f3 = cn2.n.nextFloat() * f2 + (1.0f - f2) * 0.5f;
                    float f4 = 1.2f;
                    float f5 = cn2.n.nextFloat() * f2 + (1.0f - f2) * 0.5f;
                    dx dx2 = new dx(cn2, (float)n2 + f3, (float)n3 + f4, (float)n4 + f5, new ev(di.Q));
                    dx2.c = 10;
                    cn2.a(dx2);
                }
            }
            return true;
        }
        return false;
    }

    public boolean a() {
        return true;
    }
}

