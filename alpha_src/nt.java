/*
 * Decompiled with CFR 0.152.
 */
public class nt
extends dm {
    private int bg;
    private double bh;
    private double bi;
    private double bj;
    private double bk;
    private double bl;
    float a = 0.0f;

    public nt(cn cn2, String string) {
        super(cn2);
        this.i = string;
        this.aB = 0.0f;
        this.aM = 0.0f;
        if (string != null && string.length() > 0) {
            this.aY = "http://www.minecraft.net/skin/" + string + ".png";
            System.out.println("Loading texture " + this.aY);
        }
        this.aN = true;
        this.ac = 10.0;
    }

    public boolean a(kh kh2, int n2) {
        return true;
    }

    public void a(double d2, double d3, double d4, float f2, float f3, int n2) {
        this.aB = 0.0f;
        this.bh = d2;
        this.bi = d3;
        this.bj = d4;
        this.bk = f2;
        this.bl = f3;
        this.bg = n2;
    }

    public void e_() {
        super.e_();
        this.Q = this.R;
        double d2 = this.ak - this.ah;
        double d3 = this.am - this.aj;
        float f2 = eo.a(d2 * d2 + d3 * d3) * 4.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        this.R += (f2 - this.R) * 0.4f;
        this.S += this.R;
    }

    public float h_() {
        return 0.0f;
    }

    public void j() {
        super.b_();
        if (this.bg > 0) {
            double d2;
            double d3 = this.ak + (this.bh - this.ak) / (double)this.bg;
            double d4 = this.al + (this.bi - this.al) / (double)this.bg;
            double d5 = this.am + (this.bj - this.am) / (double)this.bg;
            for (d2 = this.bk - (double)this.aq; d2 < -180.0; d2 += 360.0) {
            }
            while (d2 >= 180.0) {
                d2 -= 360.0;
            }
            this.aq = (float)((double)this.aq + d2 / (double)this.bg);
            this.ar = (float)((double)this.ar + (this.bl - (double)this.ar) / (double)this.bg);
            --this.bg;
            this.a(d3, d4, d5);
            this.c(this.aq, this.ar);
        }
        this.e = this.f;
        float f2 = eo.a(this.an * this.an + this.ap * this.ap);
        float f3 = (float)Math.atan(-this.ao * (double)0.2f) * 15.0f;
        if (f2 > 0.1f) {
            f2 = 0.1f;
        }
        if (!this.av || this.E <= 0) {
            f2 = 0.0f;
        }
        if (this.av || this.E <= 0) {
            f3 = 0.0f;
        }
        this.f += (f2 - this.f) * 0.4f;
        this.M += (f3 - this.M) * 0.8f;
    }
}

