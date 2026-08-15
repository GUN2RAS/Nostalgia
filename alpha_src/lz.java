/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class lz
implements aw {
    private ga a;
    private Map b = new HashMap();
    private List c = new ArrayList();
    private cn d;

    public lz(cn cn2) {
        this.a = new ga(cn2, new byte[32768], 0, 0);
        this.a.q = true;
        this.a.p = true;
        this.d = cn2;
    }

    public boolean a(int n2, int n3) {
        hc hc2 = new hc(n2, n3);
        return this.b.containsKey(hc2);
    }

    public void c(int n2, int n3) {
        ga ga2 = this.b(n2, n3);
        if (!ga2.q) {
            ga2.e();
        }
        this.b.remove(new hc(n2, n3));
        this.c.remove(ga2);
    }

    public ga d(int n2, int n3) {
        hc hc2 = new hc(n2, n3);
        byte[] byArray = new byte[32768];
        ga ga2 = new ga(this.d, byArray, n2, n3);
        Arrays.fill(ga2.f.a, (byte)-1);
        this.b.put(hc2, ga2);
        ga2.c = true;
        return ga2;
    }

    public ga b(int n2, int n3) {
        hc hc2 = new hc(n2, n3);
        ga ga2 = (ga)this.b.get(hc2);
        if (ga2 == null) {
            return this.a;
        }
        return ga2;
    }

    public boolean a(boolean bl2, nu nu2) {
        return true;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public void a(aw aw2, int n2, int n3) {
    }
}

