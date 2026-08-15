/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class fn {
    private static Map a = new HashMap();
    private static Map b = new HashMap();
    public boolean j = false;

    static void a(int n2, Class clazz) {
        if (a.containsKey(n2)) {
            throw new IllegalArgumentException("Duplicate packet id:" + n2);
        }
        if (b.containsKey(clazz)) {
            throw new IllegalArgumentException("Duplicate packet class:" + clazz);
        }
        a.put(n2, clazz);
        b.put(clazz, n2);
    }

    public static fn a(int n2) {
        try {
            Class clazz = (Class)a.get(n2);
            if (clazz == null) {
                return null;
            }
            return (fn)clazz.newInstance();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Skipping packet with id " + n2);
            return null;
        }
    }

    public final int b() {
        return (Integer)b.get(this.getClass());
    }

    public static fn b(DataInputStream dataInputStream) {
        int n2 = dataInputStream.read();
        if (n2 == -1) {
            return null;
        }
        fn fn2 = fn.a(n2);
        if (fn2 == null) {
            throw new IOException("Bad packet id " + n2);
        }
        fn2.a(dataInputStream);
        return fn2;
    }

    public static void a(fn fn2, DataOutputStream dataOutputStream) {
        dataOutputStream.write(fn2.b());
        fn2.a(dataOutputStream);
    }

    public abstract void a(DataInputStream var1);

    public abstract void a(DataOutputStream var1);

    public abstract void a(lb var1);

    public abstract int a();

    static {
        fn.a(0, gi.class);
        fn.a(1, hp.class);
        fn.a(2, gt.class);
        fn.a(3, ij.class);
        fn.a(4, du.class);
        fn.a(5, m.class);
        fn.a(6, ji.class);
        fn.a(10, eh.class);
        fn.a(11, s.class);
        fn.a(12, mh.class);
        fn.a(13, ch.class);
        fn.a(14, fg.class);
        fn.a(15, do.class);
        fn.a(16, dz.class);
        fn.a(17, ld.class);
        fn.a(18, hf.class);
        fn.a(20, gp.class);
        fn.a(21, ha.class);
        fn.a(22, bm.class);
        fn.a(23, kj.class);
        fn.a(24, ez.class);
        fn.a(29, ju.class);
        fn.a(30, lq.class);
        fn.a(31, kp.class);
        fn.a(32, jx.class);
        fn.a(33, is.class);
        fn.a(34, jl.class);
        fn.a(50, ka.class);
        fn.a(51, bz.class);
        fn.a(52, na.class);
        fn.a(53, li.class);
        fn.a(59, ny.class);
        fn.a(255, oh.class);
    }
}

