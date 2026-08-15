/*
 * Decompiled with CFR 0.152.
 */
public class bs
extends di {
    private ly[] aX;
    private float aY = 4.0f;
    private int aZ;
    protected int a;

    public bs(int n2, int n3, int n4, ly[] lyArray) {
        super(n2);
        this.a = n4;
        this.aX = lyArray;
        this.aT = 1;
        this.aU = 32 << n4;
        if (n4 == 3) {
            this.aU *= 4;
        }
        this.aY = (n4 + 1) * 2;
        this.aZ = n3 + n4;
    }

    public float a(ev ev2, ly ly2) {
        for (int i2 = 0; i2 < this.aX.length; ++i2) {
            if (this.aX[i2] != ly2) continue;
            return this.aY;
        }
        return 1.0f;
    }

    public void a(ev ev2, ge ge2) {
        ev2.b(2);
    }

    public void a(ev ev2, int n2, int n3, int n4, int n5) {
        ev2.b(1);
    }

    public int a(kh kh2) {
        return this.aZ;
    }

    public boolean a() {
        return true;
    }
}

