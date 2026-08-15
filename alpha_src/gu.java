/*
 * Decompiled with CFR 0.152.
 */
public class gu
implements gh {
    private ev[] a;
    private int b;
    private ar c;

    public gu(ar ar2, int n2, int n3) {
        this.b = n2 * n3;
        this.a = new ev[this.b];
        this.c = ar2;
    }

    public gu(ar ar2, ev[] evArray) {
        this.b = evArray.length;
        this.a = evArray;
        this.c = ar2;
    }

    public int c() {
        return this.b;
    }

    public ev c(int n2) {
        return this.a[n2];
    }

    public String d() {
        return "Crafting";
    }

    public ev a(int n2, int n3) {
        if (this.a[n2] != null) {
            if (this.a[n2].a <= n3) {
                ev ev2 = this.a[n2];
                this.a[n2] = null;
                this.c.a(this);
                return ev2;
            }
            ev ev3 = this.a[n2].a(n3);
            if (this.a[n2].a == 0) {
                this.a[n2] = null;
            }
            this.c.a(this);
            return ev3;
        }
        return null;
    }

    public void a(int n2, ev ev2) {
        this.a[n2] = ev2;
        this.c.a(this);
    }

    public int e() {
        return 64;
    }

    public void j_() {
    }
}

