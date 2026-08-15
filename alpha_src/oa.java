/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class oa
extends ik {
    public boolean a(cn cn2, Random random, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10 = random.nextInt(3) + 4;
        boolean bl2 = true;
        if (n3 < 1 || n3 + n10 + 1 > 128) {
            return false;
        }
        for (n9 = n3; n9 <= n3 + 1 + n10; ++n9) {
            n8 = 1;
            if (n9 == n3) {
                n8 = 0;
            }
            if (n9 >= n3 + 1 + n10 - 2) {
                n8 = 2;
            }
            for (n7 = n2 - n8; n7 <= n2 + n8 && bl2; ++n7) {
                for (n6 = n4 - n8; n6 <= n4 + n8 && bl2; ++n6) {
                    if (n9 >= 0 && n9 < 128) {
                        n5 = cn2.a(n7, n9, n6);
                        if (n5 == 0 || n5 == ly.L.bc) continue;
                        bl2 = false;
                        continue;
                    }
                    bl2 = false;
                }
            }
        }
        if (!bl2) {
            return false;
        }
        n9 = cn2.a(n2, n3 - 1, n4);
        if (n9 != ly.v.bc && n9 != ly.w.bc || n3 >= 128 - n10 - 1) {
            return false;
        }
        cn2.a(n2, n3 - 1, n4, ly.w.bc);
        for (n8 = n3 - 3 + n10; n8 <= n3 + n10; ++n8) {
            n7 = n8 - (n3 + n10);
            n6 = 1 - n7 / 2;
            for (n5 = n2 - n6; n5 <= n2 + n6; ++n5) {
                int n11 = n5 - n2;
                for (int i2 = n4 - n6; i2 <= n4 + n6; ++i2) {
                    int n12 = i2 - n4;
                    if (Math.abs(n11) == n6 && Math.abs(n12) == n6 && (random.nextInt(2) == 0 || n7 == 0) || ly.p[cn2.a(n5, n8, i2)]) continue;
                    cn2.a(n5, n8, i2, ly.L.bc);
                }
            }
        }
        for (n8 = 0; n8 < n10; ++n8) {
            n7 = cn2.a(n2, n3 + n8, n4);
            if (n7 != 0 && n7 != ly.L.bc) continue;
            cn2.a(n2, n3 + n8, n4, ly.K.bc);
        }
        return true;
    }
}

