/*
 * Decompiled with CFR 0.152.
 */
public class iy {
    private int b;
    private int c;
    public int a;

    public iy(int n2, int n3) {
        this.b = n2;
        this.c = n3;
        this.a = 1;
        while (this.b / (this.a + 1) >= 320 && this.c / (this.a + 1) >= 240) {
            ++this.a;
        }
        this.b /= this.a;
        this.c /= this.a;
    }

    public int a() {
        return this.b;
    }

    public int b() {
        return this.c;
    }
}

