/*
 * Decompiled with CFR 0.152.
 */
public class lw
extends bh {
    private bh h;
    protected String a = "Controls";
    private fr i;
    private int j = -1;

    public lw(bh bh2, fr fr2) {
        this.h = bh2;
        this.i = fr2;
    }

    public void a() {
        for (int i2 = 0; i2 < this.i.t.length; ++i2) {
            this.e.add(new o(i2, this.c / 2 - 155 + i2 % 2 * 160, this.d / 6 + 24 * (i2 >> 1), this.i.a(i2)));
        }
        this.e.add(new fk(200, this.c / 2 - 100, this.d / 6 + 168, "Done"));
    }

    protected void a(fk fk2) {
        for (int i2 = 0; i2 < this.i.t.length; ++i2) {
            ((fk)this.e.get((int)i2)).e = this.i.a(i2);
        }
        if (fk2.f == 200) {
            this.b.a(this.h);
        } else {
            this.j = fk2.f;
            fk2.e = "> " + this.i.a(fk2.f) + " <";
        }
    }

    protected void a(char c2, int n2) {
        if (this.j >= 0) {
            this.i.a(this.j, n2);
            ((fk)this.e.get((int)this.j)).e = this.i.a(this.j);
            this.j = -1;
        } else {
            super.a(c2, n2);
        }
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, this.a, this.c / 2, 20, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

