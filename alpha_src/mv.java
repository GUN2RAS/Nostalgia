/*
 * Decompiled with CFR 0.152.
 */
public class mv
extends ag {
    public boolean a = false;

    public mv(cn cn2) {
        super(cn2);
        this.u = "/mob/pig.png";
        this.a(0.9f, 0.9f);
        this.a = false;
    }

    public void a(hm hm2) {
        super.a(hm2);
        hm2.a("Saddle", this.a);
    }

    public void b(hm hm2) {
        super.b(hm2);
        this.a = hm2.m("Saddle");
    }

    protected String c() {
        return "mob.pig";
    }

    protected String d() {
        return "mob.pig";
    }

    protected String e() {
        return "mob.pigdeath";
    }

    public boolean a(dm dm2) {
        if (this.a) {
            dm2.g(this);
            return true;
        }
        return false;
    }

    protected int g() {
        return di.ao.aS;
    }
}

