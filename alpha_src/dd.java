/*
 * Decompiled with CFR 0.152.
 */
public class dd
extends dq {
    int a;
    int b;
    int c = 30;
    int d = -1;

    public dd(cn cn2) {
        super(cn2);
        this.u = "/mob/creeper.png";
    }

    public void a(hm hm2) {
        super.a(hm2);
    }

    public void b(hm hm2) {
        super.b(hm2);
    }

    protected void b_() {
        this.b = this.a;
        if (this.a > 0 && this.d < 0) {
            --this.a;
        }
        if (this.d >= 0) {
            this.d = 2;
        }
        super.b_();
        if (this.d != 1) {
            this.d = -1;
        }
    }

    protected String d() {
        return "mob.creeper";
    }

    protected String e() {
        return "mob.creeperdeath";
    }

    public void b(kh kh2) {
        super.b(kh2);
        if (kh2 instanceof cw) {
            this.b(di.aQ.aS + this.aQ.nextInt(2), 1);
        }
    }

    protected void a(kh kh2, float f2) {
        if (this.d <= 0 && f2 < 3.0f || this.d > 0 && f2 < 7.0f) {
            if (this.a == 0) {
                this.ag.a(this, "random.fuse", 1.0f, 0.5f);
            }
            this.d = 1;
            ++this.a;
            if (this.a == this.c) {
                this.ag.a((kh)this, this.ak, this.al, this.am, 3.0f);
                this.F();
            }
            this.g = true;
        }
    }

    public float b(float f2) {
        return ((float)this.b + (float)(this.a - this.b) * f2) / (float)(this.c - 2);
    }

    protected int g() {
        return di.K.aS;
    }
}

