/*
 * Decompiled with CFR 0.152.
 */
import java.util.Comparator;

class fs
implements Comparator {
    final /* synthetic */ dw a;

    fs(dw dw2) {
        this.a = dw2;
    }

    public int a(bv bv2, bv bv3) {
        if (bv3.a() < bv2.a()) {
            return -1;
        }
        if (bv3.a() > bv2.a()) {
            return 1;
        }
        return 0;
    }
}

