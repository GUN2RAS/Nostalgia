/*
 * Decompiled with CFR 0.152.
 */
public class et
extends ar {
    public gu a = new gu(this, 3, 3);
    public gh b = new mw();

    public void a(gh gh2) {
        int[] nArray = new int[9];
        for (int i2 = 0; i2 < 3; ++i2) {
            for (int i3 = 0; i3 < 3; ++i3) {
                int n2 = i2 + i3 * 3;
                ev ev2 = this.a.c(n2);
                nArray[n2] = ev2 == null ? -1 : ev2.c;
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

