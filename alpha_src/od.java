/*
 * Decompiled with CFR 0.152.
 */
public class od
extends di {
    public od(int n2) {
        super(n2);
        this.aU = 64;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        jc jc2;
        if (n5 == 0) {
            return false;
        }
        if (n5 == 1) {
            return false;
        }
        int n6 = 0;
        if (n5 == 4) {
            n6 = 1;
        }
        if (n5 == 3) {
            n6 = 2;
        }
        if (n5 == 5) {
            n6 = 3;
        }
        if ((jc2 = new jc(cn2, n2, n3, n4, n6)).i()) {
            cn2.a(jc2);
            --ev2.a;
        }
        return true;
    }
}

