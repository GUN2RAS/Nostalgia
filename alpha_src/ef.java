/*
 * Decompiled with CFR 0.152.
 */
public class ef
extends di {
    public ef(int n2) {
        super(n2);
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        if (n5 == 0) {
            --n3;
        }
        if (n5 == 1) {
            ++n3;
        }
        if (n5 == 2) {
            --n4;
        }
        if (n5 == 3) {
            ++n4;
        }
        if (n5 == 4) {
            --n2;
        }
        if (n5 == 5) {
            ++n2;
        }
        if (cn2.a(n2, n3, n4) != 0) {
            return false;
        }
        if (ly.aw.a(cn2, n2, n3, n4)) {
            --ev2.a;
            cn2.d(n2, n3, n4, ly.aw.bc);
        }
        return true;
    }
}

