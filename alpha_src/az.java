/*
 * Decompiled with CFR 0.152.
 */
import java.util.HashSet;
import java.util.Set;

public class az {
    private int a;
    private Class b;
    private Class[] c;
    private Set d = new HashSet();

    public az(int n2, Class clazz, Class[] classArray) {
        this.a = n2;
        this.b = clazz;
        this.c = classArray;
    }

    public void a(cn cn2) {
        int n2 = cn2.b(this.b);
        if (n2 < this.a) {
            for (int i2 = 0; i2 < 3; ++i2) {
                this.a(cn2, 1, null);
            }
        }
    }

    protected mt a(cn cn2, int n2, int n3) {
        int n4 = n2 + cn2.n.nextInt(16);
        int n5 = cn2.n.nextInt(128);
        int n6 = n3 + cn2.n.nextInt(16);
        return new mt(n4, n5, n6);
    }

    private int a(cn cn2, int n2, nu nu2) {
        int n3;
        int n4;
        int n5;
        int n6;
        this.d.clear();
        for (n6 = 0; n6 < cn2.k.size(); ++n6) {
            dm dm2 = (dm)cn2.k.get(n6);
            int n7 = eo.b(dm2.ak / 16.0);
            n5 = eo.b(dm2.am / 16.0);
            int n8 = 4;
            for (n4 = -n8; n4 <= n8; ++n4) {
                for (n3 = -n8; n3 <= n8; ++n3) {
                    this.d.add(new ol(n4 + n7, n3 + n5));
                }
            }
        }
        n6 = 0;
        for (ol ol2 : this.d) {
            if (cn2.n.nextInt(10) != 0) continue;
            n5 = cn2.n.nextInt(this.c.length);
            mt mt2 = this.a(cn2, ol2.a * 16, ol2.b * 16);
            n4 = mt2.a;
            n3 = mt2.b;
            int n9 = mt2.c;
            if (cn2.g(n4, n3, n9)) {
                return 0;
            }
            if (cn2.f(n4, n3, n9) != gb.a) {
                return 0;
            }
            for (int i2 = 0; i2 < 3; ++i2) {
                int n10 = n4;
                int n11 = n3;
                int n12 = n9;
                int n13 = 6;
                for (int i3 = 0; i3 < 2; ++i3) {
                    ge ge2;
                    float f2;
                    float f3;
                    float f4;
                    float f5;
                    float f6;
                    float f7;
                    float f8;
                    if (!cn2.g(n10 += cn2.n.nextInt(n13) - cn2.n.nextInt(n13), (n11 += cn2.n.nextInt(1) - cn2.n.nextInt(1)) - 1, n12 += cn2.n.nextInt(n13) - cn2.n.nextInt(n13)) || cn2.g(n10, n11, n12) || cn2.f(n10, n11, n12).d() || cn2.g(n10, n11 + 1, n12) || cn2.a(f8 = (float)n10 + 0.5f, f7 = (float)n11, (double)(f6 = (float)n12 + 0.5f), 24.0) != null || (f5 = (f4 = f8 - (float)cn2.o) * f4 + (f3 = f7 - (float)cn2.p) * f3 + (f2 = f6 - (float)cn2.q) * f2) < 576.0f) continue;
                    try {
                        ge2 = (ge)this.c[n5].getConstructor(cn.class).newInstance(cn2);
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return n6;
                    }
                    ge2.c(f8, f7, f6, cn2.n.nextFloat() * 360.0f, 0.0f);
                    if (!ge2.a()) continue;
                    ++n6;
                    cn2.a(ge2);
                    if (!(ge2 instanceof ax) || cn2.n.nextInt(100) != 0) continue;
                    cw cw2 = new cw(cn2);
                    cw2.c(f8, f7, f6, ge2.aq, 0.0f);
                    cn2.a(cw2);
                    cw2.g(ge2);
                }
            }
        }
        return n6;
    }
}

