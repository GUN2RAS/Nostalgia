/*
 * Decompiled with CFR 0.152.
 */
public class jo
extends di {
    public int a;

    public jo(int n2, int n3) {
        super(n2);
        this.aT = 1;
        this.a = n3;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.a(n2, n3, n4);
        if (n6 == ly.aH.bc) {
            cn2.a(new oc(cn2, (float)n2 + 0.5f, (float)n3 + 0.5f, (float)n4 + 0.5f, this.a));
            --ev2.a;
            return true;
        }
        return false;
    }
}

