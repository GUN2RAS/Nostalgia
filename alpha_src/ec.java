/*
 * Decompiled with CFR 0.152.
 */
public class ec
extends di {
    private gb a;

    public ec(int n2, gb gb2) {
        super(n2);
        this.a = gb2;
        this.aU = 64;
        this.aT = 1;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        if (n5 != 1) {
            return false;
        }
        ly ly2 = this.a == gb.c ? ly.aF : ly.aM;
        if (!ly2.a(cn2, n2, ++n3, n4)) {
            return false;
        }
        int n6 = eo.b((double)((dm2.aq + 180.0f) * 4.0f / 360.0f) - 0.5) & 3;
        int n7 = 0;
        int n8 = 0;
        if (n6 == 0) {
            n8 = 1;
        }
        if (n6 == 1) {
            n7 = -1;
        }
        if (n6 == 2) {
            n8 = -1;
        }
        if (n6 == 3) {
            n7 = 1;
        }
        int n9 = (cn2.g(n2 - n7, n3, n4 - n8) ? 1 : 0) + (cn2.g(n2 - n7, n3 + 1, n4 - n8) ? 1 : 0);
        int n10 = (cn2.g(n2 + n7, n3, n4 + n8) ? 1 : 0) + (cn2.g(n2 + n7, n3 + 1, n4 + n8) ? 1 : 0);
        boolean bl2 = cn2.a(n2 - n7, n3, n4 - n8) == ly2.bc || cn2.a(n2 - n7, n3 + 1, n4 - n8) == ly2.bc;
        boolean bl3 = cn2.a(n2 + n7, n3, n4 + n8) == ly2.bc || cn2.a(n2 + n7, n3 + 1, n4 + n8) == ly2.bc;
        boolean bl4 = false;
        if (bl2 && !bl3) {
            bl4 = true;
        } else if (n10 > n9) {
            bl4 = true;
        }
        if (bl4) {
            n6 = n6 - 1 & 3;
            n6 += 4;
        }
        cn2.d(n2, n3, n4, ly2.bc);
        cn2.b(n2, n3, n4, n6);
        cn2.d(n2, n3 + 1, n4, ly2.bc);
        cn2.b(n2, n3 + 1, n4, n6 + 8);
        --ev2.a;
        return true;
    }
}

