/*
 * Decompiled with CFR 0.152.
 */
public class cw
extends dq {
    public cw(cn cn2) {
        super(cn2);
        this.u = "/mob/skeleton.png";
    }

    protected String c() {
        return "mob.skeleton";
    }

    protected String d() {
        return "mob.skeletonhurt";
    }

    protected String e() {
        return "mob.skeletonhurt";
    }

    public void j() {
        float f2;
        if (this.ag.b() && (f2 = this.a(1.0f)) > 0.5f && this.ag.i(eo.b(this.ak), eo.b(this.al), eo.b(this.am)) && this.aQ.nextFloat() * 30.0f < (f2 - 0.4f) * 2.0f) {
            this.aT = 300;
        }
        super.j();
    }

    protected void a(kh kh2, float f2) {
        if (f2 < 10.0f) {
            double d2 = kh2.ak - this.ak;
            double d3 = kh2.am - this.am;
            if (this.K == 0) {
                kg kg2 = new kg(this.ag, this);
                kg2.al += (double)1.4f;
                double d4 = kh2.al - (double)0.2f - kg2.al;
                float f3 = eo.a(d2 * d2 + d3 * d3) * 0.2f;
                this.ag.a(this, "random.bow", 1.0f, 1.0f / (this.aQ.nextFloat() * 0.4f + 0.8f));
                this.ag.a(kg2);
                kg2.a(d2, d4 + (double)f3, d3, 0.6f, 12.0f);
                this.K = 30;
            }
            this.aq = (float)(Math.atan2(d3, d2) * 180.0 / 3.1415927410125732) - 90.0f;
            this.g = true;
        }
    }

    public void a(hm hm2) {
        super.a(hm2);
    }

    public void b(hm hm2) {
        super.b(hm2);
    }

    protected int g() {
        return di.j.aS;
    }
}

