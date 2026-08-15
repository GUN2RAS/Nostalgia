/*
 * Decompiled with CFR 0.152.
 */
import java.io.DataInput;
import java.io.DataOutput;
import java.util.HashMap;
import java.util.Map;

public class hm
extends el {
    private Map a = new HashMap();

    void a(DataOutput dataOutput) {
        for (el el2 : this.a.values()) {
            el.a(el2, dataOutput);
        }
        dataOutput.writeByte(0);
    }

    void a(DataInput dataInput) {
        el el2;
        this.a.clear();
        while ((el2 = el.b(dataInput)).a() != 0) {
            this.a.put(el2.b(), el2);
        }
    }

    public byte a() {
        return 10;
    }

    public void a(String string, el el2) {
        this.a.put(string, el2.a(string));
    }

    public void a(String string, byte by2) {
        this.a.put(string, new ix(by2).a(string));
    }

    public void a(String string, short s2) {
        this.a.put(string, new ls(s2).a(string));
    }

    public void a(String string, int n2) {
        this.a.put(string, new io(n2).a(string));
    }

    public void a(String string, long l2) {
        this.a.put(string, new gn(l2).a(string));
    }

    public void a(String string, float f2) {
        this.a.put(string, new f(f2).a(string));
    }

    public void a(String string, double d2) {
        this.a.put(string, new kr(d2).a(string));
    }

    public void a(String string, String string2) {
        this.a.put(string, new ne(string2).a(string));
    }

    public void a(String string, byte[] byArray) {
        this.a.put(string, new dy(byArray).a(string));
    }

    public void a(String string, hm hm2) {
        this.a.put(string, hm2.a(string));
    }

    public void a(String string, boolean bl2) {
        this.a(string, bl2 ? (byte)1 : 0);
    }

    public boolean b(String string) {
        return this.a.containsKey(string);
    }

    public byte c(String string) {
        if (!this.a.containsKey(string)) {
            return 0;
        }
        return ((ix)this.a.get((Object)string)).a;
    }

    public short d(String string) {
        if (!this.a.containsKey(string)) {
            return 0;
        }
        return ((ls)this.a.get((Object)string)).a;
    }

    public int e(String string) {
        if (!this.a.containsKey(string)) {
            return 0;
        }
        return ((io)this.a.get((Object)string)).a;
    }

    public long f(String string) {
        if (!this.a.containsKey(string)) {
            return 0L;
        }
        return ((gn)this.a.get((Object)string)).a;
    }

    public float g(String string) {
        if (!this.a.containsKey(string)) {
            return 0.0f;
        }
        return ((f)this.a.get((Object)string)).a;
    }

    public double h(String string) {
        if (!this.a.containsKey(string)) {
            return 0.0;
        }
        return ((kr)this.a.get((Object)string)).a;
    }

    public String i(String string) {
        if (!this.a.containsKey(string)) {
            return "";
        }
        return ((ne)this.a.get((Object)string)).a;
    }

    public byte[] j(String string) {
        if (!this.a.containsKey(string)) {
            return new byte[0];
        }
        return ((dy)this.a.get((Object)string)).a;
    }

    public hm k(String string) {
        if (!this.a.containsKey(string)) {
            return new hm();
        }
        return (hm)this.a.get(string);
    }

    public ki l(String string) {
        if (!this.a.containsKey(string)) {
            return new ki();
        }
        return (ki)this.a.get(string);
    }

    public boolean m(String string) {
        return this.c(string) != 0;
    }

    public String toString() {
        return "" + this.a.size() + " entries";
    }
}

