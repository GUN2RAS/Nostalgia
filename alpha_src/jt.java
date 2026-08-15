/*
 * Decompiled with CFR 0.152.
 */
public abstract class jt
extends ly {
    protected jt(int n2, gb gb2) {
        super(n2, gb2);
        jt.q[n2] = true;
    }

    protected jt(int n2, int n3, gb gb2) {
        super(n2, n3, gb2);
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        super.e(cn2, n2, n3, n4);
        cn2.a(n2, n3, n4, this.a_());
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        super.b(cn2, n2, n3, n4);
        cn2.l(n2, n3, n4);
    }

    protected abstract ic a_();
}

