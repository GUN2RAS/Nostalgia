/*
 * Decompiled with CFR 0.152.
 */
public class fx {
    private a[] a = new a[1024];
    private int b = 0;

    public a a(a a2) {
        if (a2.e >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.b == this.a.length) {
            a[] aArray = new a[this.b << 1];
            System.arraycopy(this.a, 0, aArray, 0, this.b);
            this.a = aArray;
        }
        this.a[this.b] = a2;
        a2.e = this.b;
        this.a(this.b++);
        return a2;
    }

    public void a() {
        this.b = 0;
    }

    public a b() {
        a a2 = this.a[0];
        this.a[0] = this.a[--this.b];
        this.a[this.b] = null;
        if (this.b > 0) {
            this.b(0);
        }
        a2.e = -1;
        return a2;
    }

    public void a(a a2, float f2) {
        float f3 = a2.h;
        a2.h = f2;
        if (f2 < f3) {
            this.a(a2.e);
        } else {
            this.b(a2.e);
        }
    }

    private void a(int n2) {
        a a2 = this.a[n2];
        float f2 = a2.h;
        while (n2 > 0) {
            int n3 = n2 - 1 >> 1;
            a a3 = this.a[n3];
            if (!(f2 < a3.h)) break;
            this.a[n2] = a3;
            a3.e = n2;
            n2 = n3;
        }
        this.a[n2] = a2;
        a2.e = n2;
    }

    private void b(int n2) {
        a a2 = this.a[n2];
        float f2 = a2.h;
        while (true) {
            float f3;
            a a3;
            int n3 = 1 + (n2 << 1);
            int n4 = n3 + 1;
            if (n3 >= this.b) break;
            a a4 = this.a[n3];
            float f4 = a4.h;
            if (n4 >= this.b) {
                a3 = null;
                f3 = Float.POSITIVE_INFINITY;
            } else {
                a3 = this.a[n4];
                f3 = a3.h;
            }
            if (f4 < f3) {
                if (!(f4 < f2)) break;
                this.a[n2] = a4;
                a4.e = n2;
                n2 = n3;
                continue;
            }
            if (!(f3 < f2)) break;
            this.a[n2] = a3;
            a3.e = n2;
            n2 = n4;
        }
        this.a[n2] = a2;
        a2.e = n2;
    }

    public boolean c() {
        return this.b == 0;
    }
}

