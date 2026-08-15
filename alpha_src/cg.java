/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class cg
extends ik {
    public boolean a(cn cn2, Random random, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8 = 3;
        int n9 = random.nextInt(2) + 2;
        int n10 = random.nextInt(2) + 2;
        int n11 = 0;
        for (n7 = n2 - n9 - 1; n7 <= n2 + n9 + 1; ++n7) {
            for (n6 = n3 - 1; n6 <= n3 + n8 + 1; ++n6) {
                for (n5 = n4 - n10 - 1; n5 <= n4 + n10 + 1; ++n5) {
                    gb gb2 = cn2.f(n7, n6, n5);
                    if (n6 == n3 - 1 && !gb2.a()) {
                        return false;
                    }
                    if (n6 == n3 + n8 + 1 && !gb2.a()) {
                        return false;
                    }
                    if (n7 != n2 - n9 - 1 && n7 != n2 + n9 + 1 && n5 != n4 - n10 - 1 && n5 != n4 + n10 + 1 || n6 != n3 || cn2.a(n7, n6, n5) != 0 || cn2.a(n7, n6 + 1, n5) != 0) continue;
                    ++n11;
                }
            }
        }
        if (n11 < 1 || n11 > 5) {
            return false;
        }
        for (n7 = n2 - n9 - 1; n7 <= n2 + n9 + 1; ++n7) {
            for (n6 = n3 + n8; n6 >= n3 - 1; --n6) {
                for (n5 = n4 - n10 - 1; n5 <= n4 + n10 + 1; ++n5) {
                    if (n7 == n2 - n9 - 1 || n6 == n3 - 1 || n5 == n4 - n10 - 1 || n7 == n2 + n9 + 1 || n6 == n3 + n8 + 1 || n5 == n4 + n10 + 1) {
                        if (n6 >= 0 && !cn2.f(n7, n6 - 1, n5).a()) {
                            cn2.d(n7, n6, n5, 0);
                            continue;
                        }
                        if (!cn2.f(n7, n6, n5).a()) continue;
                        if (n6 == n3 - 1 && random.nextInt(4) != 0) {
                            cn2.d(n7, n6, n5, ly.ap.bc);
                            continue;
                        }
                        cn2.d(n7, n6, n5, ly.x.bc);
                        continue;
                    }
                    cn2.d(n7, n6, n5, 0);
                }
            }
        }
        block6: for (n7 = 0; n7 < 2; ++n7) {
            for (n6 = 0; n6 < 3; ++n6) {
                int n12;
                int n13;
                n5 = n2 + random.nextInt(n9 * 2 + 1) - n9;
                if (cn2.a(n5, n13 = n3, n12 = n4 + random.nextInt(n10 * 2 + 1) - n10) != 0) continue;
                int n14 = 0;
                if (cn2.f(n5 - 1, n13, n12).a()) {
                    ++n14;
                }
                if (cn2.f(n5 + 1, n13, n12).a()) {
                    ++n14;
                }
                if (cn2.f(n5, n13, n12 - 1).a()) {
                    ++n14;
                }
                if (cn2.f(n5, n13, n12 + 1).a()) {
                    ++n14;
                }
                if (n14 != 1) continue;
                cn2.d(n5, n13, n12, ly.av.bc);
                fe fe2 = (fe)cn2.b(n5, n13, n12);
                for (int i2 = 0; i2 < 8; ++i2) {
                    ev ev2 = this.a(random);
                    if (ev2 == null) continue;
                    fe2.a(random.nextInt(fe2.c()), ev2);
                }
                continue block6;
            }
        }
        cn2.d(n2, n3, n4, ly.at.bc);
        bd bd2 = (bd)cn2.b(n2, n3, n4);
        bd2.b = this.b(random);
        return true;
    }

    private ev a(Random random) {
        int n2 = random.nextInt(11);
        if (n2 == 0) {
            return new ev(di.ay);
        }
        if (n2 == 1) {
            return new ev(di.m, random.nextInt(4) + 1);
        }
        if (n2 == 2) {
            return new ev(di.S);
        }
        if (n2 == 3) {
            return new ev(di.R, random.nextInt(4) + 1);
        }
        if (n2 == 4) {
            return new ev(di.K, random.nextInt(4) + 1);
        }
        if (n2 == 5) {
            return new ev(di.I, random.nextInt(4) + 1);
        }
        if (n2 == 6) {
            return new ev(di.au);
        }
        if (n2 == 7 && random.nextInt(100) == 0) {
            return new ev(di.ar);
        }
        if (n2 == 8 && random.nextInt(2) == 0) {
            return new ev(di.aA, random.nextInt(4) + 1);
        }
        if (n2 == 9 && random.nextInt(10) == 0) {
            return new ev(di.c[di.aQ.aS + random.nextInt(2)]);
        }
        return null;
    }

    private String b(Random random) {
        int n2 = random.nextInt(4);
        if (n2 == 0) {
            return "Skeleton";
        }
        if (n2 == 1) {
            return "Zombie";
        }
        if (n2 == 2) {
            return "Zombie";
        }
        if (n2 == 3) {
            return "Spider";
        }
        return "";
    }
}

