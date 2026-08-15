/*
 * Decompiled with CFR 0.152.
 */
public class ek
extends ge {
    private bl a;
    protected kh f;
    protected boolean g = false;

    public ek(cn cn2) {
        super(cn2);
    }

    protected void b_() {
        int n2;
        int n3;
        this.g = false;
        float f2 = 16.0f;
        if (this.f == null) {
            this.f = this.i();
            if (this.f != null) {
                this.a = this.ag.a(this, this.f, f2);
            }
        } else if (!this.f.B()) {
            this.f = null;
        } else {
            float f3 = this.f.d(this);
            if (this.c(this.f)) {
                this.a(this.f, f3);
            }
        }
        if (!(this.g || this.f == null || this.a != null && this.aQ.nextInt(20) != 0)) {
            this.a = this.ag.a(this, this.f, f2);
        } else if (this.a == null && this.aQ.nextInt(80) == 0 || this.aQ.nextInt(80) == 0) {
            boolean bl2 = false;
            n3 = -1;
            n2 = -1;
            int n4 = -1;
            float f4 = -99999.0f;
            for (int i2 = 0; i2 < 10; ++i2) {
                int n5;
                int n6;
                int n7 = eo.b(this.ak + (double)this.aQ.nextInt(13) - 6.0);
                float f5 = this.a(n7, n6 = eo.b(this.al + (double)this.aQ.nextInt(7) - 3.0), n5 = eo.b(this.am + (double)this.aQ.nextInt(13) - 6.0));
                if (!(f5 > f4)) continue;
                f4 = f5;
                n3 = n7;
                n2 = n6;
                n4 = n5;
                bl2 = true;
            }
            if (bl2) {
                this.a = this.ag.a((kh)this, n3, n2, n4, 10.0f);
            }
        }
        int n8 = eo.b(this.au.b);
        n3 = this.g_() ? 1 : 0;
        n2 = this.G() ? 1 : 0;
        this.ar = 0.0f;
        if (this.a == null || this.aQ.nextInt(100) == 0) {
            super.b_();
            this.a = null;
            return;
        }
        aj aj2 = this.a.a(this);
        double d2 = this.aC * 2.0f;
        while (aj2 != null && aj2.d(this.ak, aj2.b, this.am) < d2 * d2) {
            this.a.a();
            if (this.a.b()) {
                aj2 = null;
                this.a = null;
                continue;
            }
            aj2 = this.a.a(this);
        }
        this.Y = false;
        if (aj2 != null) {
            float f6;
            double d3 = aj2.a - this.ak;
            double d4 = aj2.c - this.am;
            double d5 = aj2.b - (double)n8;
            float f7 = (float)(Math.atan2(d4, d3) * 180.0 / 3.1415927410125732) - 90.0f;
            this.W = this.aa;
            for (f6 = f7 - this.aq; f6 < -180.0f; f6 += 360.0f) {
            }
            while (f6 >= 180.0f) {
                f6 -= 360.0f;
            }
            if (f6 > 30.0f) {
                f6 = 30.0f;
            }
            if (f6 < -30.0f) {
                f6 = -30.0f;
            }
            this.aq += f6;
            if (this.g && this.f != null) {
                double d6 = this.f.ak - this.ak;
                double d7 = this.f.am - this.am;
                float f8 = this.aq;
                this.aq = (float)(Math.atan2(d7, d6) * 180.0 / 3.1415927410125732) - 90.0f;
                f6 = (f8 - this.aq + 90.0f) * (float)Math.PI / 180.0f;
                this.V = -eo.a(f6) * this.W * 1.0f;
                this.W = eo.b(f6) * this.W * 1.0f;
            }
            if (d5 > 0.0) {
                this.Y = true;
            }
        }
        if (this.f != null) {
            this.b(this.f, 30.0f);
        }
        if (this.aw) {
            this.Y = true;
        }
        if (this.aQ.nextFloat() < 0.8f && (n3 != 0 || n2 != 0)) {
            this.Y = true;
        }
    }

    protected void a(kh kh2, float f2) {
    }

    protected float a(int n2, int n3, int n4) {
        return 0.0f;
    }

    protected kh i() {
        return null;
    }

    public boolean a() {
        int n2 = eo.b(this.ak);
        int n3 = eo.b(this.au.b);
        int n4 = eo.b(this.am);
        return super.a() && this.a(n2, n3, n4) >= 0.0f;
    }
}

