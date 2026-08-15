/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;

public class fh
extends ly {
    public fh(int n2, int n3) {
        super(n2, n3, gb.c);
    }

    public void a(cn cn2, int n2, int n3, int n4, cf cf2, ArrayList arrayList) {
        arrayList.add(cf.b(n2, n3, n4, n2 + 1, (double)n3 + 1.5, n4 + 1));
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        if (cn2.a(n2, n3 - 1, n4) == this.bc) {
            return false;
        }
        if (!cn2.f(n2, n3 - 1, n4).a()) {
            return false;
        }
        return super.a(cn2, n2, n3, n4);
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 11;
    }
}

