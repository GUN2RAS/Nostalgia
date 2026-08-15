/*
 * Decompiled with CFR 0.152.
 */
public class mb
extends dq {
    public mb(cn cn2) {
        super(cn2);
        this.u = "/mob/zombie.png";
        this.aa = 0.5f;
        this.e = 5;
    }

    public void j() {
        float f2;
        if (this.ag.b() && (f2 = this.a(1.0f)) > 0.5f && this.ag.i(eo.b(this.ak), eo.b(this.al), eo.b(this.am)) && this.aQ.nextFloat() * 30.0f < (f2 - 0.4f) * 2.0f) {
            this.aT = 300;
        }
        super.j();
    }

    protected String c() {
        return "mob.zombie";
    }

    protected String d() {
        return "mob.zombiehurt";
    }

    protected String e() {
        return "mob.zombiedeath";
    }

    protected int g() {
        return di.J.aS;
    }
}

