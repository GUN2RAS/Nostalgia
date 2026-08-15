/*
 * Decompiled with CFR 0.152.
 */
public class ol {
    public int a;
    public int b;

    public ol(int n2, int n3) {
        this.a = n2;
        this.b = n3;
    }

    public int hashCode() {
        return this.a << 8 | this.b;
    }

    public boolean equals(Object object) {
        ol ol2 = (ol)object;
        return ol2.a == this.a && ol2.b == this.b;
    }
}

