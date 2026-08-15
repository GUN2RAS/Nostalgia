/*
 * Decompiled with CFR 0.152.
 */
public class ng
extends ly {
    protected ng(int n2) {
        super(n2, gb.j);
        this.bb = 48;
    }

    public void e(cn cn2, int n2, int n3, int n4) {
        int n5 = 2;
        for (int i2 = n2 - n5; i2 <= n2 + n5; ++i2) {
            for (int i3 = n3 - n5; i3 <= n3 + n5; ++i3) {
                for (int i4 = n4 - n5; i4 <= n4 + n5; ++i4) {
                    if (cn2.f(i2, i3, i4) != gb.f) continue;
                }
            }
        }
    }

    public void b(cn cn2, int n2, int n3, int n4) {
        int n5 = 2;
        for (int i2 = n2 - n5; i2 <= n2 + n5; ++i2) {
            for (int i3 = n3 - n5; i3 <= n3 + n5; ++i3) {
                for (int i4 = n4 - n5; i4 <= n4 + n5; ++i4) {
                    cn2.g(i2, i3, i4, cn2.a(i2, i3, i4));
                }
            }
        }
    }
}

