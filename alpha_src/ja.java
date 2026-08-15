/*
 * Decompiled with CFR 0.152.
 */
public class ja
extends bh {
    private bh a;
    private String h;
    private String i;
    private int j;

    public ja(bh bh2, String string, String string2, int n2) {
        this.a = bh2;
        this.h = string;
        this.i = string2;
        this.j = n2;
    }

    public void a() {
        this.e.add(new o(0, this.c / 2 - 155 + 0, this.d / 6 + 96, "Yes"));
        this.e.add(new o(1, this.c / 2 - 155 + 160, this.d / 6 + 96, "No"));
    }

    protected void a(fk fk2) {
        this.a.a(fk2.f == 0, this.j);
    }

    public void a(int n2, int n3, float f2) {
        this.i();
        this.a(this.g, this.h, this.c / 2, 70, 0xFFFFFF);
        this.a(this.g, this.i, this.c / 2, 90, 0xFFFFFF);
        super.a(n2, n3, f2);
    }
}

