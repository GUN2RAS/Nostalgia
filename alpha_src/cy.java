/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class cy {
    protected int a = 8;
    protected Random b = new Random();

    public void a(nw nw2, cn cn2, int n2, int n3, byte[] byArray) {
        int n4 = this.a;
        this.b.setSeed(cn2.u);
        long l2 = this.b.nextLong() / 2L * 2L + 1L;
        long l3 = this.b.nextLong() / 2L * 2L + 1L;
        for (int i2 = n2 - n4; i2 <= n2 + n4; ++i2) {
            for (int i3 = n3 - n4; i3 <= n3 + n4; ++i3) {
                this.b.setSeed((long)i2 * l2 + (long)i3 * l3 ^ cn2.u);
                this.a(cn2, i2, i3, n2, n3, byArray);
            }
        }
    }

    protected void a(cn cn2, int n2, int n3, int n4, int n5, byte[] byArray) {
    }
}

