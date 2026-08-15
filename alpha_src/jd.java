/*
 * Decompiled with CFR 0.152.
 */
public class jd
extends kh {
    public int a = 0;

    public jd(cn cn2) {
        super(cn2);
        this.ad = true;
        this.a(0.98f, 0.98f);
        this.aB = this.aD / 2.0f;
    }

    public jd(cn cn2, float f2, float f3, float f4) {
        this(cn2);
        this.a((double)f2, (double)f3, (double)f4);
        float f5 = (float)(Math.random() * 3.1415927410125732 * 2.0);
        this.an = -eo.a(f5 * (float)Math.PI / 180.0f) * 0.02f;
        this.ao = 0.2f;
        this.ap = -eo.b(f5 * (float)Math.PI / 180.0f) * 0.02f;
        this.aG = false;
        this.a = 80;
        this.ah = f2;
        this.ai = f3;
        this.aj = f4;
    }

    public boolean c_() {
        return !this.aA;
    }

    public void e_() {
        this.ah = this.ak;
        this.ai = this.al;
        this.aj = this.am;
        this.ao -= (double)0.04f;
        this.c(this.an, this.ao, this.ap);
        this.an *= (double)0.98f;
        this.ao *= (double)0.98f;
        this.ap *= (double)0.98f;
        if (this.av) {
            this.an *= (double)0.7f;
            this.ap *= (double)0.7f;
            this.ao *= -0.5;
        }
        if (this.a-- <= 0) {
            this.F();
            this.i();
        } else {
            this.ag.a("smoke", this.ak, this.al + 0.5, this.am, 0.0, 0.0, 0.0);
        }
    }

    private void i() {
        float f2 = 4.0f;
        this.ag.a(null, this.ak, this.al, this.am, f2);
    }

    protected void a(hm hm2) {
        hm2.a("Fuse", (byte)this.a);
    }

    protected void b(hm hm2) {
        this.a = hm2.c("Fuse");
    }

    public float h_() {
        return 0.0f;
    }
}

