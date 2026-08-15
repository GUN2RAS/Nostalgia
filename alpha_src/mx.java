/*
 * Decompiled with CFR 0.152.
 */
class mx {
    final int a;
    Object b;
    mx c;
    final int d;

    mx(int n2, int n3, Object object, mx mx2) {
        this.b = object;
        this.c = mx2;
        this.a = n3;
        this.d = n2;
    }

    public final int a() {
        return this.a;
    }

    public final Object b() {
        return this.b;
    }

    public final boolean equals(Object object) {
        Object object2;
        Object object3;
        Integer n2;
        if (!(object instanceof mx)) {
            return false;
        }
        mx mx2 = (mx)object;
        Integer n3 = this.a();
        return (n3 == (n2 = Integer.valueOf(mx2.a())) || n3 != null && ((Object)n3).equals(n2)) && ((object3 = this.b()) == (object2 = mx2.b()) || object3 != null && object3.equals(object2));
    }

    public final int hashCode() {
        return fi.d(this.a);
    }

    public final String toString() {
        return this.a() + "=" + this.b();
    }
}

