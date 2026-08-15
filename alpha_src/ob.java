/*
 * Decompiled with CFR 0.152.
 */
public class ob
extends ic {
    public String[] a = new String[]{"", "", "", ""};
    public int b = -1;

    public void b(hm hm2) {
        super.b(hm2);
        hm2.a("Text1", this.a[0]);
        hm2.a("Text2", this.a[1]);
        hm2.a("Text3", this.a[2]);
        hm2.a("Text4", this.a[3]);
    }

    public void a(hm hm2) {
        super.a(hm2);
        for (int i2 = 0; i2 < 4; ++i2) {
            this.a[i2] = hm2.i("Text" + (i2 + 1));
            if (this.a[i2].length() <= 15) continue;
            this.a[i2] = this.a[i2].substring(0, 15);
        }
    }
}

