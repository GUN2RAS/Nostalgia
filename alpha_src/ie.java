/*
 * Decompiled with CFR 0.152.
 */
public class ie
extends bh {
    private int a = 0;
    private int h = 0;

    public void a() {
        this.a = 0;
        this.e.clear();
        this.e.add(new fk(1, this.c / 2 - 100, this.d / 4 + 48, "Save and quit to title"));
        if (this.b.j()) {
            ((fk)this.e.get((int)0)).e = "Disconnect";
        }
        this.e.add(new fk(4, this.c / 2 - 100, this.d / 4 + 24, "Back to game"));
        this.e.add(new fk(0, this.c / 2 - 100, this.d / 4 + 96, "Options..."));
    }

    protected void a(fk fk2) {
        if (fk2.f == 0) {
            this.b.a(new ay(this, this.b.y));
        }
        if (fk2.f == 1) {
            if (this.b.j()) {
                this.b.e.k();
            }
            this.b.a((cn)null);
            this.b.a(new cx());
        }
        if (fk2.f == 4) {
            this.b.a((bh)null);
            this.b.e();
        }
    }

    public void g() {
        super.g();
        ++this.h;
    }

    public void a(int n2, int n3, float f2) {
        boolean bl2;
        this.i();
        boolean bl3 = bl2 = !this.b.e.a(this.a++);
        if (bl2 || this.h < 20) {
            float f3 = ((float)(this.h % 10) + f2) / 10.0f;
            f3 = eo.a(f3 * (float)Math.PI * 2.0f) * 0.2f + 0.8f;
            int n4 = (int)(255.0f * f3);
            this.b(this.g, "Saving level..", 8, this.d - 16, n4 << 16 | n4 << 8 | n4);
        }
        this.a(this.g, "Game menu", this.c / 2, 40, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

