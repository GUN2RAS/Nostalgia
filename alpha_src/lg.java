/*
 * Decompiled with CFR 0.152.
 */
public class lg
extends di {
    private String a;

    protected lg(int n2, String string) {
        super(n2);
        this.a = string;
        this.aT = 1;
    }

    public boolean a(ev ev2, dm dm2, cn cn2, int n2, int n3, int n4, int n5) {
        if (cn2.a(n2, n3, n4) == ly.aZ.bc && cn2.e(n2, n3, n4) == 0) {
            cn2.b(n2, n3, n4, this.aS - di.aQ.aS + 1);
            cn2.a(this.a, n2, n3, n4);
            --ev2.a;
            return true;
        }
        return false;
    }
}

