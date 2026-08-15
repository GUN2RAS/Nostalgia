/*
 * Decompiled with CFR 0.152.
 */
public class ed {
    public aj a;
    public float b;
    public float c;

    public ed(float f2, float f3, float f4, float f5, float f6) {
        this(aj.a(f2, f3, f4), f5, f6);
    }

    public ed a(float f2, float f3) {
        return new ed(this, f2, f3);
    }

    public ed(ed ed2, float f2, float f3) {
        this.a = ed2.a;
        this.b = f2;
        this.c = f3;
    }

    public ed(aj aj2, float f2, float f3) {
        this.a = aj2;
        this.b = f2;
        this.c = f3;
    }
}

