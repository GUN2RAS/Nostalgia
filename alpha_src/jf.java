/*
 * Decompiled with CFR 0.152.
 */
public class jf
implements Comparable {
    private static long f = 0L;
    public int a;
    public int b;
    public int c;
    public int d;
    public long e;
    private long g = f++;

    public jf(int n2, int n3, int n4, int n5) {
        this.a = n2;
        this.b = n3;
        this.c = n4;
        this.d = n5;
    }

    public boolean equals(Object object) {
        if (object instanceof jf) {
            jf jf2 = (jf)object;
            return this.a == jf2.a && this.b == jf2.b && this.c == jf2.c && this.d == jf2.d;
        }
        return false;
    }

    public int hashCode() {
        return (this.a * 128 * 1024 + this.c * 128 + this.b) * 256 + this.d;
    }

    public jf a(long l2) {
        this.e = l2;
        return this;
    }

    public int a(jf jf2) {
        if (this.e < jf2.e) {
            return -1;
        }
        if (this.e > jf2.e) {
            return 1;
        }
        if (this.g < jf2.g) {
            return -1;
        }
        if (this.g > jf2.g) {
            return 1;
        }
        return 0;
    }
}

