/*
 * Decompiled with CFR 0.152.
 */
public class gf
extends di {
    private int a;

    public gf(int n2, ly ly2) {
        super(n2);
        this.a = ly2.bc;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        if (cn2.a(n2, n3, n4) == ly.aT.bc) {
            n5 = 0;
        } else {
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
        }
        if (ev2.a == 0) {
            return false;
        }
        if (cn2.a(this.a, n2, n3, n4, false)) {
            ly ly2 = ly.n[this.a];
            if (cn2.d(n2, n3, n4, this.a)) {
                ly.n[this.a].d(cn2, n2, n3, n4, n5);
                cn2.a((float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, ly2.bl.d(), (ly2.bl.b() + 1.0f) / 2.0f, ly2.bl.c() * 0.8f);
                --ev2.a;
            }
        }
        return true;
    }
}

