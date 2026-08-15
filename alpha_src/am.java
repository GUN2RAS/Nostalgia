/*
 * Decompiled with CFR 0.152.
 */
public class am
extends ag {
    public boolean a = false;

    public am(cn cn2) {
        super(cn2);
        this.u = "/mob/cow.png";
        this.a(0.9f, 1.3f);
    }

    public void a(hm hm2) {
        super.a(hm2);
    }

    public void b(hm hm2) {
        super.b(hm2);
    }

    protected String c() {
        return "mob.cow";
    }

    protected String d() {
        return "mob.cowhurt";
    }

    protected String e() {
        return "mob.cowhurt";
    }

    protected float f() {
        return 0.4f;
    }

    protected int g() {
        return di.aD.aS;
    }

    public boolean a(dm dm2) {
        ev ev2 = dm2.b.a();
        if (ev2 != null && ev2.c == di.au.aS) {
            dm2.b.a(dm2.b.d, new ev(di.aE));
            return true;
        }
        return false;
    }
}

