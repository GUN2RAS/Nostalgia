/*
 * Decompiled with CFR 0.152.
 */
public class jn
extends di {
    private int a;

    public jn(int n2, int n3) {
        super(n2);
        this.a = n3;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        if (n5 != 1) {
            return false;
        }
        int n6 = cn2.a(n2, n3, n4);
        if (n6 == ly.aB.bc) {
            cn2.d(n2, n3 + 1, n4, this.a);
            --ev2.a;
            return true;
        }
        return false;
    }
}

