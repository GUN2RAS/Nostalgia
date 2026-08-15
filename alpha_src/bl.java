/*
 * Decompiled with CFR 0.152.
 */
public class bl {
    private final a[] b;
    public final int a;
    private int c;

    public bl(a[] aArray) {
        this.b = aArray;
        this.a = aArray.length;
    }

    public void a() {
        ++this.c;
    }

    public boolean b() {
        return this.c >= this.b.length;
    }

    public aj a(kh kh2) {
        double d2 = (double)this.b[this.c].a + (double)((int)(kh2.aC + 1.0f)) * 0.5;
        double d3 = this.b[this.c].b;
        double d4 = (double)this.b[this.c].c + (double)((int)(kh2.aC + 1.0f)) * 0.5;
        return aj.b(d2, d3, d4);
    }
}

