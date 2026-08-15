/*
 * Decompiled with CFR 0.152.
 */
public class ma
extends ge
implements co {
    public float a;
    public float b;
    private int d = 0;
    public int c = 1;

    public ma(cn cn2) {
        super(cn2);
        this.u = "/mob/slime.png";
        this.c = 1 << this.aQ.nextInt(3);
        this.aB = 0.0f;
        this.d = this.aQ.nextInt(20) + 10;
        this.c(this.c);
    }

    public void c(int n2) {
        this.c = n2;
        this.a(0.6f * (float)n2, 0.6f * (float)n2);
        this.E = n2 * n2;
        this.a(this.ak, this.al, this.am);
    }

    public void a(hm hm2) {
        super.a(hm2);
        hm2.a("Size", this.c - 1);
    }

    public void b(hm hm2) {
        super.b(hm2);
        this.c = hm2.e("Size") + 1;
    }

    public void e_() {
        this.b = this.a;
        boolean bl2 = this.av;
        super.e_();
        if (this.av && !bl2) {
            for (int i2 = 0; i2 < this.c * 8; ++i2) {
                float f2 = this.aQ.nextFloat() * (float)Math.PI * 2.0f;
                float f3 = this.aQ.nextFloat() * 0.5f + 0.5f;
                float f4 = eo.a(f2) * (float)this.c * 0.5f * f3;
                float f5 = eo.b(f2) * (float)this.c * 0.5f * f3;
                this.ag.a("slime", this.ak + (double)f4, this.au.b, this.am + (double)f5, 0.0, 0.0, 0.0);
            }
            if (this.c > 2) {
                this.ag.a(this, "mob.slime", this.f(), ((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            }
            this.a = -0.5f;
        }
        this.a *= 0.6f;
    }

    protected void b_() {
        dm dm2 = this.ag.a((kh)this, 16.0);
        if (dm2 != null) {
            this.b((kh)dm2, 10.0f);
        }
        if (this.av && this.d-- <= 0) {
            this.d = this.aQ.nextInt(20) + 10;
            if (dm2 != null) {
                this.d /= 3;
            }
            this.Y = true;
            if (this.c > 1) {
                this.ag.a(this, "mob.slime", this.f(), ((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f) * 0.8f);
            }
            this.a = 1.0f;
            this.V = 1.0f - this.aQ.nextFloat() * 2.0f;
            this.W = 1 * this.c;
        } else {
            this.Y = false;
            if (this.av) {
                this.W = 0.0f;
                this.V = 0.0f;
            }
        }
    }

    public void F() {
        if (this.c > 1 && this.E == 0) {
            for (int i2 = 0; i2 < 4; ++i2) {
                float f2 = ((float)(i2 % 2) - 0.5f) * (float)this.c / 4.0f;
                float f3 = ((float)(i2 / 2) - 0.5f) * (float)this.c / 4.0f;
                ma ma2 = new ma(this.ag);
                ma2.c(this.c / 2);
                ma2.c(this.ak + (double)f2, this.al + 0.5, this.am + (double)f3, this.aQ.nextFloat() * 360.0f, 0.0f);
                this.ag.a(ma2);
            }
        }
        super.F();
    }

    public void b(dm dm2) {
        if (this.c > 1 && this.c(dm2) && (double)this.d(dm2) < 0.6 * (double)this.c && dm2.a(this, this.c)) {
            this.ag.a(this, "mob.slimeattack", 1.0f, (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f);
        }
    }

    protected String d() {
        return "mob.slime";
    }

    protected String e() {
        return "mob.slime";
    }

    protected int g() {
        if (this.c == 1) {
            return di.aK.aS;
        }
        return 0;
    }

    public boolean a() {
        ga ga2 = this.ag.a(eo.b(this.ak), eo.b(this.al));
        return (this.c == 1 || this.ag.l > 0) && this.aQ.nextInt(10) == 0 && ga2.a(987234911L).nextInt(10) == 0 && this.al < 16.0;
    }

    protected float f() {
        return 0.6f;
    }
}

