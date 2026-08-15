/*
 * Decompiled with CFR 0.152.
 */
public abstract class ag
extends ek {
    public ag(cn cn2) {
        super(cn2);
    }

    protected float a(int n2, int n3, int n4) {
        if (this.ag.a(n2, n3 - 1, n4) == ly.v.bc) {
            return 10.0f;
        }
        return this.ag.c(n2, n3, n4) - 0.5f;
    }

    public void a(hm hm2) {
        super.a(hm2);
    }

    public void b(hm hm2) {
        super.b(hm2);
    }

    public boolean a() {
        int n2;
        int n3;
        int n4 = eo.b(this.ak);
        return this.ag.a(n4, (n3 = eo.b(this.au.b)) - 1, n2 = eo.b(this.am)) == ly.v.bc && this.ag.j(n4, n3, n2) > 8 && super.a();
    }

    public int b() {
        return 120;
    }
}

