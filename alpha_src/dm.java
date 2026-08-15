/*
 * Decompiled with CFR 0.152.
 */
import java.util.List;

public class dm
extends ge {
    public eu b = new eu(this);
    public byte c = 0;
    public int d = 0;
    public float e;
    public float f;
    public boolean g = false;
    public int h = 0;
    public String i;
    private int a = 0;

    public dm(cn cn2) {
        super(cn2);
        this.aB = 1.62f;
        this.c((double)cn2.o + 0.5, cn2.p + 1, (double)cn2.q + 0.5, 0.0f, 0.0f);
        this.E = 20;
        this.x = "humanoid";
        this.w = 180.0f;
        this.aS = 20;
        this.u = "/char.png";
    }

    public void p() {
        super.p();
        this.e = this.f;
        this.f = 0.0f;
    }

    public void q() {
        this.aB = 1.62f;
        this.a(0.6f, 1.8f);
        super.q();
        this.E = 20;
        this.J = 0;
    }

    protected void b_() {
        if (this.g) {
            ++this.h;
            if (this.h == 8) {
                this.h = 0;
                this.g = false;
            }
        } else {
            this.h = 0;
        }
        this.D = (float)this.h / 8.0f;
    }

    public void j() {
        List list;
        if (this.ag.l == 0 && this.E < 20 && this.aR % 20 * 4 == 0) {
            this.b(1);
        }
        this.b.b();
        this.e = this.f;
        super.j();
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
        if (this.E > 0 && (list = this.ag.b(this, this.au.b(1.0, 0.0, 1.0))) != null) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                this.h((kh)list.get(i2));
            }
        }
    }

    private void h(kh kh2) {
        kh2.b(this);
    }

    public int r() {
        return this.d;
    }

    public void b(kh kh2) {
        this.a(0.2f, 0.2f);
        this.a(this.ak, this.al, this.am);
        this.ao = 0.1f;
        if (this.i.equals("Notch")) {
            this.a(new ev(di.h, 1), true);
        }
        this.b.g();
        if (kh2 != null) {
            this.an = -eo.b((this.I + this.aq) * (float)Math.PI / 180.0f) * 0.1f;
            this.ap = -eo.a((this.I + this.aq) * (float)Math.PI / 180.0f) * 0.1f;
        } else {
            this.ap = 0.0;
            this.an = 0.0;
        }
        this.aB = 0.1f;
    }

    public void b(kh kh2, int n2) {
        this.d += n2;
    }

    public void a(ev ev2) {
        this.a(ev2, false);
    }

    public void a(ev ev2, boolean bl2) {
        if (ev2 == null) {
            return;
        }
        dx dx2 = new dx(this.ag, this.ak, this.al - (double)0.3f + (double)this.s(), this.am, ev2);
        dx2.c = 40;
        float f2 = 0.1f;
        if (bl2) {
            float f3 = this.aQ.nextFloat() * 0.5f;
            float f4 = this.aQ.nextFloat() * (float)Math.PI * 2.0f;
            dx2.an = -eo.a(f4) * f3;
            dx2.ap = eo.b(f4) * f3;
            dx2.ao = 0.2f;
        } else {
            f2 = 0.3f;
            dx2.an = -eo.a(this.aq / 180.0f * (float)Math.PI) * eo.b(this.ar / 180.0f * (float)Math.PI) * f2;
            dx2.ap = eo.b(this.aq / 180.0f * (float)Math.PI) * eo.b(this.ar / 180.0f * (float)Math.PI) * f2;
            dx2.ao = -eo.a(this.ar / 180.0f * (float)Math.PI) * f2 + 0.1f;
            f2 = 0.02f;
            float f5 = this.aQ.nextFloat() * (float)Math.PI * 2.0f;
            dx2.an += Math.cos(f5) * (double)(f2 *= this.aQ.nextFloat());
            dx2.ao += (double)((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.1f);
            dx2.ap += Math.sin(f5) * (double)f2;
        }
        this.a(dx2);
    }

    protected void a(dx dx2) {
        this.ag.a(dx2);
    }

    public float a(ly ly2) {
        float f2 = this.b.a(ly2);
        if (this.a(gb.f)) {
            f2 /= 5.0f;
        }
        if (!this.av) {
            f2 /= 5.0f;
        }
        return f2;
    }

    public boolean b(ly ly2) {
        return this.b.b(ly2);
    }

    public void b(hm hm2) {
        super.b(hm2);
        ki ki2 = hm2.l("Inventory");
        this.b.b(ki2);
    }

    public void a(hm hm2) {
        super.a(hm2);
        hm2.a("Inventory", this.b.a(new ki()));
    }

    public void a(gh gh2) {
    }

    public void l() {
    }

    public void a_(kh kh2, int n2) {
    }

    protected float s() {
        return 0.12f;
    }

    public boolean a(kh kh2, int n2) {
        this.U = 0;
        if (this.E <= 0) {
            return false;
        }
        if ((float)this.aW > (float)this.j / 2.0f) {
            return false;
        }
        if (kh2 instanceof dq || kh2 instanceof kg) {
            if (this.ag.l == 0) {
                n2 = 0;
            }
            if (this.ag.l == 1) {
                n2 = n2 / 3 + 1;
            }
            if (this.ag.l == 3) {
                n2 = n2 * 3 / 2;
            }
        }
        int n3 = 25 - this.b.f();
        int n4 = n2 * n3 + this.a;
        this.b.e(n2);
        n2 = n4 / 25;
        this.a = n4 % 25;
        if (n2 == 0) {
            return false;
        }
        return super.a(kh2, n2);
    }

    public void a(ke ke2) {
    }

    public void a(ob ob2) {
    }

    public void a_(kh kh2) {
    }

    public ev t() {
        return this.b.a();
    }

    public void u() {
        this.b.a(this.b.d, null);
    }

    public double v() {
        return this.aB - 0.5f;
    }

    public void w() {
        this.h = -1;
        this.g = true;
    }
}

