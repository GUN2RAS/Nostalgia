/*
 * Decompiled with CFR 0.152.
 */
public class n
extends ar {
    public gu a;
    public gh b = new mw();

    public n(ev[] evArray) {
        this.a = new gu(this, evArray);
        this.a(this.a);
    }

    public void a(gh gh2) {
        int[] nArray = new int[9];
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int i3 = 0; i3 < 3; ++i3) {
                ev ev2;
                int n2 = -1;
                if (i2 < 2 && i3 < 2 && (ev2 = this.a.c(i2 + i3 * 2)) != null) {
                    n2 = ev2.c;
                }
                nArray[i2 + i3 * 3] = n2;
            }
        }
        this.b.a(0, dw.a().a(nArray));
    }

    public void a(dm dm2) {
        super.a(dm2);
        for (int i2 = 0; i2 < 9; ++i2) {
            ev ev2 = this.a.c(i2);
            if (ev2 == null) continue;
            dm2.a(ev2);
        }
    }
}

