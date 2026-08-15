/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class lr
extends jt {
    private Class a;
    private boolean b;

    protected lr(int n2, Class clazz, boolean bl2) {
        super(n2, gb.c);
        this.b = bl2;
        this.bb = 4;
        this.a = clazz;
        float f2 = 0.25f;
        float f3 = 1.0f;
        this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, f3, 0.5f + f2);
    }

    public cf d(cn cn2, int n2, int n3, int n4) {
        return null;
    }

    public cf f(cn cn2, int n2, int n3, int n4) {
        this.a((nm)cn2, n2, n3, n4);
        return super.f(cn2, n2, n3, n4);
    }

    public void a(nm nm2, int n2, int n3, int n4) {
        if (this.b) {
            return;
        }
        int n5 = nm2.e(n2, n3, n4);
        float f2 = 0.28125f;
        float f3 = 0.78125f;
        float f4 = 0.0f;
        float f5 = 1.0f;
        float f6 = 0.125f;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        if (n5 == 2) {
            this.a(f4, f2, 1.0f - f6, f5, f3, 1.0f);
        }
        if (n5 == 3) {
            this.a(f4, f2, 0.0f, f5, f3, f6);
        }
        if (n5 == 4) {
            this.a(1.0f - f6, f2, f4, 1.0f, f3, f5);
        }
        if (n5 == 5) {
            this.a(0.0f, f2, f4, f6, f3, f5);
        }
    }

    public int f() {
        return -1;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    protected ic a_() {
        try {
            return (ic)this.a.newInstance();
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public int a(int n2, Random random) {
        return di.as.aS;
    }

    public void a(cn cn2, int n2, int n3, int n4, int n5) {
        boolean bl2 = false;
        if (this.b) {
            if (!cn2.f(n2, n3 - 1, n4).a()) {
                bl2 = true;
            }
        } else {
            int n6 = cn2.e(n2, n3, n4);
            bl2 = true;
            if (n6 == 2 && cn2.f(n2, n3, n4 + 1).a()) {
                bl2 = false;
            }
            if (n6 == 3 && cn2.f(n2, n3, n4 - 1).a()) {
                bl2 = false;
            }
            if (n6 == 4 && cn2.f(n2 + 1, n3, n4).a()) {
                bl2 = false;
            }
            if (n6 == 5 && cn2.f(n2 - 1, n3, n4).a()) {
                bl2 = false;
            }
        }
        if (bl2) {
            this.b_(cn2, n2, n3, n4, cn2.e(n2, n3, n4));
            cn2.d(n2, n3, n4, 0);
        }
        super.a(cn2, n2, n3, n4, n5);
    }
}

