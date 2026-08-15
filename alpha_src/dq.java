/*
 * Decompiled with CFR 0.152.
 */
public class dq
extends ek
implements co {
    protected int e = 2;

    public dq(cn cn2) {
        super(cn2);
        this.E = 20;
    }

    public void j() {
        float f2 = this.a(1.0f);
        if (f2 > 0.5f) {
            this.U += 2;
        }
        super.j();
    }

    public void e_() {
        super.e_();
        if (this.ag.l == 0) {
            this.F();
        }
    }

    protected kh i() {
        dm dm2 = this.ag.a((kh)this, 16.0);
        if (dm2 != null && this.c(dm2)) {
            return dm2;
        }
        return null;
    }

    public boolean a(kh kh2, int n2) {
        if (super.a(kh2, n2)) {
            if (this.ae == kh2 || this.af == kh2) {
                return true;
            }
            if (kh2 != this) {
                this.f = kh2;
            }
            return true;
        }
        return false;
    }

    protected void a(kh kh2, float f2) {
        if ((double)f2 < 2.5 && kh2.au.e > this.au.b && kh2.au.b < this.au.e) {
            this.K = 20;
            kh2.a(this, this.e);
        }
    }

    protected float a(int n2, int n3, int n4) {
        return 0.5f - this.ag.c(n2, n3, n4);
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
        if (this.ag.a(by.a, n4, n3 = eo.b(this.au.b), n2 = eo.b(this.am)) > this.aQ.nextInt(32)) {
            return false;
        }
        int n5 = this.ag.j(n4, n3, n2);
        return n5 <= this.aQ.nextInt(8) && super.a();
    }
}

