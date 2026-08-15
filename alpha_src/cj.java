/*
 * Decompiled with CFR 0.152.
 */
public class cj
extends bh {
    private String a;
    private String h;

    public cj(String string, String string2) {
        this.a = string;
        this.h = string2;
    }

    public void g() {
    }

    protected void a(char c2, int n2) {
    }

    public void a() {
        this.e.clear();
        this.e.add(new fk(0, this.c / 2 - 100, this.d / 4 + 120 + 12, "Back to title screen"));
    }

    protected void a(fk fk2) {
        if (fk2.f == 0) {
            this.b.a(new cx());
        }
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, this.a, this.c / 2, this.d / 2 - 50, 0xFFFFFF);
        this.a(this.g, this.h, this.c / 2, this.d / 2 - 10, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

