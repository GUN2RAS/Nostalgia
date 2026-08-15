/*
 * Decompiled with CFR 0.152.
 */
public class dr {
    private String[][] a = new String[][]{{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
    private Object[][] b = new Object[][]{{ly.y, ly.x, di.m, di.l, di.n}, {di.r, di.v, di.e, di.z, di.G}, {di.q, di.u, di.d, di.y, di.F}, {di.s, di.w, di.f, di.A, di.H}, {di.L, di.M, di.N, di.O, di.P}};

    public void a(dw dw2) {
        for (int i2 = 0; i2 < this.b[0].length; ++i2) {
            Object object = this.b[0][i2];
            for (int i3 = 0; i3 < this.b.length - 1; ++i3) {
                di di2 = (di)this.b[i3 + 1][i2];
                dw2.a(new ev(di2), this.a[i3], Character.valueOf('#'), di.B, Character.valueOf('X'), object);
            }
        }
    }
}

