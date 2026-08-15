/*
 * Decompiled with CFR 0.152.
 */
public class iu
extends di {
    private int a;

    public iu(int n2, int n3) {
        super(n2);
        this.aT = 1;
        this.aU = 32 << n3;
        if (n3 == 3) {
            this.aU *= 4;
        }
        this.a = 4 + n3 * 2;
    }

    public float a(ev ev2, ly ly2) {
        return 1.5f;
    }

    public void a(ev ev2, ge ge2) {
        ev2.b(1);
    }

    public void a(ev ev2, int n2, int n3, int n4, int n5) {
        ev2.b(2);
    }

    public int a(kh kh2) {
        return this.a;
    }

    public boolean a() {
        return true;
    }
}

