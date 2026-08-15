/*
 * Decompiled with CFR 0.152.
 */
public class l {
    private String[][] a = new String[][]{{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private Object[][] b = new Object[][]{{di.aD, ly.as, di.m, di.l, di.n}, {di.T, di.X, di.ab, di.af, di.aj}, {di.U, di.Y, di.ac, di.ag, di.ak}, {di.V, di.Z, di.ad, di.ah, di.al}, {di.W, di.aa, di.ae, di.ai, di.am}};

    public void a(dw dw2) {
        for (int i2 = 0; i2 < this.b[0].length; ++i2) {
            Object object = this.b[0][i2];
            for (int i3 = 0; i3 < this.b.length - 1; ++i3) {
                di di2 = (di)this.b[i3 + 1][i2];
                dw2.a(new ev(di2), this.a[i3], Character.valueOf('X'), object);
            }
        }
    }
}

