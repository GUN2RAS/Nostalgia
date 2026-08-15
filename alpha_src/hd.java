/*
 * Decompiled with CFR 0.152.
 */
import java.util.Random;

public class hd
extends mq {
    protected hd(int n2, int n3) {
        super(n2, n3);
        this.bb = n3;
        this.b(true);
        float f2 = 0.5f;
        this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, 0.25f, 0.5f + f2);
    }

    protected boolean b(int n2) {
        return n2 == ly.aB.bc;
    }

    public void a(cn cn2, int n2, int n3, int n4, Random random) {
        float f2;
        int n5;
        super.a(cn2, n2, n3, n4, random);
        if (cn2.j(n2, n3 + 1, n4) >= 9 && (n5 = cn2.e(n2, n3, n4)) < 7 && random.nextInt((int)(100.0f / (f2 = this.i(cn2, n2, n3, n4)))) == 0) {
            cn2.b(n2, n3, n4, ++n5);
        }
    }

    private float i(cn cn2, int n2, int n3, int n4) {
        float f2 = 1.0f;
        int n5 = cn2.a(n2, n3, n4 - 1);
        int n6 = cn2.a(n2, n3, n4 + 1);
        int n7 = cn2.a(n2 - 1, n3, n4);
        int n8 = cn2.a(n2 + 1, n3, n4);
        int n9 = cn2.a(n2 - 1, n3, n4 - 1);
        int n10 = cn2.a(n2 + 1, n3, n4 - 1);
        int n11 = cn2.a(n2 + 1, n3, n4 + 1);
        int n12 = cn2.a(n2 - 1, n3, n4 + 1);
        boolean bl2 = n7 == this.bc || n8 == this.bc;
        boolean bl3 = n5 == this.bc || n6 == this.bc;
        boolean bl4 = n9 == this.bc || n10 == this.bc || n11 == this.bc || n12 == this.bc;
        for (int i2 = n2 - 1; i2 <= n2 + 1; ++i2) {
            for (int i3 = n4 - 1; i3 <= n4 + 1; ++i3) {
                int n13 = cn2.a(i2, n3 - 1, i3);
                float f3 = 0.0f;
                if (n13 == ly.aB.bc) {
                    f3 = 1.0f;
                    if (cn2.e(i2, n3 - 1, i3) > 0) {
                        f3 = 3.0f;
                    }
                }
                if (i2 != n2 || i3 != n4) {
                    f3 /= 4.0f;
                }
                f2 += f3;
            }
        }
        if (bl4 || bl2 && bl3) {
            f2 /= 2.0f;
        }
        return f2;
    }

    public int a(int n2, int n3) {
        if (n3 < 0) {
            n3 = 7;
        }
        return this.bb + n3;
    }

    public int f() {
        return 6;
    }

    public void b(cn cn2, int n2, int n3, int n4, int n5) {
        super.b(cn2, n2, n3, n4, n5);
        for (int i2 = 0; i2 < 3; ++i2) {
            if (cn2.n.nextInt(15) > n5) continue;
            float f2 = 0.7f;
            float f3 = cn2.n.nextFloat() * f2 + (1.0f - f2) * 0.5f;
            float f4 = cn2.n.nextFloat() * f2 + (1.0f - f2) * 0.5f;
            float f5 = cn2.n.nextFloat() * f2 + (1.0f - f2) * 0.5f;
            dx dx2 = new dx(cn2, (float)n2 + f3, (float)n3 + f4, (float)n4 + f5, new ev(di.Q));
            dx2.c = 10;
            cn2.a(dx2);
        }
    }

    public int a(int n2, Random random) {
        System.out.println("Get resource: " + n2);
        if (n2 == 7) {
            return di.R.aS;
        }
        return -1;
    }

    public int a(Random random) {
        return 1;
    }
}

