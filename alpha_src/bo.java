/*
 * Decompiled with CFR 0.152.
 */
public class bo
extends ag {
    public boolean a = false;

    public bo(cn cn2) {
        super(cn2);
        this.u = "/mob/sheep.png";
        this.a(0.9f, 1.3f);
    }

    public boolean a(kh kh2, int n2) {
        if (!this.a && kh2 instanceof ge) {
            this.a = true;
            int n3 = 1 + this.aQ.nextInt(3);
            for (int i2 = 0; i2 < n3; ++i2) {
                dx dx2 = this.a(ly.ac.bc, 1, 1.0f);
                dx2.ao += (double)(this.aQ.nextFloat() * 0.05f);
                dx2.an += (double)((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.1f);
                dx2.ap += (double)((this.aQ.nextFloat() - this.aQ.nextFloat()) * 0.1f);
            }
        }
        return super.a(kh2, n2);
    }

    public void a(hm hm2) {
        super.a(hm2);
        hm2.a("Sheared", this.a);
    }

    public void b(hm hm2) {
        super.b(hm2);
        this.a = hm2.m("Sheared");
    }

    protected String c() {
        return "mob.sheep";
    }

    protected String d() {
        return "mob.sheep";
    }

    protected String e() {
        return "mob.sheep";
    }
}

