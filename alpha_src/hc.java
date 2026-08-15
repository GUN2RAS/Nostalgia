/*
 * Decompiled with CFR 0.152.
 */
final class hc {
    public final int a;
    public final int b;

    public hc(int n2, int n3) {
        this.a = n2;
        this.b = n3;
    }

    public boolean equals(Object object) {
        if (object instanceof hc) {
            hc hc2 = (hc)object;
            return this.a == hc2.a && this.b == hc2.b;
        }
        return false;
    }

    public int hashCode() {
        return this.a << 16 ^ this.b;
    }
}

