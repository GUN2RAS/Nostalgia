/*
 * Decompiled with CFR 0.152.
 */
public class mt {
    public final int a;
    public final int b;
    public final int c;

    public mt(int n2, int n3, int n4) {
        this.a = n2;
        this.b = n3;
        this.c = n4;
    }

    public boolean equals(Object object) {
        if (object instanceof mt) {
            mt mt2 = (mt)object;
            return mt2.a == this.a && mt2.b == this.b && mt2.c == this.c;
        }
        return false;
    }

    public int hashCode() {
        return this.a * 8976890 + this.b * 981131 + this.c;
    }
}

