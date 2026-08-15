/*
 * Decompiled with CFR 0.152.
 */
public class mz
extends ag {
    public boolean a = false;
    public float b = 0.0f;
    public float c = 0.0f;
    public float d;
    public float e;
    public float h = 1.0f;
    public int i;

    public mz(cn cn2) {
        super(cn2);
        this.u = "/mob/chicken.png";
        this.a(0.3f, 0.4f);
        this.E = 4;
        this.i = this.aQ.nextInt(6000) + 6000;
    }

    public void j() {
        super.j();
        this.e = this.b;
        this.d = this.c;
        this.c = (float)((double)this.c + (double)(this.av ? -1 : 4) * 0.3);
        if (this.c < 0.0f) {
            this.c = 0.0f;
        }
        if (this.c > 1.0f) {
            this.c = 1.0f;
        }
        if (!this.av && this.h < 1.0f) {
            this.h = 1.0f;
        }
        this.h = (float)((double)this.h * 0.9);
        if (!this.av && this.ao < 0.0) {
            this.ao *= 0.6;
        }
        this.b += this.h * 2.0f;
        if (!this.ag.y && --this.i <= 0) {
            this.ag.a(this, "mob.chickenplop", 1.0f, (this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.2f + 1.0f);
            this.b(di.aN.aS, 1);
            this.i = this.aQ.nextInt(6000) + 6000;
        }
    }

    protected void c(float f2) {
    }

    public void a(hm hm2) {
        super.a(hm2);
    }

    public void b(hm hm2) {
        super.b(hm2);
    }

    protected String c() {
        return "mob.chicken";
    }

    protected String d() {
        return "mob.chickenhurt";
    }

    protected String e() {
        return "mob.chickenhurt";
    }

    protected int g() {
        return di.J.aS;
    }
}

