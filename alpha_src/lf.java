/*
 * Decompiled with CFR 0.152.
 */
public class lf {
    private Object[][] a = new Object[][]{{ly.ai, di.n}, {ly.aj, di.m}, {ly.ay, di.l}};

    public void a(dw dw2) {
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            ly ly2 = (ly)this.a[i2][0];
            di di2 = (di)this.a[i2][1];
            dw2.a(new ev(ly2), "###", "###", "###", Character.valueOf('#'), di2);
            dw2.a(new ev(di2, 9), "#", Character.valueOf('#'), ly2);
        }
    }
}

