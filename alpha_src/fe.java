/*
 * Decompiled with CFR 0.152.
 */
public class fe
extends ic
implements gh {
    private ev[] a = new ev[36];

    public int c() {
        return 27;
    }

    public ev c(int n2) {
        return this.a[n2];
    }

    public ev a(int n2, int n3) {
        if (this.a[n2] != null) {
            if (this.a[n2].a <= n3) {
                ev ev2 = this.a[n2];
                this.a[n2] = null;
                this.j_();
                return ev2;
            }
            ev ev3 = this.a[n2].a(n3);
            if (this.a[n2].a == 0) {
                this.a[n2] = null;
            }
            this.j_();
            return ev3;
        }
        return null;
    }

    public void a(int n2, ev ev2) {
        this.a[n2] = ev2;
        if (ev2 != null && ev2.a > this.e()) {
            ev2.a = this.e();
        }
        this.j_();
    }

    public String d() {
        return "Chest";
    }

    public void a(hm hm2) {
        super.a(hm2);
        ki ki2 = hm2.l("Items");
        this.a = new ev[this.c()];
        for (int i2 = 0; i2 < ki2.c(); ++i2) {
            hm hm3 = (hm)ki2.a(i2);
            int n2 = hm3.c("Slot") & 0xFF;
            if (n2 < 0 || n2 >= this.a.length) continue;
            this.a[n2] = new ev(hm3);
        }
    }

    public void b(hm hm2) {
        super.b(hm2);
        ki ki2 = new ki();
        for (int i2 = 0; i2 < this.a.length; ++i2) {
            if (this.a[i2] == null) continue;
            hm hm3 = new hm();
            hm3.a("Slot", (byte)i2);
            this.a[i2].a(hm3);
            ki2.a(hm3);
        }
        hm2.a("Items", ki2);
    }

    public int e() {
        return 64;
    }
}

