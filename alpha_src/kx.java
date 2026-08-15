/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class kx {
    private Map o = new HashMap();
    public static kx a = new kx();
    private kd p;
    public static double b;
    public static double c;
    public static double d;
    public ey e;
    public jh f;
    public cn g;
    public dm h;
    public float i;
    public float j;
    public fr k;
    public double l;
    public double m;
    public double n;

    private kx() {
        this.o.put(ax.class, new ok());
        this.o.put(mv.class, new gm(new ca(), new ca(0.5f), 0.7f));
        this.o.put(bo.class, new ns(new gx(), new bx(), 0.7f));
        this.o.put(am.class, new mc(new dv(), 0.7f));
        this.o.put(mz.class, new eq(new kv(), 0.3f));
        this.o.put(dd.class, new d());
        this.o.put(cw.class, new dn(new fv(), 0.5f));
        this.o.put(mb.class, new dn(new cb(), 0.5f));
        this.o.put(ma.class, new gq(new hh(16), new hh(0), 0.25f));
        this.o.put(dm.class, new bu());
        this.o.put(hl.class, new nz(new cb(), 0.5f, 6.0f));
        this.o.put(ge.class, new dn(new cr(), 0.5f));
        this.o.put(kh.class, new gj());
        this.o.put(jc.class, new bw());
        this.o.put(kg.class, new gk());
        this.o.put(ao.class, new ei());
        this.o.put(dx.class, new ab());
        this.o.put(jd.class, new hw());
        this.o.put(ff.class, new dj());
        this.o.put(oc.class, new kt());
        this.o.put(dc.class, new cp());
        for (ak ak2 : this.o.values()) {
            ak2.a(this);
        }
    }

    public ak a(Class clazz) {
        ak ak2 = (ak)this.o.get(clazz);
        if (ak2 == null && clazz != kh.class) {
            ak2 = this.a(clazz.getSuperclass());
            this.o.put(clazz, ak2);
        }
        return ak2;
    }

    public ak a(kh kh2) {
        return this.a(kh2.getClass());
    }

    public void a(cn cn2, ey ey2, kd kd2, dm dm2, fr fr2, float f2) {
        this.g = cn2;
        this.e = ey2;
        this.k = fr2;
        this.h = dm2;
        this.p = kd2;
        this.i = dm2.as + (dm2.aq - dm2.as) * f2;
        this.j = dm2.at + (dm2.ar - dm2.at) * f2;
        this.l = dm2.aI + (dm2.ak - dm2.aI) * (double)f2;
        this.m = dm2.aJ + (dm2.al - dm2.aJ) * (double)f2;
        this.n = dm2.aK + (dm2.am - dm2.aK) * (double)f2;
    }

    public void a(kh kh2, float f2) {
        double d2 = kh2.aI + (kh2.ak - kh2.aI) * (double)f2;
        double d3 = kh2.aJ + (kh2.al - kh2.aJ) * (double)f2;
        double d4 = kh2.aK + (kh2.am - kh2.aK) * (double)f2;
        float f3 = kh2.as + (kh2.aq - kh2.as) * f2;
        float f4 = kh2.a(f2);
        GL11.glColor3f((float)f4, (float)f4, (float)f4);
        this.a(kh2, d2 - b, d3 - c, d4 - d, f3, f2);
    }

    public void a(kh kh2, double d2, double d3, double d4, float f2, float f3) {
        ak ak2 = this.a(kh2);
        if (ak2 != null) {
            ak2.a(kh2, d2, d3, d4, f2, f3);
            ak2.b(kh2, d2, d3, d4, f2, f3);
        }
    }

    public void a(cn cn2) {
        this.g = cn2;
    }

    public double a(double d2, double d3, double d4) {
        double d5 = d2 - this.l;
        double d6 = d3 - this.m;
        double d7 = d4 - this.n;
        return d5 * d5 + d6 * d6 + d7 * d7;
    }

    public kd a() {
        return this.p;
    }
}

