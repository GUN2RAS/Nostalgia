/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class oi
extends ly {
    private boolean a;

    public oi(int n2, boolean bl2) {
        super(n2, 6, gb.d);
        this.a = bl2;
        if (!bl2) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
        this.d(255);
    }

    public int a(int n2) {
        if (n2 <= 1) {
            return 6;
        }
        return 5;
    }

    public boolean b() {
        return this.a;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        if (this != ly.al) {
            return;
        }
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        int n5;
        if (this != ly.al) {
            super.e(cn2, n2, n3, n4);
        }
        if ((n5 = cn2.a(n2, n3 - 1, n4)) == oi.al.bc) {
            cn2.d(n2, n3, n4, 0);
            cn2.d(n2, n3 - 1, n4, ly.ak.bc);
        }
    }

    public int a(int n2, Random random) {
        return ly.al.bc;
    }

    public boolean c() {
        return this.a;
    }

    public boolean c(nm nm2, int n2, int n3, int n4, int n5) {
        if (this != ly.al) {
            super.c(nm2, n2, n3, n4, n5);
        }
        if (n5 == 1) {
            return true;
        }
        if (!super.c(nm2, n2, n3, n4, n5)) {
            return false;
        }
        if (n5 == 0) {
            return true;
        }
        return nm2.a(n2, n3, n4) != this.bc;
    }
}

