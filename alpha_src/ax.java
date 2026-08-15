/*
 * Decompiled with CFR 0.152.
 */
public class ax
extends dq {
    public ax(cn cn2) {
        super(cn2);
        this.u = "/mob/spider.png";
        this.a(1.4f, 0.9f);
        this.aa = 0.8f;
    }

    public double h() {
        return (double)this.aD * 0.75 - 0.5;
    }

    protected kh i() {
        float f2 = this.a(1.0f);
        if (f2 < 0.5f) {
            double d2 = 16.0;
            return this.ag.a((kh)this, d2);
        }
        return null;
    }

    protected String c() {
        return "mob.spider";
    }

    protected String d() {
        return "mob.spider";
    }

    protected String e() {
        return "mob.spiderdeath";
    }

    protected void a(kh kh2, float f2) {
        float f3 = this.a(1.0f);
        if (f3 > 0.5f && this.aQ.nextInt(100) == 0) {
            this.f = null;
            return;
        }
        if (f2 > 2.0f && f2 < 6.0f && this.aQ.nextInt(10) == 0) {
            if (this.av) {
                double d2 = kh2.ak - this.ak;
                double d3 = kh2.am - this.am;
                float f4 = eo.a(d2 * d2 + d3 * d3);
                this.an = d2 / (double)f4 * 0.5 * (double)0.8f + this.an * (double)0.2f;
                this.ap = d3 / (double)f4 * 0.5 * (double)0.8f + this.ap * (double)0.2f;
                this.ao = 0.4f;
            }
        } else {
            super.a(kh2, f2);
        }
    }

    public void a(hm hm2) {
        super.a(hm2);
    }

    public void b(hm hm2) {
        super.b(hm2);
    }

    protected int g() {
        return di.I.aS;
    }
}

