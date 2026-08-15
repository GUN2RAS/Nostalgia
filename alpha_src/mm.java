/*
 * Decompiled with CFR 0.152.
 */
public class mm
extends dk {
    private final ee c;
    public final int e;
    public final int f;

    public mm(ee ee2, gh gh2, int n2, int n3, int n4) {
        super(gh2, n2);
        this.c = ee2;
        this.e = n3;
        this.f = n4;
    }

    public boolean a(int n2, int n3) {
        int n4 = (this.c.c - this.c.a) / 2;
        int n5 = (this.c.d - this.c.h) / 2;
        return (n2 -= n4) >= this.e - 1 && n2 < this.e + 16 + 1 && (n3 -= n5) >= this.f - 1 && n3 < this.f + 16 + 1;
    }
}

