/*
 * Decompiled with CFR 0.152.
 */
public class hs
implements gh {
    private String a;
    private gh b;
    private gh c;

    public hs(String string, gh gh2, gh gh3) {
        this.a = string;
        this.b = gh2;
        this.c = gh3;
    }

    public int c() {
        return this.b.c() + this.c.c();
    }

    public String d() {
        return this.a;
    }

    public ev c(int n2) {
        if (n2 >= this.b.c()) {
            return this.c.c(n2 - this.b.c());
        }
        return this.b.c(n2);
    }

    public ev a(int n2, int n3) {
        if (n2 >= this.b.c()) {
            return this.c.a(n2 - this.b.c(), n3);
        }
        return this.b.a(n2, n3);
    }

    public void a(int n2, ev ev2) {
        if (n2 >= this.b.c()) {
            this.c.a(n2 - this.b.c(), ev2);
        } else {
            this.b.a(n2, ev2);
        }
    }

    public int e() {
        return this.b.e();
    }

    public void j_() {
        this.b.j_();
        this.c.j_();
    }
}

