/*
 * Decompiled with CFR 0.152.
 */
public class an
extends mm {
    private final gh c;

    public an(ee ee2, gh gh2, gh gh3, int n2, int n3, int n4) {
        super(ee2, gh3, n2, n3, n4);
        this.c = gh2;
    }

    public boolean a(ev ev2) {
        return false;
    }

    public void a() {
        for (int i2 = 0; i2 < this.c.c(); ++i2) {
            if (this.c.c(i2) == null) continue;
            this.c.a(i2, 1);
        }
    }
}

