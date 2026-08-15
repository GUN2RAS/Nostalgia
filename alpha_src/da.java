/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class da
extends ik {
    public boolean a(cn cn2, Random random, int n2, int n3, int n4) {
        for (int i2 = 0; i2 < 10; ++i2) {
            int n5;
            int n6;
            int n7 = n2 + random.nextInt(8) - random.nextInt(8);
            if (cn2.a(n7, n6 = n3 + random.nextInt(4) - random.nextInt(4), n5 = n4 + random.nextInt(8) - random.nextInt(8)) != 0) continue;
            int n8 = 1 + random.nextInt(random.nextInt(3) + 1);
            for (int i3 = 0; i3 < n8; ++i3) {
                if (!ly.aW.g(cn2, n7, n6 + i3, n5)) continue;
                cn2.a(n7, n6 + i3, n5, ly.aW.bc);
            }
        }
        return true;
    }
}

