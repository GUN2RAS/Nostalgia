/*
 * Decompiled with CFR 0.152.
 */
import java.util.Comparator;

public class gl
implements Comparator {
    private dm a;

    public gl(dm dm2) {
        this.a = dm2;
    }

    public int a(bn bn2, bn bn3) {
        boolean bl2 = bn2.o;
        boolean bl3 = bn3.o;
        if (bl2 && !bl3) {
            return 1;
        }
        if (bl3 && !bl2) {
            return -1;
        }
        return bn2.a(this.a) < bn3.a(this.a) ? 1 : -1;
    }
}

