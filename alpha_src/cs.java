/*
 * Decompiled with CFR 0.152.
 */
public class cs
extends ly {
    protected cs(int n2) {
        super(n2, gb.c);
        this.bb = 59;
    }

    public int a(int n2) {
        if (n2 == 1) {
            return this.bb - 16;
        }
        if (n2 == 0) {
            return ly.y.a(0);
        }
        if (n2 == 2 || n2 == 4) {
            return this.bb + 1;
        }
        return this.bb;
    }

    public boolean a(cn cn2, int n2, int n3, int n4, dm dm2) {
        dm2.l();
        return true;
    }
}

