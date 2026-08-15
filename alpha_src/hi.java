/*
 * Decompiled with CFR 0.152.
 */
public class hi
extends ly {
    protected boolean a;

    protected hi(int n2, int n3, gb gb2, boolean bl2) {
        super(n2, n3, gb2);
        this.a = bl2;
    }

    public boolean b() {
        return false;
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        int n6 = nm2.a(n2, n3, n4);
        if (!this.a && n6 == this.bc) {
            return false;
        }
        return super.c(nm2, n2, n3, n4, n5);
    }
}

