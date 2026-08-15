/*
 * Decompiled with CFR 0.152.
 */
class kc {
    public double a;
    public double b;
    public double c;
    final /* synthetic */ cx d;

    public kc(cx cx2, int n2, int n3) {
        this.d = cx2;
        this.a = this.b = (double)(10 + n3) + cx.j().nextDouble() * 32.0 + (double)n2;
    }

    public void a() {
        this.b = this.a;
        if (this.a > 0.0) {
            this.c -= 0.6;
        }
        this.a += this.c;
        this.c *= 0.9;
        if (this.a < 0.0) {
            this.a = 0.0;
            this.c = 0.0;
        }
    }
}

