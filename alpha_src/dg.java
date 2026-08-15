/*
 * Decompiled with CFR 0.152.
 */
public class dg
extends bh {
    private gy a;
    private int h = 0;

    public dg(gy gy2) {
        this.a = gy2;
    }

    protected void a(char c2, int n2) {
    }

    public void a() {
        this.e.clear();
    }

    public void g() {
        ++this.h;
        if (this.h % 20 == 0) {
            this.a.a(new gi());
        }
        if (this.a != null) {
            this.a.a();
        }
    }

    protected void a(fk fk2) {
    }

    public void a(int n2, int n3, float f2) {
        this.b(0);
        this.a(this.g, "Downloading terrain", this.c / 2, this.d / 2 - 50, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

