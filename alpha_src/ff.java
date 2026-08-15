/*
 * Decompiled with CFR 0.152.
 */
public class ff
extends kh {
    public int a;
    public int b = 0;

    public ff(cn cn2) {
        super(cn2);
    }

    public ff(cn cn2, float f2, float f3, float f4, int n2) {
        super(cn2);
        this.a = n2;
        this.ad = true;
        this.a(0.98f, 0.98f);
        this.aB = this.aD / 2.0f;
        this.a((double)f2, (double)f3, (double)f4);
        this.an = 0.0;
        this.ao = 0.0;
        this.ap = 0.0;
        this.aG = false;
        this.ah = f2;
        this.ai = f3;
        this.aj = f4;
    }

    public boolean c_() {
        return !this.aA;
    }

    public void e_() {
        if (this.a == 0) {
            this.F();
            return;
        }
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        ++this.b;
        this.ao -= (double)0.04f;
        this.c(this.an, this.ao, this.ap);
        this.an *= (double)0.98f;
        this.ao *= (double)0.98f;
        this.ap *= (double)0.98f;
        int n2 = eo.b(this.ak);
        int n3 = eo.b(this.al);
        int n4 = eo.b(this.am);
        if (this.ag.a(n2, n3, n4) == this.a) {
            this.ag.d(n2, n3, n4, 0);
        }
        if (this.av) {
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
            this.ao *= -0.5;
            this.F();
            if (!this.ag.a(this.a, n2, n3, n4, true) || !this.ag.d(n2, n3, n4, this.a)) {
                this.b(this.a, 1);
            }
        } else if (this.b > 100) {
            this.b(this.a, 1);
            this.F();
        }
    }

    protected void a(hm hm2) {
        hm2.a("Tile", (byte)this.a);
    }

    protected void b(hm hm2) {
        this.a = hm2.c("Tile") & 0xFF;
    }

    public float h_() {
        return 0.0f;
    }

    public cn i() {
        return this.ag;
    }
}

