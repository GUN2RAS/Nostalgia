/*
 * Decompiled with CFR 0.152.
 */
import java.util.Comparator;

public class fb
implements Comparator {
    private kh a;

    public fb(kh kh2) {
        this.a = kh2;
    }

    public int a(bn bn2, bn bn3) {
        return bn2.a(this.a) < bn3.a(this.a) ? -1 : 1;
    }
}

