/*
 * Decompiled with CFR 0.152.
 */
public class fi {
    private transient mx[] a = new mx[16];
    private transient int b;
    private int c = 12;
    private final float d;
    private volatile transient int e;

    public fi() {
        this.d = 0.75f;
    }

    private static int e(int n2) {
        n2 ^= n2 >>> 20 ^ n2 >>> 12;
        return n2 ^ n2 >>> 7 ^ n2 >>> 4;
    }

    private static int a(int n2, int n3) {
        return n2 & n3 - 1;
    }

    public Object a(int n2) {
        int n3 = fi.e(n2);
        mx mx2 = this.a[fi.a(n3, this.a.length)];
        while (mx2 != null) {
            if (mx2.a == n2) {
                return mx2.b;
            }
            mx2 = mx2.c;
        }
        return null;
    }

    public void a(int n2, Object object) {
        int n3 = fi.e(n2);
        int n4 = fi.a(n3, this.a.length);
        mx mx2 = this.a[n4];
        while (mx2 != null) {
            if (mx2.a == n2) {
                mx2.b = object;
            }
            mx2 = mx2.c;
        }
        ++this.e;
        this.a(n3, n2, object, n4);
    }

    private void f(int n2) {
        mx[] mxArray = this.a;
        int n3 = mxArray.length;
        if (n3 == 0x40000000) {
            this.c = Integer.MAX_VALUE;
            return;
        }
        mx[] mxArray2 = new mx[n2];
        this.a(mxArray2);
        this.a = mxArray2;
        this.c = (int)((float)n2 * this.d);
    }

    private void a(mx[] mxArray) {
        mx[] mxArray2 = this.a;
        int n2 = mxArray.length;
        for (int i2 = 0; i2 < mxArray2.length; ++i2) {
            mx mx2;
            mx mx3 = mxArray2[i2];
            if (mx3 == null) continue;
            mxArray2[i2] = null;
            do {
                mx2 = mx3.c;
                int n3 = fi.a(mx3.d, n2);
                mx3.c = mxArray[n3];
                mxArray[n3] = mx3;
            } while ((mx3 = mx2) != null);
        }
    }

    public Object b(int n2) {
        mx mx2 = this.c(n2);
        return mx2 == null ? null : mx2.b;
    }

    final mx c(int n2) {
        mx mx2;
        int n3 = fi.e(n2);
        int n4 = fi.a(n3, this.a.length);
        mx mx3 = mx2 = this.a[n4];
        while (mx3 != null) {
            mx mx4 = mx3.c;
            if (mx3.a == n2) {
                ++this.e;
                --this.b;
                if (mx2 == mx3) {
                    this.a[n4] = mx4;
                } else {
                    mx2.c = mx4;
                }
                return mx3;
            }
            mx2 = mx3;
            mx3 = mx4;
        }
        return mx3;
    }

    public void a() {
        ++this.e;
        mx[] mxArray = this.a;
        for (int i2 = 0; i2 < mxArray.length; ++i2) {
            mxArray[i2] = null;
        }
        this.b = 0;
    }

    private void a(int n2, int n3, Object object, int n4) {
        mx mx2 = this.a[n4];
        this.a[n4] = new mx(n2, n3, object, mx2);
        if (this.b++ >= this.c) {
            this.f(2 * this.a.length);
        }
    }

    static /* synthetic */ int d(int n2) {
        return fi.e(n2);
    }
}

