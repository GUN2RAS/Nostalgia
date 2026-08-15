/*
 * Decompiled with CFR 0.152.
 */
public class md
extends di {
    public md(int n2) {
        super(n2);
        this.aU = 64;
        this.aT = 1;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        if (n5 == 0) {
            return false;
        }
        if (!cn2.f(n2, n3, n4).a()) {
            return false;
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
        if (!ly.aE.a(cn2, n2, n3, n4)) {
            return false;
        }
        if (n5 == 1) {
            cn2.b(n2, n3, n4, ly.aE.bc, eo.b((double)((dm2.aq + 180.0f) * 16.0f / 360.0f) + 0.5) & 0xF);
        } else {
            cn2.b(n2, n3, n4, ly.aJ.bc, n5);
        }
        --ev2.a;
        dm2.a((ob)cn2.b(n2, n3, n4));
        return true;
    }
}

