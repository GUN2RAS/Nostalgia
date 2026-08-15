/*
 * Decompiled with CFR 0.152.
 */
import java.util.HashMap;
import java.util.Map;

public class ic {
    private static Map a = new HashMap();
    private static Map b = new HashMap();
    public cn e;
    public int f;
    public int g;
    public int h;

    private static void a(Class clazz, String string) {
        if (b.containsKey(string)) {
            throw new IllegalArgumentException("Duplicate id: " + string);
        }
        a.put(string, clazz);
        b.put(clazz, string);
    }

    public void a(hm hm2) {
        this.f = hm2.e("x");
        this.g = hm2.e("y");
        this.h = hm2.e("z");
    }

    public void b(hm hm2) {
        String string = (String)b.get(this.getClass());
        if (string == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        hm2.a("id", string);
        hm2.a("x", this.f);
        hm2.a("y", this.g);
        hm2.a("z", this.h);
    }

    public void b() {
    }

    public static ic c(hm hm2) {
        ic ic2 = null;
        try {
            Class clazz = (Class)a.get(hm2.i("id"));
            if (clazz != null) {
                ic2 = (ic)clazz.newInstance();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (ic2 != null) {
            ic2.a(hm2);
        } else {
            System.out.println("Skipping TileEntity with id " + hm2.i("id"));
        }
        return ic2;
    }

    public int f() {
        return this.e.e(this.f, this.g, this.h);
    }

    public void j_() {
        this.e.b(this.f, this.g, this.h, this);
    }

    public double a(double d2, double d3, double d4) {
        double d5 = (double)this.f + 0.5 - d2;
        double d6 = (double)this.g + 0.5 - d3;
        double d7 = (double)this.h + 0.5 - d4;
        return d5 * d5 + d6 * d6 + d7 * d7;
    }

    public ly g() {
        return ly.n[this.e.a(this.f, this.g, this.h)];
    }

    static {
        ic.a(ke.class, "Furnace");
        ic.a(fe.class, "Chest");
        ic.a(ob.class, "Sign");
        ic.a(bd.class, "MobSpawner");
    }
}

