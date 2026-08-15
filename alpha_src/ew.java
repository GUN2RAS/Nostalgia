/*
 * Decompiled with CFR 0.152.
 */
import java.util.HashMap;
import java.util.Map;

public class ew {
    private static Map a = new HashMap();
    private static Map b = new HashMap();
    private static Map c = new HashMap();
    private static Map d = new HashMap();

    private static void a(Class clazz, String string, int n2) {
        a.put(string, clazz);
        b.put(clazz, string);
        c.put(n2, clazz);
        d.put(clazz, n2);
    }

    public static kh a(String string, cn cn2) {
        kh kh2 = null;
        try {
            Class clazz = (Class)a.get(string);
            if (clazz != null) {
                kh2 = (kh)clazz.getConstructor(cn.class).newInstance(cn2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return kh2;
    }

    public static kh a(hm hm2, cn cn2) {
        kh kh2 = null;
        try {
            Class clazz = (Class)a.get(hm2.i("id"));
            if (clazz != null) {
                kh2 = (kh)clazz.getConstructor(cn.class).newInstance(cn2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (kh2 != null) {
            kh2.e(hm2);
        } else {
            System.out.println("Skipping Entity with id " + hm2.i("id"));
        }
        return kh2;
    }

    public static kh a(int n2, cn cn2) {
        kh kh2 = null;
        try {
            Class clazz = (Class)c.get(n2);
            if (clazz != null) {
                kh2 = (kh)clazz.getConstructor(cn.class).newInstance(cn2);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (kh2 == null) {
            System.out.println("Skipping Entity with id " + n2);
        }
        return kh2;
    }

    public static int a(kh kh2) {
        return (Integer)d.get(kh2.getClass());
    }

    public static String b(kh kh2) {
        return (String)b.get(kh2.getClass());
    }

    static {
        ew.a(kg.class, "Arrow", 10);
        ew.a(ao.class, "Snowball", 11);
        ew.a(dx.class, "Item", 1);
        ew.a(jc.class, "Painting", 9);
        ew.a(ge.class, "Mob", 48);
        ew.a(dq.class, "Monster", 49);
        ew.a(dd.class, "Creeper", 50);
        ew.a(cw.class, "Skeleton", 51);
        ew.a(ax.class, "Spider", 52);
        ew.a(hl.class, "Giant", 53);
        ew.a(mb.class, "Zombie", 54);
        ew.a(ma.class, "Slime", 55);
        ew.a(mv.class, "Pig", 90);
        ew.a(bo.class, "Sheep", 91);
        ew.a(am.class, "Cow", 91);
        ew.a(mz.class, "Chicken", 91);
        ew.a(jd.class, "PrimedTnt", 20);
        ew.a(ff.class, "FallingSand", 21);
        ew.a(oc.class, "Minecart", 40);
        ew.a(dc.class, "Boat", 41);
    }
}

