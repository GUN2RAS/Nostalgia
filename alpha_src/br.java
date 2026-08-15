/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class br
extends ly {
    protected br(int n2, int n3) {
        super(n2, n3, gb.n);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        float f2 = 0.125f;
        if (n5 == 2) {
            this.a(0.0f, 0.0f, 1.0f - f2, 1.0f, 1.0f, 1.0f);
        }
        if (n5 == 3) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f2);
        }
        if (n5 == 4) {
            this.a(1.0f - f2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        if (n5 == 5) {
            this.a(0.0f, 0.0f, 0.0f, f2, 1.0f, 1.0f);
        }
        return super.d(cn2, n2, n3, n4);
    }

    public cf f(cn cn2, int n2, int n3, int n4) {
        int n5 = cn2.e(n2, n3, n4);
        float f2 = 0.125f;
        if (n5 == 2) {
            this.a(0.0f, 0.0f, 1.0f - f2, 1.0f, 1.0f, 1.0f);
        }
        if (n5 == 3) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f2);
        }
        if (n5 == 4) {
            this.a(1.0f - f2, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        if (n5 == 5) {
            this.a(0.0f, 0.0f, 0.0f, f2, 1.0f, 1.0f);
        }
        return super.f(cn2, n2, n3, n4);
    }

    public boolean b() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int f() {
        return 8;
    }

    public boolean a(cn cn2, int n2, int n3, int n4) {
        if (cn2.g(n2 - 1, n3, n4)) {
            return true;
        }
        if (cn2.g(n2 + 1, n3, n4)) {
            return true;
        }
        if (cn2.g(n2, n3, n4 - 1)) {
            return true;
        }
        return cn2.g(n2, n3, n4 + 1);
    }

    public void d(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        if ((n6 == 0 || n5 == 2) && cn2.g(n2, n3, n4 + 1)) {
            n6 = 2;
        }
        if ((n6 == 0 || n5 == 3) && cn2.g(n2, n3, n4 - 1)) {
            n6 = 3;
        }
        if ((n6 == 0 || n5 == 4) && cn2.g(n2 + 1, n3, n4)) {
            n6 = 4;
        }
        if ((n6 == 0 || n5 == 5) && cn2.g(n2 - 1, n3, n4)) {
            n6 = 5;
        }
        cn2.b(n2, n3, n4, n6);
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        int n6 = cn2.e(n2, n3, n4);
        boolean bl2 = false;
        if (n6 == 2 && cn2.g(n2, n3, n4 + 1)) {
            bl2 = true;
        }
        if (n6 == 3 && cn2.g(n2, n3, n4 - 1)) {
            bl2 = true;
        }
        if (n6 == 4 && cn2.g(n2 + 1, n3, n4)) {
            bl2 = true;
        }
        if (n6 == 5 && cn2.g(n2 - 1, n3, n4)) {
            bl2 = true;
        }
        if (!bl2) {
            this.b_(cn2, n2, n3, n4, n6);
            cn2.d(n2, n3, n4, 0);
        }
        super.a(cn2, n2, n3, n4, n5);
    }

    public int a(Random random) {
        return 1;
    }
}

