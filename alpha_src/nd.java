/*
 * Decompiled with CFR 0.152.
 */
public class nd {
    private String[][] a = new String[][]{{"X", "X", "#"}};
    private Object[][] b = new Object[][]{{ly.y, ly.x, di.m, di.l, di.n}, {di.p, di.t, di.o, di.x, di.E}};

    public void a(dw dw2) {
        for (int i2 = 0; i2 < this.b[0].length; ++i2) {
            Object object = this.b[0][i2];
            for (int i3 = 0; i3 < this.b.length - 1; ++i3) {
                di di2 = (di)this.b[i3 + 1][i2];
                dw2.a(new ev(di2), this.a[i3], Character.valueOf('#'), di.B, Character.valueOf('X'), object);
            }
        }
        dw2.a(new ev(di.i, 1), " #X", "# X", " #X", Character.valueOf('X'), di.I, Character.valueOf('#'), di.B);
        dw2.a(new ev(di.j, 4), "X", "#", "Y", Character.valueOf('Y'), di.J, Character.valueOf('X'), di.an, Character.valueOf('#'), di.B);
    }
}

