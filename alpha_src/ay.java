/*
 * Decompiled with CFR 0.152.
 */
public class ay
extends bh {
    private bh h;
    protected String a = "Options";
    private fr i;

    public ay(bh bh2, fr fr2) {
        this.h = bh2;
        this.i = fr2;
    }

    public void a() {
        for (int i2 = 0; i2 < this.i.v; ++i2) {
            int n2 = this.i.b(i2);
            if (n2 == 0) {
                this.e.add(new o(i2, this.c / 2 - 155 + i2 % 2 * 160, this.d / 6 + 24 * (i2 >> 1), this.i.d(i2)));
                continue;
            }
            this.e.add(new mo(i2, this.c / 2 - 155 + i2 % 2 * 160, this.d / 6 + 24 * (i2 >> 1), i2, this.i.d(i2), this.i.c(i2)));
        }
        this.e.add(new fk(100, this.c / 2 - 100, this.d / 6 + 120 + 12, "Controls..."));
        this.e.add(new fk(200, this.c / 2 - 100, this.d / 6 + 168, "Done"));
    }

    protected void a(fk fk2) {
        if (!fk2.g) {
            return;
        }
        if (fk2.f < 100) {
            this.i.b(fk2.f, 1);
            fk2.e = this.i.d(fk2.f);
        }
        if (fk2.f == 100) {
            this.b.a(new lw(this, this.i));
        }
        if (fk2.f == 200) {
            this.b.a(this.h);
        }
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, this.a, this.c / 2, 20, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

