/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class fz {
    private Map m = new HashMap();
    public static fz a = new fz();
    private kd n;
    public static double b;
    public static double c;
    public static double d;
    public ey e;
    public cn f;
    public dm g;
    public float h;
    public float i;
    public double j;
    public double k;
    public double l;

    private fz() {
        this.m.put(ob.class, new in());
        this.m.put(bd.class, new r());
        for (ex ex2 : this.m.values()) {
            ex2.a(this);
        }
    }

    public ex a(Class clazz) {
        ex ex2 = (ex)this.m.get(clazz);
        if (ex2 == null && clazz != ic.class) {
            ex2 = this.a(clazz.getSuperclass());
            this.m.put(clazz, ex2);
        }
        return ex2;
    }

    public boolean a(ic ic2) {
        return this.b(ic2) != null;
    }

    public ex b(ic ic2) {
        return this.a(ic2.getClass());
    }

    public void a(cn cn2, ey ey2, kd kd2, dm dm2, float f2) {
        this.f = cn2;
        this.e = ey2;
        this.g = dm2;
        this.n = kd2;
        this.h = dm2.as + (dm2.aq - dm2.as) * f2;
        this.i = dm2.at + (dm2.ar - dm2.at) * f2;
        this.j = dm2.aI + (dm2.ak - dm2.aI) * (double)f2;
        this.k = dm2.aJ + (dm2.al - dm2.aJ) * (double)f2;
        this.l = dm2.aK + (dm2.am - dm2.aK) * (double)f2;
    }

    public void a(ic ic2, float f2) {
        if (ic2.a(this.j, this.k, this.l) < 4096.0) {
            float f3 = this.f.c(ic2.f, ic2.g, ic2.h);
            GL11.glColor3f((float)f3, (float)f3, (float)f3);
            this.a(ic2, (double)ic2.f - b, (double)ic2.g - c, (double)ic2.h - d, f2);
        }
    }

    public void a(ic ic2, double d2, double d3, double d4, float f2) {
        ex ex2 = this.b(ic2);
        if (ex2 != null) {
            ex2.a(ic2, d2, d3, d4, f2);
        }
    }

    public kd a() {
        return this.n;
    }
}

