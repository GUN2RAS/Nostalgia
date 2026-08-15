/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class je {
    public void a(cn cn2, kh kh2, double d2, double d3, double d4, float f2) {
        double d5;
        double d6;
        double d7;
        double d8;
        int n2;
        int n3;
        int n4;
        cn2.a(d2, d3, d4, "random.explode", 4.0f, (1.0f + (cn2.n.nextFloat() - cn2.n.nextFloat()) * 0.2f) * 0.7f);
        HashSet<mt> hashSet = new HashSet<mt>();
        float f3 = f2;
        int n5 = 16;
        for (n4 = 0; n4 < n5; ++n4) {
            for (n3 = 0; n3 < n5; ++n3) {
                for (n2 = 0; n2 < n5; ++n2) {
                    if (n4 != 0 && n4 != n5 - 1 && n3 != 0 && n3 != n5 - 1 && n2 != 0 && n2 != n5 - 1) continue;
                    double d9 = (float)n4 / ((float)n5 - 1.0f) * 2.0f - 1.0f;
                    double d10 = (float)n3 / ((float)n5 - 1.0f) * 2.0f - 1.0f;
                    double d11 = (float)n2 / ((float)n5 - 1.0f) * 2.0f - 1.0f;
                    double d12 = Math.sqrt(d9 * d9 + d10 * d10 + d11 * d11);
                    d9 /= d12;
                    d10 /= d12;
                    d11 /= d12;
                    d8 = d2;
                    d7 = d3;
                    d6 = d4;
                    float f4 = 0.3f;
                    for (float f5 = f2 * (0.7f + cn2.n.nextFloat() * 0.6f); f5 > 0.0f; f5 -= f4 * 0.75f) {
                        int n6;
                        int n7;
                        int n8 = eo.b(d8);
                        int n9 = cn2.a(n8, n7 = eo.b(d7), n6 = eo.b(d6));
                        if (n9 > 0) {
                            f5 -= (ly.n[n9].a(kh2) + 0.3f) * f4;
                        }
                        if (f5 > 0.0f) {
                            hashSet.add(new mt(n8, n7, n6));
                        }
                        d8 += d9 * (double)f4;
                        d7 += d10 * (double)f4;
                        d6 += d11 * (double)f4;
                    }
                }
            }
        }
        n4 = eo.b(d2 - (double)(f2 *= 2.0f) - 1.0);
        n3 = eo.b(d2 + (double)f2 + 1.0);
        n2 = eo.b(d3 - (double)f2 - 1.0);
        int n10 = eo.b(d3 + (double)f2 + 1.0);
        int n11 = eo.b(d4 - (double)f2 - 1.0);
        int n12 = eo.b(d4 + (double)f2 + 1.0);
        List list = cn2.b(kh2, cf.b(n4, n2, n11, n3, n10, n12));
        aj aj2 = aj.b(d2, d3, d4);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            kh kh3 = (kh)list.get(i2);
            double d13 = kh3.e(d2, d3, d4) / (double)f2;
            if (!(d13 <= 1.0)) continue;
            d8 = kh3.ak - d2;
            d7 = kh3.al - d3;
            d6 = kh3.am - d4;
            double d14 = eo.a(d8 * d8 + d7 * d7 + d6 * d6);
            d8 /= d14;
            d7 /= d14;
            d6 /= d14;
            double d15 = cn2.a(aj2, kh3.au);
            double d16 = (1.0 - d13) * d15;
            kh3.a(kh2, (int)((d16 * d16 + d16) / 2.0 * 8.0 * (double)f2 + 1.0));
            d5 = d16;
            kh3.an += d8 * d5;
            kh3.ao += d7 * d5;
            kh3.ap += d6 * d5;
        }
        f2 = f3;
        ArrayList<mt> arrayList = new ArrayList<mt>();
        arrayList.addAll(hashSet);
        for (int i3 = arrayList.size() - 1; i3 >= 0; --i3) {
            mt mt2 = (mt)arrayList.get(i3);
            int n13 = mt2.a;
            int n14 = mt2.b;
            int n15 = mt2.c;
            int n16 = cn2.a(n13, n14, n15);
            for (int i4 = 0; i4 < 1; ++i4) {
                d6 = (float)n13 + cn2.n.nextFloat();
                double d17 = (float)n14 + cn2.n.nextFloat();
                double d18 = (float)n15 + cn2.n.nextFloat();
                double d19 = d6 - d2;
                d5 = d17 - d3;
                double d20 = d18 - d4;
                double d21 = eo.a(d19 * d19 + d5 * d5 + d20 * d20);
                d19 /= d21;
                d5 /= d21;
                d20 /= d21;
                double d22 = 0.5 / (d21 / (double)f2 + 0.1);
                cn2.a("explode", (d6 + d2 * 1.0) / 2.0, (d17 + d3 * 1.0) / 2.0, (d18 + d4 * 1.0) / 2.0, d19 *= (d22 *= (double)(cn2.n.nextFloat() * cn2.n.nextFloat() + 0.3f)), d5 *= d22, d20 *= d22);
                cn2.a("smoke", d6, d17, d18, d19, d5, d20);
            }
            if (n16 <= 0) continue;
            ly.n[n16].a(cn2, n13, n14, n15, cn2.e(n13, n14, n15), 0.3f);
            cn2.d(n13, n14, n15, 0);
            ly.n[n16].c(cn2, n13, n14, n15);
        }
    }
}

