/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class nn
extends ik {
    private int a;

    public nn(int n2) {
        this.a = n2;
    }

    public boolean a(cn cn2, Random random, int n2, int n3, int n4) {
        if (cn2.a(n2, n3 + 1, n4) != ly.u.bc) {
            return false;
        }
        if (cn2.a(n2, n3 - 1, n4) != ly.u.bc) {
            return false;
        }
        if (cn2.a(n2, n3, n4) != 0 && cn2.a(n2, n3, n4) != ly.u.bc) {
            return false;
        }
        int n5 = 0;
        if (cn2.a(n2 - 1, n3, n4) == ly.u.bc) {
            ++n5;
        }
        if (cn2.a(n2 + 1, n3, n4) == ly.u.bc) {
            ++n5;
        }
        if (cn2.a(n2, n3, n4 - 1) == ly.u.bc) {
            ++n5;
        }
        if (cn2.a(n2, n3, n4 + 1) == ly.u.bc) {
            ++n5;
        }
        int n6 = 0;
        if (cn2.a(n2 - 1, n3, n4) == 0) {
            ++n6;
        }
        if (cn2.a(n2 + 1, n3, n4) == 0) {
            ++n6;
        }
        if (cn2.a(n2, n3, n4 - 1) == 0) {
            ++n6;
        }
        if (cn2.a(n2, n3, n4 + 1) == 0) {
            ++n6;
        }
        if (n5 == 3 && n6 == 1) {
            cn2.d(n2, n3, n4, this.a);
        }
        return true;
    }
}

