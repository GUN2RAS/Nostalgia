/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class es
extends ik {
    public boolean a(cn cn2, Random random, int n2, int n3, int n4) {
        for (int i2 = 0; i2 < 20; ++i2) {
            int n5;
            int n6;
            int n7 = n2 + random.nextInt(4) - random.nextInt(4);
            if (cn2.a(n7, n6 = n3, n5 = n4 + random.nextInt(4) - random.nextInt(4)) != 0 || cn2.f(n7 - 1, n6 - 1, n5) != gb.f && cn2.f(n7 + 1, n6 - 1, n5) != gb.f && cn2.f(n7, n6 - 1, n5 - 1) != gb.f && cn2.f(n7, n6 - 1, n5 + 1) != gb.f) continue;
            int n8 = 2 + random.nextInt(random.nextInt(3) + 1);
            for (int i3 = 0; i3 < n8; ++i3) {
                if (!ly.aY.g(cn2, n7, n6 + i3, n5)) continue;
                cn2.a(n7, n6 + i3, n5, ly.aY.bc);
            }
        }
        return true;
    }
}

